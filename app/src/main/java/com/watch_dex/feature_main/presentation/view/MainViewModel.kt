package com.watch_dex.feature_main.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.watch_dex.core.data.BalanceManager
import com.watch_dex.core.data.model.PokemonFromList
import com.watch_dex.core.presentation.util.navigation.Screen
import com.watch_dex.feature_home.presentation.state.HomeEvent
import com.watch_dex.feature_home.presentation.state.HomeState
import com.watch_dex.feature_list_selection.presentation.event.ListSelectionEvent
import com.watch_dex.feature_list_selection.presentation.state.ListSelectionState
import com.watch_dex.feature_main.presentation.state.MainSelectedState
import com.watch_dex.feature_type_selection.data.repository.PokemonRepositoryImpl
import com.watch_dex.feature_type_selection.presentation.state.TypeSelectionEvent
import com.watch_dex.feature_type_selection.presentation.state.TypeSelectionState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val MAX_TYPE_AMOUNT = 2

class MainViewModel : ViewModel() {
    private val balanceManager = BalanceManager()
    private val repository = PokemonRepositoryImpl()

    private val allPokemonFromList = mutableListOf<PokemonFromList>()
    private val typesSelectedState = MutableStateFlow(MainSelectedState())

    // Screen states
    private val _homeScreenState = MutableStateFlow(HomeState())
    private val _typeScreenState = MutableStateFlow(TypeSelectionState())
    private val _listScreenState = MutableStateFlow(ListSelectionState())
    // ****

    // Public screen states
    val homeScreenState = combine(_homeScreenState, typesSelectedState) { state, selected ->
        HomeState(
            selected.typesSelected,
            state.isOffensive,
            selected.pokemonName,
            state.byListEnabled,
            state.randomType,
            balanceManager.getBalanceMap(state.isOffensive, selected.typesSelected)
        )
    }.distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), HomeState())

    val byTypeScreenState = combine(_typeScreenState, typesSelectedState) { state, selected ->
        TypeSelectionState(
            selected.typesSelected,
            selected.typesSelected.size >= MAX_TYPE_AMOUNT,
            state.allTypes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), TypeSelectionState())

    val byListScreenState = _listScreenState.asStateFlow()
    // ****

    init {
        processAllPokemon()
        processAllTypes()
    }

    private fun getPokemonByChar(char: Char?) {
        val newToDisplay = if (char == null) emptyList()
        else allPokemonFromList
            .filter { it.name.startsWith(char, true) }
            .sortedBy { it.name }
        _listScreenState.update { current ->
            current.copy(letterSelected = char, pokemonDisplayed = newToDisplay)
        }
    }

    private fun handleLetterClick(char: Char?) {
        val newLetterSelected = if (char == _listScreenState.value.letterSelected) null else char
        getPokemonByChar(newLetterSelected)
    }

    private fun processAllTypes() = process {
        _typeScreenState.update { current -> current.copy(allTypes = repository.getAllTypes()) }
    }

    private fun processAllPokemon() = process(::onCompletionAllPokemon) {
        allPokemonFromList.apply {
            clear()
            addAll(repository.getAllPokemon())
        }
    }

    private fun onCompletionAllPokemon(it: Throwable?) {
        if (it == null) _homeScreenState.update { current ->
            current.copy(byListEnabled = true)
        }
    }

    private fun clearSelection() = typesSelectedState.update { current ->
        current.copy(typesSelected = emptyList())
    }

    private fun navigateTo(controller: NavController, route: String) =
        controller.navigate(route)

    private fun process(
        onCompletion: ((Throwable?) -> Unit)? = null,
        action: suspend () -> Unit
    ) {
        viewModelScope.launch { action() }.invokeOnCompletion { onCompletion?.invoke(it) }
    }

    private fun updateSide(
        isOffensive: Boolean
    ) = _homeScreenState.update { current -> current.copy(isOffensive = isOffensive) }

    private fun handleListSelection(
        selection: PokemonFromList
    ) = typesSelectedState.update { current ->
        current.copy(pokemonName = selection.name, typesSelected = selection.typeList)
    }

    private fun handleTypeSelection(position: Int) {
        val selected = _typeScreenState.value.allTypes[position]
        val typesSelected = typesSelectedState.value.typesSelected
        val newSelected = when {
            selected in typesSelected -> typesSelected - selected
            typesSelected.size < MAX_TYPE_AMOUNT -> typesSelected + selected
            else -> typesSelected
        }
        typesSelectedState.update { current ->
            current.copy(pokemonName = null, typesSelected = newSelected)
        }
    }

    fun onHomeEvent(event: HomeEvent) = when (event) {
        is HomeEvent.SelectByPokemon -> navigateTo(
            event.navController,
            Screen.ListSelectionScreen.route
        )
        is HomeEvent.SelectByType -> navigateTo(
            event.navController,
            Screen.TypeSelectionScreen.route
        )
        HomeEvent.SelectDefensive -> updateSide(false)
        HomeEvent.SelectOffensive -> updateSide(true)
    }

    fun onListEvent(event: ListSelectionEvent) = when (event) {
        is ListSelectionEvent.OnPokemonClick -> handleListSelection(event.selection)
        is ListSelectionEvent.OnLetterClick -> handleLetterClick(event.char)
    }

    fun onTypeEvent(event: TypeSelectionEvent) {
        when (event) {
            TypeSelectionEvent.OnClearClick -> clearSelection()
            is TypeSelectionEvent.OnTypeClick -> handleTypeSelection(event.position)
            is TypeSelectionEvent.OnDoneClick -> event.navController.navigateUp()
        }
    }

}
