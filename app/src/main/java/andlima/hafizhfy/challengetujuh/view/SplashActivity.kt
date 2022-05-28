package andlima.hafizhfy.challengetujuh.view

import andlima.hafizhfy.challengetujuh.R
import andlima.hafizhfy.challengetujuh.func.matchUserLocalAndDatabase
import andlima.hafizhfy.challengetujuh.func.updateUserDataRealTime
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import andlima.hafizhfy.challengetujuh.BuildConfig

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (BuildConfig.FLAVOR == "premium") {
            matchUserLocalAndDatabase(this, this) { result, response, message ->
                Handler(Looper.getMainLooper()).postDelayed({
                    updateUserDataRealTime(this, result, response, message)
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }, 3000)
            }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 3000)
        }
    }
}