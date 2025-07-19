package com.themarto.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DBCity::class], version = 1)
abstract class CityDatabase : RoomDatabase() {

    abstract val cityDao: CityDao

    companion object {
        private const val DATABASE_NAME = "cities_db"
        private var INSTANCE: CityDatabase? = null

        fun getInstance(context: Context): CityDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        CityDatabase::class.java, DATABASE_NAME)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}