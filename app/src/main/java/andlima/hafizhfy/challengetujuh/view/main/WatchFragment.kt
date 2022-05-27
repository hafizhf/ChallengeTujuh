package andlima.hafizhfy.challengetujuh.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.challengetujuh.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import kotlinx.android.synthetic.main.fragment_watch.*

class WatchFragment : Fragment() {

    private var mPlayer: ExoPlayer? = null
    private val videoURL = "https://rr6---sn-poqvn5u-jb3d.googlevideo.com/videoplayback?expire=1653676081&ei=0MOQYvLZNPm2rtoP44Cv8AI&ip=114.5.251.41&id=o-AAIY1Uz7rn8eEQBCUNvgVaKeMi2zpPxHgV-VWseJWkCg&itag=18&source=youtube&requiressl=yes&mh=hs&mm=31%2C26&mn=sn-poqvn5u-jb3d%2Csn-30a7yne7&ms=au%2Conr&mv=m&mvi=6&pl=23&initcwndbps=160000&spc=4ocVC9YCZ2KaZr_i1bYO7PqOAZ9w&vprv=1&mime=video%2Fmp4&ns=z0aGcO2lv_9rGA-z1AnScN8G&gir=yes&clen=10492665&ratebypass=yes&dur=149.443&lmt=1611492930153347&mt=1653654116&fvip=2&fexp=24001373%2C24007246&c=WEB&txp=5438432&n=qhBZ8Ji5INY_GtNvP6p7&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cspc%2Cvprv%2Cmime%2Cns%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&lsparams=mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AG3C_xAwRQIgLbBpn37Foqz9wiZelPihUFbilYOotNVo8tlTnN0mf1YCIQCggoffgTvLf6d8YIHVwesuNddHYlgMx8Muf3IN0CtF2Q%3D%3D&sig=AOq0QJ8wRgIhAP62-XvGeSWGwveJfz6sVEtosURqdSNVx44W8hKQAVx4AiEAiM2mxvMh8p4r5GrmGX1F43LeI2MMjpT3EYFo3lzyafE%3D"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewExoPlayer()
    }

    private fun viewExoPlayer() {
        mPlayer = ExoPlayer.Builder(requireContext()).build()
        video_player.player = mPlayer
        video_player_control.player = mPlayer

        mPlayer!!.playWhenReady = true
        mPlayer!!.setMediaSource(buildMediaSource())
        mPlayer!!.prepare()

    }

    private fun buildMediaSource(): MediaSource {
        // Create a data source factory.
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

        // Create a progressive media source pointing to a stream uri.
        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoURL))

        return mediaSource
    }

    private fun releasePlayer() {
        if (mPlayer == null) {
            return
        }
        mPlayer!!.release()
        mPlayer = null
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
}