package com.eighthours.flow.utility

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

class InitAtomicReference<T> {

    private val ref = AtomicReference<T>()

    private val latch = CountDownLatch(1)

    fun waitAndGet(): T {
        latch.await()
        return ref.get()
    }

    fun waitAndGet(timeout: Long, unit: TimeUnit): T {
        latch.await(timeout, unit)
        return ref.get()
    }

    fun set(value: T) {
        ref.set(value)
        latch.countDown()
    }
}
