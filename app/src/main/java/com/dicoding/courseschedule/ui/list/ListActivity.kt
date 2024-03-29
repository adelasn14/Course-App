package com.dicoding.courseschedule.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.databinding.ActivityListBinding
import com.dicoding.courseschedule.paging.CourseAdapter
import com.dicoding.courseschedule.paging.CourseViewHolder
import com.dicoding.courseschedule.ui.add.AddCourseActivity
import com.dicoding.courseschedule.ui.detail.DetailActivity
import com.dicoding.courseschedule.ui.setting.SettingsActivity
import com.dicoding.courseschedule.util.SortType

class ListActivity : AppCompatActivity() {
    private lateinit var viewModel: ListViewModel
    private lateinit var binding: ActivityListBinding
    private lateinit var rvCourse: RecyclerView
    private val courseAdapter: CourseAdapter by lazy {
        CourseAdapter(::onCourseClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ListViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[ListViewModel::class.java]

        setFabClick()
        setUpRecycler()
        initAction()
        updateList()
    }

    private fun setUpRecycler() {
        rvCourse = findViewById(R.id.rv_course)
        rvCourse.layoutManager = LinearLayoutManager(this)
        rvCourse.adapter = courseAdapter
    }

    private fun onCourseClick(course: Course) {
        //TODO 8 : Intent and show detailed course
        val intentToDetail = Intent(this, DetailActivity::class.java)
        intentToDetail.putExtra(DetailActivity.COURSE_ID, course.id)

        startActivity(intentToDetail)
    }

    private fun initAction() {
        val callback = Callback()
        val itemTouchHelper = ItemTouchHelper(callback)

        itemTouchHelper.attachToRecyclerView(rvCourse)
    }

    private fun updateList() {
        viewModel.courses.observe(this) {
            courseAdapter.submitList(it)
            rvCourse.adapter = courseAdapter
            findViewById<TextView>(R.id.tv_empty_list).visibility =
                if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setFabClick() {
        //TODO 9 : Create AddCourseActivity to set new course schedule
        binding.fab.setOnClickListener {
            val intentAddNewCourse = Intent(this, AddCourseActivity::class.java)
            startActivity(intentAddNewCourse)
        }

    }

    //TODO 14 : Fixing bug : sort menu not show and course not deleted when list is swiped
    private fun showSortMenu() {
        val view = findViewById<View>(R.id.action_sort) ?: return
        PopupMenu(this, view).run {
            menuInflater.inflate(R.menu.sort_course, menu)

            setOnMenuItemClickListener {
                viewModel.sort(
                    when (it.itemId) {
                        R.id.sort_time -> SortType.TIME
                        R.id.sort_course_name -> SortType.COURSE_NAME
                        else -> SortType.LECTURER
                    }
                )
                true
            }
            show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort -> {
                showSortMenu()
                true
            }
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    inner class Callback : ItemTouchHelper.Callback() {

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(0, ItemTouchHelper.RIGHT)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val course = (viewHolder as CourseViewHolder).getCourse
            viewModel.delete(course)
            Toast.makeText(applicationContext, "Task successfully deleted", Toast.LENGTH_SHORT).show()

        }
    }
}