package com.rskopyl.tyander.ui.chat.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rskopyl.tyander.R
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.data.model.Message
import com.rskopyl.tyander.databinding.ItemChatBinding
import com.rskopyl.tyander.ui.chat.list.MatchWithMessageAdapter.MatchWithMessageViewHolder
import com.rskopyl.tyander.util.toJavaDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.text.SimpleDateFormat
import java.util.*

class MatchWithMessageAdapter(
    private val onItemClick: ((Pair<Match, Message>) -> Unit)? = null
) : ListAdapter<Pair<Match, Message>, MatchWithMessageViewHolder>(
    MatchWithMessageDiffCallback
) {
    private val dateFormat =
        SimpleDateFormat("MMM, dd", Locale.getDefault())

    private val timeFormat =
        SimpleDateFormat("kk:mm", Locale.getDefault()).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MatchWithMessageViewHolder {
        return MatchWithMessageViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MatchWithMessageViewHolder,
        position: Int
    ) {
        val matchWithMessage = getItem(position)
        holder.bind(matchWithMessage)
    }

    inner class MatchWithMessageViewHolder(
        private val binding: ItemChatBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(matchWithMessage: Pair<Match, Message>) = binding.run {
            val (match, message) = matchWithMessage
            ivCharacterImage.load(match.character.imageUrl)
            tvCharacterNameAge.text = root.context.getString(
                R.string.name_age,
                match.character.name, match.character.age
            )
            tvLastMessageText.text = message.text
            tvLastMessageTime.text = message.dateTime.let {
                if (it.date == Clock.System
                        .todayIn(TimeZone.currentSystemDefault())
                ) {
                    timeFormat.format(it.toJavaDate())
                } else {
                    dateFormat.format(it.toJavaDate())
                }
            }
            if (onItemClick != null) {
                root.setOnClickListener {
                    onItemClick.invoke(matchWithMessage)
                }
            }
        }
    }

    private object MatchWithMessageDiffCallback :
        DiffUtil.ItemCallback<Pair<Match, Message>>() {

        override fun areItemsTheSame(
            oldItem: Pair<Match, Message>,
            newItem: Pair<Match, Message>
        ): Boolean {
            return listOf(oldItem, newItem)
                .distinctBy { (match, message) ->
                    listOf(match.id, message.id)
                }
                .size == 1
        }

        override fun areContentsTheSame(
            oldItem: Pair<Match, Message>,
            newItem: Pair<Match, Message>
        ): Boolean {
            return listOf(oldItem, newItem)
                .distinctBy { (match, message) ->
                    listOf(
                        match.character.imageUrl,
                        match.character.name,
                        match.character.age,
                        message.text,
                        message.dateTime
                    )
                }
                .size == 1
        }
    }
}