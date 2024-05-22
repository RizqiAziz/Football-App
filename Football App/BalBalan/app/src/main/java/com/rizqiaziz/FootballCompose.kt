package com.rizqiaziz

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rizqiaziz.footballapp.R
import com.rizqiaziz.footballapp.navigation.NavigationItem
import com.rizqiaziz.footballapp.navigation.Screen
import com.rizqiaziz.footballapp.ui.screen.AboutScreen
import com.rizqiaziz.footballapp.ui.screen.BookmarkScreen
import com.rizqiaziz.footballapp.ui.screen.DetailScreen
import com.rizqiaziz.footballapp.ui.screen.HomeScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FootballCompose(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if(currentRoute != Screen.DetailNews.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding) ){
            composable(Screen.Home.route){
                HomeScreen(
                    navigateToDetail = { newsId ->
                        navController.navigate(Screen.DetailNews.createRoute(newsId))
                    })
            }
            composable(Screen.Bookmark.route){
                BookmarkScreen(
                    navigateToDetail = { footballId ->
                        navController.navigate(Screen.DetailNews.createRoute(footballId))
                    })
            }
            composable(Screen.Profile.route){
                AboutScreen()
            }
            composable(
                route = Screen.DetailNews.route,
                arguments = listOf(
                    navArgument("newsId"){type = NavType.IntType}
                )
            ){
                val id = it.arguments?.getInt("newsId") ?: -1
                DetailScreen(newsId = id, navigateBack = { navController.navigateUp()})
            }
        }

    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = stringResource(R.string.menu_bookmark),
                icon = Icons.Default.Favorite,
                screen = Screen.Bookmark
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile
            )
        )
        NavigationBar{
            navigationItems.map { item ->
                NavigationBarItem(
                    icon = {
                        Icon(imageVector = item.icon, contentDescription = item.title)

                    },
                    label = { Text(item.title)},
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}