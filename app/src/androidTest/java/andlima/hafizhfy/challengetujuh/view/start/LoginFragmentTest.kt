package andlima.hafizhfy.challengetujuh.view.start

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest: TestCase() {

    private lateinit var loginFragment: LoginFragment

    @Before
    public override fun setUp() {
        super.setUp()
        loginFragment = LoginFragment()
    }

    @After
    public override fun tearDown() {
    }

    @Test
    fun loginAuth() {
        loginFragment.loginAuth("admin@gmail.com", "123")
    }
}