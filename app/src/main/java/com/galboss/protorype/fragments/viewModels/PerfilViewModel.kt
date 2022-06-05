package com.galboss.protorype.fragments.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galboss.protorype.model.entities.User

class PerfilViewModel: ViewModel() {
    private var _user = MutableLiveData<User>().apply { value = null }
    var user: LiveData<User> = _user
    fun setUser(usuario:User){
        _user.value=usuario
        this.user=_user
        Log.i("Llego","${user.value.toString()}\n${_user.value.toString()}")
    }
}