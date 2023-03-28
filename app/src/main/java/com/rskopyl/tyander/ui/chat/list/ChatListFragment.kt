package com.rskopyl.tyander.ui.chat.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rskopyl.tyander.R
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.data.model.Message
import com.rskopyl.tyander.databinding.FragmentChatListBinding
import com.rskopyl.tyander.util.Resource
import com.rskopyl.tyander.util.SpacingItemDecoration
import com.rskopyl.tyander.util.collectOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : Fragment(R.layout.fragment_chat_list) {

    private val viewModel: ChatListViewModel by viewModels()

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val matchWithMessageAdapter = MatchWithMessageAdapter(
        onItemClick = { (match, _) ->
            ChatListFragmentDirections
                .actionChatListFragmentToChatMessagingFragment(
                    matchId = match.id
                )
                .let { findNavController().navigate(it) }
        }
    )

    private fun setupUi() = binding.run {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mi_delete_all -> showDeleteAllChatsDialog()
                else -> return@setOnMenuItemClickListener false
            }
            true
        }
        rvChats.run {
            adapter = matchWithMessageAdapter
            setHasFixedSize(true)
            addItemDecoration(
                SpacingItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.smallest)
                )
            )
        }
    }

    private fun setupObservers() = viewModel.run {
        matchToLastMessage.collectOnLifecycle(
            viewLifecycleOwner,
            collector = ::showMatchToLastMessage
        )
    }

    private fun showMatchToLastMessage(
        matchToLastMessage: Resource<Map<Match, Message>>
    ) = binding.run {
        cpiChats.isVisible = matchToLastMessage is Resource.Loading
        rvChats.isVisible = matchToLastMessage.let {
            it is Resource.Success && it.data.isNotEmpty()
        }
        flowEmpty.isVisible = matchToLastMessage.let {
            it is Resource.Success && it.data.isEmpty()
        }
        matchWithMessageAdapter.submitList(
            matchToLastMessage.let {
                if (it is Resource.Success) it.data.toList()
                else emptyList()
            }
        )
    }

    private fun showDeleteAllChatsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_all_chats)
            .setMessage(R.string.delete_all_chats_hint)
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.deleteAllChats()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatListBinding.bind(view)
        setupUi()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}