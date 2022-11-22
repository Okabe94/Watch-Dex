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
import com.watch_dex.feature_home.presentation.state.Side
import com.watch_dex.feature_list_selection.presentation.event.ListSelectionEvent
import com.watch_dex.feature_list_selection.presentation.state.ListSelectionState
import com.watch_dex.feature_type_selection.data.repository.PokemonRepositoryImpl
import com.watch_dex.feature_type_selection.presentation.state.TypeSelectionEvent
import com.watch_dex.feature_type_selection.presentation.state.TypeSelectionState
import java.util.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val MAX_TYPE_AMOUNT = 2

class MainViewModel : ViewModel() {
    private val balanceManager = BalanceManager()
    private val repository = PokemonRepositoryImpl()
    private val allPokemonFromList = mutableListOf<PokemonFromList>()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState

    private val _typeState = MutableStateFlow(TypeSelectionState())
    val typeState: StateFlow<TypeSelectionState> = _typeState

    private val _pokemonState = MutableStateFlow(ListSelectionState())
    val pokemonState: StateFlow<ListSelectionState> = _pokemonState

    init {
        processAllPokemon()
        processAllTypes()
    }

    private fun getTypesSelected() = _homeState.value.typesSelected

    private fun getPokemonByChar(char: Char?) {
        val newToDisplay = if (char == null) emptyList()
        else allPokemonFromList
            .filter { it.name.startsWith(char, true) }
            .sortedBy { it.name }
        _pokemonState.update { current ->
            current.copy(letterSelected = char, pokemonDisplayed = newToDisplay)
        }
    }

    private fun handleLetterClick(char: Char?) {
        val newLetterSelected = if (char == _pokemonState.value.letterSelected) null else char
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
                offensiveMap = offensiveMap,
                defensiveMap = defensiveMap
            )
        }
    }

    private fun processAllTypes() = process {
        _typeState.update { current -> current.copy(allTypes = repository.getAllTypes()) }
    }

    private fun processAllPokemon() = process(::onCompletionAllPokemon) {
        allPokemonFromList.apply {
            clear()
            addAll(repository.getAllPokemon())
        }
    }

    private fun onCompletionAllPokemon(it: Throwable?) {
        if (it == null) _homeState.update { current ->
            current.copy(byPokemonEnabled = true)
        }
    }

    private fun clearSelection() {
        val emptyMap: EnumMap<Effectiveness, MutableList<Type>> = EnumMap(Effectiveness::class.java)
        updateHomeState(
            typesSelected = emptyList(),
            offensiveMap = emptyMap,
            defensiveMap = emptyMap
        )
    }

    private fun handleListSelection(selection: PokemonFromList) = _homeState.update { current ->
        current.copy(
            pokemonName = selection.name,
            typesSelected = selection.typeList,
            offensiveMap = balanceManager.offense(selection.typeList),
            defensiveMap = balanceManager.defense(selection.typeList)
        )
    }

    private fun handleTypeSelection(position: Int) {
        val selection = _typeState.value.allTypes[position]
        val current = getTypesSelected().toMutableList()
        val update = when {
            selection in current -> {
                current.remove(selection)
                true
            }
            current.size < MAX_TYPE_AMOUNT -> {
                current.add(selection)
                true
            }
            else -> false
        }

        if (update) {
            updateHomeState(
                typesSelected = current,
                offensiveMap = balanceManager.offense(current),
                defensiveMap = balanceManager.defense(current)
            )
        }
    }

    private fun navigateTo(controller: NavController, route: String) =
        controller.navigate(route)

    private fun process(
        onCompletion: ((Throwable?) -> Unit)? = null,
        action: suspend () -> Unit
    ) {
        viewModelScope.launch { action() }.invokeOnCompletion { onCompletion?.invoke(it) }
    }

    private fun updateSide(side: Side) {
        if (_homeState.value.side != side)
            _homeState.update { current -> current.copy(side = side) }
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
            HomeEvent.SelectDefensive -> updateSide(Side.Defensive)
            HomeEvent.SelectOffensive -> updateSide(Side.Offensive)
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
