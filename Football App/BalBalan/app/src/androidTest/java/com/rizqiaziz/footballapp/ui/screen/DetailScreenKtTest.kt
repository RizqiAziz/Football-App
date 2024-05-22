package com.rizqiaziz.footballapp.ui.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.rizqiaziz.footballapp.model.Football
import com.rizqiaziz.footballapp.R
import com.rizqiaziz.footballapp.ui.theme.BalBalAnTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailScreenKtTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeDataNews = Football(
        id = 0,
        name = "Curi Iphone 13 Milik Polisi",
        image = R.drawable.curiipon,
        description = "Tim Puma Satreskrim Polresta Mataram menangkap pencuri HP inisial AM (24), Rabu (22/11) kemarin. Korbannya ialah Muhammad Fadilah Zulni Prayoga, seorang anggota polisi yang bertugas di Biddokkes Polda NTB. Iya korbannya seorang anggota polisi, kata Kasatreskrim Polresta Mataram Kompol I Made Yogi Purusa Utama, Kamis (23/11). Tim Puma Satreskrim Polresta Mataram mengamankan AM di rumahnya di Desa Keruak, Kecamatan Keruak, Kabupaten Lombok Timur. Dari interogasi, pelaku mengaku pertama kali mencuri. Pencurian itu terjadi awal Oktober lalu, sekitar pukul 17.00 WITA. Awalnya pelaku hendak bermain futsal, namun karena melihat ada salah satu tas yang terbuka sedikit dan melihat iPhone, sehingga pelaku langsung timbul niat, sebutnya. Pelaku terpesona dengan iPhone 13 milik korban, sehingga tergerak mengambil HP mewah itu di saat korban sedang main futsal. Setelah jadi mengambil HP korban, pelaku tidak jadi bermain futsal dan memilih pulang, ujarnya. Selengkapnya di www.radarlombok.co.id",
        isBookmark = false
    )

    @Before
    fun setUp() {
        composeTestRule.setContent {
            BalBalAnTheme {
                DetailInformation(
                    id = fakeDataNews.id,
                    name = fakeDataNews.name,
                    image = fakeDataNews.image,
                    description = fakeDataNews.description,
                    isBookmark = fakeDataNews.isBookmark,
                    navigateBack = {},
                    onBookmarkButtonClicked = {_, _ ->}
                )
            }
        }
    }

    @Test
    fun detailInformation_isDisplayed() {
        composeTestRule.onNodeWithTag("scrollToBottom").performTouchInput {
            swipeUp()
        }
        composeTestRule.onNodeWithText(fakeDataNews.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDataNews.description).assertIsDisplayed()
    }

    @Test
    fun addToBookmarkButton_hasClickAction() {
        composeTestRule.onNodeWithTag("bookmark_detail_button").assertHasClickAction()
    }

    @Test
    fun detailInformation_isScrollable() {
        composeTestRule.onNodeWithTag("scrollToBottom").performTouchInput {
            swipeUp()
        }
    }

    @Test
    fun bookmarkButton_hasCorrectStatus() {
        // Assert that the bookmark button is displayed
        composeTestRule.onNodeWithTag("bookmark_detail_button").assertIsDisplayed()

        // Assert that the content description of the bookmark button is correct based on the isBookmark state
        val isBookmark = fakeDataNews.isBookmark // Set the isBookmark state here
        val expectedContentDescription = if (isBookmark) {
            "Remove from Bookmark"
        } else {
            "Add to Bookmark"
        }

        composeTestRule.onNodeWithTag("bookmark_detail_button")
            .assertContentDescriptionEquals(expectedContentDescription)
    }
}