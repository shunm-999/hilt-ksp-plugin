package com.example.hilt_ksp_plugin.ui.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.hilt_ksp_plugin.model.InjectTarget
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SampleBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var injectTarget: InjectTarget
    override fun onReceive(context: Context?, intent: Intent?) {

    }
}