package com.eighthours.flow.presenter.behavior.bean.position

import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.sum
import com.eighthours.flow.presenter.utility.Formatter
import io.reactivex.disposables.Disposable
import java.util.concurrent.atomic.AtomicBoolean

data class AssetGroupPositionBean(
        val group: Account,
        val positions: List<AssetPositionBean>)
    : GroupPositionBean, Disposable {

    override val id get() = group.id!!

    override val type get() = PositionBeanType.ASSET_GROUP

    override val name get() = group.name

    override val amount = Formatter.format(positions.map { it.position.amount }.sum())

    private val isDisposed = AtomicBoolean()

    override fun isDisposed() = isDisposed.get()

    override fun dispose() {
        if (isDisposed.compareAndSet(false, true)) {
            positions.forEach { it.dispose() }
        }
    }
}
