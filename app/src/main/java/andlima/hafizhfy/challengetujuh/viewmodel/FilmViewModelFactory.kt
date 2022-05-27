package andlima.hafizhfy.challengetujuh.viewmodel

import andlima.hafizhfy.challengetujuh.api.film.FilmApiHelper
import andlima.hafizhfy.challengetujuh.repository.FilmRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class FilmViewModelFactory(private val filmApiHelper: FilmApiHelper)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmViewModel::class.java)) {
            return FilmViewModel(FilmRepository(filmApiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}