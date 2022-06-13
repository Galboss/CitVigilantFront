package com.galboss.protorype.model.entities

data class UserLoggin(
    var _id:String?,
    var userName:String?,
    var email:String?,
    var password:String?,
    var suspended:Boolean?,
    var provincia:Int?,
    var canton:Int?,
    var distrito:Int?
)
