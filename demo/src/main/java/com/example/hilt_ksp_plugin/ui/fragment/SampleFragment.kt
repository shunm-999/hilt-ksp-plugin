package com.example.hilt_ksp_plugin.ui.fragment

import androidx.fragment.app.Fragment
import com.example.hilt_ksp_plugin.model.InjectTarget
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SampleFragment : Fragment() {
    @Inject
    lateinit var injectTarget: InjectTarget
}