package com.eighthours.flow

import android.app.Application
import android.util.Log
import com.eighthours.flow.di.DaggerFlowComponent
import com.eighthours.flow.di.FlowComponent
import com.eighthours.flow.di.module.AppModule

class FlowApplication
    : Application() {

    companion object {
        const val TAG = "Flow"
    }

    lateinit var component: FlowComponent

    override fun onCreate() {
        Log.i(TAG, """
███████╗██╗      ██████╗ ██╗    ██╗██╗██╗██╗
██╔════╝██║     ██╔═══██╗██║    ██║██║██║██║
█████╗  ██║     ██║   ██║██║ █╗ ██║██║██║██║
██╔══╝  ██║     ██║   ██║██║███╗██║╚═╝╚═╝╚═╝
██║     ███████╗╚██████╔╝╚███╔███╔╝██╗██╗██╗
╚═╝     ╚══════╝ ╚═════╝  ╚══╝╚══╝ ╚═╝╚═╝╚═╝""")
        super.onCreate()

        component = DaggerFlowComponent.builder()
                .appModule(AppModule(applicationContext))
                .build()
    }
}
