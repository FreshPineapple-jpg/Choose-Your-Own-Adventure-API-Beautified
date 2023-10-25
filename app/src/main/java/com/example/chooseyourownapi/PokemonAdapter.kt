package com.example.chooseyourownapi
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonAdapter(val pokemonList: MutableList<PokemonData>) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {
    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView
        val name: TextView
        val number: TextView
        val type: TextView

        init {
            image = view.findViewById(R.id.pokemonImage)

            name = view.findViewById(R.id.pokemonName)
            number = view.findViewById(R.id.pokemonNumber)
            type = view.findViewById(R.id.pokemonType)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]

        holder.name.text = pokemon.name
        holder.number.text = pokemon.number
        holder.type.text = pokemon.types  // Corrected property name

        // Load Pokémon image
        loadPokemonImage(holder.image, pokemon.imageUrl)
    }

    private fun loadPokemonImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .into(imageView)
    }
}
