package com.eighthours.flow.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.eighthours.flow.di.ViewModelFactory
import com.eighthours.flow.presenter.viewmodel.AccountViewModel
import com.eighthours.flow.presenter.viewmodel.AssetPositionViewModel
import com.eighthours.flow.presenter.viewmodel.PLPositionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(vm: AccountViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(AssetPositionViewModel::class)
    abstract fun bindAssetPositionViewModel(vm: AssetPositionViewModel): ViewModel

    @Binds @IntoMap
    @ViewModelKey(PLPositionViewModel::class)
    abstract fun bindPLPositionViewModel(vm: PLPositionViewModel): ViewModel
}
