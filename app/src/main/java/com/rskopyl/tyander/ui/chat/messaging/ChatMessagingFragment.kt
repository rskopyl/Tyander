package com.rskopyl.tyander.ui.chat.messaging

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.rskopyl.tyander.R
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.data.model.Message
import com.rskopyl.tyander.databinding.FragmentChatMessagingBinding
import com.rskopyl.tyander.util.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatMessagingFragment : Fragment(R.layout.fragment_chat_messaging) {

    @Inject
    lateinit var viewModelDaggerFactory: ChatMessagingViewModel.DaggerFactory

    private val viewModel: ChatMessagingViewModel by viewModels {
        val args by navArgs<ChatMessagingFragmentArgs>()
        ViewModelProvider.Factory {
            viewModelDaggerFactory.create(args.matchId)
        }
    }

    private var _binding: FragmentChatMessagingBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val messageAdapter = MessageAdapter()

    private fun setupUi() = binding.run {
        toolbar.run {
            setupWithNavController(findNavController())
            setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.mi_delete_all -> viewModel.deleteMessages()
                    else -> return@setOnMenuItemClickListener false
                }
                true
            }
        }
        rvMessages.run {
            adapter = messageAdapter
            setHasFixedSize(true)
            addItemDecoration(
                SpacingItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.small_100)
                )
            )
        }
        tilMessageText.run {
            isEndIconVisible = false
            setEndIconOnClickListener {
                val text = etMessageText.text?.toString() ?: ""
                etMessageText.text = null
                viewModel.sendMessage(text)
            }
        }
        etMessageText.doOnTextChanged { text, _, _, _ ->
            tilMessageText.isEndIconVisible = !text.isNullOrBlank()
        }
    }

    private fun setupObservers() = viewModel.run {
        match.collectOnLifecycle(
            viewLifecycleOwner,
            collector = ::showMatch
        )
        messages.collectOnLifecycle(
            viewLifecycleOwner,
            collector = ::showMessages
        )
    }

    private fun showMatch(match: Resource<Match>) = binding.run {
        toolbar.title = if (match is Resource.Success) {
            getString(
                R.string.name_age,
                match.data.character.name, match.data.character.age
            )
        } else {
            ""
        }
    }

    private fun showMessages(messages: Resource<List<Message>>) {
        messageAdapter.submitList(messages.dataOrNull() ?: emptyList())
        binding.rvMessages.smoothScrollToPosition(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatMessagingBinding.bind(view)
        setupUi()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}