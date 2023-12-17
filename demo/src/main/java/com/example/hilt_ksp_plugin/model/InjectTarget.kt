package com.example.hilt_ksp_plugin.model

import javax.inject.Inject

class InjectTarget @Inject constructor() {
    fun doSomething() {
        println("doSomething")
    }
}