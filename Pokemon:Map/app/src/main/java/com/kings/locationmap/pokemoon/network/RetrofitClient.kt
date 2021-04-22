package com.kings.locationmap.pokemoon.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    var ourInstance: Retrofit? = null
    val instance: Retrofit
        get() {
            if (ourInstance == null) {
                ourInstance = Retrofit.Builder()
                    .baseUrl("https://pokeapi.co/api/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            }
            return ourInstance!!
        }
}


//object RetrofitClient {
//    var retrofit : Retrofit? = null
//    fun getClient(baseUrl: String) : Retrofit {
//        if(retrofit == null) {
//            retrofit = Retrofit.Builder()
//                .baseUrl("https://pokeapi.co/api/v2/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build()
//        }
//        return retrofit!!
//    }
//}
//
//
//object Common {
//    val retrofitService: PokemonApi
//        get() = RetrofitClient.getClient("https://pokeapi.co/api/v2/")
//            .create(PokemonApi::class.java)
//}