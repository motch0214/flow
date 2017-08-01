package com.eighthours.flow.presenter.behavior

import android.view.View
import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.presenter.adapter.PositionListAdapter
import com.eighthours.flow.presenter.behavior.bean.position.AssetGroupPositionBean
import com.eighthours.flow.presenter.behavior.bean.position.AssetPositionBean
import com.eighthours.flow.presenter.behavior.bean.position.GroupPositionBean
import com.eighthours.flow.presenter.behavior.bean.position.TotalAssetPositionBean
import com.eighthours.flow.presenter.viewmodel.AccountViewModel
import com.eighthours.flow.presenter.viewmodel.AssetPositionViewModel
import com.eighthours.flow.utility.*
import com.orhanobut.logger.Logger
import io.reactivex.subjects.PublishSubject

class AssetPositionListBehavior(
        listAdapter: PositionListAdapter,
        vm: AssetPositionViewModel,
        accountVm: AccountViewModel)
    : ManagedDisposable by Disposer() {

    val menu: PublishSubject<Pair<View, Position>> = PublishSubject.create()

    init {
        Logger.v("")
        async {
            managed((accountVm.loadAccountMap() to vm.assetPositions)
                    .combineLatest(this::mapToBeans)
                    .onBackpressureLatest()
                    .observeOn(UI, false, 1)
                    .subscribe {
                        listAdapter.update(it)
                    })
        }
    }

    private fun mapToBeans(accountMap: Map<Long, Account>, positions: List<Position>): List<GroupPositionBean> {
        val items = positions.filter { accountMap[it.accountId] != null }
                .map { position ->
                    AssetPositionBean(position, accountMap.getValue(position.accountId)).apply {
                        managed(openMenu.subscribe { view -> menu.onNext(view to position) })
                    }
                }
                .sortedBy { it.account.name }
        return listOf(TotalAssetPositionBean(items))
                .plus(items.groupBy { accountMap.getValue(it.account.groupId!!) }
                        .entries.sortedBy { it.key.name }
                        .map {
                            AssetGroupPositionBean(it.key, it.value)
                        })
    }
}
