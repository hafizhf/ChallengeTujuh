package andlima.hafizhfy.challengetujuh.view.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.challengetujuh.R
import andlima.hafizhfy.challengetujuh.func.alertDialog
import andlima.hafizhfy.challengetujuh.func.decodeBase64Image
import andlima.hafizhfy.challengetujuh.func.toast
import andlima.hafizhfy.challengetujuh.local.datastore.UserManager
import andlima.hafizhfy.challengetujuh.view.MainActivity
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    // Get data store
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Display user data from data store to profile view
        showDataFromDataStore()

        btn_goto_edit_profile.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        btn_logout.setOnClickListener {
            alertDialog(requireContext(), "Logout", "Are you sure want to log out?") {
                GlobalScope.launch {
                    userManager.clearData()
                }
                toast(requireContext(), "You are logged out")
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().overridePendingTransition(0,0)
                requireActivity().finish()
//                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }
    }

    private fun showDataFromDataStore() {
        userManager.id.asLiveData().observe(this, { userID ->
            userManager.avatar.asLiveData().observe(this, { avatar ->
                userManager.username.asLiveData().observe(this, { username ->
                    userManager.email.asLiveData().observe(this, { email ->
                        userManager.completeName.asLiveData().observe(this, { completeName ->
                            userManager.address.asLiveData().observe(this, { address ->
                                userManager.dateOfBirth.asLiveData().observe(this, { dateOfBirth ->

                                    if (avatar != "") {
                                        iv_image_detail.setImageBitmap(decodeBase64Image(avatar))
                                    }
                                    tv_username_detail.text = username
                                    tv_email_detail.text = email

                                    if (completeName == "complete_name $id" || completeName == "") {
                                        tv_complete_name.visibility = View.GONE
                                    } else {
                                        tv_complete_name.text = completeName
                                    }

                                    if (address == "address $id" || address == "") {
                                        tv_address.visibility = View.GONE
                                    } else {
                                        tv_address.text = address
                                    }

                                    if (dateOfBirth == "dateofbirth $id" || dateOfBirth == "") {
                                        tv_date_of_birth.visibility = View.GONE
                                    } else {
                                        tv_date_of_birth.text = dateOfBirth
                                    }
                                })
                            })
                        })
                    })
                })
            })
        })
    }
}