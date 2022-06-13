package com.galboss.protorype.user.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.galboss.protorype.MainActivity
import com.galboss.protorype.MainActivityViewModel
import com.galboss.protorype.R
import com.galboss.protorype.databinding.FragmentArticleReviewBinding
import com.galboss.protorype.model.entities.Commentary
import com.galboss.protorype.model.entities.CommentaryWithUser
import com.galboss.protorype.user.fragments.viewModels.ArticleDisplayViewModel
import com.galboss.protorype.user.reciclerAdapters.ListaComentariosAdapter
import com.galboss.protorype.user.responses.ArticleResponses
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ArticleReview.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArticleReview : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recycler:RecyclerView
    private var _binding : FragmentArticleReviewBinding?=null
    private val binding get() = _binding!!
    var gson = Gson()
    private lateinit var viewModel: ArticleDisplayViewModel
    private lateinit var recyclerAdapter: ListaComentariosAdapter
    private var articleId:String?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel=ViewModelProvider(this).get(ArticleDisplayViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleReviewBinding.inflate(inflater,container,false)
        //Check for arguments in callback
        var extras = this.arguments
        //Binding UI
        var articleTitle = binding.articleTitle
        var articleContent = binding.articleContent
        var articleStatus = binding.articleStatus
        var articleImagesContainer = binding.articleImagesContainer
        var articleComentaryBox = binding.articleComentaryBox
        var articleRecycleListCommentary= binding.articleComentaryList
        var buttnPublis = binding.articleBtnPublish
        recycler = binding.articleComentaryList
        recycler.layoutManager = LinearLayoutManager(recycler.context)
        recyclerAdapter = ListaComentariosAdapter(arrayListOf(),inflater,binding.root.context)
        recycler!!.adapter=recyclerAdapter
        //Set data where it should be
        viewModel.article.observe(this.viewLifecycleOwner, Observer {
            if(viewModel.article.value!=null) {
                articleTitle.setText(viewModel.article.value?.title)
                articleContent.setText(viewModel.article.value?.content)
                when (viewModel.article.value!!.estado) {
                    0 -> {
                        articleStatus.setText("Aún no se ha atendído.")
                        articleStatus.setTextColor(Color.RED)
                    }
                    1 -> {
                        articleStatus.setText("Se encuentra en mantenimiento.")
                        articleStatus.setTextColor(Color.YELLOW)
                    }
                    2 -> {
                        articleStatus.setText("Se ha solucionado el problema.")
                        articleStatus.setTextColor(Color.GREEN)
                    }
                }
            }
        })
        //call article data request
        if(!extras?.get("articleId").toString().isNullOrEmpty()){
            articleId=extras?.get("articleId").toString()
            getArticleById(articleId!!)
        }
        viewModel.commentaryList.observe(this.viewLifecycleOwner, Observer {
            recycler!!.adapter= ListaComentariosAdapter(viewModel.commentaryList.value!!,inflater,binding.root.context)
        })

        if(articleId!=null){
            getCommentaryList(articleId!!)
        }

        //Send commentaryData to server
        buttnPublis.setOnClickListener(){
            if(!articleComentaryBox.text.toString().isNullOrEmpty()){
                var comm = CommentaryWithUser(null,MainActivity.viewModelAc.userActivity.value!!,
                    extras?.get("articleId").toString(),articleComentaryBox.text.toString(),null)
                postComentary(comm.createCommentary())
                MaterialAlertDialogBuilder(this.requireContext()).setTitle("Comentario Publicado")
                    .setMessage("El comentario ha sido publicado con éxito!!")
                    .setPositiveButton("Ok"){dialog, which->}
                    .show()
            }else{
                MaterialAlertDialogBuilder(this.requireContext()).setTitle("Error")
                    .setMessage("Por favor, escriba algo primero en la caja de comentarios y luego pulse el botón de publicar")
                    .setPositiveButton("Ok"){dialog, which->}
                    .show()
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }
    fun getArticleById(id:String){
        lifecycleScope.launch {
            var responses = ArticleResponses()
            var data = responses.getArticleById(id)
            viewModel.setArticle(data.body()!!)
        }
    }
    fun getCommentaryList(id:String){
        lifecycleScope.launch {
            var responses = ArticleResponses()
            var data = responses.getCommentaryListWithUser(id)
            Log.i("DataRetrofit","${data.body().toString()}")
            viewModel.setListCommentary(data.body()!!)
        }
    }
    fun postComentary(comm:Commentary){
        lifecycleScope.launch {
            var responses = ArticleResponses()
            var data = responses.postCommentary(comm)
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ArticleReview.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ArticleReview().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}