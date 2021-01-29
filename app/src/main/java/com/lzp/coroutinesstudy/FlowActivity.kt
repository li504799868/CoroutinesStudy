package com.lzp.coroutinesstudy

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lzp.coroutinesstudy.bean.User
import com.lzp.coroutinesstudy.database.UserDatabase
import kotlinx.android.synthetic.main.activity_flow.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FlowActivity : AppCompatActivity() {

    private val mViewModel: UserViewModel by viewModels()

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)

        list.layoutManager = LinearLayoutManager(this)
        val adapter = UserAdapter()
        list.adapter = adapter

        GlobalScope.launch {
            UserDatabase.getDatabase(this@FlowActivity).userDao().selectAll().collect(object :
                FlowCollector<List<User>> {
                override suspend fun emit(value: List<User>) {
                    adapter.submitList(value)
                }
            })
        }

        mViewModel.userLiveData.observe(this, Observer {
            adapter.submitList(it)
        })

        save.setOnClickListener {
            GlobalScope.launch {
                UserDatabase.getDatabase(this@FlowActivity).userDao().insertUser(
                    User(
                        (System.currentTimeMillis() / 1000).toInt(),
                        firstName.text.toString(),
                        secondName.text.toString()
                    )
                )
            }
        }

//        mViewModel.startFlow()
        startFlow()
    }

    private fun startFlow() {
        GlobalScope.launch {
            // 该作业无法被取消，会输出到100
            val job = launch {
                (1..100).asFlow()
//                    .cancellable() // 调用此方法，会在51取消成功
                    .collect {
                        Log.e("lzp", "$it")
                        if (it > 50) cancel()
                    }
            }
//            job.cancel()
        }
    }

}