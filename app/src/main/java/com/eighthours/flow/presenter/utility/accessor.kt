package com.eighthours.flow.presenter.utility

import android.app.Activity
import com.eighthours.flow.FlowApplication
import com.eighthours.flow.di.FlowComponent

interface FragmentAccessor {

    val component: FlowComponent
        get() = (getActivity().application as FlowApplication).component

    fun getActivity(): Activity
}
