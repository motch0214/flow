package com.eighthours.flow.domain.model

import com.eighthours.flow.domain.entity.Amount
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.domain.repository.Repository
import com.eighthours.flow.utility.async
import com.orhanobut.logger.Logger
import javax.inject.Inject

class ReconcileAction
@Inject constructor(
        private val repository: Repository) {

    fun reconcile(position: Position, amount: Amount) = async {
        Logger.d("reconcile $position by $amount")

    }
}
