package com.jeluchu.jchuplayer.core.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.jeluchu.jchuplayer.core.utils.DestinationsIds
import com.jeluchu.jchuplayer.core.utils.NavigationIds
import com.jeluchu.jchuplayer.features.dashboard.MainView
import com.jeluchu.jchuplayer.video.VideoPlayerView
import java.net.URLEncoder

fun NavGraphBuilder.dashboardNav(nav: Destinations) {
    composable(Feature.DASHBOARD.nav) {
        MainView { option ->
            when (option.id) {
                DestinationsIds.mp4Player -> nav.goToPlayer(
                    URLEncoder.encode(option.url, Charsets.UTF_8.toString())
                )
            }
        }
    }
}

fun NavGraphBuilder.playerNav(nav: Destinations) {
    navigation(
        startDestination = Feature.PLAYER.route,
        route = NavigationIds.player
    ) {
        composable(NavItem.ContentPlayer(Feature.PLAYER)) { backStackEntry ->
            VideoPlayerView(
                title = "Example Video Test",
                url = backStackEntry.findArg(NavArgs.ItemLink),
            )
        }
    }
}
