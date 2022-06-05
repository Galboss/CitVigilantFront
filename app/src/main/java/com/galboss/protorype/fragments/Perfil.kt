package com.galboss.protorype.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.galboss.protorype.R
import com.galboss.protorype.fragments.viewModels.PerfilViewModel
import com.galboss.protorype.task.CoroutinesAsyncTask
import com.galboss.protorype.task.httpRequestGet
import com.galboss.protorype.task.httpRequestPath
import com.galboss.protorype.databinding.FragmentPerfilBinding
import com.galboss.protorype.model.Constant
import com.galboss.protorype.model.entities.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import java.lang.IllegalArgumentException
import java.util.logging.Level.INFO

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Perfil.newInstance] factory method to
 * create an instance of this fragment.
 */
class Perfil : Fragment() {


    var _binding:FragmentPerfilBinding?=null
    val binding get()=_binding!!
    private lateinit var viewModel: PerfilViewModel
    private var task:PerfilAsyncTask?=null
    private var user:User?=null

    enum class MethodRequest(val meth:Int){
        GET(1),
        POST(2),
        PATCH(3),
        DELETE(4)
    }
    enum class UrlsApis(val url:String){
        GET_USER_DATA("http://192.168.0.143:3000/api/user/findById/"),
        PATH_UPDATE_USER_DATA("http://192.168.0.143:3000/api/user/updateAll")
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)
        ejecutarTarea(viewModel,1,1,"","627b0c391512b7afcd1e43ef")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentPerfilBinding.inflate(inflater,container,false)
        val root:View=binding.root

        //Binding zone
        var userNameEdit = binding.perfilNombre.editText
        var emailEdit = binding.perfilEmail.editText
        var passOldEdit = binding.perfilPassword.editText
        var passNewEdit = binding.perfilNewpassword.editText
        var passConfirmEdit = binding.perfilConfirmpassword.editText
        var imageView = binding.perfilImagen
        var bottnActu=binding.perfilActualizar
        var bottnMisArt = binding.perfilActualizar
        userNameEdit?.setText("Pepe")
        // ViewModel initialization

        // ViewModel zone update
        viewModel.user.observe(this.viewLifecycleOwner, Observer {
            user=viewModel.user.value
            userNameEdit?.setText(viewModel.user.value?.userName)
            emailEdit?.setText(viewModel.user.value?.email)
            passOldEdit?.setText(viewModel.user.value?.password)
            var pica=Picasso.get().load("http://192.168.0.143:3000/api/images/user/file/627b0c391512b7afcd1e43ef").into(imageView)

        })
        Log.i("Tenemos",user.toString())


        //Return zone
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Perfil.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Perfil().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun ejecutarTarea(viewModel:PerfilViewModel,method: Int,service:Int,params:String,userId: String?){
        if(task?.status== Constant.Status.RUNNING){
            task?.cancel(true)
        }
        task = PerfilAsyncTask(params!!,method,service,this.requireContext(),userId,this.viewModel)
        task?.execute()
    }

    inner class PerfilAsyncTask(
        private var parametros:String,
        private var method:Int,
        private var service: Int,
        private var context:Context,
        private var userId:String?,
        private var viewModel:PerfilViewModel
        ): CoroutinesAsyncTask<Int, Int, String>("PerfilTask") {

        var gson = Gson()

        override fun doInBackground(vararg params: Int?):String {
            when(method){
                1-> when(service){
                    1->return httpRequestGet("${UrlsApis.GET_USER_DATA.url}${userId!!}")
                    else->throw IllegalArgumentException("El servicio solicitado no existe")
                }
                3-> when(service){
                    1->return return httpRequestPath("${UrlsApis.PATH_UPDATE_USER_DATA.url}", parametros)
                    else -> throw IllegalArgumentException("El servicio solicitado no existe")
                }
                else -> throw IllegalArgumentException("El servicio solicitado no existe")
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            when(method){
                1->when(service){
                    1->{
                        var sType=object : TypeToken<User>(){}.type
                        var data = gson.fromJson<User>(result,sType)
                        viewModel.setUser(data)
                    }
                }
                /* for list
                   var sType=object : TypeToken<List<Vuelo>>(){}.type
                   var data=gson.fromJson<List<Vuelo>>(result,sType)
                   viewModel.listVuelo.value=data
                   cargarVuelos(context)
                   * */
            }
        }

    }
}