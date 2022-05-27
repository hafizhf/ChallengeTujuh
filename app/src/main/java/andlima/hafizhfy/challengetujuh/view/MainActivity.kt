package andlima.hafizhfy.challengetujuh.view

import andlima.hafizhfy.challengetujuh.R
import andlima.hafizhfy.challengetujuh.func.matchUserLocalAndDatabase
import andlima.hafizhfy.challengetujuh.func.updateUserDataRealTime
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            checkUserData()
        }
    }

    private suspend fun checkUserData() {
        delay(10000)
        runOnUiThread {
            matchUserLocalAndDatabase(this, this) { result, response, message ->
                if (updateUserDataRealTime(this, result, response, message)) {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                }
            }
        }
        checkUserData()
    }
}