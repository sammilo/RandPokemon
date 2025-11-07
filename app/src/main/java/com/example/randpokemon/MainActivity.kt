package com.example.randpokemon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pokeBtn: Button
    private lateinit var adapter: PokemonAdapter
    private val pokemonList = mutableListOf<Pokemon>()
    private val client = AsyncHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize button, view, and adapter
        pokeBtn = findViewById<Button>(R.id.pokemonButton)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = PokemonAdapter(pokemonList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Add custom dividers between views
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val drawable = ContextCompat.getDrawable(this, R.drawable.divider_line)
        if (drawable != null) {
            divider.setDrawable(drawable)
        }
        recyclerView.addItemDecoration(divider)

        // Load new pokemon set whenever button is clicked
        pokeBtn.setOnClickListener {
            loadRandPokemon()
        }
        // Load 10 rand pokemon on start-up
        loadRandPokemon()
    }

    private fun loadRandPokemon() {
        pokemonList.clear()
        adapter.notifyDataSetChanged() // Inform that pokemonList data has changed

        Toast.makeText(this, "Loading Pokémon...", Toast.LENGTH_SHORT).show()

        // Load 10 random pokemon from PokeAPI
        for (i in 1..10) {
            val randID = Random.nextInt(1, 1026)
            fetchPokemon(randID)
        }
    }

    private fun fetchPokemon(id: Int) {
        val url = "https://pokeapi.co/api/v2/pokemon/$id"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                status: Int,
                headers: Headers?,
                json: JSON?
            ) {
                val jsonObject = json?.jsonObject ?: return
                val name = jsonObject.getString("name")
                val imageUrl = jsonObject
                    .getJSONObject("sprites")
                    .getString("front_default")

                val typesArray = jsonObject.getJSONArray("types")
                val typesList = ArrayList<String>()
                for (i in 0 until typesArray.length()) {
                    val typeName =
                        typesArray.getJSONObject(i)
                            .getJSONObject("type")
                            .getString("name")
                    typesList.add(typeName)
                }

                val types = typesList.joinToString(", ")

                val pokemon = Pokemon(name, imageUrl, types)
                pokemonList.add(pokemon)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(
                status: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Toast.makeText(
                    this@MainActivity,
                    "Failed to load Pokémon (ID: $id)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}