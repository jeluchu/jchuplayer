package com.jeluchu.jchuplayer.core.ui.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.jeluchu.jchucomponents.ktx.navigation.lifecycleIsResumed

class Destinations(private val navController: NavHostController) {

    private fun String.navigate() = navController.navigate(this)

    val goToDashboard: () -> Unit = { Feature.DASHBOARD.route.navigate() }
    val goToPlayer: (String) -> Unit = { url ->
        navController.navigate(NavItem.ContentPlayer(Feature.PLAYER).createRoute(url))
    }

    val goBack: (from: NavBackStackEntry) -> Unit = { from ->
        if (from.lifecycleIsResumed()) navController.popBackStack()
    }
}