package com.galboss.protorype.user.fragments.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galboss.protorype.model.entities.Article
import com.galboss.protorype.user.responses.ArticleResponses
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewModel: ViewModel(){


    private val _article = MutableLiveData<Article>().apply{ value = null }
    var article:LiveData<Article> = _article
    private val _provincias = MutableLiveData<List<String>>().apply { value = listOf() }
    var provincias = _provincias
    private val _cantones = MutableLiveData<List<String>>().apply { value = listOf() }
    var cantones = _cantones
    private val _distritos = MutableLiveData<List<String>>().apply { value = listOf() }
    var distritos = _distritos
    private val _images = MutableLiveData<ArrayList<Uri>>().apply { value= arrayListOf() }
    var images = _images

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

    fun setImages(im:ArrayList<Uri>){
        _images.value=im
        images=_images
    }
}