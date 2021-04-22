package com.kings.locationmap.pokemoon.util

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kings.locationmap.R
import com.kings.locationmap.pokemoon.ui.DetailsActivity
import com.kings.locationmap.pokemoon.model.model.Result
import kotlinx.android.synthetic.main.pokemon_sample.view.*
import java.util.*


class RecyclerAdapter(val context: Context, private val pokemon: List<Result>) :
        RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_sample, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pokemon = pokemon[position]
        holder.setData(pokemon, position)
    }

    override fun getItemCount(): Int {
        return pokemon.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currentPokemon: Result? = null
        var currentPos = 0
        var currentUrl = ""
        var currentId = 0

        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailsActivity::class.java).apply {
                    putExtra("NAME", currentPokemon?.name)
                    putExtra("URL", currentUrl)
                    putExtra("ID", currentId)
                }

                itemView.context.startActivity(intent)
            }
        }

        /**
         * Sets pokemon data from to declared variables
         */

        fun setData(pokemon: Result?, position: Int) {
            itemView.pokey_name.text = pokemon!!.name.toUpperCase(Locale.ROOT)
            val pokemonUrl = pokemon.url
            val imgUrl = getPokemonId(pokemonUrl)
            this.currentUrl = imgUrl
            this.currentPokemon = pokemon
            this.currentPos = position
            this.currentId = pokemonUrl.substring(34, pokemonUrl.length - 1).toInt()
            Glide.with(context).load(imgUrl).placeholder(R.drawable.loading_image_holder).into(itemView.pokey_img)
            Glide.with(context).load(imgUrl).into(itemView.bg_img)
        }
    }

    /**
     *Gets id and concatenates it to the url
     */
    private fun getPokemonId(item: String): String {
        val id = item.substring(34, item.length - 1).toInt()
        return "https://pokeres.bastionbot.org/images/pokemon/$id.png"
    }
}