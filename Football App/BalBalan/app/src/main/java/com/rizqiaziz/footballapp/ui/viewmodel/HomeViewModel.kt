package com.rizqiaziz.footballapp.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizqiaziz.footballapp.model.Football
import com.rizqiaziz.footballapp.repository.FootballRepository
import com.rizqiaziz.footballapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: FootballRepository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<Football>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Football>>>
        get() = _uiState

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) = viewModelScope.launch {
        _query.value = newQuery
        repository.searchNews(_query.value)
            .catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
            .collect{
                _uiState.value = UiState.Success(it)
            }
    }

    fun updateNews(id: Int, newState: Boolean) = viewModelScope.launch {
        repository.updateNews(id, newState)
            .collect{isUpdated ->
                if (isUpdated) search(_query.value)
            }
    }
}