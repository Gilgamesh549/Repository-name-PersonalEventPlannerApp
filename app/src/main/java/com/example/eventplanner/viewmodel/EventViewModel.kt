package com.example.eventplanner.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.eventplanner.EventApplication
import com.example.eventplanner.data.Event
import com.example.eventplanner.repository.EventRepository
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventRepository =
        (application as EventApplication).repository

    val allEvents: LiveData<List<Event>> = repository.allEvents

    fun insert(event: Event) = viewModelScope.launch {
        repository.insert(event)
    }

    fun update(event: Event) = viewModelScope.launch {
        repository.update(event)
    }

    fun delete(event: Event) = viewModelScope.launch {
        repository.delete(event)
    }

    fun getEventById(id: Int): LiveData<Event?> {
        val result = MutableLiveData<Event?>()
        viewModelScope.launch {
            result.postValue(repository.getEventById(id))
        }
        return result
    }
}