package andlima.hafizhfy.challengetujuh.view.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.challengetujuh.R
import andlima.hafizhfy.challengetujuh.di.UserClient
import andlima.hafizhfy.challengetujuh.func.*
import andlima.hafizhfy.challengetujuh.local.datastore.UserManager
import andlima.hafizhfy.challengetujuh.model.user.GetUserItem
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LoginFragment : Fragment() {

    // Get data store
    lateinit var userManager: UserManager

    // Used for double back to exit app
    private var doubleBackToExit = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Check if user click back button twice
        doubleBackExit()

        // Check if user logged in all ready
        isUserLoggedIn()

        btn_show_pwd.setOnClickListener {
            showPassword(et_password, btn_show_pwd)
        }

        btn_goto_register.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btn_login.setOnClickListener {
            hideAllPopUp(cv_email_popup, cv_password_popup)

            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (email != "" && password != "") {
                loading_login.visibility = View.VISIBLE
                loginAuth(email, password)
            } else {
                showPopUp(cv_email_popup, tv_email_popup, "Please input all field")
            }
        }
    }

    // END OF OVERRIDE FRAGMENT METHOD -------------------------------------------------------------

    private fun loginAuth(email: String, password: String) {
        UserClient.instance.getUser(email)
            .enqueue(object : retrofit2.Callback<List<GetUserItem>>{
                override fun onResponse(
                    call: Call<List<GetUserItem>>,
                    response: Response<List<GetUserItem>>
                ) {
                    loading_login.visibility = View.GONE
                    if (response.isSuccessful) {
                        when {
                            response.body()?.isEmpty() == true -> {
                                toast(requireContext(), "Unknown user, please register")
                            }
                            response.body()?.size!! > 1 -> {
                                toast(requireContext(), "Please input data correctly")
                            }
                            email != response.body()!![0].email -> {
                                showPopUp(cv_email_popup, tv_email_popup, "Email not registered")
                            }
                            password != response.body()!![0].password -> {
                                showPopUp(cv_password_popup, tv_password_popup, "Wrong password")
                            }
                            else -> {
                                addLoginDataIntoDatastore(
                                    response.body()!![0].username,
                                    response.body()!![0].email,
                                    response.body()!![0].avatar,
                                    response.body()!![0].password,
                                    response.body()!![0].id,
                                    response.body()!![0].complete_name,
                                    response.body()!![0].address,
                                    response.body()!![0].dateofbirth
                                )
                                Navigation.findNavController(view!!)
                                    .navigate(R.id.action_loginFragment_to_homeFragment)
                            }
                        }
                    } else {
                        alertDialog(requireContext(), "Login failed", response.message()
                                +"\n\nTry again") {}
                    }
                }

                override fun onFailure(call: Call<List<GetUserItem>>, t: Throwable) {
                    loading_login.visibility = View.GONE
                    alertDialog(requireContext(), "Login error", "${t.message}") {}
                }

            })
    }

    // Function to exit app with double click on back button----------------------------------------
    private fun doubleBackExit() {
        activity?.onBackPressedDispatcher
            ?.addCallback(this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    if (doubleBackToExit) {
                        activity!!.finish()
                    } else {
                        doubleBackToExit = true
                        toast(requireContext(), "Press again to exit")

                        Handler(Looper.getMainLooper()).postDelayed(Runnable {
                            kotlin.run {
                                doubleBackToExit = false
                            }
                        }, 2000)
                    }
                }
            })
    }

    private fun addLoginDataIntoDatastore(
        username: String,
        email: String,
        avatar: String,
        password: String,
        id: String,
        completeName: String,
        address: String,
        dateOfBirth: String
    ) {
        GlobalScope.launch {
            userManager.loginUserData(
                username,
                email,
                avatar,
                password,
                id,
                completeName,
                address,
                dateOfBirth
            )
        }
    }

    private fun isUserLoggedIn() {
        userManager.email.asLiveData().observe(this, { email ->
            userManager.password.asLiveData().observe(this, { password ->

                if (email != "" && password != "") {
                    Navigation.findNavController(view!!)
                        .navigate(R.id.action_loginFragment_to_homeFragment)
                }
            })
        })
    }
}