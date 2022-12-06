package com.watch_dex.core.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.watch_dex.core.domain.entity.Pokemon
import com.watch_dex.core.domain.entity.TypeBalance
import com.watch_dex.feature_home.data.datasource.dao.PokemonDao
import com.watch_dex.feature_home.data.datasource.dao.TypeBalanceDao

@Database(entities = [TypeBalance::class, Pokemon::class], version = 1, exportSchema = false)
abstract class TypeDatabase : RoomDatabase() {

    abstract fun getTypeChartDao(): TypeBalanceDao
    abstract fun getPokemonDao(): PokemonDao

}
