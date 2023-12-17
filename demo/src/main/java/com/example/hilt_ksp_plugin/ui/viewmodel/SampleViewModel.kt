package com.example.hilt_ksp_plugin.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hilt_ksp_plugin.model.InjectTarget
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    private val injectTarget: InjectTarget
) : ViewModel() {

    fun doSomething() {
        injectTarget.doSomething()
    }
}