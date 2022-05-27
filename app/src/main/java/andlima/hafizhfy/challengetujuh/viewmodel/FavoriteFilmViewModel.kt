package andlima.hafizhfy.challengetujuh.viewmodel

import andlima.hafizhfy.challengetujuh.local.room.FavoriteFilmDatabase
import andlima.hafizhfy.challengetujuh.local.room.favfilmtable.FavFilm
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteFilmViewModel: ViewModel() {
    // Get local room database
    private var mDb : FavoriteFilmDatabase? = null

    lateinit var liveDataList: MutableLiveData<List<FavFilm>>

    init {
        liveDataList = MutableLiveData()
    }

    fun getFavoriteFilmLiveData() : MutableLiveData<List<FavFilm>> {
        return liveDataList
    }

    fun getFavoriteFilm(context: Context, ownerID: Int) {
        mDb = FavoriteFilmDatabase.getInstance(context)

        val films = mDb?.favFilmDao()?.getUsersFavoriteFilm(ownerID)

        if (films?.size != 0) {
            liveDataList.postValue(films)
        } else {
            liveDataList.postValue(null)
        }
    }
}