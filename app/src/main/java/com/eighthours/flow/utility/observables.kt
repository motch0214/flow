package com.eighthours.flow.utility

import com.orhanobut.logger.Logger
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.SingleSubject
import org.reactivestreams.Publisher

fun <T> async(scheduler: Scheduler = Schedulers.io(), task: () -> T): Single<T> {
    val single = SingleSubject.create<T>()
    Single.create<T> {
        try {
            it.onSuccess(task())
        } catch (e: Exception) {
            Logger.e(e, "")
            it.onError(e)
        }
    }.subscribeOn(scheduler).subscribe(single)
    return single
}

fun <T, R> Single<T>.then(task: (T) -> R): Single<R> = map(task)

val UI = AndroidSchedulers.mainThread()


fun <T1, T2, R> Pair<Publisher<T1>, Publisher<T2>>.combineLatest(func: (T1, T2) -> R): Flowable<R>
        = Flowable.combineLatest(this.first, this.second, BiFunction<T1, T2, R> { t1, t2 -> func(t1, t2) })
