package com.rizqiaziz.footballapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.rizqiaziz.FootballCompose
import com.rizqiaziz.footballapp.model.FootballData
import com.rizqiaziz.footballapp.navigation.Screen
import com.rizqiaziz.footballapp.ui.theme.BalBalAnTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class FootballComposeKtTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            BalBalAnTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                FootballCompose(navController = navController)
            }
        }
    }

    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    // klik item pada lazy list, lalu cek apakah item yang dituju sesuai dengan yang diharapkan
    @Test
    fun navHost_clickItem_navigatesToDetailWithData() {
        composeTestRule.onNodeWithTag("lazy_list").performScrollToIndex(5)
        composeTestRule.onNodeWithText(FootballData.dummyFootball[5].name).performClick()
        navController.assertCurrentRouteName(Screen.DetailNews.route)
        composeTestRule.onNodeWithText(FootballData.dummyFootball[5].name).assertIsDisplayed()
    }

    // melakukan navigasi antar screen, lalu cek apakah screen yang dituju sesuai dengan yang diharapkan
    @Test
    fun navHost_bottomNavigation_working() {
        composeTestRule.onNodeWithStringId(R.string.menu_bookmark).performClick()
        navController.assertCurrentRouteName(Screen.Bookmark.route)
        composeTestRule.onNodeWithStringId(R.string.menu_profile).performClick()
        navController.assertCurrentRouteName(Screen.Profile.route)
        composeTestRule.onNodeWithStringId(R.string.menu_home).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    // melakukan navigasi ke halaman about, lalu cek apakah data yang ditampilkan sesuai dengan yang diharapkan
    @Test
    fun navigateTo_AboutPage() {
        composeTestRule.onNodeWithStringId(R.string.menu_profile).performClick()
        navController.assertCurrentRouteName(Screen.Profile.route)
        composeTestRule.onNodeWithStringId(R.string.name_author).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.email_author).assertIsDisplayed()
    }

    // melakukan pencarian dengan keyword yang salah, lalu cek apakah data yang dicari tidak ada di list
    @Test
    fun searchShowEmptyListNews() {
        val incorrectSearch = "aa31z"
        composeTestRule.onNodeWithStringId(R.string.search_text).performTextInput(incorrectSearch)
        composeTestRule.onNodeWithTag("emptyList").assertIsDisplayed()
    }

    // melakukan pencarian dengan keyword yang benar, lalu cek apakah data yang dicari ada di list
    @Test
    fun searchShowListNews() {
        val rightSearch = "korupsi"
        composeTestRule.onNodeWithStringId(R.string.search_text).performTextInput(rightSearch)
        composeTestRule.onNodeWithText("korupsi").assertIsDisplayed()
    }

    // Klik favorite di detail screen, lalu cek apakah data favorite tersedia di favorite screen
    @Test
    fun bookmarkClickInDetailScreen_ShowInBookmarkScreen() {
        composeTestRule.onNodeWithText(FootballData.dummyFootball[0].name).performClick()
        navController.assertCurrentRouteName(Screen.DetailNews.route)
        composeTestRule.onNodeWithTag("bookmark_detail_button").performClick()
        composeTestRule.onNodeWithTag("back_home").performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithStringId(R.string.menu_bookmark).performClick()
        navController.assertCurrentRouteName(Screen.Bookmark.route)
        composeTestRule.onNodeWithText(FootballData.dummyFootball[0].name).assertIsDisplayed()
    }

    // Klik favorite dan delete favorite di detail screen, lalu cek apakah data tidak ada di favorite screen
    @Test
    fun bookmarkClickAndDeleteBookmarkInDetailScreen_NotShowInBookmarkScreen() {
        composeTestRule.onNodeWithText(FootballData.dummyFootball[0].name).performClick()
        navController.assertCurrentRouteName(Screen.DetailNews.route)
        composeTestRule.onNodeWithTag("bookmark_detail_button").performClick()
        composeTestRule.onNodeWithTag("bookmark_detail_button").performClick()
        composeTestRule.onNodeWithTag("back_home").performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithStringId(R.string.menu_bookmark).performClick()
        navController.assertCurrentRouteName(Screen.Bookmark.route)
        composeTestRule.onNodeWithStringId(R.string.empty_bookmark).assertIsDisplayed()
    }

    // Klik favorite di home screen, lalu cek apakah data favorite tersedia di favorite screen
    @Test
    fun bookmarkClickInHome_ShowInBookmarkScreen() {
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithTag("lazy_list").performScrollToIndex(0)
        composeTestRule.onNodeWithText(FootballData.dummyFootball[0].name).assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("item_bookmark_button").onFirst().performClick()
        composeTestRule.onNodeWithStringId(R.string.menu_bookmark).performClick()
        navController.assertCurrentRouteName(Screen.Bookmark.route)
        composeTestRule.onNodeWithText(FootballData.dummyFootball[0].name).assertIsDisplayed()
    }

    // Klik favorite dan delete favorite di home screen, lalu cek apakah data tidak ada di favorite screen
    @Test
    fun bookmarkClickAndDeleteFavoriteInHome_NotShowInBookmarkScreen() {
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithTag("lazy_list").performScrollToIndex(0)
        composeTestRule.onNodeWithText(FootballData.dummyFootball[0].name).assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("item_bookmark_button").onFirst().performClick()
        composeTestRule.onAllNodesWithTag("item_bookmark_button").onFirst().performClick()
        composeTestRule.onNodeWithStringId(R.string.menu_bookmark).performClick()
        navController.assertCurrentRouteName(Screen.Bookmark.route)
        composeTestRule.onNodeWithStringId(R.string.empty_bookmark).assertIsDisplayed()
    }
}
