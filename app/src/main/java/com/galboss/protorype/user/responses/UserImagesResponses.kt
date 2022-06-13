package com.galboss.protorype.user.responses

import com.galboss.protorype.api.MyApi
import com.galboss.protorype.model.entities.MessageResponse
import com.galboss.protorype.utils.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File

class UserImagesResponses {

    suspend fun postUserImage(file: File, user:String): Response<MessageResponse> {
        var reqBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        var multi = MultipartBody.Part.createFormData("file",file.name,reqBody)
        var userData = user.toRequestBody("text/plain".toMediaTypeOrNull())
        /*var retro = Retrofit.Builder().baseUrl(/*"https://citvigilant.herokuapp.com/api/"*/"https://webhook.site/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var re = retro.create(MyApi::class.java)*/
        return RetrofitInstance.api.uploadUserImage(userData,multi)  //re.uploadUserImage(userData,multi)
    }

    suspend fun deleteUserImage(user:String):Response<MessageResponse>{
        return RetrofitInstance.api.deleteUserImage(user)
    }
}