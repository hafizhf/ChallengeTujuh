package andlima.hafizhfy.challengetujuh.api.film

import javax.inject.Inject

class FilmApiHelper @Inject constructor(private val apiService: FilmApi) {
    suspend fun getAllFilm() = apiService.getAllFilm()
}