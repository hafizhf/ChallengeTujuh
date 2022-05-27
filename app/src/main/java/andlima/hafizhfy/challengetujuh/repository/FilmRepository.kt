package andlima.hafizhfy.challengetujuh.repository

import andlima.hafizhfy.challengetujuh.api.film.FilmApiHelper
import javax.inject.Inject

class FilmRepository @Inject constructor(private val apiHelper: FilmApiHelper) {

    suspend fun getFilm() = apiHelper.getAllFilm()
}