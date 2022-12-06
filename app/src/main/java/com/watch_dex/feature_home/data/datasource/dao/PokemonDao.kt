package com.watch_dex.feature_home.data.datasource.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.watch_dex.core.domain.entity.Pokemon
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert
    suspend fun insert(pokemon: Pokemon)

    @Update
    suspend fun update(pokemon: Pokemon)

    @Delete
    suspend fun delete(pokemon: Pokemon)

    @Query("SELECT * FROM Pokemon")
    fun getAllPokemon(): Flow<List<Pokemon>>

    @Query("SELECT * FROM Pokemon WHERE name LIKE :initial||'%'")
    fun getPokemonByInitial(initial: Char): Flow<List<Pokemon>>

}
