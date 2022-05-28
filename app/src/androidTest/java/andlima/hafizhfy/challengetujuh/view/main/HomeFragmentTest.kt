package andlima.hafizhfy.challengetujuh.view.main

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest: TestCase() {

    private lateinit var homeFragment: HomeFragment

    @Before
    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        homeFragment = HomeFragment()
    }

    @After
    public override fun tearDown() {
    }

    @Test
    fun getFilmData() {
        val getFilmData = homeFragment.getFilmData()
    }
}