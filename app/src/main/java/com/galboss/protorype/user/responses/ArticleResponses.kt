package com.galboss.protorype.user.responses


import com.galboss.protorype.model.entities.Article
import com.galboss.protorype.model.entities.Commentary
import com.galboss.protorype.model.entities.CommentaryWithUser
import com.galboss.protorype.model.entities.MessageResponse
import com.galboss.protorype.utils.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class ArticleResponses {

    suspend fun getArticleById(id:String): Response<Article> {
        return RetrofitInstance.api.getArticleById(id)
    }
    suspend fun pathArticle(article:Article):Response<MessageResponse>{
        return RetrofitInstance.api.updateArticle(article)
    }
    suspend fun getCommentaryListWithUser(article:String):Response<ArrayList<CommentaryWithUser>>{
        return RetrofitInstance.api.getCommentaryWithUsers(article)
    }
    suspend fun postCommentary(comm:Commentary):Response<MessageResponse>{
        return RetrofitInstance.api.postCommentary(comm)
    }
    suspend fun uploadImage(file: File,id:String):Response<MessageResponse>{
        var reqBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        var multi = MultipartBody.Part.createFormData("file",file.name,reqBody)
        var articleData = id.toRequestBody("text/plain".toMediaTypeOrNull())
        return RetrofitInstance.api.uploadArticleImage(articleData,multi)
    }

    suspend fun getArticleByUser(user:String):Response<ArrayList<Article>>{
        return RetrofitInstance.api.getArticlesByUser(user);
    }

}