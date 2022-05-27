package andlima.hafizhfy.challengetujuh.view.main.profile

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
import andlima.hafizhfy.challengetujuh.model.user.PutUser
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EditProfileFragment : Fragment() {

    // Get data store
    lateinit var userManager: UserManager

    private var imageURI = ""

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        userManager.id.asLiveData().observe(this, { id ->

            userManager.avatar.asLiveData().observe(this, { avatar ->

                userManager.username.asLiveData().observe(this, { username ->

                    userManager.email.asLiveData().observe(this, { email ->

                        userManager.completeName.asLiveData().observe(this, { completeName ->

                            userManager.address.asLiveData().observe(this, { address ->

                                userManager.dateOfBirth.asLiveData().observe(this, { dateOfBirth ->

                                    if (avatar != "") {
                                        iv_update_image.setImageBitmap(decodeBase64Image(avatar))
                                    }

                                    et_edit_name.setText(username)
                                    et_edit_email.setText(email)

                                    if (completeName == "complete_name $id" || completeName == "") {
                                        et_edit_complete_name.setText("")
                                    } else {
                                        et_edit_complete_name.setText(completeName)
                                    }

                                    if (dateOfBirth == "dateofbirth $id" || dateOfBirth == "") {
                                        et_edit_dateofbirth.setText("")
                                    }else {
                                        et_edit_dateofbirth.setText(dateOfBirth)
                                    }
                                    setDateOfBirth()

                                    if (address == "address $id" || address == "") {
                                        et_edit_address.setText("")
                                    } else {
                                        et_edit_address.setText(address)
                                    }

                                    btn_edit_image.setOnClickListener {
                                        pickImageFromGallery()
                                    }

                                    btn_save_profile.setOnClickListener {
                                        when {
                                            et_edit_name.text.toString() == "" -> {
                                                toast(requireContext(), "Username field cannot be empty")
                                            }
                                            et_edit_email.text.toString() == "" -> {
                                                toast(requireContext(), "Email field cannot be empty")
                                            }
                                            else -> {
                                                loading_update_profile.visibility = View.VISIBLE
                                                updateProfile(
                                                    id!!.toInt(),
                                                    et_edit_dateofbirth.text.toString(),
                                                    et_edit_address.text.toString(),
                                                    if (imageURI != "") {
                                                        encodeImageBase64(iv_update_image)
                                                    } else {
                                                        avatar
                                                    },
                                                    et_edit_complete_name.text.toString(),
                                                    et_edit_email.text.toString(),
                                                    et_edit_name.text.toString()
                                                )
                                            }
                                        }
                                    }
                                })
                            })
                        })
                    })
                })
            })
        })
    }

    private fun updateProfile(
        id: Int,
        dateofbirth: String,
        address: String,
        avatar: String,
        complete_name: String,
        email: String,
        username: String
    ) {
        UserClient.instance
            .updateUser(id, PutUser(dateofbirth, address, avatar, complete_name, email, username))
            .enqueue(object : retrofit2.Callback<GetUserItem>{
                override fun onResponse(call: Call<GetUserItem>, response: Response<GetUserItem>) {
                    if (response.isSuccessful) {
                        updateDataStore(username, email, avatar, complete_name, address, dateofbirth)
                        loading_update_profile.visibility = View.GONE
                        snackbarLong(requireView(), "Update saved")

                        Navigation.findNavController(view!!)
                            .navigate(R.id.action_editProfileFragment_to_homeFragment)
                    } else {
                        loading_update_profile.visibility = View.GONE
                        alertDialog(
                            requireContext(),
                            "Update profile failed",
                            response.message() +"\n\nTry again"
                        ) {}
                    }
                }

                override fun onFailure(call: Call<GetUserItem>, t: Throwable) {
                    loading_update_profile.visibility = View.GONE
                    alertDialog(
                        requireContext(),
                        "Update profile error",
                        "${t.message}"
                    ) {}
                }

            })
    }

    private fun updateDataStore(
        username: String,
        email: String,
        avatar: String,
        complete_name: String,
        address: String,
        dateofbirth: String
    ) {
        userManager.id.asLiveData().observe(this, { userID ->
            userManager.password.asLiveData().observe(this, { password ->

                GlobalScope.launch {
//                    userManager.clearData()

                    userManager.loginUserData(
                        username,
                        email,
                        avatar,
                        password,
                        userID,
                        complete_name,
                        address,
                        dateofbirth
                    )
                }
            })
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDateOfBirth() {
        val date = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            val dateFormat = SimpleDateFormat("dd MMMM, yyyy")
            et_edit_dateofbirth.setText(dateFormat.format(calendar.time))
        }

        et_edit_dateofbirth.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK) {
            iv_update_image.setImageURI(data?.data)
            getImageURI(data?.data.toString())
        }
    }

    private fun getImageURI(uri: String) {
        this.imageURI = uri
    }
}