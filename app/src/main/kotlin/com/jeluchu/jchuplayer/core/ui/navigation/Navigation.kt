package com.jeluchu.jchuplayer.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.jeluchu.jchuplayer.core.ui.navigation.Feature
import com.jeluchu.jchuplayer.core.ui.navigation.ProvideNavHostController
import com.jeluchu.jchuplayer.core.ui.navigation.ProvideNavigate
import com.jeluchu.jchuplayer.core.ui.navigation.dashboardNav
import com.jeluchu.jchuplayer.core.ui.navigation.mp4SingleNav

@Composable
fun Navigation() = ProvideNavHostController { navHost ->
    ProvideNavigate { nav ->
        NavHost(
            navController = navHost,
            startDestination = Feature.DASHBOARD.route
        ) {
            dashboardNav(nav)
            mp4SingleNav(nav)
        }
    }
}
