package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Mt5Account::class], version = 1, exportSchema = false)
abstract class AizenDatabase : RoomDatabase() {
    abstract fun mt5AccountDao(): Mt5AccountDao

    companion object {
        @Volatile
        private var INSTANCE: AizenDatabase? = null

        fun getDatabase(context: Context): AizenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AizenDatabase::class.java,
                    "aizen_ea_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
