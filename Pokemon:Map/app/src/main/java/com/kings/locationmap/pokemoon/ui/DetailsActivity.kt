package com.kings.locationmap.pokemoon.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.kings.locationmap.pokemoon.network.PokemonApi
import com.kings.locationmap.pokemoon.network.RetrofitClient
import com.kings.locationmap.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_details.*
import java.util.*

class DetailsActivity : AppCompatActivity() {

    lateinit var myApi: PokemonApi
    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val retrofit = RetrofitClient.instance
        myApi = retrofit.create(PokemonApi::class.java)


        //passing intent text from previous activity
        val intent = intent
        val pokemonName = intent.extras?.getString("NAME").toString().toUpperCase(Locale.ROOT)
        val pokemonUrl = intent.extras?.getString("URL").toString()
        val pokemonId = intent.extras?.getInt("ID") ?: 0


        pokey_new_name.text = pokemonName
        Glide.with(this).load(pokemonUrl).placeholder(R.drawable.loading_image_holder).into(pokey_image)

        fetchData(pokemonId)
    }

    /**
     * Making a second Api call to fetch details using id set from previous activity
     * using glide to set string url to visible images
     * the id is then used to set individual components to corresponding data
     */
    private fun fetchData(id: Int) {
        compositeDisposable.add(myApi.getPokey(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pokemon ->

                    abilities_list.text = pokemon.abilities.joinToString { it.ability.name }
                    height.text = "H: ${pokemon.height.toString()}"
                    weight.text = "W: ${pokemon.weight.toString()}"
                    baseExperience_int.text = pokemon.baseExperience.toString()
                    form_list.text = pokemon.forms.joinToString { it.name }
                    order.text = pokemon.order.toString()
                    species.text = pokemon.species.name
                    stats.text = pokemon.stats.joinToString { it.stat.name }
                    types.text = pokemon.types.joinToString { it.type.name }
                    game_indices_list.text = pokemon.gameIndices.joinToString { it.gameIndex.toString() }
                    moves_list.text = pokemon.moves.joinToString {it.move.name}

                    val sprite1 = pokemon.sprites.backDefault
                    val sprite2 = pokemon.sprites.backShiny
                    val sprite3 = pokemon.sprites.frontDefault
                    val sprite4 = pokemon.sprites.frontShiny

                    Glide.with(this).load(sprite1).placeholder(R.drawable.loading_image_holder).into(sprite_one)
                    Glide.with(this).load(sprite2).placeholder(R.drawable.loading_image_holder).into(sprite_two)
                    Glide.with(this).load(sprite3).placeholder(R.drawable.loading_image_holder).into(sprite_three)
                    Glide.with(this).load(sprite4).placeholder(R.drawable.loading_image_holder).into(sprite_four)
                }, {
                    Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
                }))
    }
}