package com.apesmedical.commonsdk.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [Empty::class], version = 6, exportSchema = false)
abstract class MainDB : RoomDatabase() {

    abstract fun emptyDao(): EmptyDao

    companion object {
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) = Unit
        }

        private fun getInstance(context: Context) = Room.databaseBuilder(
            context,
            MainDB::class.java,
            "main_database"
        ).allowMainThreadQueries()
            .addMigrations(MIGRATION_5_6).build()

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MainDB? = null


        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): MainDB {
            var tempInstance = INSTANCE
            if (tempInstance == null) {
                synchronized(this) {
                    if (tempInstance == null) {
                        tempInstance = Room.databaseBuilder(
                            context.applicationContext,
                            MainDB::class.java,
                            "main_database"
                        ).fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build()
                        return@getDatabase tempInstance!!
                    }
                }
            }
            return getInstance(context)
        }
    }
}