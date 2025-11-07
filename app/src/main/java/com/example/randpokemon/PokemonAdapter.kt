package com.example.randpokemon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(private val pokemonList: List<Pokemon>) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {
    class PokemonViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val pokemonImage: ImageView = itemView.findViewById(R.id.pokemonImage)
        val pokemonName : TextView = itemView.findViewById(R.id.pokemonName)
        val pokemonType : TextView = itemView.findViewById(R.id.pokemonType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.pokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
        holder.pokemonType.text = "Type(s): ${pokemon.types}"
        Glide.with(holder.itemView.context)
            .load(pokemon.imageUrl)
            .into(holder.pokemonImage)
    }

    override fun getItemCount(): Int = pokemonList.size
}
