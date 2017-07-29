package com.eighthours.flow.presenter.viewmodel

import android.arch.lifecycle.ViewModel
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.domain.repository.Repository
import com.eighthours.flow.utility.Disposer
import com.eighthours.flow.utility.ManagedDisposable
import com.eighthours.flow.utility.async
import com.orhanobut.logger.Logger
import io.reactivex.processors.BehaviorProcessor
import org.threeten.bp.YearMonth
import javax.inject.Inject

class PLPositionViewModel
@Inject constructor(
        repository: Repository)
    : ViewModel(), ManagedDisposable by Disposer() {

    val plPositions: BehaviorProcessor<List<Position>> = BehaviorProcessor.create()

    private val monthSubject = BehaviorProcessor.createDefault(YearMonth.now())

    init {
        Logger.v("")
        async {
            managed(monthSubject.subscribe { month ->
                managed(repository.position().loadInOutPositions(month)
                        .doOnNext { Logger.v("in/out positions in $month updated $it") }
                        .subscribe {
                            plPositions.onNext(it)
                        })
            })
        }
    }

    fun changeMonth(month: YearMonth) {
        monthSubject.onNext(month)
    }

    override fun onCleared() {
        Logger.v("")
        dispose()
        monthSubject.onComplete()
        plPositions.onComplete()
    }
}
