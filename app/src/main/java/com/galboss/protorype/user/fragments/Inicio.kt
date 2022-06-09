package com.galboss.protorype.user.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.galboss.protorype.R
import com.galboss.protorype.databinding.FragmentInicioBinding
import com.galboss.protorype.user.fragments.viewModels.InicioViewModel
import com.galboss.protorype.model.Constant
import com.galboss.protorype.model.entities.Article
import com.galboss.protorype.user.reciclerAdapters.ListaArticulosAdapter
import com.galboss.protorype.task.CoroutinesAsyncTask
import com.galboss.protorype.task.httpRequestGet
import com.galboss.protorype.task.httpRequestGetWithBody
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


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
    private var provinciaHolder=0
    private var cantonHolder=0
    private var distritoHolder=0


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
        GET_ALL_ARTICLES("https://citvigilant.herokuapp.com/api/article"),
        GET_LOCATION_PROVINCIA("https://citvigilant.herokuapp.com/api/location"),
        GET_LOCATION_CANTDIS("https://citvigilant.herokuapp.com/api/location/"),
        GET_ARTILCES_BYLOCATION("https://citvigilant.herokuapp.com/api/article/findByLocation")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(InicioViewModel::class.java)
        ejecutarTarea(MethodRequest.GET.METH,1,"",null,null)
        ejecutarTarea(MethodRequest.GET.METH,2,"",null,null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInicioBinding.inflate(inflater,container,false)
        recycler = binding.inicioRecicler
        recycler.layoutManager = LinearLayoutManager(recycler.context)
        var spinnerProv = binding.inicioAutoProvincia
        var spinnerCant= binding.inicioAutoCantones
        var spinnerDist= binding.inicioAutoDistritos
        var btnBuscar= binding.inicioBuscar
        spinnerCant.isEnabled=false
        spinnerDist.isEnabled=false
        btnBuscar.isEnabled=false
        //Adapters for Spinners
        var provAdapter = ArrayAdapter<String>(this.requireContext(),
            R.layout.spinner_item, R.id.text_item_display, arrayOf())
        spinnerProv.setAdapter(provAdapter)
        var cantAdapter = ArrayAdapter<String>(this.requireContext(),
            R.layout.spinner_item,R.id.text_item_display, arrayOf())
        var distAdapter = ArrayAdapter<String>(this.requireContext(),
            R.layout.spinner_item,R.id.text_item_display, arrayOf())
        //recycler.setHasFixedSize(true)
        //Programming zone
        adapter= ListaArticulosAdapter(listOf(),inflater,binding.root.context)
        recycler.adapter=adapter
        //Spinners onChangeItems
        spinnerProv.onItemClickListener =
            OnItemClickListener { parent, arg1, pos, id ->
                this.provinciaHolder=pos+1
                ejecutarTarea(MethodRequest.GET.METH,3,"",
                    this.provinciaHolder,null)
                spinnerCant.isEnabled=true
                spinnerCant.setText("")
                spinnerDist.isEnabled=false
                btnBuscar.isEnabled=false
            }
        spinnerCant.onItemClickListener=
            OnItemClickListener{ parent,arg1,pos,id->
                this.cantonHolder = pos+1
                ejecutarTarea(MethodRequest.GET.METH,4,"",
                    this.provinciaHolder,this.cantonHolder)
                spinnerDist.isEnabled=true
                spinnerDist.setText("")
                btnBuscar.isEnabled=false
            }
        spinnerDist.onItemClickListener=
            OnItemClickListener{ parent,arg1,pos,id->
                this.distritoHolder=pos+1
                btnBuscar.isEnabled=true
            }
        //Button Bucar
        btnBuscar.setOnClickListener{
            var location="{\"provincia\":${provinciaHolder},\"canton\":${cantonHolder},\"distrito\":${distritoHolder}}"
            ejecutarTarea(MethodRequest.GET.METH,5,location,null,null)
        }
        //viewModel Observers
        viewModel.list.observe(this.viewLifecycleOwner, Observer {
            adapter.setList(it)
            recycler.adapter=adapter
        })
        viewModel.provincias.observe(this.viewLifecycleOwner, Observer {
            var prov:Array<String> = viewModel.provincias.value!!.toTypedArray()
            provAdapter =ArrayAdapter<String>(this.requireContext(),R.layout.spinner_item,R.id.text_item_display, prov)
            spinnerProv.setAdapter(provAdapter)
        })
        viewModel.cantones.observe(this.viewLifecycleOwner, Observer {
            var cant : Array<String> = viewModel.cantones.value!!.toTypedArray()
            cantAdapter=ArrayAdapter<String>(this.requireContext(),R.layout.spinner_item,R.id.text_item_display,cant)
            spinnerCant.setAdapter(cantAdapter)
        })
        viewModel.distritos.observe(this.viewLifecycleOwner, Observer {
            var dist: Array<String> = viewModel.distritos.value!!.toTypedArray()
            distAdapter = ArrayAdapter<String>(this.requireContext(),R.layout.spinner_item,R.id.text_item_display,dist)
            spinnerDist.setAdapter(distAdapter)
        })
        return binding.root
    }

    fun ejecutarTarea(meth: Int, serv: Int, params:String,provincia: Int?,canton: Int?){
        if(task?.status ==Constant.Status.RUNNING)
            task?.cancel(true)
        task= InicioAsyncTask(meth,serv,params,viewModel,provincia,canton)
        task?.execute()
    }

    class InicioAsyncTask(
        private var meth:Int,
        private var serv:Int,
        private var parametros:String,
        private var viewModel: InicioViewModel,
        private var provincia:Int?,
        private var canton:Int?
    ):CoroutinesAsyncTask<Int,Int,String>("Inicio Async Task"){
        var gson = Gson()
        override fun doInBackground(vararg params: Int?): String {
            when(meth){
                1->when(serv){
                    1-> return httpRequestGet(UrlApis.GET_ALL_ARTICLES.URL)
                    2->return httpRequestGet("${UrlApis.GET_LOCATION_PROVINCIA.URL}")
                    3->return httpRequestGet("${UrlApis.GET_LOCATION_CANTDIS.URL}${provincia}/cantones")
                    4->return httpRequestGet("${UrlApis.GET_LOCATION_CANTDIS.URL}${provincia}/${canton}/distritos")
                    5->return httpRequestGetWithBody(UrlApis.GET_ARTILCES_BYLOCATION.URL,parametros)
                    else-> throw IllegalArgumentException("El método de petición no existe.")
                }
                else-> throw IllegalArgumentException("El método de petición no existe.")
            }
        }

        override fun onPostExecute(result: String?) {
            when(meth){
                1-> {
                    when(serv){
                        1,5->{
                            var sType = object : TypeToken<List<Article>>() {}.type
                            var data = gson.fromJson<MutableList<Article>>(result, sType)
                            viewModel.setList(data)
                        }
                        2->{
                            Log.i("Se Obtuvo","${result}")
                            var sType = object : TypeToken<List<String>>(){}.type
                            var data = gson.fromJson<List<String>>(result,sType)
                            viewModel.setProvincias(data)
                        }
                        3->{
                            var sType = object : TypeToken<List<String>>(){}.type
                            var data = gson.fromJson<List<String>>(result,sType)
                            Log.i("cantones","${result}")
                            viewModel.setCantones(data)
                        }
                        4->{
                            var sType = object : TypeToken<List<String>>(){}.type
                            var data = gson.fromJson<List<String>>(result,sType)
                            viewModel.setDistritos(data)
                        }
                        else-> throw IllegalArgumentException("El método solicitado no existe")
                    }
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