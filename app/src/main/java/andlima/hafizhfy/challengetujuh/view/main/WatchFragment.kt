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
    private val videoURL = "https://rr7---sn-poqvn5u-jb3k.googlevideo.com/videoplayback?expire=1653767272&ei=ByiSYpq3MsWGvwT_8oGQBA&ip=114.5.251.41&id=o-AMYThYtjkAink6s37BqHLt1E0BR7GVQaI03ONWUsyCve&itag=18&source=youtube&requiressl=yes&mh=xI&mm=31%2C26&mn=sn-poqvn5u-jb3k%2Csn-un57enel&ms=au%2Conr&mv=m&mvi=7&pl=23&initcwndbps=141250&spc=4ocVCyqEOzVt0RjiBSQzyUWQA3O5&vprv=1&mime=video%2Fmp4&ns=9JEhakJFZvRz7nQB_uDDvZYG&gir=yes&clen=7514569&ratebypass=yes&dur=263.848&lmt=1650185213981309&mt=1653745268&fvip=1&fexp=24001373%2C24007246&beids=23886202&c=WEB&txp=4538434&n=T_CAYLNHi_zkWt2dTO9g&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cspc%2Cvprv%2Cmime%2Cns%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&lsparams=mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AG3C_xAwRQIgZR1Ug4mx5hNEQVWRLD4FMXrq1BlrGx-3KxX9q1PbGAUCIQC7i2-eVVj5puNa1k8Dy_p8v6vPUPcyAEPPMwUnALlnHw%3D%3D&sig=AOq0QJ8wRQIgd_EdWur-q7Uw5zxwi8MJjEmZW0_3Bgdbq34S1K1SLncCIQCuX1mVvIlrnGutmn6ZigBF9foFGpYQL7R3NXNYPHRCkQ%3D%3D"

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