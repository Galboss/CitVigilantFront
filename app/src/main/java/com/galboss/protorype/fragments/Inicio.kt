package com.galboss.protorype.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.galboss.protorype.databinding.FragmentInicioBinding
import androidx.recyclerview.widget.RecyclerView
import com.galboss.protorype.R
import com.galboss.protorype.fragments.viewModels.InicioViewModel
import com.galboss.protorype.model.Constant
import com.galboss.protorype.model.entities.Article
import com.galboss.protorype.reciclerAdapters.ListaArticulosAdapter
import com.galboss.protorype.task.CoroutinesAsyncTask
import com.galboss.protorype.task.httpRequestGet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.IllegalArgumentException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Inicio.newInstance] factory method to
 * create an instance of this fragment.
 */
class Inicio : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var task:InicioAsyncTask?=null
    lateinit var recycler:RecyclerView
    private var _binding:FragmentInicioBinding?=null
    private lateinit var viewModel:InicioViewModel
    private val binding get() =_binding!!
    private lateinit var adapter: ListaArticulosAdapter
    var gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    enum class MethodRequest(val METH:Int){
        GET(1),
        POST(2),
        PATCH(3),
        DELETE(4)
    }

    enum class UrlApis(val URL:String){
        GET_ALL_ARTICLES("http://192.168.0.143:3000/api/article")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(InicioViewModel::class.java)
        ejecutarTarea(MethodRequest.GET.METH,1,"")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInicioBinding.inflate(inflater,container,false)
        recycler = binding.inicioRecicler
        recycler.layoutManager = LinearLayoutManager(recycler.context)
        //recycler.setHasFixedSize(true)
        //Programming zone
        adapter= ListaArticulosAdapter(listOf(),inflater,binding.root.context)
        recycler.adapter=adapter
        viewModel.list.observe(this.viewLifecycleOwner, Observer {
            adapter.setList(it)
            recycler.adapter=adapter
        })
        return binding.root
    }

    fun ejecutarTarea(meth: Int, serv: Int, params:String){
        if(task?.status ==Constant.Status.RUNNING)
            task?.cancel(true)
        task= InicioAsyncTask(meth,serv,params,viewModel)
        task?.execute()
    }

    class InicioAsyncTask(
        private var meth:Int,
        private var serv:Int,
        private var parametros:String,
        private var viewModel: InicioViewModel
    ):CoroutinesAsyncTask<Int,Int,String>("Inicio Async Task"){
        var gson = Gson()
        override fun doInBackground(vararg params: Int?): String {
            when(meth){
                1->when(serv){
                    1-> return httpRequestGet(UrlApis.GET_ALL_ARTICLES.URL)
                    else-> throw IllegalArgumentException("El método de petición no existe.")
                }
                else-> throw IllegalArgumentException("El método de petición no existe.")
            }
        }

        override fun onPostExecute(result: String?) {
            when(meth){
                1-> {
                    var sType = object : TypeToken<List<Article>>() {}.type
                    var data = gson.fromJson<MutableList<Article>>(result, sType)
                    viewModel.setList(data)
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Inicio.
         */

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Inicio().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}