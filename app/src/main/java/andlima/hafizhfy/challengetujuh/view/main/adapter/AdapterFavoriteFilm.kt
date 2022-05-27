package andlima.hafizhfy.challengetujuh.view.main.adapter

import andlima.hafizhfy.challengetujuh.R
import andlima.hafizhfy.challengetujuh.local.room.favfilmtable.FavFilm
import andlima.hafizhfy.challengetujuh.model.film.GetAllFilmResponseItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_film.view.*

class AdapterFavoriteFilm(private var onClick: (GetAllFilmResponseItem) -> Unit)
    : RecyclerView.Adapter<AdapterFavoriteFilm.ViewHolder>() {

    private var dataFilm: List<FavFilm>? = null

    fun setFavoriteFilmList(filmList: List<FavFilm>) {
        this.dataFilm = filmList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_film, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(dataFilm!![position].image)
            .into(holder.itemView.iv_film_thumbnail)

        holder.itemView.tv_film_title.text = dataFilm!![position].title
        holder.itemView.tv_film_director.text = dataFilm!![position].director

        holder.itemView.item.setOnClickListener {
            val matchWithApiModel = GetAllFilmResponseItem (
                "",
                dataFilm!![position].director!!,
                "",
                dataFilm!![position].image!!,
                dataFilm!![position].release_date!!,
                dataFilm!![position].synopsis!!,
                dataFilm!![position].title!!
            )

            onClick(matchWithApiModel)
        }
    }

    override fun getItemCount(): Int {
        return if (dataFilm != null) {
            dataFilm!!.size
        } else {
            0
        }
    }
}