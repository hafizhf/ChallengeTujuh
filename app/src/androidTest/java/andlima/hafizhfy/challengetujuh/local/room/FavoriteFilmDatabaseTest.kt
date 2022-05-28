package andlima.hafizhfy.challengetujuh.local.room

import andlima.hafizhfy.challengetujuh.local.room.favfilmtable.FavFilmDao
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteFilmDatabaseTest: TestCase() {

    private lateinit var db: FavoriteFilmDatabase
    private lateinit var favFilmDao: FavFilmDao

    @Before
    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, FavoriteFilmDatabase::class.java).build()

        favFilmDao = db.favFilmDao()
    }

    @After
    public override fun tearDown() {
        db.close()
    }

    @Test
    fun favFilmDao() {
        val getAllFilm = favFilmDao.getAllFavoriteFilm()
        val getUserFilm = favFilmDao.getUsersFavoriteFilm(2)
    }
}