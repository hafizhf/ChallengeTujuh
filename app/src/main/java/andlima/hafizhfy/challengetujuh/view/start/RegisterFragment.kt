package andlima.hafizhfy.challengetujuh.view.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.challengetujuh.R
import andlima.hafizhfy.challengetujuh.di.UserClient
import andlima.hafizhfy.challengetujuh.func.*
import andlima.hafizhfy.challengetujuh.model.user.GetUserItem
import andlima.hafizhfy.challengetujuh.model.user.RequestUser
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response

class RegisterFragment : Fragment() {

    private var imageURI = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Action for show password on password edittext
        btn_show_new_pwd.setOnClickListener {
            showPassword(et_new_password, btn_show_new_pwd)
        }

        // Action for show password on re-enter password edittext
        btn_show_new_repwd.setOnClickListener {
            showPassword(et_reenter_password, btn_show_new_repwd)
        }

        btn_goto_login.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }

        btn_add_new_image.setOnClickListener {
            pickImageFromGallery()
        }

        btn_register.setOnClickListener {
            // Hide all pop up
            hidePopUp(cv_name_popup)
            hidePopUp(cv_new_email_popup)
            hidePopUp(cv_re_pwd_popup)

            // Get data from edit text register form
            val username = et_new_name.text.toString()
            val email = et_new_email.text.toString()
            val password = et_new_password.text.toString()
            val repassword = et_reenter_password.text.toString()

            if (username != "" && email != "" && password != "" && repassword !="") {
                loading_register.visibility = View.VISIBLE
                GlobalScope.launch {
                    register(
                        username,
                        email,
                        password,
                        repassword,
                        if (imageURI != "") {
                            encodeImageBase64(iv_new_image)
                        } else {
                            ""
                        }
                    )
                }
            } else {
                showPopUp(cv_name_popup, tv_name_popup, "Input all field to register")
            }
        }
    }

    private suspend fun register(username: String, email: String, password: String, repassword: String, avatar: String) {
        val found = GlobalScope.async { isEmailRegistered(email) }
        val emailFound = found.await()

        when {
            emailFound < 0 -> {
                requireActivity().runOnUiThread { loading_register.visibility = View.GONE }
                alertDialog(
                    requireContext(),
                    "Register failed",
                    "Something is wrong, please try again"
                ) {}
            }
            emailFound > 0 -> {
                requireActivity().runOnUiThread { loading_register.visibility = View.GONE }
                showPopUp(
                    cv_new_email_popup,
                    tv_new_email_popup,
                    "Email already used"
                )
            }
            repassword != password -> {
                requireActivity().runOnUiThread { loading_register.visibility = View.GONE }
                showPopUp(
                    cv_re_pwd_popup,
                    tv_re_pwd_popup,
                    "Re-enter password not match"
                )
            }
            else -> {
                UserClient.instance.postUser(
                    RequestUser(
                        email,
                        password,
                        username,
                        avatar
                    )
                )
                    .enqueue(object : retrofit2.Callback<GetUserItem>{
                        override fun onResponse(
                            call: Call<GetUserItem>,
                            response: Response<GetUserItem>
                        ) {
                            // Hide loading on registering
                            requireActivity().runOnUiThread { loading_register.visibility = View.GONE }
                            if (response.isSuccessful) {
                                snackbarLong(requireView(), "Register success")
                                Navigation.findNavController(view!!)
                                    .navigate(R.id.action_registerFragment_to_loginFragment)
                            } else {
                                alertDialog(
                                    requireContext(),
                                    "Register failed",
                                    response.message() +"\n\nTry again"
                                ) {}
                            }
                        }

                        override fun onFailure(call: Call<GetUserItem>, t: Throwable) {
                            // Hide loading on registering
                            requireActivity().runOnUiThread { loading_register.visibility = View.GONE }
                            alertDialog(
                                requireContext(),
                                "Register error",
                                "${t.message}"
                            ) {}
                        }
                    })
            }
        }
    }

    private suspend fun isEmailRegistered(email: String): Int {
        // >= 0 mean response successful
        // < 0 mean response error
        // default is 0
        var result = 0

        UserClient.instance.getUser(email)
            .enqueue(object : retrofit2.Callback<List<GetUserItem>>{
                override fun onResponse(
                    call: Call<List<GetUserItem>>,
                    response: Response<List<GetUserItem>>
                ) {
                    result = if (response.isSuccessful) {
                        response.body()!!.size
                    } else {
                        -1
                    }
                }

                override fun onFailure(call: Call<List<GetUserItem>>, t: Throwable) {
                    result = -2
                }

            })

        delay(2000)
        return result
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK) {
            iv_new_image.setImageURI(data?.data)
//            uploadImage(iv_new_image)
            getImageURI(data?.data.toString())
        }
    }

    private fun getImageURI(uri: String) {
        this.imageURI = uri
    }
}