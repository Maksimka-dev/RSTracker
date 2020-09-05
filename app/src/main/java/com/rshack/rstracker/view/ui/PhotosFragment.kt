package com.rshack.rstracker.view.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rshack.rstracker.databinding.FragmentPhotosBinding
import com.rshack.rstracker.view.adapter.PhotosAdapter
import com.rshack.rstracker.viewmodel.PhotosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosFragment : Fragment() {

    private lateinit var application: Application

    private val viewModel: PhotosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        application = requireNotNull(activity).application
        val binding = FragmentPhotosBinding.inflate(inflater)

        val track = PhotosFragmentArgs.fromBundle(requireArguments())
            .selectedTrack

        binding.photoGrid.adapter = PhotosAdapter(PhotosAdapter.OnClickListener {
            viewModel.onPhotoClicked(it, track)
            this.findNavController().navigate(PhotosFragmentDirections.actionShowResults())
        })

        viewModel.photos.observe(viewLifecycleOwner) {
            val adapter = binding.photoGrid.adapter as PhotosAdapter
            adapter.submitList(it)
        }

        return binding.root
    }
}
