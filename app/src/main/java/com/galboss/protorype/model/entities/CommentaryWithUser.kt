package com.galboss.protorype.model.entities

import java.util.*

data class CommentaryWithUser(
    var _id:String?,
    var user:User?,
    var article: String?,
    var content: String?,
    var date: Date?
){
    fun createCommentary():Commentary{
        var com = Commentary(_id, user?._id,article,content,date)
        return com
    }
}
