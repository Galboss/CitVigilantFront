package com.galboss.protorype.model.entities

import java.util.*

data class ArticleGalleryItem(
    var uploadDate:Date,
    var filename:String,
    var contentType:String,
    var route:String
)
