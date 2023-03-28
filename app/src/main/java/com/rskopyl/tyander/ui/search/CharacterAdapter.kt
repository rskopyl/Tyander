package com.rskopyl.tyander.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rskopyl.tyander.R
import com.rskopyl.tyander.data.model.Character
import com.rskopyl.tyander.databinding.ItemCharacterBinding
import com.rskopyl.tyander.ui.search.CharacterAdapter.CharacterViewHolder

class CharacterAdapter :
    ListAdapter<Character, CharacterViewHolder>(CharacterDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CharacterViewHolder,
        position: Int
    ) {
        val character = getItem(position)
        holder.bind(character)
    }

    class CharacterViewHolder(
        private val binding: ItemCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) = binding.run {
            ivCharacterImage.load(character.imageUrl)
            tvCharacterNameAge.text = root.context.getString(
                R.string.name_age,
                character.name, character.age
            )
            tvCharacterAddress.text = root.context.getString(
                R.string.country_city,
                character.address.country, character.address.city
            )
        }
    }

    private object CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {

        override fun areItemsTheSame(oldItem: Character, newItem: Character) =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Character, newItem: Character
        ): Boolean {
            return listOf(oldItem, newItem)
                .distinctBy {
                    listOf(it.imageUrl, it.name, it.age, it.address)
                }
                .size == 1
        }
    }
}