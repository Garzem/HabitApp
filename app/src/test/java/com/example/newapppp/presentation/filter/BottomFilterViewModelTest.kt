package com.example.newapppp.presentation.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [33])
@RunWith(AndroidJUnit4::class)
internal class BottomFilterViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

}