package com.example.eventplanner

import android.app.Application
import com.example.eventplanner.data.EventDatabase
import com.example.eventplanner.repository.EventRepository

class EventApplication : Application() {
    val database by lazy { EventDatabase.getDatabase(this) }
    val repository by lazy { EventRepository(database.eventDao()) }
}