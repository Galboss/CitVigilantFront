package com.galboss.protorype.task

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MyApi {

    @Multipart
    @POST("user")
    fun uploadUserImage(
        @Part file: MultipartBody.Part,
        @Part("user") user: RequestBody
    )

    @Multipart
    @POST("article")
    fun uploadArticle(
        @Part file: MultipartBody.Part,
        @Part("article") article: RequestBody
    )
}