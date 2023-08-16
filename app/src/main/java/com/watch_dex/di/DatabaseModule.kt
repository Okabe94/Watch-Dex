package com.watch_dex.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.watch_dex.core.data.datasource.database.TypeDatabase
import com.watch_dex.feature_home.data.datasource.dao.PokemonDao
import com.watch_dex.feature_home.data.datasource.dao.TypeBalanceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DB_NAME = "database"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context = context,
        TypeDatabase::class.java,
        DB_NAME
    ).createFromAsset("database/initialData.db")
        .build()

    @Singleton
    @Provides
    fun providePokemonDao(database: TypeDatabase) = database.getPokemonDao()

}
