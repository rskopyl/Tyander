package com.rskopyl.tyander.ui.match.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rskopyl.tyander.R
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.databinding.FragmentMatchListBinding
import com.rskopyl.tyander.util.Resource
import com.rskopyl.tyander.util.SpacingItemDecoration
import com.rskopyl.tyander.util.collectOnLifecycle
import com.rskopyl.tyander.util.dataOrNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchListFragment : Fragment(R.layout.fragment_match_list) {

    private val viewModel: MatchListViewModel by viewModels()

    private var _binding: FragmentMatchListBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val matchAdapter = MatchAdapter(
        onItemClick = { match ->
            MatchListFragmentDirections
                .actionMatchListFragmentToMatchDetailsFragment(
                    matchId = match.id
                )
                .let { findNavController().navigate(it) }
        }
    )

    private fun setupUi() = binding.run {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mi_delete_all -> showDeleteAllMatchesDialog()
                else -> return@setOnMenuItemClickListener false
            }
            true
        }
        rvMatches.run {
            adapter = matchAdapter
            setHasFixedSize(true)
            addItemDecoration(
                SpacingItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.small_125)
                )
            )
        }
    }

    private fun setupObservers() = viewModel.run {
        matches.collectOnLifecycle(
            viewLifecycleOwner,
            collector = ::showMatches
        )
    }

    private fun showMatches(
        matches: Resource<List<Match>>
    ) = binding.run {
        cpiMatches.isVisible = matches is Resource.Loading
        rvMatches.isVisible = matches.let {
            it is Resource.Success && it.data.isNotEmpty()
        }
        flowEmpty.isVisible = matches.let {
            it is Resource.Success && it.data.isEmpty()
        }
        matchAdapter.submitList(matches.dataOrNull() ?: emptyList())
    }

    private fun showDeleteAllMatchesDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_all_matches)
            .setMessage(R.string.delete_all_matches_hint)
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.deleteAllMatches()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMatchListBinding.bind(view)
        setupUi()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}