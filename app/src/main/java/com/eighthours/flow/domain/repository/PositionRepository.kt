package com.eighthours.flow.domain.repository

import com.eighthours.flow.domain.entity.Position
import io.reactivex.Flowable
import org.threeten.bp.YearMonth

interface PositionRepository {

    fun loadAssetPositions(): Flowable<List<Position>>

    fun loadInOutPositions(month: YearMonth): Flowable<List<Position>>

    fun findAssetPosition(accountId: Long): Position?

    fun findInOutPosition(accountId: Long, month: YearMonth): Position?

    fun insertAll(positions: List<Position>)

    fun updateAll(positions: List<Position>)
}
