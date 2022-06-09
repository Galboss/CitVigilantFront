package com.galboss.protorype.user.fragments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galboss.protorype.model.entities.Article

class ArticleViewModel: ViewModel(){
    private val _article = MutableLiveData<Article>().apply{ value = null }
    var article:LiveData<Article> = _article
    private val _provincias = MutableLiveData<List<String>>().apply { value = listOf() }
    var provincias = _provincias
    private val _cantones = MutableLiveData<List<String>>().apply { value = listOf() }
    var cantones = _cantones
    private val _distritos = MutableLiveData<List<String>>().apply { value = listOf() }
    var distritos = _distritos

    fun setProvincias(new:List<String>){
        _provincias.value=new
        provincias=_provincias
    }
    fun setCantones(new:List<String>){
        _cantones.value=new
        cantones=_cantones
    }
    fun setDistritos(new:List<String>){
        _distritos.value=new
        distritos=_distritos
    }
    fun setArticle(article: Article){
        _article.value=article
        this.article = _article
    }
}