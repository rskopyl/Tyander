package com.rskopyl.tyander.ui.chat.messaging

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rskopyl.tyander.data.model.Message
import com.rskopyl.tyander.databinding.ItemMessageCharacterBinding
import com.rskopyl.tyander.databinding.ItemMessageUserBinding
import com.rskopyl.tyander.util.toJavaDate
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter :
    ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback) {

    private val timeFormat =
        SimpleDateFormat("kk:mm", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (Message.Author.values()[viewType]) {
            Message.Author.USER -> {
                UserMessageViewHolder(
                    ItemMessageUserBinding.inflate(
                        layoutInflater, parent, false
                    )
                )
            }
            Message.Author.CHARACTER -> {
                CharacterMessageViewHolder(
                    ItemMessageCharacterBinding.inflate(
                        layoutInflater, parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        Log.d("Message", "Text: ${message.text}, Position: $position")
        when (holder) {
            is UserMessageViewHolder -> holder.bind(message)
            is CharacterMessageViewHolder -> holder.bind(message)
            else -> throw IllegalStateException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return message.author.ordinal
    }

    inner class UserMessageViewHolder(
        private val binding: ItemMessageUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) = binding.run {
            tvMessageText.text = message.text
            tvMessageSendingTime.text = timeFormat.format(
                message.dateTime.toJavaDate()
            )
        }
    }

    inner class CharacterMessageViewHolder(
        private val binding: ItemMessageCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) = binding.run {
            tvMessageText.text = message.text
            tvMessageSendingTime.text = timeFormat.format(
                message.dateTime.toJavaDate()
            )
        }
    }

    private object MessageDiffCallback : DiffUtil.ItemCallback<Message>() {

        override fun areItemsTheSame(oldItem: Message, newItem: Message) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Message,
            newItem: Message
        ): Boolean {
            return listOf(oldItem, newItem)
                .distinctBy {
                    listOf(it.text, it.author, it.dateTime)
                }
                .size == 1
        }
    }
}