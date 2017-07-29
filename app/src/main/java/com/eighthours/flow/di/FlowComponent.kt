package com.eighthours.flow.di

import android.arch.lifecycle.ViewModelProvider
import com.eighthours.flow.di.module.AppModule
import com.eighthours.flow.di.module.ViewModelModule
import com.eighthours.flow.domain.model.ReconcileAction
import com.eighthours.flow.domain.model.SaveAccountAction
import com.eighthours.flow.domain.model.SubmitCashflowAction
import com.eighthours.flow.domain.repository.Repository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ViewModelModule::class))
interface FlowComponent {

    fun repository(): Repository

    fun vmFactory(): ViewModelProvider.Factory

    fun submitCashflowAction(): SubmitCashflowAction

    fun saveAccountAction(): SaveAccountAction

    fun reconcileAction(): ReconcileAction
}
