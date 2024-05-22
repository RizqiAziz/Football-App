package com.rizqiaziz.footballapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rizqiaziz.footballapp.repository.FootballRepository

class ViewModelFactory(private val repository: FootballRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)){
            return BookmarkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " +modelClass.name)
    }
}