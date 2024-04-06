package com.jeluchu.jchuplayer.core.states

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.jeluchu.jchuplayer.core.cache.VideoPlayerCacheManager

@OptIn(UnstableApi::class)
@Composable
fun rememberExoPlayer(
    url: String,
    seekBack: Long,
    seekForward: Long
): State<Player?> = rememberManagedPlayer { context ->
    ExoPlayer.Builder(context)
        .setSeekForwardIncrementMs(seekBack)
        .setSeekBackIncrementMs(seekForward)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                .setUsage(C.USAGE_MEDIA)
                .build(),
            true,
        )
        .apply {
            val cache = VideoPlayerCacheManager.getCache()
            if (cache != null) {
                val cacheDataSourceFactory = CacheDataSource.Factory()
                    .setCache(cache)
                    .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context, DefaultHttpDataSource.Factory()))
                setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            }
        }
        .build()
        .apply {
            setMediaSource(
                if (url.contains("m3u8")) HlsMediaSource.Factory(DefaultHttpDataSource.Factory())
                    .createMediaSource(MediaItem.fromUri(url))
                else ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                    .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
            )

            prepare()

            playWhenReady = true
        }
}
