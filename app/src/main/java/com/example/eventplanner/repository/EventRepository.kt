package com.example.eventplanner.repository

import androidx.lifecycle.LiveData
import com.example.eventplanner.data.Event
import com.example.eventplanner.data.EventDao

class EventRepository(private val eventDao: EventDao) {

    val allEvents: LiveData<List<Event>> = eventDao.getAllEvents()

    suspend fun insert(event: Event) {
        eventDao.insertEvent(event)
    }

    suspend fun update(event: Event) {
        eventDao.updateEvent(event)
    }

    suspend fun delete(event: Event) {
        eventDao.deleteEvent(event)
    }

    suspend fun getEventById(id: Int): Event? {
        return eventDao.getEventById(id)
    }
}