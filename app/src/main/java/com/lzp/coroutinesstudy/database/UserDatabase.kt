package com.lzp.coroutinesstudy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lzp.coroutinesstudy.bean.User
import com.lzp.coroutinesstudy.dao.UserDao

/**
 * @author li.zhipeng
 * */
@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "flow_database"
                ).build()
                INSTANCE!!
            }
        }
    }
}