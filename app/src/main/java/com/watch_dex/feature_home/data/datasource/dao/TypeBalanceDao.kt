package com.watch_dex.feature_home.data.datasource.dao

import androidx.room.*
import com.watch_dex.core.domain.entity.Balance
import com.watch_dex.core.domain.entity.TypeBalance
import com.watch_dex.core.domain.entity.Type
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeBalanceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(typeBalance: TypeBalance)

    @Delete
    suspend fun delete(typeBalance: TypeBalance)

    @Update
    suspend fun update(typeBalance: TypeBalance)

    @Query("SELECT id, name FROM TypeBalance")
    fun getTypes(): Flow<List<Type>>

    @Query("SELECT id, relation from TypeBalance WHERE id == :typeId")
    fun getOffensiveFor(typeId: Char): Flow<Balance>

    @Query("SELECT id, relation from TypeBalance WHERE id IN (:ids)")
    fun getOffensiveFor(ids: List<Char>): Flow<List<Balance>>

    @Query("SELECT id, relation from TypeBalance WHERE relation LIKE '%'||:typeId||'%'")
    fun getDefensiveFor(typeId: Char) : Flow<List<Balance>>

}
