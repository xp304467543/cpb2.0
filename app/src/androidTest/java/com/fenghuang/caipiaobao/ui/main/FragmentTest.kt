package com.fenghuang.caipiaobao.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.base.FragmentTestRule
import com.fenghuang.caipiaobao.test.TestFragment
import org.junit.Rule
import org.junit.Test


class FragmentTest {

    @get:Rule
    val fragmentTestRule = FragmentTestRule.create(TestFragment::class.java)

    @Test
    fun testClick() {
        onView(ViewMatchers.withId(R.id.btnClick)).check(matches(isDisplayed())).perform(click())
    }

}