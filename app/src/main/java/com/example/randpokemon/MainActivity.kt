package com.example.randpokemon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.w3c.dom.Text
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var img: ImageView
    private lateinit var pokeBtn: Button
    private lateinit var typeText: TextView
    private lateinit var nameText: TextView
    private val client = AsyncHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        img = findViewById<ImageView>(R.id.pokemonImage)
        pokeBtn = findViewById<Button>(R.id.pokemonButton)
        nameText = findViewById<TextView>(R.id.pokemonName)
        typeText = findViewById<TextView>(R.id.pokemonType)

        pokeBtn.setOnClickListener {
            val randId = Random.nextInt(1,1026)
            fetchPokemon(randId)
        }
    }

    private fun fetchPokemon(id: Int) {
        val url = "https://pokeapi.co/api/v2/pokemon/$id"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers?,
                json: JsonHttpResponseHandler.JSON
            ) {
                try {
                    // Grab pokemon data
                    val jsonObj = json.jsonObject

                    // Grab name from json data and capitalize first letter
                    val name = jsonObj.getString("name").replaceFirstChar { it.uppercase() }

                    // Grab all types
                    val typesArray = jsonObj.getJSONArray("types")

                    // Save types to array and capitalize first letter of type, ex: "Electric"
                    val types = mutableListOf<String>()
                    for (i in 0 until typesArray.length()) {
                        val typeObj = typesArray.getJSONObject(i)
                        val typeName = typeObj.getJSONObject("type").getString("name")
                        types.add(typeName.replaceFirstChar { it.uppercase() })
                    }
                    // Add commas between types
                    val typesString = types.joinToString(", ")

                    // Grab pokemon img, specifically the front facing image
                    val spriteURL = jsonObj
                        .getJSONObject("sprites") // get all sprite images
                        .getString("front_default") // get front image specifically

                    nameText.text = name
                    typeText.text = "Type(s): $typesString" // Idk how to fix this

                    // Load with Glide
                    Glide.with(this@MainActivity)
                        .load(spriteURL)
                        .placeholder(R.drawable.pokemon)
                        .into(img)

                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error pulling Pokémon data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Toast.makeText(
                    this@MainActivity,
                    "Failed to load Pokémon. Try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}