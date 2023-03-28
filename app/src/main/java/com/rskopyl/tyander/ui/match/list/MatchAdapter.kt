package com.rskopyl.tyander.ui.match.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rskopyl.tyander.R
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.databinding.ItemMatchBinding
import com.rskopyl.tyander.ui.match.list.MatchAdapter.MatchViewHolder

class MatchAdapter(
    private val onItemClick: ((Match) -> Unit)? = null
) : ListAdapter<Match, MatchViewHolder>(MatchDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MatchViewHolder {
        return MatchViewHolder(
            ItemMatchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = getItem(position)
        holder.bind(match)
    }

    inner class MatchViewHolder(
        private val binding: ItemMatchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(match: Match) = binding.run {
            ivCharacterImage.load(match.character.imageUrl)
            tvCharacterNameAge.text = root.context.getString(
                R.string.name_age,
                match.character.name, match.character.age
            )
            indicatorNew.isVisible = match.isNew
            if (onItemClick != null) {
                root.setOnClickListener {
                    onItemClick.invoke(match)
                }
            }
        }
    }

    private object MatchDiffCallback : DiffUtil.ItemCallback<Match>() {

        override fun areItemsTheSame(oldItem: Match, newItem: Match) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Match, newItem: Match
        ): Boolean {
            return listOf(oldItem, newItem)
                .distinctBy {
                    listOf(
                        it.character.imageUrl,
                        it.character.name,
                        it.character.age,
                        it.isNew
                    )
                }
                .size == 1
        }
    }
}