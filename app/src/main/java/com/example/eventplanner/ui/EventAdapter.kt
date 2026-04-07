package com.example.eventplanner.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventplanner.data.Event
import com.example.eventplanner.databinding.ItemEventBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventAdapter(
    private var events: List<Event>,
    private val onEditClick: (Event) -> Unit,
    private val onDeleteClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        holder.binding.textTitle.text = event.title
        holder.binding.textCategory.text = "Category: ${event.category}"
        holder.binding.textLocation.text = "Location: ${event.location}"
        holder.binding.textDateTime.text = "Date: ${formatter.format(Date(event.dateTime))}"

        holder.binding.buttonEdit.setOnClickListener { onEditClick(event) }
        holder.binding.buttonDelete.setOnClickListener { onDeleteClick(event) }
    }

    override fun getItemCount(): Int = events.size

    fun updateData(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }
}