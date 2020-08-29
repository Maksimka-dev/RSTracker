package com.rshack.rstracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rshack.rstracker.databinding.FragmentResultsBinding
import com.rshack.rstracker.view.adapter.TrackAdapter
import com.rshack.rstracker.viewmodel.ResultsViewModel

class ResultsFragment : Fragment() {

    companion object {
        fun newInstance() = ResultsFragment()
    }

    private val viewModel: ResultsViewModel by activityViewModels()
    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)

        val trackAdapter = TrackAdapter(TrackAdapter.OnClickListener {
            Toast.makeText(context, it.date.toString(), Toast.LENGTH_SHORT).show()
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trackAdapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        viewModel.tracks.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            trackAdapter.addItems(it)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
