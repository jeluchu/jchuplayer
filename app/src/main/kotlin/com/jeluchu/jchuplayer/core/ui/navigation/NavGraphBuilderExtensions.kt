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
                url = "https://be2719.rcr22.ams01.cdn112.com/hls2/03/06361/6w9l9bi85l7s_h/master.m3u8?t=PyhdRU0LLCvMkqc679APGG-c7oTXto0DP0DydH6XHyU&s=1724187308&e=10800&f=31914655&srv=53&asn=6147&sp=4000",
                embedUrl = "https://filemoon.sx/e/181ckmgidim3",
            )
        }
    }
}
