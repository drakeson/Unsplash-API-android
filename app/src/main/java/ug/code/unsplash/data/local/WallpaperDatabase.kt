package ug.code.unsplash.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ug.code.unsplash.data.local.model.photo.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 1)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract fun wallpaperDao(): WallpaperDao
}