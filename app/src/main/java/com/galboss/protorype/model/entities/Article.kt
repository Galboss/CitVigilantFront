package com.galboss.protorype.model.entities

import java.util.*

data class Article(
    var _id:String?,
    var user:String,
    var title:String,
    var content:String,
    var provincia:Int,
    var canton:Int,
    var distrito:Int,
    var estado:Int,
    var date:Date?
){
    constructor():this("","","","",0,
        0,0,0,null)


}
