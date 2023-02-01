package ug.code.unsplash.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ug.code.unsplash.data.local.WallpaperDao
import ug.code.unsplash.data.local.WallpaperDatabase
import ug.code.unsplash.util.Constants
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideChannelDao(wallpaperDatabase: WallpaperDatabase): WallpaperDao {
        return wallpaperDatabase.wallpaperDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): WallpaperDatabase {
        return Room.databaseBuilder(
            appContext, WallpaperDatabase::class.java, Constants.DATABASE_NAME
        ).build()
    }
}