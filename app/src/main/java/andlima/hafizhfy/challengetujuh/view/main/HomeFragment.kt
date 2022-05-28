package andlima.hafizhfy.challengetujuh.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.challengetujuh.R
import andlima.hafizhfy.challengetujuh.func.decodeBase64Image
import andlima.hafizhfy.challengetujuh.func.toast
import andlima.hafizhfy.challengetujuh.local.datastore.UserManager
import andlima.hafizhfy.challengetujuh.view.main.adapter.AdapterFilm
import andlima.hafizhfy.challengetujuh.viewmodel.FilmViewModel
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.subscribe_popup_modal.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    // Get data store
    lateinit var userManager: UserManager

    // Used for double back to exit app
    private var doubleBackToExit = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Check if user click back button twice
        doubleBackExit()

        showDataFromDataStore()

        getFilmData()

        btn_later.setOnClickListener {
            premium_popup.visibility = View.GONE
        }

        btn_goto_favorite.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_favoriteFilmFragment)
        }

        btn_goto_profile.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    private fun showDataFromDataStore() {
        userManager.username.asLiveData().observe(this, { username ->
            tv_username.text = username
        })

        userManager.avatar.asLiveData().observe(this, { avatar ->
            if (avatar != "") {
                iv_user_image.setImageBitmap(decodeBase64Image(avatar))
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getFilmData() {
        val filmAdapter = AdapterFilm() {
            // onClick item
            val selectedData = bundleOf("SELECTED_DATA" to it)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_detailFragment, selectedData)
        }

        rv_film_list.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_film_list.adapter = filmAdapter

        val viewModel = ViewModelProvider(this).get(FilmViewModel::class.java)
        viewModel.new.observe(this, {
            if (it != null) {
                loading_content.visibility = View.GONE
                filmAdapter.setFilmData(it)
                filmAdapter.notifyDataSetChanged()
            } else {
                loading_content.visibility = View.GONE
                nothing_handler.visibility = View.VISIBLE
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
}