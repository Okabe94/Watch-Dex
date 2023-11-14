package com.watch_dex.feature_main.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.watch_dex.core.data.Type
import com.watch_dex.core.domain.BalanceManager
import com.watch_dex.core.domain.dto.PokemonDTO
import com.watch_dex.core.presentation.util.navigation.Screen
import com.watch_dex.feature_home.data.datasource.dao.PokemonDao
import com.watch_dex.feature_home.presentation.state.HomeEvent
import com.watch_dex.feature_home.presentation.state.HomeState
import com.watch_dex.feature_list_selection.presentation.event.ListSelectionEvent
import com.watch_dex.feature_list_selection.presentation.state.ListSelectionState
import com.watch_dex.feature_main.presentation.state.MainSelectedState
import com.watch_dex.feature_type_selection.presentation.state.TypeSelectionEvent
import com.watch_dex.feature_type_selection.presentation.state.TypeSelectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val MAX_TYPE_AMOUNT = 2

class MainViewModel(
    private val pokemonDao: PokemonDao,
    private val balanceManager: BalanceManager,
    private val theme: Type,
    allTypes: List<Type>
) : ViewModel() {
    private val typesSelectedState = MutableStateFlow(MainSelectedState())

    // Screen states
    private val _homeScreenState = MutableStateFlow(HomeState(theme = theme))
    private val _listScreenState = MutableStateFlow(ListSelectionState(theme = theme))
    private val _typeScreenState = MutableStateFlow(TypeSelectionState(allTypes = allTypes))
    // ****

    // Public screen states
    val homeScreenState = combine(_homeScreenState, typesSelectedState) { state, selected ->
        HomeState(
            theme,
            selected.typesSelected,
            state.isOffensive,
            selected.pokemonDTO,
            state.byListEnabled,
            balanceManager.getBalanceMap(state.isOffensive, selected.typesSelected)
        )
    }.distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), HomeState(theme = theme))

    val byTypeScreenState = combine(_typeScreenState, typesSelectedState) { state, selected ->
        TypeSelectionState(
            selected.typesSelected,
            selected.typesSelected.size >= MAX_TYPE_AMOUNT,
            state.allTypes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), TypeSelectionState())

    val byListScreenState = _listScreenState.asStateFlow()
    // ****

    private fun getPokemonByChar(char: Char?) {
        if (char == null) {
            _listScreenState.update { current ->
                current.copy(letterSelected = null, pokemonDisplayed = emptyList())
            }
            return
        }

        viewModelScope.launch {
            pokemonDao.getPokemonByInitial(char.toString())
                .map { list -> list.map { it.toDTO() } }
                .collectLatest {
                    _listScreenState.update { current ->
                        current.copy(letterSelected = char, pokemonDisplayed = it)
                    }
                }
        }
    }

    private fun handleLetterClick(char: Char?) {
        val newLetterSelected = if (char == _listScreenState.value.letterSelected) null else char
        getPokemonByChar(newLetterSelected)
    }

    private fun clearSelection() = typesSelectedState.update { current ->
        current.copy(typesSelected = emptyList())
    }

    private fun navigateTo(controller: NavController, route: String) =
        controller.navigate(route)

    private fun updateSide(
        isOffensive: Boolean
    ) = _homeScreenState.update { current -> current.copy(isOffensive = isOffensive) }

    private fun handleListSelection(
        selection: PokemonDTO
    ) = typesSelectedState.update { current ->
        current.copy(pokemonDTO = selection, typesSelected = selection.types)
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
            current.copy(pokemonDTO = null, typesSelected = newSelected)
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
