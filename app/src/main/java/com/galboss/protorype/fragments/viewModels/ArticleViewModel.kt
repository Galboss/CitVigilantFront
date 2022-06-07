package com.galboss.protorype.fragments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galboss.protorype.model.entities.Article

class ArticleViewModel: ViewModel(){
    private val _article = MutableLiveData<Article>().apply{ value = null }
    var article:LiveData<Article> = _article

    fun setArticle(article: Article){
        _article.value=article
        this.article = _article
    }
}