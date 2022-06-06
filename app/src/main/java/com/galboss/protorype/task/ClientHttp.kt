package com.galboss.protorype.task

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.galboss.protorype.utils.UriUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit


fun httpRequestGet(apiUrl:String):String{
    var connection:HttpURLConnection
    var reader: BufferedReader
    var line:String
    var responseCont = StringBuffer()
    var url = URL(apiUrl)
    connection = url.openConnection() as HttpURLConnection
    try{
        connection.requestMethod="GET"
        connection.connectTimeout=5000
        connection.readTimeout=5000
        var status: Int = connection.responseCode
        if(status>299){
            reader = BufferedReader(InputStreamReader(connection.errorStream))
            reader.forEachLine {
                responseCont.append(it)
            }
            reader.close()
        }else{
            reader = BufferedReader(InputStreamReader(connection.inputStream))
            reader.forEachLine {
                responseCont.append(it)
            }
            reader.close()
        }
        Log.println(Log.INFO,"Info","Se obtuvo: ${responseCont.toString()}")
    }catch (ex: Exception){
        Log.println(Log.ERROR,"Error","Exception: ${ex.message}")
        ex.printStackTrace()
    }finally {
        connection.disconnect()
    }
    return responseCont.toString()
}

fun httpRequestPost(apiUrl:String,params:String):String{
    var connection:HttpURLConnection
    var reader: BufferedReader
    var line:String
    var responseCont = StringBuffer()
    var url = URL(apiUrl)
    connection = url.openConnection() as HttpURLConnection
    try{
        connection.requestMethod="POST"
        connection.connectTimeout=5000
        connection.readTimeout=5000
        connection.setRequestProperty("Content-Type","application/json; charset=utf-8")
        connection.doOutput=true
        connection.getOutputStream().use { os ->
            val input: ByteArray = params.toByteArray(Charsets.UTF_8)
            os.write(input, 0, input.size)
        }
        var status: Int = connection.responseCode
        if(status>299){
            reader = BufferedReader(InputStreamReader(connection.errorStream))
            reader.forEachLine {
                responseCont.append(it)
            }
            reader.close()
        }else{
            reader = BufferedReader(InputStreamReader(connection.inputStream))
            reader.forEachLine {
                responseCont.append(it)
            }
            reader.close()
        }
        Log.println(Log.INFO,"Info","Se obtuvo: ${responseCont.toString()}")
    }catch (ex: Exception){
        Log.println(Log.ERROR,"Error","Exception: ${ex.message}")
        ex.printStackTrace()
    }finally {
        connection.disconnect()
    }
    return responseCont.toString()
}

fun httpRequestPatch(apiUrl:String,params:String):String{
    var connection:HttpURLConnection
    var reader: BufferedReader
    var line:String
    var responseCont = StringBuffer()
    var url = URL(apiUrl)
    connection = url.openConnection() as HttpURLConnection
    try{
        connection.requestMethod="PATCH"
        connection.connectTimeout=5000
        connection.readTimeout=5000
        connection.setRequestProperty("Content-Type","application/json; charset=utf-8")
        connection.doOutput=true
        connection.getOutputStream().use { os ->
            val input: ByteArray = params.toByteArray(Charsets.UTF_8)
            os.write(input, 0, input.size)
        }
        var status: Int = connection.responseCode
        if(status>299){
            reader = BufferedReader(InputStreamReader(connection.errorStream))
            reader.forEachLine {
                responseCont.append(it)
            }
            reader.close()
        }else{
            reader = BufferedReader(InputStreamReader(connection.inputStream))
            reader.forEachLine {
                responseCont.append(it)
            }
            reader.close()
        }
        Log.println(Log.INFO,"Info","Se obtuvo: ${responseCont.toString()}")
    }catch (ex: Exception){
        Log.println(Log.ERROR,"Error","Exception: ${ex.message}")
        ex.printStackTrace()
    }finally {
        connection.disconnect()
    }
    return responseCont.toString()
}

fun multipartPostImageUser(url: String?, user: String?, uri:Uri,context:Context): String {
    var client = OkHttpClient();
    var file= File(UriUtils.getPathFromUri(context,uri))
    var formBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("user","${user}")
        .addFormDataPart("file",file.name, file.asRequestBody("image/jpg; charset=utf8".toMediaType()))
        .build()
    var request = Request.Builder().url(url!!).post(formBody).build()
    client.newBuilder().callTimeout(60,TimeUnit.SECONDS)
    var result=client.newCall(request).execute()
    Log.i("resultado","${result.body?.string()}")
    return result.toString()
}
/*
fun multipartPostImageUser(url: String?, user: String?, path: String): String{
    val post = HttpPost(url)
    try {
        val client: HttpClient = HttpClientBuilder.create().build()
        val file = File(path)
        val extension = path.split(".").toTypedArray()
        val builder = MultipartEntityBuilder.create()
        var fileBody: FileBody? = null
        when (extension[1]) {
            "png", "PNG" -> {
                fileBody = FileBody(file, ContentType.IMAGE_PNG)
            }
            "jpeg", "JPEG" -> {
                fileBody = FileBody(file, ContentType.IMAGE_JPEG)
            }
        }
        val userBody = StringBody("user", ContentType.MULTIPART_FORM_DATA)
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
        builder.addPart("file", fileBody)
        builder.addPart("user", userBody)
        val entity = builder.build()
        post.entity = entity
        val response = client.execute(post)
        return response.toString()
    } catch (e: java.lang.Exception) {
    }
    return "Error"
}

fun multipartPostImageArticle(url: String?, article: String?, path: String): String {
    val post = HttpPost(url)
    try {
        val client: HttpClient = HttpClientBuilder.create().build()
        val file = File(path)
        val extension = path.split(".").toTypedArray()
        val builder = MultipartEntityBuilder.create()
        var fileBody: FileBody? = null
        when (extension[1]) {
            "png", "PNG" -> {
                fileBody = FileBody(file, ContentType.IMAGE_PNG)
            }
            "jpeg", "JPEG" -> {
                fileBody = FileBody(file, ContentType.IMAGE_JPEG)
            }
        }
        val userBody = StringBody("user", ContentType.MULTIPART_FORM_DATA)
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
        builder.addPart("file", fileBody)
        builder.addPart("article", userBody)
        val entity = builder.build()
        post.entity = entity
        val response = client.execute(post)
        return response.toString()
    } catch (e: java.lang.Exception) {
    }
    return "Error"
}*/
