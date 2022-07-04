package com.galboss.protorype


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class Loggin_Register_Activity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_loggin_register)
        val fragmentContainer: FrameLayout =this.findViewById(R.id.LogginFragContainer)
        var ft = supportFragmentManager.beginTransaction()
        var fragment = LogginFragment()
        ft.replace(R.id.LogginFragContainer,fragment)
        ft.commit()
    }
}