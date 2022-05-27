package andlima.hafizhfy.challengetujuh.local.room

import andlima.hafizhfy.challengetujuh.local.room.favfilmtable.FavFilm
import andlima.hafizhfy.challengetujuh.local.room.favfilmtable.FavFilmDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavFilm::class],
    version = 1
)
abstract class FavoriteFilmDatabase : RoomDatabase() {

    abstract fun favFilmDao() : FavFilmDao

    companion object {
        private var INSTANCE : FavoriteFilmDatabase? = null
        fun getInstance(context: Context) : FavoriteFilmDatabase? {
            synchronized(FavoriteFilmDatabase::class) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    FavoriteFilmDatabase::class.java, "FavoriteFilm.db")
                    .allowMainThreadQueries().build()
            }
            return INSTANCE
        }
    }

    fun destroyInstance() {
        INSTANCE = null
    }
}