package com.galboss.protorype.model.entities

import java.util.*

data class Commentary(
    var _id:String,
    var user:String,
    var article: String,
    var date:Date
)
