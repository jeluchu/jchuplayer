package com.jeluchu.jchuplayer.video

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.jeluchu.jchuplayer.core.extensions.findActivity
import com.jeluchu.jchuplayer.core.mode.RepeatMode
import com.jeluchu.jchuplayer.core.mode.ResizeMode
import com.jeluchu.jchuplayer.core.mode.toExoPlayerRepeatMode
import com.jeluchu.jchuplayer.core.states.rememberExoPlayer
import com.jeluchu.jchuplayer.core.states.rememberMediaState
import com.jeluchu.jchuplayer.core.utils.enterPIPMode

@Composable
fun VideoPlayerView(
    modifier: Modifier = Modifier,
    url: String,
    title: String,
    seekBack: Long = 10000L,
    autoPlay: Boolean = true,
    seekForward: Long = 10000L,
    resizeMode: ResizeMode = ResizeMode.Fit,
    showBuffering: ShowBuffering = ShowBuffering.Always
) {
    val link by rememberSaveable { mutableStateOf(url) }
    val setPlayer by rememberSaveable { mutableStateOf(true) }
    val resize by rememberSaveable { mutableStateOf(resizeMode) }
    val useArtwork by rememberSaveable { mutableStateOf(true) }
    val playWhenReady by rememberSaveable { mutableStateOf(autoPlay) }
    val controllerAutoShow by rememberSaveable { mutableStateOf(true) }
    val buffering by rememberSaveable { mutableStateOf(showBuffering) }
    val controllerHideOnTouch by rememberSaveable { mutableStateOf(true) }
    val keepContentOnPlayerReset by rememberSaveable { mutableStateOf(false) }

    VideoPlayerView(
        modifier = modifier,
        url = link,
        title = title,
        resizeMode = resize,
        seekBack = seekBack,
        setPlayer = setPlayer,
        useArtwork = useArtwork,
        seekForward = seekForward,
        showBuffering = buffering,
        playWhenReady = playWhenReady,
        controllerAutoShow = controllerAutoShow,
        controllerHideOnTouch = controllerHideOnTouch,
        keepContentOnPlayerReset = keepContentOnPlayerReset
    )
}

@Composable
internal fun VideoPlayerView(
    modifier: Modifier = Modifier,
    url: String,
    title: String,
    seekBack: Long,
    seekForward: Long,
    setPlayer: Boolean,
    useArtwork: Boolean,
    playWhenReady: Boolean,
    controllerAutoShow: Boolean,
    controllerHideOnTouch: Boolean,
    keepContentOnPlayerReset: Boolean,
    resizeMode: ResizeMode = ResizeMode.Fit,
    showBuffering: ShowBuffering = ShowBuffering.Always,
) {
    val player by rememberExoPlayer(
        url = url,
        seekBack = seekBack,
        seekForward = seekForward
    )
    var repeatMode by rememberSaveable { mutableStateOf( RepeatMode.NONE) }
    val state = rememberMediaState(player = player.takeIf { setPlayer })

    DisposableEffect(player, playWhenReady) {
        player?.playWhenReady = playWhenReady
        onDispose {}
    }

    LocalContext.current.findActivity()?.let { activity ->
        val enterFullscreen = { activity.requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        }
        val exitFullscreen = {
            @SuppressLint("SourceLockedOrientationActivity")
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
        }

        WindowInsetsControllerCompat(activity.window, activity.window.decorView).apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        val content = remember {
            movableContentOf { isLandscape: Boolean, modifier: Modifier ->
                Media(
                    modifier = modifier.background(Color.Black),
                    resizeMode = resizeMode,
                    state = state,
                    keepContentOnPlayerReset = keepContentOnPlayerReset,
                    useArtwork = useArtwork,
                    showBuffering = showBuffering,
                    buffering = {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    },
                    errorMessage = { error ->
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            Text(
                                error.message ?: "",
                                modifier = Modifier
                                    .background(Color(0x80808080), RoundedCornerShape(16.dp))
                                    .padding(horizontal = 12.dp, vertical = 4.dp),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                    controllerHideOnTouch = controllerHideOnTouch,
                    controllerAutoShow = controllerAutoShow,
                    controller =  {
                        PlayerController(
                            mediaState = state,
                            title = title,
                            repeatMode = repeatMode,
                            isFullScreen = isLandscape,
                            modifier = Modifier.fillMaxSize(),
                            actions = VideoControllerActions(
                                onSeekBackRequest = { player?.seekBack() },
                                onSeekForwardRequest = { player?.seekForward() },
                                onRepeatModeRequest = {
                                    repeatMode = when (repeatMode) {
                                        RepeatMode.NONE -> RepeatMode.ALL
                                        RepeatMode.ALL -> RepeatMode.ONE
                                        RepeatMode.ONE -> RepeatMode.NONE
                                    }
                                    player?.repeatMode = repeatMode.toExoPlayerRepeatMode()
                                },
                                onFullScreenRequest = {
                                    if (isLandscape) exitFullscreen() else enterFullscreen()
                                },
                                onPictureInPictureRequest = {
                                    player?.let { enterPIPMode(activity, it) }
                                    player?.play()
                                }
                            )
                        )
                    }
                )
            }
        }

        content(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE, modifier)
    }
}