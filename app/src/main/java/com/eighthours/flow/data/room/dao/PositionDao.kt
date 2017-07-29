package com.eighthours.flow.data.room.dao

import android.arch.persistence.room.*
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.domain.repository.PositionRepository
import io.reactivex.Flowable
import org.threeten.bp.YearMonth

@Dao
interface PositionDao
    : PositionRepository {

    @Query("""
        SELECT p.* FROM Position p
        INNER JOIN Account a ON a.id = p.accountId
        WHERE a.type = 'ASSET'
    """)
    override fun loadAssetPositions(): Flowable<List<Position>>

    @Query("""
        SELECT p.* FROM Position p
        INNER JOIN Account a ON a.id = p.accountId
        WHERE a.type IN ('INCOME', 'OUTGO') AND p.month = :month
    """)
    override fun loadInOutPositions(month: YearMonth): Flowable<List<Position>>

    @Query("""
        SELECT * FROM Position
        WHERE accountId = :accountId AND month IS NULL
    """)
    override fun findAssetPosition(accountId: Long): Position?

    @Query("""
        SELECT * FROM Position
        WHERE accountId = :accountId AND month = :month
    """)
    override fun findInOutPosition(accountId: Long, month: YearMonth): Position?

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    override fun insertAll(positions: List<Position>)

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    override fun updateAll(positions: List<Position>)
}
