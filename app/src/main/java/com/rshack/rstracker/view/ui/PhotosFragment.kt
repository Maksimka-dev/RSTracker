package com.rshack.rstracker.view.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.rshack.rstracker.databinding.FragmentPhotosBinding
import com.rshack.rstracker.view.adapter.PhotosAdapter
import com.rshack.rstracker.viewmodel.PhotosViewModel

class PhotosFragment : Fragment() {
    private lateinit var application: Application

    private val viewModel: PhotosViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        application = requireNotNull(activity).application
        val binding = FragmentPhotosBinding.inflate(inflater)
        binding.photoGrid.adapter = PhotosAdapter(PhotosAdapter.OnClickListener{
            Toast.makeText(application, "click", Toast.LENGTH_SHORT).show()
        })

        viewModel.photos.observe(viewLifecycleOwner, Observer {
            val adapter = binding.photoGrid.adapter as PhotosAdapter
            adapter.submitList(it)
        })

        return binding.root
    }
}