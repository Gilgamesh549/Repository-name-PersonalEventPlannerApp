package com.example.eventplanner.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.eventplanner.R
import com.example.eventplanner.data.Event
import com.example.eventplanner.databinding.FragmentEditEventBinding
import com.example.eventplanner.viewmodel.EventViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditEventFragment : Fragment(R.layout.fragment_edit_event) {

    private var _binding: FragmentEditEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by activityViewModels()
    private var selectedDateTimeMillis: Long? = null
    private var currentEventId: Int = -1

    private val categories = listOf("Work", "Social", "Travel")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditEventBinding.bind(view)

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = spinnerAdapter

        currentEventId = arguments?.getInt("eventId") ?: -1

        if (currentEventId != -1) {
            viewModel.getEventById(currentEventId).observe(viewLifecycleOwner) { event ->
                event?.let { populateFields(it) }
            }
        }

        binding.buttonPickDateTime.setOnClickListener {
            showDateTimePicker()
        }

        binding.buttonUpdate.setOnClickListener {
            updateEvent()
        }
    }

    private fun populateFields(event: Event) {
        binding.editTitle.setText(event.title)
        binding.editLocation.setText(event.location)
        selectedDateTimeMillis = event.dateTime

        val index = categories.indexOf(event.category)
        if (index >= 0) binding.spinnerCategory.setSelection(index)

        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        binding.textSelectedDateTime.text = formatter.format(event.dateTime)
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        val selectedCalendar = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth, hourOfDay, minute, 0)
                            set(Calendar.MILLISECOND, 0)
                        }

                        if (selectedCalendar.timeInMillis < System.currentTimeMillis()) {
                            Snackbar.make(binding.root, "Past dates are not allowed", Snackbar.LENGTH_SHORT).show()
                            return@TimePickerDialog
                        }

                        selectedDateTimeMillis = selectedCalendar.timeInMillis
                        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        binding.textSelectedDateTime.text =
                            formatter.format(selectedCalendar.time)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateEvent() {
        val title = binding.editTitle.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem.toString()
        val location = binding.editLocation.text.toString().trim()
        val dateTime = selectedDateTimeMillis

        if (title.isEmpty()) {
            binding.editTitle.error = "Title is required"
            return
        }

        if (dateTime == null) {
            Snackbar.make(binding.root, "Please select a date", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (dateTime < System.currentTimeMillis()) {
            Snackbar.make(binding.root, "Past dates are not allowed", Snackbar.LENGTH_SHORT).show()
            return
        }

        val updatedEvent = Event(
            id = currentEventId,
            title = title,
            category = category,
            location = location,
            dateTime = dateTime
        )

        viewModel.update(updatedEvent)
        Toast.makeText(requireContext(), "Event updated successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}