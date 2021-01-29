package com.lzp.coroutinesstudy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lzp.coroutinesstudy.crash.DefaultCoroutineExceptionHandler
import kotlinx.android.synthetic.main.activity_crash.*
import kotlinx.coroutines.*
import java.util.*

class CrashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash)

        // 全局捕获异常
        crash1.setOnClickListener {
            GlobalScope.launch {
                val job = launch {
                    // 出现一个异常
                    val x = 0
                    val y = 0
                    val result = x / y
                }
                job.join()

                Log.e("lzp", "task1 done")
            }
        }

        // 指定CrashHandler接收异常
        crash2.setOnClickListener {
//            GlobalScope.launch(CoroutineExceptionHandler { _, throwable ->
//                Log.e("lzp", "CoroutineExceptionHandler catch : $throwable")
//            }) {
//                // 出现一个异常
//                val x = 0
//                val y = 0
//                val result = x / y
//            }


            Log.e("lzp", "${ ServiceLoader.load(
                DefaultCoroutineExceptionHandler::class.java
            ).toList()}")
        }

        // try_catch捕获异常
        crash3.setOnClickListener {
//            try {
//                GlobalScope.launch {
//                    // 出现一个异常
//                    val x = 0
//                    val y = 0
//                    val result = x / y
//                    Log.e("lzp", "task3 done")
//                }
//            } catch (e: ArithmeticException) {
//                e.printStackTrace()
//            }
//            Log.e("lzp", "OnClickListener done")

            GlobalScope.launch {
                try {
                    // 出现一个异常
                    val x = 0
                    val y = 0
                    val result = x / y
                } catch (e: ArithmeticException) {
                    e.printStackTrace()
                } finally {
                    Log.e("lzp", "calculate task done")
                }

                Log.e("lzp", "other task done")

            }
        }

        // 子作业互不影响
        crash4.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, throwable ->
                Log.e("lzp", "Task A catch : $throwable")
            }) {

                println("start")
//                supervisorScope {
                    launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
                        Log.e("lzp", "Task A catch : $throwable")
                    }) {
                        delay(2000)
                        throw IllegalArgumentException()
                    }

//                }
//
//                launch(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
//                    Log.e("lzp", "Task A catch : $throwable")
//                }) {
//                    delay(2000)
//                    throw IllegalArgumentException()
//                }

                launch(SupervisorJob()) {
                    delay(3000)
                    Log.e("lzp", "Task B done")
                }
            }
        }
    }
}