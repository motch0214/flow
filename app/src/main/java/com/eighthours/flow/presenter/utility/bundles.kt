package com.eighthours.flow.presenter.utility

import android.os.Bundle
import java.io.Serializable

class BundleKey<T : Serializable>(val identifier: String)

@Suppress("UNCHECKED_CAST")
fun <T : Serializable> Bundle?.get(key: BundleKey<T>): T? {
    return this?.getSerializable(key.identifier) as? T
}

fun <T : Serializable> Bundle.put(key: BundleKey<T>, value: Serializable) {
    this.putSerializable(key.identifier, value)
}

fun <T : Serializable> Bundle.putIfNotNull(key: BundleKey<T>, value: Serializable?) {
    value?.let { this.put(key, value) }
}
