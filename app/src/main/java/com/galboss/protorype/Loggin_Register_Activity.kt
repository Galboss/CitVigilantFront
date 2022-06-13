package com.galboss.protorype

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout

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