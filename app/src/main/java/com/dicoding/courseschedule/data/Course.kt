package com.dicoding.courseschedule.data

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//TODO 1 : Define a local database table using the schema in app/schema/course.json
@Parcelize
@Entity(tableName = "course")
data class Course(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    @NonNull
    val courseName: String,

    @NonNull
    val day: Int,

    @NonNull
    val startTime: String,

    @NonNull
    val endTime: String,

    @NonNull
    val lecturer: String,

    @NonNull
    val note: String
) : Parcelable
