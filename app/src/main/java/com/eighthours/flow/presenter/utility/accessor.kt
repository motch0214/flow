package com.eighthours.flow.presenter.utility

import android.app.Activity
import android.app.Application
import com.eighthours.flow.FlowApplication
import com.eighthours.flow.di.FlowComponent

interface ActivityAccessor {

    val component: FlowComponent
        get() = (getApplication() as FlowApplication).component

    fun getApplication(): Application
}

interface FragmentAccessor {

    val component: FlowComponent
        get() = (getActivity().application as FlowApplication).component

    fun getActivity(): Activity
}
