package com.eighthours.flow.presenter.utility.property

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.atomic.AtomicBoolean

class ObservableProperty<T>(
        initial: T? = null,
        obsVisibility: Observable<Int> = Observable.just(View.VISIBLE),
        private val format: (T) -> String)
    : Observable<T>(), Disposable {

    // For layout
    val text = ObservableField<String>()

    // For layout
    val visibility = ObservableInt()

    // For behavior
    val command: PublishSubject<Unit> = PublishSubject.create()

    private val subject = BehaviorSubject.create<T>()

    val disposables = CompositeDisposable()

    private val isDisposed = AtomicBoolean()

    init {
        disposables.add(obsVisibility.doOnError {
            Logger.e(it, "")
        }.subscribe {
            visibility.set(it)
        })
        if (initial != null) update(initial)
    }

    // For layout
    fun execute() {
        command.onNext(Unit)
    }

    // For behavior
    fun update(value: T) {
        text.set(format(value))
        subject.onNext(value)
    }

    // For behavior
    fun get(): T? = subject.value

    override fun subscribeActual(observer: Observer<in T>) {
        subject.subscribe(observer)
    }

    override fun isDisposed(): Boolean = isDisposed.get()

    override fun dispose() {
        if (isDisposed.compareAndSet(false, true)) {
            disposables.dispose()
            subject.onComplete()
            command.onComplete()
        }
    }
}
