package com.lzp.coroutinesstudy

import android.app.Application
import com.lzp.coroutinesstudy.crash.DemoThreadCrashHandler

/**
 * @author li.zhipeng
 * */
class DemoApplication : Application() {

    companion object {
        lateinit var app: Application
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        Thread.setDefaultUncaughtExceptionHandler(DemoThreadCrashHandler())
    }
}