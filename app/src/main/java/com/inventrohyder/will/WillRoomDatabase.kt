package com.inventrohyder.will

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Will class
@Database(entities = [Will::class], version = 1, exportSchema = false)
abstract class WillRoomDatabase : RoomDatabase() {

    abstract fun willDao(): WillDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: WillRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): WillRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WillRoomDatabase::class.java,
                    "will_database"
                )
                    .addCallback(WillDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class WillDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.willDao())
                    }
                }
            }

            suspend fun populateDatabase(willDao: WillDao) {
                // Delete all content here.
                willDao.deleteAll()

                // Add sample wills.
                var will = Will("Hello")
                willDao.insert(will)
                will = Will("World!")
                willDao.insert(will)
            }
        }
    }
}