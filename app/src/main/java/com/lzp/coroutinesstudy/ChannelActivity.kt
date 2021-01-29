package com.lzp.coroutinesstudy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_channel.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.channels.ticker

// 计数器 Actor 的各种类型
sealed class CounterMsg
object IncCounter : CounterMsg()// 递增计数器的单向消息
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // 携带回复的请求

// 这个函数启动一个新的计数器 actor
@ObsoleteCoroutinesApi
fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0 // actor 状态
    for (msg in channel) { // 即将到来消息的迭代器
        when (msg) {
            is IncCounter -> counter++
            is GetCounter -> msg.response.complete(counter)
        }
    }
}

class ChannelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)
        launch.setOnClickListener {
            launchChannel()
        }

        ticker.setOnClickListener {
            startTicker()

        }

        buffer_channel.setOnClickListener {
            bufferChannel()

        }

        no_actor.setOnClickListener {
            noActor()

        }

        actor.setOnClickListener {
            actor()

        }

    }

    private fun noActor() {
        // 非线程安全
        var count = 0
        GlobalScope.launch {

            val job = launch {
                repeat(1000) {
                    launch {
                        count++
                    }

                }
            }
            job.join()
            Log.e("lzp", "no actor count: $count")
        }
    }


    private fun actor() {
        // 使用actor实现线程安全
        GlobalScope.launch {

            val countActor = counterActor()

            val job = launch {
                repeat(1000) {
                    launch {
                        countActor.send(IncCounter)
                    }
                }
            }
            // 等待任务结束
            job.join()
            // 获取结果
            val response = CompletableDeferred<Int>()
            countActor.send(GetCounter(response))
            Log.e("lzp", "actor count: ${response.await()}")
            countActor.close()
        }
    }

    private fun bufferChannel() {
        val channel = Channel<Int>(0)
        GlobalScope.launch {
            launch {
                repeat(20) {
                    Log.e("lzp", "send: $it")
                    channel.send(it)
                }
                channel.close()
            }


            launch {
                repeat(10) {
                    val result = channel.receive()
                    Log.e("lzp", "receive: $result")
                    delay(1000)
                }
            }
        }
    }

    private fun startTicker() {
        GlobalScope.launch {
            val tickerChannel = ticker(1000, 1000)
            var count = 0
            while (true) {
                Log.e("lzp", "count: $count")
                tickerChannel.receiveOrNull()
                count++
//                Log.e("lzp", "result: $result")
//                Log.e("lzp", "count: $count")
                if (count > 10) {
                    tickerChannel.cancel()
//                    break
                }

                if (count > 20) {
                    break
                }
            }
            Log.e("lzp", "Done")
//            tickerChannel.cancel()
        }

    }

    private fun launchChannel() {
        val channel = Channel<Int>()
        GlobalScope.launch {
            // 这里可能是消耗大量 CPU 运算的异步逻辑，我们将仅仅做 5 次整数的平方并发送
            launch {
                for (x in 1..5) {
                    delay(100)
                    Log.e("lzp", "send: $x")
                    channel.send(x)
                }
            }

//            launch {
//                for (x in 6..10) {
//                    Log.e("lzp", "send: $x")
//                    channel.send(x)
//                }
//            }
//
//            launch {
//                for (x in 11..15) {
//                    Log.e("lzp", "send: $x")
//                    channel.send(x)
//                }
//            }

//            delay(100)
//            repeat(1) {
//                Log.e(
//                    "lzp",
//                    "task2 receive :${channel.receive().toString()}"
//                )
//            }

            // 这里我们打印了 5 次被接收的整数：
            launch {
                repeat(5) {
                    Log.e(
                        "lzp",
                        "task1 receive :${channel.receive().toString()}"
                    )
                }

            }
            launch {
                repeat(5) {
                    Log.e(
                        "lzp",
                        "task2 receive :${channel.receive().toString()}"
                    )
                }

            }
//            repeat(5) { Log.e("lzp", channel.receive().toString()) }
//            Log.e("lzp", "Done!")
        }
        channel.close()
//        channel.cancel()
    }
}