package com.jeluchu.jchuplayer.core.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavItem(
    internal val feature: Feature,
    private val arguments: List<NavArgs> = emptyList()
) {

    class ContentScreen(feature: Feature) : NavItem(feature)

    class ContentPlayer(feature: Feature) : NavItem(feature, listOf(NavArgs.ItemLink)) {
        fun createRoute(link: String) = "${feature.route}/$link"
    }

    val route = run {
        val argValues = arguments.map { "{${it.key}}" }
        listOf(feature.route)
            .plus(argValues)
            .joinToString("/")
    }

    val args = arguments.map {
        navArgument(it.key) { type = it.navType }
    }
}

enum class NavArgs(
    val key: String,
    val navType: NavType<*>
) {
    ItemLink("itemLink", NavType.StringType)
}