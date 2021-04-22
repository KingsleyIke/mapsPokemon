package com.kings.locationmap.pokemoon.network

import com.kings.locationmap.pokemoon.model.model.PokeAll
import com.kings.locationmap.pokemoon.model.model.Pokemon
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokemonApi {
    @GET("pokemon")
//    val pokemon: Observable<PokeAll>
    fun get(@Query ("limit") limit: Int): Observable<PokeAll>
//    val pokemon: Observable<PokeAll>

    @GET ("pokemon/{id}")
//    val pokemonExtra: Observable<Pokemon>
    fun getPokey(@Path("id")id: Int): Observable<Pokemon>
}