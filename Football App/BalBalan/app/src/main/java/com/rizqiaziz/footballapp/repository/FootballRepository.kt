package com.rizqiaziz.footballapp.repository

import com.rizqiaziz.footballapp.model.Football
import com.rizqiaziz.footballapp.model.FootballData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FootballRepository {
    private val dummyFootball = mutableListOf<Football>()

    init {
        if (dummyFootball.isEmpty()){
            FootballData.dummyFootball.forEach{
                dummyFootball.add(it)
            }
        }
    }

    fun getNewsById(footballId: Int): Football {
        return dummyFootball.first {
            it.id == footballId
        }
    }

    fun getBookmarkNews(): Flow<List<Football>> {
        return flowOf(dummyFootball.filter { it.isBookmark })
    }

    fun searchNews(query: String) = flow {
        val data = dummyFootball.filter {
            it.name.contains(query, ignoreCase = true)
        }
        emit(data)
    }

    fun updateNews(newsId: Int, newState: Boolean): Flow<Boolean> {
        val index = dummyFootball.indexOfFirst { it.id == newsId }
        val result = if (index >= 0) {
            val news = dummyFootball[index]
            dummyFootball[index] = news.copy(isBookmark = newState)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    companion object{
        @Volatile
        private var instance: FootballRepository? = null

        fun getInstance(): FootballRepository =
            instance ?: synchronized(this){
                FootballRepository().apply {
                    instance = this
                }
            }
    }
}
