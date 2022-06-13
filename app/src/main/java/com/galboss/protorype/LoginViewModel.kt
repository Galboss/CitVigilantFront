package com.galboss.protorype

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galboss.protorype.model.entities.User
import com.galboss.protorype.model.entities.UserLoggin

class LoginViewModel: ViewModel() {
    private val _user = MutableLiveData<UserLoggin>().apply { value=null }
    var user = _user

    fun setUser(us:UserLoggin){
        _user.value=us
        user=_user
    }
}