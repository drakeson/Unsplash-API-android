package ug.code.unsplash.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ug.code.unsplash.data.repository.WallpaperRepositoryImpl
import ug.code.unsplash.domain.repository.WallpaperRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindWallpaperRepository(
        repository: WallpaperRepositoryImpl
    ): WallpaperRepository
}