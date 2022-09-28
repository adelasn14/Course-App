package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.databinding.ActivityAddCourseBinding
import com.dicoding.courseschedule.ui.home.ViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private var startTime: String = ""
    private var endTime: String = ""
    private lateinit var binding: ActivityAddCourseBinding
    private lateinit var addCourseViewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_course)
        setupViewModel()
    }

    private fun setupViewModel() {
        addCourseViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[AddCourseViewModel::class.java]

        addCourseViewModel.saved.observe(this) {
            if (it.getContentIfNotHandled() == true) {
                finish()
            } else {
                Toast.makeText(applicationContext, "Time cant be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                binding.apply {
                    val addCourse = edAddCourse.text.toString()
                    val addDay = chooseDaySpinner.selectedItemPosition
                    val addLecturer = edAddLecturer.text.toString()
                    val addNote = edAddNote.text.toString()

                    if (addCourse.isNotEmpty() && addLecturer.isNotEmpty() && addNote.isNotEmpty()) {
                        addCourseViewModel.insertCourse(addCourse, addDay, startTime, endTime, addLecturer, addNote)
                        Log.d("AddTaskActivity", "Menambahkan : $addCourse, $addDay, $startTime, $endTime, $addLecturer, $addNote")
                        finish()
                        Toast.makeText(applicationContext, "New Course successfully added", Toast.LENGTH_SHORT).show()
                    }
                    else
                        Toast.makeText(applicationContext, "New course cannot be empty", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showStartTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, START_TIME_PICKER)
    }

    fun showEndTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, END_TIME_PICKER)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            START_TIME_PICKER -> {
                binding.tvStartTime.text = timeFormat.format(calendar.time)
                startTime = timeFormat.format(calendar.time)
            }
            END_TIME_PICKER -> {
                binding.tvEndTime.text = timeFormat.format(calendar.time)
                endTime = timeFormat.format(calendar.time)
            }
        }
    }

    companion object {
        private const val START_TIME_PICKER = "startTimePicker"
        private const val END_TIME_PICKER = "endTimePicker"
    }
}