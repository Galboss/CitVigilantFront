package com.galboss.protorype.api

import com.galboss.protorype.model.entities.Article
import com.galboss.protorype.model.entities.Commentary
import com.galboss.protorype.model.entities.CommentaryWithUser
import com.galboss.protorype.model.entities.MessageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface MyApi {
    //Article
    @GET("article/findById/{id}")
    suspend fun getArticleById(
        @Path("id") id:String
    ): Response<Article>

    @PATCH("article/")
    suspend fun updateArticle(
        @Body article: Article
    ):Response<MessageResponse>

    @GET("commentary/{article}")
    suspend fun getCommentaryWithUsers(
        @Path("article") article:String
    ):Response<ArrayList<CommentaryWithUser>>

    @POST("commentary/")
    suspend fun postCommentary(
        @Body commentary:Commentary
    ):Response<MessageResponse>

    //user

    //images
    @Multipart
    @POST("images/user")
    suspend fun uploadUserImage(
        @Part("user") user: RequestBody,
        @Part file: MultipartBody.Part
    ):Response<MessageResponse>

    @Multipart
    @POST("images/article")
    suspend fun uploadArticleImage(
        @Part("article") article: RequestBody,
        @Part file: MultipartBody.Part
    ):Response<MessageResponse>

    @DELETE("images/user/deleteByUser/{id}")
    suspend fun deleteUserImage (
        @Path("id") id:String
    ):Response<MessageResponse>
}