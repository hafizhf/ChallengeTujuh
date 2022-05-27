package andlima.hafizhfy.challengetujuh.view.main.adapter

import andlima.hafizhfy.challengetujuh.R
import andlima.hafizhfy.challengetujuh.model.film.GetAllFilmResponseItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_film_grid.view.*

class AdapterFilm(private var onClick : (GetAllFilmResponseItem)-> Unit)
    : RecyclerView.Adapter<AdapterFilm.ViewHolder>() {

    private var dataFilm : List<GetAllFilmResponseItem>? = null

    fun setFilmData(film: List<GetAllFilmResponseItem>) {
        this.dataFilm = film
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_film_grid, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.itemView.context).load(dataFilm!![position].image)
            .into(holder.itemView.iv_film_thumbnail)

        holder.itemView.tv_film_title.text = dataFilm!![position].title
        holder.itemView.tv_film_director.text = dataFilm!![position].director

        holder.itemView.item.setOnClickListener {
            onClick(dataFilm!![position])
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