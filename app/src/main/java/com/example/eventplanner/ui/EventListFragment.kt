package com.example.eventplanner.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventplanner.R
import com.example.eventplanner.data.Event
import com.example.eventplanner.databinding.FragmentEventListBinding
import com.example.eventplanner.viewmodel.EventViewModel
import com.google.android.material.snackbar.Snackbar

class EventListFragment : Fragment(R.layout.fragment_event_list) {

    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by activityViewModels()
    private lateinit var adapter: EventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEventListBinding.bind(view)

        adapter = EventAdapter(
            events = emptyList(),
            onEditClick = { event -> openEditScreen(event) },
            onDeleteClick = { event -> deleteEvent(event) }
        )

        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEvents.adapter = adapter

        viewModel.allEvents.observe(viewLifecycleOwner) { events ->
            adapter.updateData(events)
        }
    }

    private fun openEditScreen(event: Event) {
        val bundle = Bundle().apply {
            putInt("eventId", event.id)
        }
        findNavController().navigate(
            R.id.action_eventListFragment_to_editEventFragment,
            bundle
        )
    }

    private fun deleteEvent(event: Event) {
        viewModel.delete(event)
        Snackbar.make(binding.root, "Event deleted successfully", Snackbar.LENGTH_SHORT).show()
        Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}