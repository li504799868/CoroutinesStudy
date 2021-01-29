package com.lzp.coroutinesstudy

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launchWhenCreated {  }
        lifecycleScope.launchWhenStarted {  }
        lifecycleScope.launchWhenResumed {  }
        launch.setOnClickListener {
            launch()
        }

        cancel.setOnClickListener {
            cancel()

        }

        no_cancel.setOnClickListener { noCancel() }

        join.setOnClickListener {
            join()

        }

        join2.setOnClickListener {
            join2()

        }

        timeout.setOnClickListener {
            timeout()
        }

        timeoutwithnull.setOnClickListener {
            withTimeoutOrNull()
        }

        async.setOnClickListener {
            async()
        }

        AtomicInteger()
    }

    private fun crash() {
        TODO("Not yet implemented")
    }

    private fun async() {
        GlobalScope.launch(start = CoroutineStart.LAZY) {

            val time = measureTimeMillis {
                val one = async(start = CoroutineStart.DEFAULT) {
                    delay(1000)
                    1
                }

                val two = async {
                    delay(2000)
                    2
                }
                Log.i("MainActivity", "result = ${one.await() + two.await()}")
            }
            Log.i("MainActivity", "measureTimeMillis = $time")
        }.start()
    }

    private fun noCancel() {
        GlobalScope.launch {

            val job = launch {
                Log.i("MainActivity", "job start")

                launch {
                    Log.i("MainActivity", "child job start")
                    delay(1000)
                    Log.i("MainActivity", "child job end")
                }

                withContext(NonCancellable) {
                    Log.i("MainActivity", "NonCancellable task start")
                    delay(1000)
                    Log.i("MainActivity", "NonCancellable task end")
                }

                // 注意做状态检查
//                delay(1500)
                if (isActive) {
                    Log.i("MainActivity", "job end")
                }

            }
            // 延时两秒
            delay(500)
            // 结束任务
            Log.i("MainActivity", "cancel job")
            job.cancel()
        }
    }

    private fun cancel() {
        GlobalScope.launch {
            val job = launch(Dispatchers.Default) {
                var i = 0
                var nextPrintTime = System.currentTimeMillis()
                while (i < 100 && isActive) {
                    // 一个执行计算的循环，只是为了占用 CPU
//                    if (System.currentTimeMillis() >= nextPrintTime) {
                    Log.i("MainActivity", "job: I'm work for ${i++} ...")
                    delay(500)
//                        nextPrintTime += 500L
//                }
                }
            }
            // 延时两秒
            delay(10)
            // 结束任务
            Log.i("MainActivity", "cancel job")
            job.cancel()
        }
    }

    private fun launch() {
        println("start")
        val job = CoroutineScope(Dispatchers.IO + CoroutineName("test")).launch {
            repeat(100) {
                println("hello: $it")
                delay(100)
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "task complete", Toast.LENGTH_SHORT).show()
            }
        }
//            job.cancel()
    }

    private fun join() {
        CoroutineScope(Dispatchers.IO + CoroutineName("test")).launch {
            // 启动任务1
            Log.i("MainActivity", "start task1")

            // 启动任务2
            val job = launch {
                repeat(10) {
                    Log.i("MainActivity", "task2 run: $it")
                    delay(100)
                }
            }
            // 阻塞之后的任务
            job.join()

            withContext(Dispatchers.Main) {
                Log.i("MainActivity", "start task3")
                Toast.makeText(this@MainActivity, "task complete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun join2() {
        CoroutineScope(Dispatchers.IO + CoroutineName("test")).launch {
            // 启动任务1
            Log.i("MainActivity", "start task1")

            // 启动任务2
            launch {
                val job = launch {
                    repeat(10) {
                        Log.i("MainActivity", "task2 run: $it")
                        delay(100)
                    }

                }

                // 阻塞之后的任务
                job.join()
            }
            withContext(Dispatchers.Main) {
                Log.i("MainActivity", "start task3")
                Toast.makeText(this@MainActivity, "task complete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun timeout() {
        GlobalScope.launch {

            // 该任务必须在1秒内结束，否则会自动取消
            val result = withTimeout(1_000) {
                Log.i("MainActivity", "timeout task start")
                delay(1100)
                Log.i("MainActivity", "timeout task end")
                "Done"
            }
            Log.i("MainActivity", "timeout task result:$result")

        }
    }

    private fun withTimeoutOrNull() {
        GlobalScope.launch {

            // 该任务必须在1秒内结束，否则会自动取消
            val result = withTimeoutOrNull(1_000) {
                Log.i("MainActivity", "timeout task start")
                delay(1100)
                Log.i("MainActivity", "timeout task end")
                "Done"
            }
            Log.i("MainActivity", "timeout task result:$result")

        }
    }


}