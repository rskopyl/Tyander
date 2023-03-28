package com.rskopyl.tyander.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.rskopyl.tyander.R
import com.rskopyl.tyander.data.model.Character
import com.rskopyl.tyander.databinding.FragmentSearchBinding
import com.rskopyl.tyander.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val characterAdapter = CharacterAdapter()

    private val fakeDragOffset: Float
        get() = -binding.vpCharacters.width * REALISTIC_DRAG_COEFFICIENT

    private fun setupUi() = binding.run {
        val stackPageAppearance = StackPageTransformer.StackAppearance(
            translationYStep = resources.getDimension(R.dimen.medium_100),
            scaleStep = 0.05f,
            alphaStep = 0.25f
        )
        val dragLeftPageTransformer = StackPageTransformer(
            stackPageAppearance,
            StackPageTransformer.DragAppearance(
                direction = StackPageTransformer.Direction.LEFT
            )
        )
        val dragRightPageTransformer = StackPageTransformer(
            stackPageAppearance,
            StackPageTransformer.DragAppearance(
                direction = StackPageTransformer.Direction.RIGHT
            )
        )
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mi_theme -> viewModel.toggleNightMode()
                else -> return@setOnMenuItemClickListener false
            }
            true
        }
        vpCharacters.run {
            adapter = characterAdapter
            isUserInputEnabled = false
            offscreenPageLimit = 3
            setPageTransformer(dragLeftPageTransformer)
            registerOnScrollStateChangeCallback { state ->
                val isIdling = state == ViewPager2.SCROLL_STATE_IDLE
                btnSkipCharacter.isClickable = isIdling
                btnSaveCharacter.isClickable = isIdling
            }
        }
        btnSkipCharacter.setOnClickListener {
            vpCharacters.run {
                setPageTransformer(dragLeftPageTransformer)
                instantFakeDragBy(fakeDragOffset)
                viewModel.skipCharacter(currentItem)
            }
        }
        btnSaveCharacter.setOnClickListener {
            vpCharacters.run {
                setPageTransformer(dragRightPageTransformer)
                instantFakeDragBy(fakeDragOffset)
                viewModel.saveCharacter(currentItem)
            }
        }
    }

    private fun setupObservers() = viewModel.run {
        characters.collectOnLifecycle(
            viewLifecycleOwner,
            collector = ::showCharacters
        )
    }

    private fun showCharacters(
        characters: Resource<List<Character>>
    ) = binding.run {
        cpiCharacters.isVisible = characters is Resource.Loading
        vpCharacters.isVisible = characters is Resource.Success
        ivCharactersError.isVisible = characters is Resource.Error
        if (characters is Resource.Success) {
            vpCharacters.doOnNextIdle {
                characterAdapter.submitList(characters.data)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        setupUi()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {

        const val REALISTIC_DRAG_COEFFICIENT = 0.6f
    }
}