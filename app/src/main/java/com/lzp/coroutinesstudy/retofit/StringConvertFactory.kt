package com.lzp.coroutinesstudy.retofit

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * @author li.zhipeng
 * */
class StringConvertFactory: Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return StringConvert()
    }

    private class StringConvert: Converter<ResponseBody, String>{
        override fun convert(value: ResponseBody): String? {
            return value.string()
        }

    }
}