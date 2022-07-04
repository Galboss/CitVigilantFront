package com.galboss.protorype.user.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.galboss.protorype.MainActivity
import com.galboss.protorype.R
import com.galboss.protorype.databinding.FragmentCrearBinding
import com.galboss.protorype.user.fragments.viewModels.ArticleViewModel
import com.galboss.protorype.model.Constant
import com.galboss.protorype.model.entities.Article
import com.galboss.protorype.model.entities.MessageResponse
import com.galboss.protorype.task.CoroutinesAsyncTask
import com.galboss.protorype.task.httpRequestGet
import com.galboss.protorype.task.httpRequestPost
import com.galboss.protorype.user.responses.ArticleResponses
import com.galboss.protorype.utils.UriUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.lang.IllegalArgumentException
import java.lang.StringBuilder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Crear.newInstance] factory method to
 * create an instance of this fragment.
 */
class Crear : Fragment() {
    var _binding:FragmentCrearBinding ?=null
    var task:CrearAsyncTask?=null
    lateinit var binding:FragmentCrearBinding
    //private lateinit var task
    private var article: Article?=null
    var gson = Gson()
    private lateinit var fileChooserResult: ActivityResultLauncher<Intent>
    private  lateinit var viewModel:ArticleViewModel
    private var provinciaHolder=0
    private var cantonHolder=0
    private var distritoHolder=0

    enum class MethodRequest(var METH:Int){
        GET(1),
        POST(2),
        PATCH(3),
        DELETE(4)
    }
    enum class UrlsApis(val URL:String){
        POST_ARTICLE("https://citvigilant.herokuapp.com/api/article"),
        GET_LOCATION_PROVINCIA("https://citvigilant.herokuapp.com/api/location"),
        GET_LOCATION_CANTDIS("https://citvigilant.herokuapp.com/api/location/"),
        GET_ARTILCES_BYLOCATION("https://citvigilant.herokuapp.com/api/article/findByLocation")
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

        fileChooserResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult() ){ result->
            if(result.resultCode==Activity.RESULT_OK){
                var images: ArrayList<Uri> = arrayListOf()
                var dat = result.data
                if(dat?.clipData!=null){
                    val count = dat.clipData?.itemCount?:0
                    //multiples imagenes
                    for(i in 0 until count){
                        images.add(dat.clipData?.getItemAt(i)?.uri!!)
                    }
                }else if(dat?.data!=null){
                    //Cuando solo viene una unica imagen
                    images.add(dat.data!!)
                }
                Log.i("Galeria de imagenes","${images.toString()}")
                viewModel.setImages(images)
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel=ViewModelProvider(this).get(ArticleViewModel::class.java)
        viewModel.setArticle(Article())
        ejecutarTarea(viewModel,MethodRequest.GET.METH,1,"",null,null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var extra = this.arguments
        _binding = FragmentCrearBinding.inflate(inflater,container,false)
         binding= _binding!!
        val root:View = binding?.root!!
        //Binding zone
        var titulo = binding.crearTituloArticulo?.editText
        var contenido = binding.crearContenido?.editText
        var spinnerProvincia = binding.crearAutoProvincia
        var spinnerCanton = binding.crearAutoCanton
        spinnerCanton.isEnabled=false
        var spinnerDistrito = binding.crearAutoDistrito
        spinnerDistrito.isEnabled=false
        var bottnImages = binding.buttonChooseImages
        bottnImages.isEnabled=false
        var bottnPublis = binding.buttonPublicar
        var titleFragment = binding.crearTituloFrag
        bottnPublis.isEnabled=false
        var textImages = binding.textviewPaths
        var bottSubirIma= binding.buttonUploadImages
        bottSubirIma.isEnabled=false
        // Defining spinners adapters
        var provAdapter = ArrayAdapter<String>(this.requireContext(),
            R.layout.spinner_item, R.id.text_item_display, arrayOf())
        spinnerProvincia.setAdapter(provAdapter)
        var cantAdapter = ArrayAdapter<String>(this.requireContext(),
            R.layout.spinner_item, R.id.text_item_display, arrayOf())
        var distAdapter = ArrayAdapter<String>(this.requireContext(),
            R.layout.spinner_item, R.id.text_item_display, arrayOf())
        //programming zone
        viewModel.article.observe(this.viewLifecycleOwner, Observer {
            titulo?.setText(viewModel.article.value?.title)
            contenido?.setText(viewModel.article.value?.content)
            if(!viewModel.article.value!!._id.isNullOrEmpty()){
                bottnImages.isEnabled=true
            }
        })
        titulo?.setOnFocusChangeListener{_,hasFocus->
            if(!hasFocus){
                viewModel.article.value?.title=titulo.text.toString()
            }
        }
        contenido?.setOnFocusChangeListener{_,hasFocus->
            if(!hasFocus){
                viewModel.article.value?.content=contenido.text.toString()
            }
        }
        spinnerProvincia.onItemClickListener=
            OnItemClickListener{ parent,arg1,pos,id->
                this.provinciaHolder = pos+1
                viewModel.article.value!!.provincia=provinciaHolder
                ejecutarTarea(viewModel,MethodRequest.GET.METH,2,"",provinciaHolder,null)
                spinnerCanton.isEnabled=true
                bottnPublis.isEnabled=false
                spinnerCanton.setText("")
                spinnerDistrito.setText("")
            }
        viewModel.provincias.observe(this.viewLifecycleOwner, Observer {
            var prov : Array<String> = viewModel.provincias.value!!.toTypedArray()
            provAdapter = ArrayAdapter<String>(this.requireContext(),R.layout.spinner_item,R.id.text_item_display, prov)
            spinnerProvincia.setAdapter(provAdapter)
        })
        spinnerCanton.onItemClickListener=
            OnItemClickListener{parent,arg1,pos,id->
                this.cantonHolder = pos+1
                viewModel.article.value!!.canton=cantonHolder
                ejecutarTarea(viewModel,MethodRequest.GET.METH,3,"",provinciaHolder,cantonHolder)
                spinnerDistrito.isEnabled=true
                bottnPublis.isEnabled=false
                spinnerDistrito.setText("")
            }
        viewModel.cantones.observe(this.viewLifecycleOwner, Observer {
            var cant : Array<String> = viewModel.cantones.value!!.toTypedArray()
            cantAdapter = ArrayAdapter<String>(this.requireContext(),R.layout.spinner_item,R.id.text_item_display, cant)
            spinnerCanton.setAdapter(cantAdapter)
        })
        viewModel.distritos.observe(this.viewLifecycleOwner, Observer {
            var dist : Array<String> = viewModel.distritos.value!!.toTypedArray()
            distAdapter = ArrayAdapter<String>(this.requireContext(),R.layout.spinner_item,R.id.text_item_display, dist)
            spinnerDistrito.setAdapter(distAdapter)
        })
        spinnerDistrito.onItemClickListener=
            OnItemClickListener{parent,arg1,pos,id->
                this.distritoHolder=pos+1
                viewModel.article.value!!.distrito=distritoHolder
                bottnPublis.isEnabled=true
            }
        if(arguments!=null){
            var id = arguments?.getString("articleId")!!
            getArticleById(id,viewModel)
            titleFragment.setText("Modificar el articulo")
            bottnPublis.isEnabled=true
            bottnPublis.setText("Actualizar Artículo")
            bottnPublis.setOnClickListener {
                viewModel.article.value!!.user=MainActivity.viewModelAc.userActivity.value!!._id!!
                viewModel.article.value!!.content=contenido?.text.toString()
                viewModel.article.value!!.title=titulo?.text.toString()
                var json = gson.toJson(viewModel.article.value!!)
                patchArticle(viewModel.article.value!!)
                MaterialAlertDialogBuilder(this.requireContext()).setTitle("Artículo Actualizado")
                    .setMessage("El articulo ha sido publicado con exito.")
                    .setPositiveButton("Ok"){dialog, which->}
                    .show()
            }
        }else{
            bottnPublis.setOnClickListener{
                var data = viewModel.article.value
                Log.i("ArticleViewModel","${viewModel.article.value.toString()}")
                if(data!!.title.isEmpty()||data!!.content.isEmpty()
                    ||data!!.provincia==0||data!!.canton==0||data!!.distrito==0){
                    MaterialAlertDialogBuilder(this.requireContext()).setTitle("Error")
                        .setMessage("Falta llenar algunos campos en el formulario anterior...")
                        .setPositiveButton("Ok"){dialog, which->}
                        .show()
                }else{
                    data.user=MainActivity.viewModelAc.userActivity.value!!._id!!
                    var json = gson.toJson(data)
                    ejecutarTarea(viewModel,MethodRequest.POST.METH,1,json.toString(),null,null)
                    MaterialAlertDialogBuilder(this.requireContext()).setTitle("Artículo Publicado")
                        .setMessage("El articulo ha sido publicado con exito.")
                        .setPositiveButton("Ok"){dialog, which->}
                        .show()
                }
            }
        }
        bottnImages.setOnClickListener(){
            getImagesFromPhone()
        }
        bottSubirIma.setOnClickListener(){
            uploadImagesToServer()
        }
        viewModel.images.observe(this.viewLifecycleOwner, Observer {
            val count = viewModel.images.value?.count()
            val allPath =StringBuilder()
            for(i in 0 until count!!){
                allPath.append("${viewModel.images.value!!.get(i)}\n")
            }
            textImages.setText(allPath.toString())
            if(!viewModel.images.value!!.isEmpty())
                bottSubirIma.isEnabled=true
        })


        //Return vista
        return root
    }

    private fun getArticleById(id:String,viewModel: ArticleViewModel){
        lifecycleScope.launch {
            var responses = ArticleResponses()
            var data = responses.getArticleById(id)
            viewModel.setArticle(data.body()!!)
        }
    }

    private fun patchArticle(article:Article){
        lifecycleScope.launch {
            var responses = ArticleResponses()
            var data = responses.pathArticle(article)
            Log.i("Patch Protocol","${data.body()}")
        }
    }

    fun getImagesFromPhone(){
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        fileChooserResult.launch(intent)
    }

    fun createImageFile(context: Context,filename: String):File{
        var storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("${filename}","jpg",storageDir)
    }
    fun uriToFile(context: Context,uri:Uri,filename:String):File?{
        context.contentResolver.openInputStream(uri)?.let { inputStream ->
            val tempFile:File = createImageFile(context,filename)
            val fileOutputStream = FileOutputStream(tempFile)
            inputStream.copyTo(fileOutputStream)
            inputStream.close()
            fileOutputStream.close()
            return tempFile
        }
        return null
    }

    fun uploadImagesToServer(){
        var count = viewModel.images.value!!.count()
        for (i in 0 until count){
            lifecycleScope.launch {
                var responses= ArticleResponses()
                var file = uriToFile(requireContext(),viewModel.images.value!!.get(i)!!,viewModel.article.value!!._id!!)!!
                var id = viewModel.article.value!!._id
                var data = responses.uploadImage(file,id!!)
            }
        }
        MaterialAlertDialogBuilder(this.requireContext()).setTitle("Imagenes subidas")
            .setMessage("Las imagenes has sido subidas con éxito")
            .setPositiveButton("Ok"){dialog, which->}
            .show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Crear.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Crear().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun ejecutarTarea(viewModel: ArticleViewModel,method: Int,service: Int,params:String,provincia: Int?,canton: Int?){
        if(task?.status == Constant.Status.RUNNING)
            task?.cancel(true)
        task = CrearAsyncTask(params!!,method,service,this.requireContext(),viewModel,provincia,canton)
        task?.execute()
    }

    inner class CrearAsyncTask(
        private var parametros:String,
        private var method:Int,
        private var service:Int,
        private var context: Context?,
        private var viewModel:ArticleViewModel,
        private var provincia:Int?,
        private var canton:Int?
    ):CoroutinesAsyncTask<Int,Int,String>("Servicios de articulos"){
        override fun doInBackground(vararg params: Int?):String {
            var gson=Gson()
            when(method){
                1->when(service){
                    1->return httpRequestGet("${Inicio.UrlApis.GET_LOCATION_PROVINCIA.URL}")
                    2->return httpRequestGet("${Inicio.UrlApis.GET_LOCATION_CANTDIS.URL}${provincia}/cantones")
                    3->return httpRequestGet("${Inicio.UrlApis.GET_LOCATION_CANTDIS.URL}${provincia}/${canton}/distritos")
                    else-> throw IllegalArgumentException("El servicio solicitado no existe")
                }
                2->when(service){
                    1->return httpRequestPost(UrlsApis.POST_ARTICLE.URL,parametros)
                    else-> throw IllegalArgumentException("El servicio solicitado no existe")
                }
                else-> throw IllegalArgumentException("El método solicitado no existe")
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            when(method){
                1->when(service){
                    1->{
                        Log.i("Se Obtuvo","${result}")
                        var sType = object : TypeToken<List<String>>(){}.type
                        var data = gson.fromJson<List<String>>(result,sType)
                        viewModel.setProvincias(data)
                    }
                    2->{
                        var sType = object : TypeToken<List<String>>(){}.type
                        var data = gson.fromJson<List<String>>(result,sType)
                        Log.i("cantones","${result}")
                        viewModel.setCantones(data)
                    }
                    3->{
                        var sType = object : TypeToken<List<String>>(){}.type
                        var data = gson.fromJson<List<String>>(result,sType)
                        viewModel.setDistritos(data)
                    }
                }
                2->when(service){
                    1->{
                        var sType = object : TypeToken<Article>(){}.type
                        var data = gson.fromJson<Article>(result,sType)
                        viewModel.setArticle(data)
                    }
                }
            }
        }
    }
}