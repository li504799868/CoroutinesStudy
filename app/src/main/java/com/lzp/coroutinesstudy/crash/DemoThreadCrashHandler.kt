package com.lzp.coroutinesstudy.crash

/**
 * @author li.zhipeng
 * */
class DemoThreadCrashHandler: Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        e.printStackTrace()
    }

}