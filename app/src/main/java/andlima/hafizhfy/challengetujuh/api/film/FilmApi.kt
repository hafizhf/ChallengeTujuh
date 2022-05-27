package andlima.hafizhfy.challengetujuh.api.film

import andlima.hafizhfy.challengetujuh.model.film.GetAllFilmResponseItem
import retrofit2.Call
import retrofit2.http.GET

interface FilmApi {
//    @GET("apifilm.php")
//    suspend fun getAllFilm() : List<GetAllFilmResponseItem>

    @GET("film")
    suspend fun getAllFilm() : List<GetAllFilmResponseItem>
}