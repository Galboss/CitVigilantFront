package com.galboss.protorype

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var image = findViewById<ImageView>(R.id.splash_icon)
        //scalateImage(image,this)
        supportActionBar?.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent (this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },1500)
    }

    fun scalateImage(image:ImageView,context: Context){
        var bit:Bitmap? = null;
        try {
            val drawing: Drawable = image.drawable
            bit = (drawing as BitmapDrawable).bitmap
            var width=bit.width
            var heigth=bit.width
            var xBound = image.width
            var yBound = image.height
            var xScale = 250
            var yScale = 250
            var matrix = Matrix()
            matrix.postScale(xScale.toFloat(),yScale.toFloat())
            var scalatedBit = Bitmap.createBitmap(bit,0,0,width,heigth,matrix,true)
            width = scalatedBit.width
            heigth = scalatedBit.height
            var result = BitmapDrawable(context.resources,scalatedBit)
            image.setImageDrawable(result)
        }catch (e:Exception){

        }
    }

}