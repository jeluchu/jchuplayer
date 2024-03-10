package com.jeluchu.jchuplayer.video


import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jeluchu.jchuplayer.core.extensions.msToTime
import com.jeluchu.jchuplayer.R
import com.jeluchu.jchuplayer.core.mode.RepeatMode
import com.jeluchu.jchuplayer.core.states.MediaState
import com.jeluchu.jchuplayer.core.states.rememberControllerState
import com.jeluchu.jchuplayer.core.ui.TimeBar
import kotlinx.coroutines.delay

@Composable
fun PlayerController(
    modifier: Modifier = Modifier,
    title: String,
    isFullScreen: Boolean,
    mediaState: MediaState,
    repeatMode: RepeatMode = RepeatMode.NONE,
    icons: VideoControllerIcons = VideoControllerIcons(),
    actions: VideoControllerActions = VideoControllerActions(),
) = Crossfade(targetState = mediaState.isControllerShowing, modifier, label = "") { isShowing ->
    if (isShowing) {
        val controllerState = rememberControllerState(mediaState)
        var scrubbing by remember { mutableStateOf(false) }
        val hideWhenTimeout = !mediaState.shouldShowControllerIndefinitely && !scrubbing
        var hideEffectReset by remember { mutableIntStateOf(0) }
        LaunchedEffect(hideWhenTimeout, hideEffectReset) {
            if (hideWhenTimeout) {
                delay(3000)
                mediaState.isControllerShowing = false
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x98000000))
                .padding(10.dp)
        ) {
            Text(
                text = title,
                color = Color.White
            )

            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    painter = painterResource(icons.seekBack),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { actions.onSeekBackRequest() },
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Image(
                    painter = painterResource(if (controllerState.showPause) icons.pause else icons.play),
                    contentDescription = null,
                    modifier = Modifier
                        .size(52.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            hideEffectReset++
                            controllerState.playOrPause()
                        },
                    colorFilter = ColorFilter.tint(Color.White)
                )

                Image(
                    painter = painterResource(icons.seekForward),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { actions.onSeekForwardRequest() },
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(200)
                    controllerState.triggerPositionUpdate()
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .systemGestureExclusion()
            ) {
                TimeBar(
                    controllerState.durationMs,
                    controllerState.positionMs,
                    controllerState.bufferedPositionMs,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp),
                    contentPadding = PaddingValues(12.dp),
                    scrubberCenterAsAnchor = true,
                    onScrubStart = { scrubbing = true },
                    onScrubStop = { positionMs ->
                        scrubbing = false
                        controllerState.seekTo(positionMs)
                    },
                    scrubber = { enabled, _ ->
                        if (enabled) {
                            Icon(
                                modifier = Modifier.size(35.dp),
                                tint = Color.White,
                                painter = painterResource(icons.timeBar),
                                contentDescription = null,
                            )
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${msToTime(controllerState.positionMs)} · ${msToTime(controllerState.durationMs)}",
                        color = Color.White
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { actions.onRepeatModeRequest() },
                            tint = Color.White.copy(
                                if (repeatMode == RepeatMode.NONE) .5f else 1f
                            ),
                            painter = painterResource(if (repeatMode == RepeatMode.ONE) R.drawable.ic_deco_repeat_one
                            else R.drawable.ic_deco_repeat),
                            contentDescription = null,
                        )

                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { actions.onPictureInPictureRequest() },
                            tint = Color.White,
                            painter = painterResource(icons.pictureInPicture),
                            contentDescription = null,
                        )

                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { actions.onFullScreenRequest() },
                            tint = Color.White,
                            painter = painterResource(if (isFullScreen) icons.exitFullScreen
                                else icons.fullScreen
                            ),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Immutable
class VideoControllerIcons(
    @DrawableRes val play: Int = R.drawable.ic_deco_play,
    @DrawableRes val pause: Int = R.drawable.ic_deco_pause,
    @DrawableRes val timeBar: Int = R.drawable.ic_deco_alpi,
    @DrawableRes val fullScreen: Int = R.drawable.ic_deco_expand,
    @DrawableRes val pictureInPicture: Int = R.drawable.ic_deco_pip,
    @DrawableRes val exitFullScreen: Int = R.drawable.ic_deco_collapse,
    @DrawableRes val seekBack: Int = R.drawable.ic_deco_ten_seconds_left,
    @DrawableRes val seekForward: Int = R.drawable.ic_deco_ten_seconds_right
)

@Immutable
class VideoControllerActions(
    val onSeekBackRequest: () -> Unit = {},
    val onFullScreenRequest: () -> Unit = {},
    val onRepeatModeRequest: () -> Unit = {},
    val onSeekForwardRequest: () -> Unit = {},
    val onPictureInPictureRequest: () -> Unit = {}
)