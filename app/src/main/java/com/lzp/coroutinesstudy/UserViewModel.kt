package com.lzp.coroutinesstudy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lzp.coroutinesstudy.bean.User
import com.lzp.coroutinesstudy.database.UserDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * @author li.zhipeng
 * */
class UserViewModel : ViewModel() {

    val userLiveData: LiveData<List<User>> =
        UserDatabase.getDatabase(DemoApplication.app).userDao().selectAll().asLiveData()

    fun startFlow() {
        viewModelScope.launch {
            flow {
                for (i in 1..100) {
                    delay(100)
                    emit(i) // 发射下一个值
                }
            }
                .collect {
                    Log.e("lzp", "$it")
                }
        }
    }

}