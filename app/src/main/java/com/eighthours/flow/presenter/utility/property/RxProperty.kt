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
import java.util.concurrent.atomic.AtomicBoolean

abstract class RxProperty<T>(
        initial: T? = null,
        obsVisibility: Observable<Int> = Observable.just(View.VISIBLE))
    : Observable<T>(), Disposable {

    // For layout
    val text = StringField()

    // For layout
    val visibility = ObservableInt()

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

    // For behavior
    fun update(value: T) {
        synchronized(this) {
            text.setText(format(value))
            subject.onNext(value)
        }
    }

    open fun onTextChanged(text: String?) {}

    // For behavior
    fun get(): T? = subject.value

    fun set(string: String?) {
        val value = parse(string)
        if (value != null) {
            update(value)
        } else {
            text.set(null)
        }
        onTextChanged(string)
    }

    final override fun subscribeActual(observer: Observer<in T>) {
        subject.subscribe(observer)
    }

    final override fun isDisposed(): Boolean = isDisposed.get()

    override fun dispose() {
        if (isDisposed.compareAndSet(false, true)) {
            disposables.dispose()
            subject.onComplete()
        }
    }

    abstract fun format(value: T): String?

    abstract fun parse(string: String?): T


    inner class StringField : ObservableField<String>() {

        override fun set(string: String?) {
            this@RxProperty.set(string)
        }

        fun setText(string: String?) {
            super.set(string)
        }
    }
}

class RxStringProperty(
        initial: String? = null,
        obsVisibility: Observable<Int> = Observable.just(View.VISIBLE))
    : RxProperty<String>(initial, obsVisibility) {

    override fun format(value: String): String? = value.takeUnless { it == "" }

    override fun parse(string: String?): String = string ?: ""
}
