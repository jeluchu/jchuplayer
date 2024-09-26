package com.jeluchu.jchuplayer.core.commons.models

import com.jeluchu.jchuplayer.core.utils.DestinationsIds
import com.jeluchu.jchuplayer.core.utils.Names

data class MenuOptions(
    val id: String,
    val name: String,
    val url: String,
) {
    companion object {
        val ui = listOf(
            MenuOptions(
                id = DestinationsIds.mp4Player,
                name = Names.mp4Player,
                url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            )
        )
    }
}