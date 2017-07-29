package com.eighthours.flow.presenter.viewmodel

import android.arch.lifecycle.ViewModel
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.domain.repository.Repository
import com.eighthours.flow.utility.Disposer
import com.eighthours.flow.utility.ManagedDisposable
import com.eighthours.flow.utility.async
import com.orhanobut.logger.Logger
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

class AssetPositionViewModel
@Inject constructor(
        private val repository: Repository)
    : ViewModel(), ManagedDisposable by Disposer() {

    val assetPositions: BehaviorProcessor<List<Position>> = BehaviorProcessor.create()

    init {
        Logger.v("")
        async {
            managed(repository.position().loadAssetPositions()
                    .doOnNext { Logger.v("asset positions updated $it") }
                    .subscribe {
                        assetPositions.onNext(it)
                    })
        }
    }

    override fun onCleared() {
        Logger.v("")
        dispose()
        assetPositions.onComplete()
    }
}
