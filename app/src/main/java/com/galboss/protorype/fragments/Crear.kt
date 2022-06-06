package com.galboss.protorype.fragments

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.galboss.protorype.R
import com.galboss.protorype.databinding.FragmentCrearBinding
import com.galboss.protorype.fragments.viewModels.ArticleViewModel
import com.galboss.protorype.model.entities.Article
import com.galboss.protorype.task.CoroutinesAsyncTask
import com.google.gson.Gson
import com.squareup.picasso.Picasso.get
import org.apache.commons.logging.impl.Log4JLogger

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


    private var _binding:FragmentCrearBinding?=null
    var binding get() = _binding!!
    //private lateinit var task
    private var article: Article?=null
    var gson = Gson()
    private lateinit var fileChooserResult: Instrumentation.ActivityResult<Intent>
    private lateinit var viewModel:ArticleViewModel
    enum class MethodRequest(var meth:Int){
        GET(1),
        POST(2),
        PATCH(3),
        DELETE(4)
    }
    enum class UrlsApis(val url:String){

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCrearBinding.inflate(inflater,container,false)
        val root:View = binding?.root!!

        //Binding zone

        var titulo = binding.crearTituloArticulo?.editText
        var contenido = binding.crearContenido?.editText
        var provincia = binding.crearProvincia?.editText
        var canton = binding.crearCanton?.editText
        var distrito = binding.crearDistrito?.editText
        var bottnImages = binding.buttonChooseImages
        bottnImages.isEnabled=false
        var bottnPublis = binding.buttonPublicar

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

   /* inner class CrearAsyncTask(
        private var method:Int
    ):CoroutinesAsyncTask<Int,Int,String>("Servicios de articulos"){
        override fun doInBackground(vararg params: Int?):String {
            when(method){

            }
        }


    }*/
}