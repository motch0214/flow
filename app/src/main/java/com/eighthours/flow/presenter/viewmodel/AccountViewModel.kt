package com.eighthours.flow.presenter.viewmodel

import android.arch.lifecycle.ViewModel
import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.repository.Repository
import com.eighthours.flow.utility.Disposer
import com.eighthours.flow.utility.InitAtomicReference
import com.eighthours.flow.utility.ManagedDisposable
import com.eighthours.flow.utility.async
import com.orhanobut.logger.Logger
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

class AccountViewModel
@Inject constructor(
        private val repository: Repository)
    : ViewModel(), ManagedDisposable by Disposer() {

    private val accountMap: BehaviorProcessor<Map<Long, Account>> = BehaviorProcessor.create()

    private val accountMapRef = InitAtomicReference<Map<Long, Account>>()

    init {
        Logger.v("")
        async {
            managed(repository.account().loadAll()
                    .doOnNext { Logger.v("accounts updated $it") }
                    .subscribe { list ->
                        val map = list.map { it.id!! to it }.toMap()
                        accountMapRef.set(map)
                        accountMap.onNext(map)
                    })
        }
    }

    fun findAccount(id: Long): Account? {
        return accountMapRef.waitAndGet()[id]
    }

    fun findAccountMap(): Map<Long, Account> {
        return accountMapRef.waitAndGet()
    }

    fun loadAccountMap(): Flowable<Map<Long, Account>> {
        return accountMap
    }

    override fun onCleared() {
        Logger.v("")
        dispose()
        accountMap.onComplete()
    }
}
