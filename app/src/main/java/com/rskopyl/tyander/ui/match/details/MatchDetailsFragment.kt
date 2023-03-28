package com.rskopyl.tyander.ui.match.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rskopyl.tyander.R
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.databinding.FragmentMatchDetailsBinding
import com.rskopyl.tyander.util.Resource
import com.rskopyl.tyander.util.collectOnLifecycle
import com.rskopyl.tyander.util.invoke
import com.rskopyl.tyander.util.toJavaDate
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MatchDetailsFragment : Fragment(R.layout.fragment_match_details) {

    @Inject
    lateinit var viewModelDaggerFactory: MatchDetailsViewModel.DaggerFactory

    private val viewModel: MatchDetailsViewModel by viewModels {
        val args by navArgs<MatchDetailsFragmentArgs>()
        ViewModelProvider.Factory {
            viewModelDaggerFactory.create(args.matchId)
        }
    }

    private var _binding: FragmentMatchDetailsBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val dateFormat =
        SimpleDateFormat("MMM, dd", Locale.getDefault())

    private fun setupUi() = binding.run {
        toolbar.run {
            setupWithNavController(findNavController())
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.mi_delete -> showDeleteMatchDialog()
                    else -> return@setOnMenuItemClickListener false
                }
                true
            }
        }
        fabChat.setOnClickListener {
            MatchDetailsFragmentDirections
                .actionMatchDetailsFragmentToChatMessagingFragment(
                    matchId = viewModel.matchId
                )
                .let { findNavController().navigate(it) }
        }
    }

    private fun setupObservers() = viewModel.run {
        match.collectOnLifecycle(
            viewLifecycleOwner,
            collector = ::showMatch
        )
    }

    private fun showMatch(match: Resource<Match>) = binding.run {
        cpiMatch.isVisible = match is Resource.Loading
        if (match is Resource.Success) {
            val character = match.data.character
            ivCharacterImage.load(character.imageUrl)
            tvCharacterNameAge.text = getString(
                R.string.name_age,
                character.name, character.age
            )
            tvCharacterAddress.text = getString(
                R.string.country_city,
                character.address.country, character.address.city
            )
            tvCharacterEmail.text = character.email
            tvCharacterPhone.text = character.phone
            tvMatchTime.text = getString(
                R.string.matched_on_value,
                dateFormat.format(match.data.dateTime.toJavaDate())
            )
        }
    }

    private fun showDeleteMatchDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_match)
            .setMessage(R.string.delete_match_hint)
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.deleteMatch()
                findNavController().navigateUp()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMatchDetailsBinding.bind(view)
        setupUi()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}