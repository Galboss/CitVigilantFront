package com.galboss.protorype.user.responses

import com.galboss.protorype.model.entities.UserLoggin
import com.galboss.protorype.utils.RetrofitInstance
import retrofit2.Response

class LoginResponses {

    suspend fun  login (user: UserLoggin): Response<UserLoggin> {
        return RetrofitInstance.api.loging(user)
    }

    suspend fun crearUsuario(user:UserLoggin):Response<UserLoggin>{
        return RetrofitInstance.api.createUser(user)
    }
}