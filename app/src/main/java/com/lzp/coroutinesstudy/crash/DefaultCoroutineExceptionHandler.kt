package com.lzp.coroutinesstudy.crash

import android.util.Log
import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * @author li.zhipeng
 *
 *      默认的协程异常处理
 * */
@Keep
class DefaultCoroutineExceptionHandler :
    AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {

    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Log.e("lzp", "DefaultCoroutineExceptionHandler")
    }
}