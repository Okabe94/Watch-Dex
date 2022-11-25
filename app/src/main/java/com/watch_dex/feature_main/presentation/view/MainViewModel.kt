package com.watch_dex.feature_main.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.watch_dex.core.data.BalanceManager
import com.watch_dex.core.data.Type
import com.watch_dex.core.data.model.PokemonFromList
import com.watch_dex.core.presentation.util.navigation.Screen
import com.watch_dex.feature_home.presentation.model.Effectiveness
import com.watch_dex.feature_home.presentation.state.HomeEvent
import com.watch_dex.feature_home.presentation.state.HomeState
import com.watch_dex.feature_list_selection.presentation.event.ListSelectionEvent
import com.watch_dex.feature_list_selection.presentation.state.ListSelectionState
import com.watch_dex.feature_type_selection.data.repository.PokemonRepositoryImpl
import com.watch_dex.feature_type_selection.presentation.state.TypeSelectionEvent
import com.watch_dex.feature_type_selection.presentation.state.TypeSelectionState
import java.util.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val MAX_TYPE_AMOUNT = 2

class MainViewModel : ViewModel() {
    private val balanceManager = BalanceManager()
    private val repository = PokemonRepositoryImpl()
    private val allPokemonFromList = mutableListOf<PokemonFromList>()

    // HomeScreenState
    // ByTypeScreenState
    // ByListScreenState

    // Depends on
    // TypesSelectedState

    private val _typeSelectedState = MutableStateFlow(listOf<Type>())

    private val _homeScreenState = MutableStateFlow(HomeState())
    private val _byTypeScreenState = MutableStateFlow(TypeSelectionState())
    private val _byListScreenState = MutableStateFlow(ListSelectionState())

    val byTypeState = combine(_byTypeScreenState, _typeSelectedState) { state, selected ->
        TypeSelectionState(selected, state.allTypes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), TypeSelectionState())


    //    private val _byTypeState = MutableStateFlow(TypeSelectionState())
    val typeState = _byTypeScreenState.asStateFlow()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private val _byListState = MutableStateFlow(ListSelectionState())
    val pokemonState = _byListState.asStateFlow()

//    val homeState = combine(_typeSelected, _homeState) { typeSelected, home ->
//        HomeState(
//            typeSelected.typesSelected,
//            home.isOffensive,
//            home.pokemonName,
//            home.byListEnabled,
//            home.randomType,
//            home.balanceMap
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    init {
        processAllPokemon()
        processAllTypes()
    }

    private fun getTypesSelected() = _typeSelectedState.value

    private fun getPokemonByChar(char: Char?) {
        val newToDisplay = if (char == null) emptyList()
        else allPokemonFromList
            .filter { it.name.startsWith(char, true) }
            .sortedBy { it.name }
        _byListState.update { current ->
            current.copy(letterSelected = char, pokemonDisplayed = newToDisplay)
        }
    }

    private fun handleLetterClick(char: Char?) {
        val newLetterSelected = if (char == _byListState.value.letterSelected) null else char
        getPokemonByChar(newLetterSelected)
    }

    private fun updateHomeState(
        pokemonName: String? = null,
        typesSelected: List<Type> = listOf(),
        offensiveMap: EnumMap<Effectiveness, MutableList<Type>> = EnumMap(Effectiveness::class.java),
        defensiveMap: EnumMap<Effectiveness, MutableList<Type>> = EnumMap(Effectiveness::class.java)
    ) {
        _homeState.update { current ->
            current.copy(
                pokemonName = pokemonName,
                typesSelected = typesSelected,
                balanceMap = offensiveMap
//                offensiveMap = offensiveMap,
//                defensiveMap = defensiveMap
            )
        }
    }

    private fun processAllTypes() = process {
        _byTypeScreenState.update { current -> current.copy(allTypes = repository.getAllTypes()) }
    }

    private fun processAllPokemon() = process(::onCompletionAllPokemon) {
        allPokemonFromList.apply {
            clear()
            addAll(repository.getAllPokemon())
        }
    }

    private fun onCompletionAllPokemon(it: Throwable?) {
        if (it == null) _homeState.update { current ->
            current.copy(byListEnabled = true)
        }
    }

    private fun clearSelection() {
        _typeSelectedState.update { emptyList() }
//        val emptyMap: EnumMap<Effectiveness, MutableList<Type>> = EnumMap(Effectiveness::class.java)
//        updateHomeState(
//            typesSelected = emptyList(),
//            offensiveMap = emptyMap,
//            defensiveMap = emptyMap
//        )
    }

    private fun handleListSelection(selection: PokemonFromList) = _homeState.update { current ->
        current.copy(
            pokemonName = selection.name,
            typesSelected = selection.typeList,
            balanceMap = balanceManager.offense(selection.typeList)
//            offensiveMap = balanceManager.offense(selection.typeList),
//            defensiveMap = balanceManager.defense(selection.typeList)
        )
    }

    private fun handleTypeSelection(position: Int) {
        val selected = _byTypeScreenState.value.allTypes[position]
        val current = _typeSelectedState.value
        when {
            selected in current -> {
                _typeSelectedState.update { types -> types - selected }
            }
            current.size < MAX_TYPE_AMOUNT -> {
                _typeSelectedState.update { types -> types + selected }
            }
        }

//        if (update) {
//            updateHomeState(
//                typesSelected = current,
//                offensiveMap = balanceManager.offense(current),
//                defensiveMap = balanceManager.defense(current)
//            )
//        }
    }

    private fun navigateTo(controller: NavController, route: String) =
        controller.navigate(route)

    private fun process(
        onCompletion: ((Throwable?) -> Unit)? = null,
        action: suspend () -> Unit
    ) {
        viewModelScope.launch { action() }.invokeOnCompletion { onCompletion?.invoke(it) }
    }

    private fun updateSide(isOffensive: Boolean) {
        if (_homeState.value.isOffensive != isOffensive)
            _homeState.update { current -> current.copy(isOffensive = isOffensive) }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
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
    }

    fun onEvent(event: ListSelectionEvent) {
        when (event) {
            is ListSelectionEvent.OnPokemonClick -> handleListSelection(event.selection)
            is ListSelectionEvent.OnLetterClick -> handleLetterClick(event.char)
        }
    }

    fun onEvent(event: TypeSelectionEvent) {
        when (event) {
            TypeSelectionEvent.OnClearClick -> clearSelection()
            is TypeSelectionEvent.OnTypeClick -> handleTypeSelection(event.position)
            is TypeSelectionEvent.OnDoneClick -> event.navController.navigateUp()
        }
    }

    fun hasMaxAmountSelected() = getTypesSelected().size >= MAX_TYPE_AMOUNT
}
