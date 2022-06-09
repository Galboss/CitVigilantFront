package com.galboss.protorype.user.fragments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galboss.protorype.model.entities.Article

class InicioViewModel:ViewModel() {

    private var _list = MutableLiveData<List<Article>>().apply { value= listOf() }
    var list : LiveData<List<Article>> = _list
    private var _provincias = MutableLiveData<List<String>>().apply { value= listOf() }
    var provincias: LiveData<List<String>> = _provincias
    private var _cantones = MutableLiveData<List<String>>().apply { value= listOf() }
    var cantones: LiveData<List<String>> = _cantones
    private var _distritos = MutableLiveData<List<String>>().apply { value= listOf() }
    var distritos: LiveData<List<String>> = _distritos

    fun setList(list:MutableList<Article>){
        _list.value=list
        this.list=_list
    }

    fun setProvincias(new:List<String>){
        _provincias.value=new
        provincias = _provincias
    }

    fun setCantones(new:List<String>){
        _cantones.value=new
        cantones = _cantones
    }

    fun setDistritos(new:List<String>){
        _distritos.value=new
        distritos = _distritos
    }
}