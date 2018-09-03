package com.qingmei2.rhine.http.service

import com.qingmei2.rhine.di.TIME_OUT_SECONDS
import com.qingmei2.rhine.http.APIConstants
import com.qingmei2.rhine.http.entity.LoginUser
import com.qingmei2.rhine.http.interceptor.BasicAuthInterceptor
import io.reactivex.Flowable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LoginService(val builder: Retrofit.Builder,
                   val okHttpClientBuilder: OkHttpClient.Builder,
                   val httpInterceptor: Interceptor) {

    fun login(username: String,
              password: String): Flowable<LoginUser> {

        val client =
                okHttpClientBuilder
                        .connectTimeout(
                                TIME_OUT_SECONDS.toLong(),
                                TimeUnit.SECONDS)
                        .readTimeout(TIME_OUT_SECONDS.toLong(),
                                TimeUnit.SECONDS)
                        .addInterceptor(httpInterceptor)
                        .addInterceptor(BasicAuthInterceptor(username, password))
                        .build()

        val retrofit =
                builder.baseUrl(APIConstants.BASE_API)
                        .client(client)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

        return retrofit.create(UserService::class.java).login()
    }
}