package com.galboss.protorype

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.galboss.protorype.user.fragments.*
import com.galboss.protorype.model.Constant
import com.galboss.protorype.model.entities.User
import com.galboss.protorype.task.CoroutinesAsyncTask
import com.galboss.protorype.task.httpRequestGet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    var task:MainActivityAsyncTask?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val menu:BottomNavigationView = this.findViewById(R.id.navigationBar)
        val fragmentContainer:FrameLayout=this.findViewById(R.id.fragmentContainer)
        var extras= this.intent
        menu.setOnItemSelectedListener { item: MenuItem? ->
            when (item?.itemId) {
                R.id.menu_perfil->replaceFragment(Perfil())
                R.id.menu_buscar->{
                    var inicio = Inicio()
                    var extras2 = Bundle()
                    extras2.putString("userId",extras.getStringExtra("userId"))
                    inicio.arguments=extras2
                    replaceFragment(inicio)
                }
                R.id.menu_inicio->replaceFragment(Inicio())
                R.id.menu_agregar->replaceFragment(Crear())
                R.id.menu_acerca->replaceFragment(Acerca())
            }
            true
        }
        viewModelAc = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModelAc.userActivity.observe(this, Observer {

        })
        ejecutarTarea(viewModelAc,1,1,extras.getStringExtra("userId"))

    }

    private fun replaceFragment(fragment:Fragment){
        val fragManager=supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.fragmentContainer,fragment)
        fragTransaction.commit()
    }

    fun ejecutarTarea(viewModel: MainActivityViewModel, method: Int, service:Int,user: String?){
        if(task?.status== Constant.Status.RUNNING){
            task?.cancel(true)
        }
        task = MainActivityAsyncTask(method,service, viewModelAc,user)
        task?.execute()
    }

    inner class MainActivityAsyncTask(
        private var meth:Int,
        private var serv:Int,
        private var viewModel: MainActivityViewModel,
        private var user:String?
    ): CoroutinesAsyncTask<Int,Int,String>("MainActivityAsyncTask"){
        var gson= Gson()
        override fun doInBackground(vararg params: Int?): String {
            when(meth){
                1->when(serv){
                    1-> return httpRequestGet("https://citvigilant.herokuapp.com/api/user/findById/${user}")
                    else -> throw IllegalArgumentException("El servicio solicitado no existe")
                }
                else -> throw IllegalArgumentException("El servicio solicitado no existe")
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            when(meth){
                1->when(serv){
                        1->{
                            var sType=object : TypeToken<User>(){}.type
                            var data = gson.fromJson<User>(result,sType)
                            viewModel.setUserActi(data)
                        }
                }
            }
        }
    }

    companion object{
        lateinit var viewModelAc:MainActivityViewModel
    }
}