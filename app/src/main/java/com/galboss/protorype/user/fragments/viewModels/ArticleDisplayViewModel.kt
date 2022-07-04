package com.galboss.protorype.user.fragments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galboss.protorype.model.entities.Article
import com.galboss.protorype.model.entities.ArticleGalleryItem
import com.galboss.protorype.model.entities.CommentaryWithUser

class ArticleDisplayViewModel:ViewModel() {
    private val _article = MutableLiveData<Article>().apply { value = null }
    var article:LiveData<Article> = _article
    private val _commentaryList= MutableLiveData<ArrayList<CommentaryWithUser>>().apply { value= arrayListOf() }
    var commentaryList = _commentaryList
    private val _gallery = MutableLiveData<ArrayList<ArticleGalleryItem>>().apply { value = arrayListOf() }
    var gallery: LiveData<ArrayList<ArticleGalleryItem>> = _gallery

    fun setArticle(arti:Article){
        _article.value=arti
        article=_article
    }

    fun setListCommentary(list : ArrayList<CommentaryWithUser>){
        _commentaryList.value=list
        commentaryList=_commentaryList
    }

    fun setGallery (gallery:ArrayList<ArticleGalleryItem>){
        _gallery.value = gallery
        this.gallery= _gallery
    }
}