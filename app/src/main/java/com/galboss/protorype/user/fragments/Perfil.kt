package com.galboss.protorype.user.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.galboss.protorype.Loggin_Register_Activity
import com.galboss.protorype.MainActivity
import com.galboss.protorype.R
import com.galboss.protorype.databinding.FragmentPerfilBinding
import com.galboss.protorype.user.fragments.viewModels.PerfilViewModel
import com.galboss.protorype.model.Constant
import com.galboss.protorype.model.entities.User
import com.galboss.protorype.task.*
import com.galboss.protorype.user.responses.UserImagesResponses
import com.galboss.protorype.utils.UriUtils.getPathFromUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.io.File


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
    var gson = Gson()
    //private lateinit var fileChooserResult:ActivityResultLauncher<String>
    private lateinit var fileChooserResult:ActivityResultLauncher<Intent>
    enum class MethodRequest(val meth:Int){
        GET(1),
        POST(2),
        PATCH(3),
        DELETE(4)
    }
    enum class UrlsApis(val url:String){
        GET_USER_DATA("https://citvigilant.herokuapp.com/api/user/findById/"),
        PATCH_UPDATE_USER_DATA("https://citvigilant.herokuapp.com/api/user/updateAll"),
        USER_IMAGE_UPDATE("https://citvigilant.herokuapp.com/api/images/user")
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
        /*fileChooserResult = registerForActivityResult(ActivityResultContracts.GetContent())
            ActivityResultCallback {
                viewModel.setUserImage(it)
                var path =UriUtils.getImageFilePath(this.requireContext(),it)
                Log.i("Path" ,"${it.path.toString()}")
                Log.i("Path", "${path}")
            }*/
            fileChooserResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result:ActivityResult->
            if(result.resultCode == Activity.RESULT_OK){
                var dat= result.data?.data
                viewModel.setUserImage(dat!!)
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)
        ejecutarTarea(viewModel,1,1,"",MainActivity.viewModelAc.userActivity.value!!._id,null)
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
        var bottnMisArt = binding.perfilVermisarticulos
        var bottnCambFoto = binding.perfilCambiarFoto
        var bottnSubirFoto= binding.perfilEnviarFoto
        var bottnCerrar=binding.perfilCerrarSesion
        userNameEdit?.setText("Pepe")
        // ViewModel initialization

        // ViewModel zone update
        viewModel.user.observe(this.viewLifecycleOwner, Observer {
            user=viewModel.user.value
            userNameEdit?.setText(viewModel.user.value?.userName)
            emailEdit?.setText(viewModel.user.value?.email)
            passOldEdit?.setText(viewModel.user.value?.password)
            Picasso.get().load("http:192.168.0.143:3000/api/images/user/file/${user?._id}").into(imageView)
        })
        viewModel.userImage.observe(this.viewLifecycleOwner, Observer {
            Picasso.get().load(viewModel.userImage.value.toString()).into(imageView)
        })
        Log.i("Tenemos",user.toString())
        bottnCambFoto.setOnClickListener{
            eliminarFoto(viewModel.user.value!!._id!!)
            fileChooser()
        }
        bottnSubirFoto.setOnClickListener{
            if(viewModel.userImage.value?.path!=null)
                subirFoto(this.requireContext(),viewModel)
            MaterialAlertDialogBuilder(this.requireContext()).setTitle("Imagen Remplazada")
                .setMessage("La imagen de perfil ha sido remplazada con exito")
                .setPositiveButton("Ok"){dialog, which->}
                .show()
        }
        bottnActu.setOnClickListener{
            if(passNewEdit?.text.toString().equals(passConfirmEdit?.text.toString())){
                var user = viewModel.user.value
                user?.password=passNewEdit?.editableText.toString()
                user?.email=emailEdit?.editableText.toString()
                user?.userName= userNameEdit?.editableText.toString()
                viewModel.setUser(user!!)
                var data = gson.toJson(viewModel.user.value).toString()
                ejecutarTarea(viewModel,MethodRequest.PATCH.meth,1,data,null,null)
            }
        }
        bottnMisArt.setOnClickListener{
            var arguments = Bundle()
            arguments.putString("userId",viewModel.user.value!!._id)
            var fragment = Inicio()
            fragment.arguments=arguments
            var transaction:FragmentTransaction = parentFragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer,fragment)
            transaction.addToBackStack("Mis Articulos")
            transaction.commit()
        }
        bottnCerrar.setOnClickListener {
            val intent = Intent (this.activity,Loggin_Register_Activity::class.java)
            startActivity(intent)
            activity?.finish()
        }
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

    fun subirFoto(context: Context,viewModel:PerfilViewModel){
        var path=getPathFromUri(context,viewModel.userImage.value!!)
        var file = File(path)
        lifecycleScope.launch {
            var responses= UserImagesResponses()
            var data = responses.postUserImage(file,viewModel.user?.value?._id!!)
            Log.i("Retrofit uploadImage",data.toString())
        }
    }
    fun ejecutarTarea(viewModel:PerfilViewModel,method: Int,service:Int,params:String,userId: String?,uri:Uri?){
        if(task?.status== Constant.Status.RUNNING){
            task?.cancel(true)
        }
        task = PerfilAsyncTask(params!!,method,service,this.requireContext(),userId,uri,viewModel)
        task?.execute()
    }
    fun fileChooser(){
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("image/*");
        fileChooserResult.launch(intent)
    }
    fun eliminarFoto(user:String){
        lifecycleScope.launch {
            var responses= UserImagesResponses()
            var data = responses.deleteUserImage(user)
            Log.i("Retrofit uploadImage",data.toString())
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode){
            10->if(requestCode==RESULT_OK){
                var path = data?.getData()?.path.toString()
                Log.i("FileChooser",path)
            }
        }
    }*/
    inner class PerfilAsyncTask(
        private var parametros:String,
        private var method:Int,
        private var service: Int,
        private var context:Context,
        private var userId:String?,
        private var uri:Uri?,
        private var viewModel:PerfilViewModel
        ): CoroutinesAsyncTask<Int, Int, String>("PerfilTask") {

        var gson = Gson()

        override fun doInBackground(vararg params: Int?): String {
            when(method){
                1-> when(service){
                    1->return httpRequestGet("${UrlsApis.GET_USER_DATA.url}${userId!!}")
                    else->throw IllegalArgumentException("El servicio solicitado no existe")
                }
                2->{
                    when(service){
                        1->return multipartPostImageUser(UrlsApis.USER_IMAGE_UPDATE.url, userId!!, viewModel.userImage.value!!,context)
                        else->throw IllegalArgumentException("El servicio solicitado no existe")
                    }
                }
                3-> when(service){
                    1->return httpRequestPatch("${UrlsApis.PATCH_UPDATE_USER_DATA.url}", parametros)
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