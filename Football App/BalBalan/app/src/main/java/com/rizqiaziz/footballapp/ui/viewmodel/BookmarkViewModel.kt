package com.rizqiaziz.footballapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizqiaziz.footballapp.model.Football
import com.rizqiaziz.footballapp.repository.FootballRepository
import com.rizqiaziz.footballapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BookmarkViewModel(private val repository: FootballRepository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<Football>>> = MutableStateFlow(UiState.Loading)
    val  uiState: StateFlow<UiState<List<Football>>>
        get() = _uiState

    fun getBookmarkNews() = viewModelScope.launch {
        repository.getBookmarkNews()
            .catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
            .collect{
                _uiState.value = UiState.Success(it)
            }
    }

    fun updateNews(id: Int, newState: Boolean){
        repository.updateNews(id, newState)
        getBookmarkNews()
    }
}