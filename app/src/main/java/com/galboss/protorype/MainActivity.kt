package com.galboss.protorype

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.galboss.protorype.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val menu:BottomNavigationView = this.findViewById(R.id.navigationBar)
        val fragmentContainer:FrameLayout=this.findViewById(R.id.fragmentContainer)
        replaceFragment(Inicio())
        menu.setOnItemSelectedListener { item: MenuItem? ->
            when (item?.itemId) {
                R.id.menu_perfil->replaceFragment(Perfil())
                R.id.menu_buscar->replaceFragment(Buscar())
                R.id.menu_inicio->replaceFragment(Inicio())
                R.id.menu_agregar->replaceFragment(Crear())
                R.id.menu_acerca->replaceFragment(Acerca())
            }
            true
        }
    }

    private fun replaceFragment(fragment:Fragment){
        val fragManager=supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.fragmentContainer,fragment)
        fragTransaction.commit()
    }

}