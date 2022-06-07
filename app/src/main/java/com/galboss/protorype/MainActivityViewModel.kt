package com.galboss.protorype

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galboss.protorype.model.entities.User

class MainActivityViewModel:ViewModel() {

    private var _userAct = MutableLiveData<User>().apply { value=null }
    var userActivity:LiveData<User> = _userAct
    fun setUserActi(newUser:User){
        _userAct.value=newUser
        userActivity=_userAct
    }
}