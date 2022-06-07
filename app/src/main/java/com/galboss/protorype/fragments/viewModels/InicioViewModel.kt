package com.galboss.protorype.fragments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galboss.protorype.model.entities.Article
import okhttp3.internal.notifyAll

class InicioViewModel:ViewModel() {

    private var _list = MutableLiveData<List<Article>>().apply { value= listOf() }
    var list : LiveData<List<Article>> = _list

    fun setList(list:MutableList<Article>){
        _list.value=list
        this.list=_list
    }
}