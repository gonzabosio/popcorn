package com.popcorn.api

import com.popcorn.api.Constants.MY_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MovieInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request =  chain.request()
        val request: Request = original.newBuilder()
            .header("Authorization", MY_TOKEN)
            .build()
        return chain.proceed(request)
    }
}