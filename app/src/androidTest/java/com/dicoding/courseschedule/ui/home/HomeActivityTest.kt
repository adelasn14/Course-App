package com.dicoding.courseschedule.ui.home

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.courseschedule.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class TaskActivityTest {
    @Before
    fun setup(){
        ActivityScenario.launch(HomeActivity::class.java)
    }

    @Test
    fun testDisplayingAddCourseActivity() {
        onView(withId(R.id.view_home)).check(matches(isDisplayed()))
        onView(withId(R.id.action_add)).perform(click())
        onView(withId(R.id.ed_add_course)).check(matches(isDisplayed()))
        onView(withId(R.id.choose_day_spinner)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_lecturer)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_note)).check(matches(isDisplayed()))

    }
}