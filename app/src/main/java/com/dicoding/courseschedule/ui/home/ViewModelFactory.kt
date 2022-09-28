package com.dicoding.courseschedule.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.ui.add.AddCourseViewModel

class ViewModelFactory private constructor(private val repository: DataRepository?) :
    ViewModelProvider.Factory{

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    DataRepository.getInstance(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                repository?.let { HomeViewModel(it) } as T
            }
            modelClass.isAssignableFrom(AddCourseViewModel::class.java) -> {
                repository?.let { AddCourseViewModel(it) } as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}