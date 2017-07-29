package com.eighthours.flow.presenter.utility.property

import android.databinding.ObservableBoolean
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.atomic.AtomicBoolean

class CommandProperty<T>(
        obsEnabled: Observable<Boolean> = Observable.just(true))
    : Observable<T>(), Disposable {

    // For layout
    val enabled = ObservableBoolean()

    private val subject = PublishSubject.create<T>().toSerialized()

    private val disposables = CompositeDisposable()

    private val isDisposed = AtomicBoolean()

    init {
        disposables.add(obsEnabled.doOnError {
            Logger.e(it, "")
        }.subscribe {
            enabled.set(it)
        })
    }

    // For layout
    fun execute(value: T) {
        subject.onNext(value)
    }

    override fun subscribeActual(observer: Observer<in T>) {
        subject.subscribe(observer)
    }

    override fun isDisposed() = isDisposed.get()

    override fun dispose() {
        if (isDisposed.compareAndSet(false, true)) {
            disposables.dispose()
            subject.onComplete()
            enabled.set(false)
        }
    }
}
