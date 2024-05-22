package com.rizqiaziz.footballapp.di

import com.rizqiaziz.footballapp.repository.FootballRepository

object Injection {
    fun provideRepository(): FootballRepository {
        return FootballRepository.getInstance()
    }
}