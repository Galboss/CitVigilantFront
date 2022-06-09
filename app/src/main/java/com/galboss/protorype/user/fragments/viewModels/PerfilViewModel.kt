package com.galboss.protorype.user.fragments.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galboss.protorype.MainActivity
import com.galboss.protorype.model.entities.User

class PerfilViewModel: ViewModel() {
    private var _user = MutableLiveData<User>().apply { value = null }
    var user: LiveData<User> = _user
    private val _userImage = MutableLiveData<Uri>().apply { value=null }
    var userImage:LiveData<Uri> = _userImage

    fun setUser(usuario:User){
        _user.value=usuario
        this.user=_user
        MainActivity.viewModelAc.setUserActi(user.value!!)
    }

    fun setUserImage(nuevaImagen:Uri){
        _userImage.value=nuevaImagen
        userImage=_userImage
    }
}