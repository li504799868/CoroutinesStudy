package com.lzp.coroutinesstudy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lzp.coroutinesstudy.bean.User
import kotlinx.coroutines.flow.Flow


/**
 * @author li.zhipeng
 * */
@Dao
interface UserDao {

    @Query("select * from user")
    fun selectAll(): Flow<List<User>>

    @Insert
    fun insertUser(user: User)
}