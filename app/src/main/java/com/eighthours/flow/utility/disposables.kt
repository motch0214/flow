package com.eighthours.flow.utility

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

interface ManagedDisposable : Disposable {

    fun <T : Disposable> managed(disposable: T): T
}


class MultiTimeDisposer : ManagedDisposable {

    private var disposables = CompositeDisposable()

    override fun dispose() {
        disposables.dispose()
        synchronized(this) {
            if (disposables.isDisposed)
                disposables = CompositeDisposable()
        }
    }

    override fun isDisposed() = false

    override fun <T : Disposable> managed(disposable: T): T {
        return disposable.also { disposables.add(it) }
    }
}


class Disposer : ManagedDisposable {

    private var disposables = CompositeDisposable()

    override fun dispose() {
        disposables.dispose()
    }

    override fun isDisposed() = disposables.isDisposed

    override fun <T : Disposable> managed(disposable: T): T {
        return disposable.also { disposables.add(it) }
    }
}
