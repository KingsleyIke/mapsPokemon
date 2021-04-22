package com.kings.locationmap.pokemoon.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.kings.locationmap.pokemoon.network.PokemonApi
import com.kings.locationmap.pokemoon.network.RetrofitClient
import com.kings.locationmap.pokemoon.util.RecyclerAdapter
import com.kings.locationmap.R
import com.kings.locationmap.pokemoon.model.model.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    lateinit var myApi: PokemonApi
    private var compositeDisposable = CompositeDisposable()
    private lateinit var internetDisposable: Disposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)


        val retrofit = RetrofitClient.instance
        myApi = retrofit.create(PokemonApi::class.java)

//        recycler_View.setHasFixedSize(true)
        recycler_View.layoutManager = GridLayoutManager(this, 2)

        val setLimit =  set_limit_edittext
        set_limit_edittext.setText("50")
        var limit: Int = setLimit.text.toString().toInt()


        /**
         * Handle network changes and observe differnet network states
         * Ref
         */

        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it == true) {

                    loading_network.visibility = View.INVISIBLE
                    text_network.visibility = View.INVISIBLE
                    fetchData(limit)
                    set_limit.setOnClickListener {
                        limit = setLimit.text.toString().toInt()
                        fetchData(limit)
                    }
                } else {
//                        makeSnackBar()
                    loading_network.visibility = View.VISIBLE
                    text_network.visibility = View.VISIBLE
                    Toast.makeText(this, "Error in network", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * Making an Api call to retrieve data from network using retrofit and rxJava
     * The network call is made on a different thread and observed on the main thread
     */
    private fun fetchData(limit: Int) {
        compositeDisposable.add(myApi.get(limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                displayData(it.results)
            }, {
                Toast.makeText(this, "$it", Toast.LENGTH_LONG ).show()
            })
        )
    }

    private fun displayData(pokemon: List<Result>) {
        val adapter =  RecyclerAdapter(this, pokemon)
        recycler_View.adapter = adapter
    }
}