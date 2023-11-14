package com.watch_dex.di

import android.content.Context
import androidx.room.Room
import com.watch_dex.core.domain.BalanceManager
import com.watch_dex.core.data.BalanceManagerImpl
import com.watch_dex.core.data.Type
import com.watch_dex.core.data.datasource.database.TypeDatabase
import com.watch_dex.feature_home.data.datasource.dao.PokemonDao
import com.watch_dex.feature_type_selection.data.repository.TypeRepositoryImpl
import com.watch_dex.feature_type_selection.domain.repository.TypeRepository
import kotlin.random.Random

private const val DB_NAME = "database"

interface AppModule {
    val database: TypeDatabase
    val pokemonDao: PokemonDao
    val balanceManager: BalanceManager
    val typeRepository: TypeRepository
    val appTheme: Type
}

class AppModuleImpl(private val applicationContext: Context) : AppModule {

    override val database: TypeDatabase by lazy {
        Room.databaseBuilder(
            context = applicationContext,
            klass = TypeDatabase::class.java,
            name = DB_NAME
        ).createFromAsset("database/initialData.db")
            .build()
    }

    override val pokemonDao: PokemonDao by lazy { database.getPokemonDao() }

    override val typeRepository: TypeRepository by lazy { TypeRepositoryImpl() }

    override val balanceManager: BalanceManager by lazy {
        BalanceManagerImpl(typeRepository.getAllTypes())
    }

    override val appTheme: Type by lazy {
        val items = typeRepository.getAllTypes()
        val maxSize = items.size + 1
        val randomItem = Random.nextInt(until = maxSize)
        items[randomItem]
    }
}
