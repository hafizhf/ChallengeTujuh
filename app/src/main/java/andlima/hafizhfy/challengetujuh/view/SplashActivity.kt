package andlima.hafizhfy.challengetujuh.view

import andlima.hafizhfy.challengetujuh.R
import andlima.hafizhfy.challengetujuh.di.UserClient
import andlima.hafizhfy.challengetujuh.func.alertDialog
import andlima.hafizhfy.challengetujuh.func.matchUserLocalAndDatabase
import andlima.hafizhfy.challengetujuh.func.toast
import andlima.hafizhfy.challengetujuh.func.updateUserDataRealTime
import andlima.hafizhfy.challengetujuh.local.datastore.UserManager
import andlima.hafizhfy.challengetujuh.model.user.GetUserItem
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.WindowManager
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        matchUserLocalAndDatabase(this, this) { result, response, message ->
            Handler(Looper.getMainLooper()).postDelayed({
                updateUserDataRealTime(this, result, response, message)
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 3000)
        }
    }
}