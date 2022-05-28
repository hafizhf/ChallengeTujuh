package andlima.hafizhfy.challengetujuh.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.challengetujuh.R
import andlima.hafizhfy.challengetujuh.func.snackbarCustom
import andlima.hafizhfy.challengetujuh.func.snackbarLong
import andlima.hafizhfy.challengetujuh.func.toast
import andlima.hafizhfy.challengetujuh.local.datastore.UserManager
import andlima.hafizhfy.challengetujuh.local.room.FavoriteFilmDatabase
import andlima.hafizhfy.challengetujuh.local.room.favfilmtable.FavFilm
import andlima.hafizhfy.challengetujuh.model.film.GetAllFilmResponseItem
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import andlima.hafizhfy.challengetujuh.BuildConfig

class DetailFragment : Fragment() {

    // Get data store
    lateinit var userManager: UserManager

    // Get local room database
    private var mDb : FavoriteFilmDatabase? = null

    // Init floating action button clicked
    private var fabClicked : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Get room database instance
        mDb = FavoriteFilmDatabase.getInstance(requireContext())

        // Get data from recyclerview selected item
        val selectedData = arguments?.getParcelable<GetAllFilmResponseItem>("SELECTED_DATA") as GetAllFilmResponseItem
        showSelectedFilmData(selectedData)

        if (BuildConfig.FLAVOR == "premium") {
            userManager.id.asLiveData().observe(this, { userID ->
                setCurrentFabStatus(selectedData.title, userID.toInt())

                fab_add_to_favorite.setOnClickListener {
                    addOrRemoveFilmFavorite(userID.toInt(), selectedData)
                }
            })

            btn_watch_now.setOnClickListener {
                Navigation.findNavController(view).navigate(R.id.action_detailFragment_to_watchFragment)
            }
        }
    }

    private fun showSelectedFilmData(detail: GetAllFilmResponseItem) {
        Glide.with(this).load(detail.image)
            .into(iv_thumbnail_film_detail)
        tv_title_film_detail.text = detail.title
        tv_director_detail.append(detail.director)
        tv_release_date_detail.append(detail.releaseDate)
        tv_synopsis_detail.text = detail.synopsis
    }

    private fun setCurrentFabStatus(title: String, userID: Int) {
        val data = mDb?.favFilmDao()?.checkFilmAddedByUser(title, userID)

        fabClicked = data?.size != 0

        if (fabClicked) {
            fab_add_to_favorite.setImageResource(R.drawable.ic_faved)
        } else {
            fab_add_to_favorite.setImageResource(R.drawable.ic_fav)
        }
    }

    private fun addOrRemoveFilmFavorite(userID: Int, selectedData: GetAllFilmResponseItem) {
        if (!fabClicked) {
            // Add film to favorite
            val selectedFilm = FavFilm(null,
                selectedData.title,
                selectedData.synopsis,
                selectedData.releaseDate,
                selectedData.image,
                selectedData.director,
                userID)

            val addFavorite = mDb?.favFilmDao()?.insertNewFavorite(selectedFilm)

            if (addFavorite != 0.toLong()) {
                fab_add_to_favorite.setImageResource(R.drawable.ic_faved)
                snackbarCustom(
                    requireView(),
                    "Added to favorite",
                    "See Favorite",
                ) {
                    Navigation.findNavController(view!!)
                        .navigate(R.id.action_detailFragment_to_favoriteFilmFragment)
                }
                fabClicked = true
            } else {
                toast(requireContext(), "Failed to add to favorite")
            }

        } else {

            GlobalScope.launch {

                val filmID = mDb?.favFilmDao()?.getFavoriteFilmID(
                    selectedData.title,
                    userID.toInt()
                )

                val removeFromFavorite = mDb?.favFilmDao()?.removeFromFavorite(filmID!!)

                requireActivity().runOnUiThread {
                    if (removeFromFavorite != 0) {
                        fab_add_to_favorite.setImageResource(R.drawable.ic_fav)
                        snackbarLong(requireView(), "Removed from favorite")
                        fabClicked = false
                    } else {
                        toast(requireContext(), "Failed to remove favorite")
                    }
                }
            }
        }
    }
}