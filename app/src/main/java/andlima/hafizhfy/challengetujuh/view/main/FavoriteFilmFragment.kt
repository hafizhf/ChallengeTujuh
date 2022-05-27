package andlima.hafizhfy.challengetujuh.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.challengetujuh.R
import andlima.hafizhfy.challengetujuh.local.datastore.UserManager
import andlima.hafizhfy.challengetujuh.local.room.FavoriteFilmDatabase
import andlima.hafizhfy.challengetujuh.view.main.adapter.AdapterFavoriteFilm
import andlima.hafizhfy.challengetujuh.viewmodel.FavoriteFilmViewModel
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_favorite_film.*

class FavoriteFilmFragment : Fragment() {

    // Get data store
    lateinit var userManager: UserManager

    // Get local room database
    private var mDb : FavoriteFilmDatabase? = null

    // Favorite film adapter init
    lateinit var favoriteFilmAdapter: AdapterFavoriteFilm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_film, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Get room database instance
        mDb = FavoriteFilmDatabase.getInstance(requireContext())


    }

    override fun onResume() {
        super.onResume()

        userManager = UserManager(requireContext())
        userManager.id.asLiveData().observe(this, { userID ->
            rv_favorite_film_list.layoutManager = LinearLayoutManager(requireContext())
            favoriteFilmAdapter = AdapterFavoriteFilm {
                val selectedData = bundleOf("SELECTED_DATA" to it)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_favoriteFilmFragment_to_detailFragment, selectedData)
            }
            rv_favorite_film_list.adapter = favoriteFilmAdapter
            initViewModel(userID.toInt())
        })
    }

    private fun initViewModel(ownerID: Int) {
        val viewModel = ViewModelProvider(this).get(FavoriteFilmViewModel::class.java)
        viewModel.getFavoriteFilmLiveData().observe(this, {
            if (it != null) {
                loading_content.visibility = View.GONE
                no_favorite_handler.visibility = View.GONE
                favoriteFilmAdapter.setFavoriteFilmList(it)
                favoriteFilmAdapter.notifyDataSetChanged()
            } else {
                no_favorite_handler.visibility = View.VISIBLE
                loading_content.visibility = View.GONE
            }
        })
        viewModel.getFavoriteFilm(requireContext(), ownerID)
    }
}