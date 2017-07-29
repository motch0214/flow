package com.eighthours.flow.domain.repository

interface Repository {

    fun account(): AccountRepository

    fun position(): PositionRepository

    fun transaction(task: () -> Unit)
}
