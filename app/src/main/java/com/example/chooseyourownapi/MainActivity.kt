package com.example.chooseyourownapi
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONArray


data class PokemonData(val name: String, val number: String, val imageUrl: String, val types: String)

class MainActivity : AppCompatActivity() {
    private lateinit var pokemonList: MutableList<PokemonData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.pokemon_recycle_view)
        pokemonList = mutableListOf()
        adapter = PokemonAdapter(pokemonList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Set up an endless scrolling listener
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                // Load more data if the user is near the end of the list
                if (visibleItemCount + firstVisibleItem >= totalItemCount - 5) {
                    getPokemon()
                }
            }
        })

        // Fetch and add initial Pokémon data
        getPokemon()
    }

    private fun getPokemon() {
        val randomPokemonNumber = (1..1000).random()
        val randomNumber = "#$randomPokemonNumber"

        val client = AsyncHttpClient()
        val url = "https://pokeapi.co/api/v2/pokemon/$randomPokemonNumber/"

        client.get(url, null, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("PokemonFetch", "Failed to fetch Pokemon data")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                try {
                    if (json != null) {
                        val name = json.jsonObject.getJSONArray("forms").getJSONObject(0).getString("name")
                        val imageUrl = json.jsonObject.getJSONObject("sprites").getString("front_default")
                        val types = json.jsonObject.getJSONArray("types")
                        val pokemon = PokemonData(name, randomNumber, imageUrl, getTypesString(types))
                        pokemonList.add(pokemon)

                        runOnUiThread {
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        Log.e("PokemonFetch", "Error parsing Pokemon data: Invalid JSON format")
                    }
                } catch (e: Exception) {
                    Log.e("PokemonFetch", "Error parsing Pokemon data: $e")
                }
            }
        })
    }

    private fun getTypesString(types: JSONArray): String {
        val typeList = mutableListOf<String>()
        for (i in 0 until types.length()) {
            val type = types.getJSONObject(i).getJSONObject("type").getString("name")
            typeList.add(type)
        }
        return typeList.joinToString(", ")
    }
}

