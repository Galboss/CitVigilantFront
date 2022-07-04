package com.galboss.protorype.utils

import com.galboss.protorype.api.MyApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {

    private val retrofit by lazy{
        Retrofit.Builder().baseUrl("https://citvigilant.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api =  retrofit.create(MyApi::class.java)
}

/*"https://citvigilant.herokuapp.com/api/"*/
/*"http://192.168.0.143:3000/api/"*/