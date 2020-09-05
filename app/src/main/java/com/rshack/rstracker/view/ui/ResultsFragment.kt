package com.rshack.rstracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rshack.rstracker.databinding.FragmentResultsBinding
import com.rshack.rstracker.view.adapter.TrackAdapter
import com.rshack.rstracker.viewmodel.ResultsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultsFragment : Fragment() {

    private val viewModel: ResultsViewModel by viewModels()
    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)

        val detailClickListener = TrackAdapter.OnDetailClickListener {
            viewModel.displayMapFragment(it)
        }
        val imageClickListener = TrackAdapter.OnImageClickListener {
            viewModel.displayPhotoFragment(it)
        }

        viewModel.navigateToPhotoFragment.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController().navigate(ResultsFragmentDirections.actionShowPhotos(it))
                viewModel.displayPhotoFragmentComplete()
            }
        }

        viewModel.navigateToMapFragment.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController().navigate(ResultsFragmentDirections.actionShowMap(it))
                viewModel.displayMapFragmentComplete()
            }
        }

        val trackAdapter = TrackAdapter(detailClickListener, imageClickListener)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trackAdapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        viewModel.tracks.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            trackAdapter.submitList(it)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
