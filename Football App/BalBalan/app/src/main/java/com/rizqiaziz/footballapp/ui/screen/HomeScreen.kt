package com.rizqiaziz.footballapp.ui.screen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rizqiaziz.footballapp.R
import com.rizqiaziz.footballapp.di.Injection
import com.rizqiaziz.footballapp.model.Football
import com.rizqiaziz.footballapp.ui.common.UiState
import com.rizqiaziz.footballapp.ui.item.EmptyList
import com.rizqiaziz.footballapp.ui.item.FootballItem
import com.rizqiaziz.footballapp.ui.item.SearchButton
import com.rizqiaziz.footballapp.ui.viewmodel.HomeViewModel
import com.rizqiaziz.footballapp.ui.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Int) -> Unit
) {
    val query by viewModel.query
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when(uiState){
            is UiState.Loading -> {
                viewModel.search(query)
            }
            is UiState.Success -> {
                HomeContent(
                    query = query,
                    onQueryChange = viewModel::search,
                    listNews = uiState.data,
                    onBookmarkIconClicked = {id, newState ->
                        viewModel.updateNews(id, newState)
                    },
                    navigateToDetail = navigateToDetail
                )
            }
            is UiState.Error ->{}
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun HomeContent(
    query: String,
    onQueryChange: (String)-> Unit,
    listNews: List<Football>,
    onBookmarkIconClicked : (id: Int, newstate: Boolean)-> Unit,
    navigateToDetail: (Int) -> Unit
){
    Column {
        SearchButton(
            query = query ,
            onQueryChange =onQueryChange
        )
        if(listNews.isNotEmpty()){
            ListNews(
                listNews = listNews,
                onBookmarkIconClicked = onBookmarkIconClicked,
                navigateToDetail = navigateToDetail
            )
        }else{
            EmptyList(
                Warning = stringResource(R.string.empty_data),
                modifier = Modifier
                    .testTag("emptyList")
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListNews(
    listNews: List<Football>,
    onBookmarkIconClicked: (id: Int, newstate: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
    contentPaddingTop: Dp = 16.dp
){
    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
            top = contentPaddingTop
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .testTag("lazy_list")
    ){
        items(listNews, key = {it.id}){ item ->
            FootballItem(
                id = item.id,
                name = item.name,
                image = item.image,
                isBookmark = item.isBookmark,
                onBookmarkIconClicked = onBookmarkIconClicked,
                modifier = Modifier
                    .animateItemPlacement(tween(durationMillis = 200))
                    .clickable { navigateToDetail(item.id) }
            )
        }
    }
}