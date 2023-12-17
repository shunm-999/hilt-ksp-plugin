package com.example.hilt_ksp_plugin.ui.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.hilt_ksp_plugin.model.InjectTarget
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SampleService : Service() {

    @Inject
    lateinit var injectTarget: InjectTarget

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}