package andlima.hafizhfy.challengetujuh.viewmodel

import andlima.hafizhfy.challengetujuh.api.film.FilmApi
import andlima.hafizhfy.challengetujuh.model.film.GetAllFilmResponseItem
import andlima.hafizhfy.challengetujuh.repository.FilmRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(api: FilmRepository) : ViewModel() {

    private val filmLiveData = MutableLiveData<List<GetAllFilmResponseItem>>()
    val new: LiveData<List<GetAllFilmResponseItem>> = filmLiveData

    init {
        viewModelScope.launch {
            val dataFilm = api.getFilm()
            delay(2000)
            filmLiveData.value = dataFilm
        }
    }

}