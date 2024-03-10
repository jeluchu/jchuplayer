package com.jeluchu.jchuplayer.core.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.jeluchu.jchuplayer.core.utils.DestinationsIds
import com.jeluchu.jchuplayer.core.utils.NavigationIds
import com.jeluchu.jchuplayer.features.dashboard.MainView
import com.jeluchu.jchuplayer.video.VideoPlayerView

fun NavGraphBuilder.dashboardNav(nav: Destinations) {
    composable(Feature.DASHBOARD.nav) {
        MainView { id ->
            when (id) {
                DestinationsIds.mp4Player -> nav.goToSingleMp4()
            }
        }
    }
}

fun NavGraphBuilder.mp4SingleNav(nav: Destinations) {
    navigation(
        startDestination = Feature.MP4_SINGLE_PLAYER.route,
        route = NavigationIds.mp4Player
    ) {
        composable(Feature.MP4_SINGLE_PLAYER.nav) {
            VideoPlayerView(
                title = "Big Buck Bunny",
                url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            )
        }
    }
}
