package com.jeluchu.jchuplayer.core.ui.navigation

enum class Feature(val route: String) {
    DASHBOARD("dashboard"),
    MP4_SINGLE_PLAYER("mp4SinglePlayer"),
}

val Feature.baseRoute: String
    get() = NavItem.ContentScreen(this).route

val Feature.nav: NavItem.ContentScreen
    get() = NavItem.ContentScreen(this)
