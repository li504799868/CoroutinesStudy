package com.lzp.coroutinesstudy.service

import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @author li.zhipeng
 * */
interface TestService {

    @GET
    suspend fun getContent(@Url url: String): String
}