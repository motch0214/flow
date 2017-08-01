package com.eighthours.flow.presenter.behavior

import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.presenter.adapter.PositionListAdapter
import com.eighthours.flow.presenter.behavior.bean.position.GroupPositionBean
import com.eighthours.flow.presenter.behavior.bean.position.InOutPositionBean
import com.eighthours.flow.presenter.behavior.bean.position.PLGroupPositionBean
import com.eighthours.flow.presenter.behavior.bean.position.TotalPLPositionBean
import com.eighthours.flow.presenter.viewmodel.AccountViewModel
import com.eighthours.flow.presenter.viewmodel.PLPositionViewModel
import com.eighthours.flow.utility.*
import com.orhanobut.logger.Logger

class PLPositionListBehavior(
        listAdapter: PositionListAdapter,
        vm: PLPositionViewModel,
        accountVm: AccountViewModel)
    : ManagedDisposable by Disposer() {

    init {
        Logger.v("")
        async {
            managed((accountVm.loadAccountMap() to vm.plPositions)
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
                    InOutPositionBean(position, accountMap.getValue(position.accountId))
                }
                .sortedBy { it.account.name }
        return listOf(TotalPLPositionBean(items))
                .plus(items.groupBy { accountMap.getValue(it.account.groupId!!) }
                        .entries.sortedBy { it.key.name }
                        .map {
                            PLGroupPositionBean(it.key, it.value)
                        })
    }
}
