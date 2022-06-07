package com.galboss.protorype.fragments

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.galboss.protorype.MainActivity
import com.galboss.protorype.databinding.FragmentCrearBinding
import com.galboss.protorype.R
import com.galboss.protorype.fragments.viewModels.ArticleViewModel
import com.galboss.protorype.model.Constant
import com.galboss.protorype.model.entities.Article
import com.galboss.protorype.task.CoroutinesAsyncTask
import com.galboss.protorype.task.httpRequestPost
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.squareup.picasso.Picasso.get
import org.apache.commons.logging.impl.Log4JLogger
import java.lang.IllegalArgumentException

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

    enum class MethodRequest(var METH:Int){
        GET(1),
        POST(2),
        PATCH(3),
        DELETE(4)
    }
    enum class UrlsApis(val URL:String){
        POST_ARTICLE("http://192.168.0.143:3000/api/article")
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

        fileChooserResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
            if(result.resultCode==Activity.RESULT_OK){
                var dat = result.data?.data
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel=ViewModelProvider(this).get(ArticleViewModel::class.java)
        viewModel.setArticle(Article())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCrearBinding.inflate(inflater,container,false)
         binding= _binding!!
        val root:View = binding?.root!!
        //Binding zone
        var titulo = binding.crearTituloArticulo?.editText
        var contenido = binding.crearContenido?.editText
        var provincia = binding.crearProvincia?.editText
        provincia?.inputType=InputType.TYPE_CLASS_NUMBER
        var canton = binding.crearCanton?.editText
        canton?.inputType=InputType.TYPE_CLASS_NUMBER
        var distrito = binding.crearDistrito?.editText
        distrito?.inputType=InputType.TYPE_CLASS_NUMBER
        var bottnImages = binding.buttonChooseImages
        bottnImages.isEnabled=false
        var bottnPublis = binding.buttonPublicar
        //programming zone
        viewModel.article.observe(this.viewLifecycleOwner, Observer {
            titulo?.setText(viewModel.article.value?.title)
            contenido?.setText(viewModel.article.value?.content)
            provincia?.setText(viewModel.article.value?.provincia.toString())
            canton?.setText(viewModel.article.value?.canton.toString())
            distrito?.setText(viewModel.article.value?.distrito.toString())
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
        provincia?. setOnFocusChangeListener{_,hasFocus->
            if(!hasFocus){
                if(!provincia.text.toString().isEmpty())
                    viewModel.article.value?.provincia=provincia.text.toString().toInt()
            }
        }
        canton?.setOnFocusChangeListener{_,hasFocus->
            if(!hasFocus)
                if(!canton?.text.toString().isEmpty())
                    viewModel.article.value?.canton=canton?.text.toString().toInt()
        }
        distrito?.setOnFocusChangeListener{_, hasFocus->
            if(!hasFocus)
                if(!distrito?.text.toString().isEmpty())
                    viewModel.article.value?.distrito=distrito?.text.toString().toInt()
        }
        Log.i("Activity ViewModel","${MainActivity.viewModelAc.userActivity.value.toString()}")
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
                ejecutarTarea(viewModel,MethodRequest.POST.METH,1,json.toString())
            }
        }
        //Return vista
        return root
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

    fun ejecutarTarea(viewModel: ArticleViewModel,method: Int,service: Int,params:String){
        if(task?.status == Constant.Status.RUNNING)
            task?.cancel(true)
        task = CrearAsyncTask(params!!,method,service,this.requireContext(),viewModel)
        task?.execute()
    }

    inner class CrearAsyncTask(
        private var parametros:String,
        private var method:Int,
        private var service:Int,
        private var context: Context?,
        private var viewModel:ArticleViewModel
    ):CoroutinesAsyncTask<Int,Int,String>("Servicios de articulos"){
        override fun doInBackground(vararg params: Int?):String {
            when(method){
                2->when(service){
                    1->return httpRequestPost(UrlsApis.POST_ARTICLE.URL,parametros)
                    else-> throw IllegalArgumentException("El servicio solicitado no existe")
                }
                else-> throw IllegalArgumentException("El método solicitado no existe")
            }
        }
    }
}