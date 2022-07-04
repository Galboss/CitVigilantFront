package com.galboss.protorype.user.reciclerAdapters

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.AttachedSurfaceControl
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.FragmentTransitionSupport
import com.galboss.protorype.R
import com.galboss.protorype.model.entities.Article
import com.galboss.protorype.user.fragments.ArticleReview
import com.galboss.protorype.user.fragments.viewModels.InicioViewModel
import com.galboss.protorype.user.responses.ArticleResponses
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class ListaArticulosAdapter(
    private var nData: List<Article>,
    private val mInflater: LayoutInflater,
    private val context: Context,
    private val parent: FragmentManager?
) : RecyclerView.Adapter<ListaArticulosAdapter.ViewHolder>() {

    var itemDeletedPosition:Int=0
    var itemDeleted:Article?=null
    var root:ViewHolder?=null
    val scope= CoroutineScope(Dispatchers.Default+ Job())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_article_item,parent,false)
        root = ViewHolder(view)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(nData.get(position))
        var item = nData.get(position)
        holder.itemView.setOnClickListener{
            var fragmentDisplay = ArticleReview()
            var argu=Bundle()
            fragmentDisplay.arguments=argu
            argu.putString("articleId","${nData.get(position)._id}")
            var ft = parent!!.beginTransaction()
            ft.replace(R.id.fragmentContainer,fragmentDisplay,"Mostrar Articulo")
            ft.addToBackStack("Mostrar el articulo seleccionado")
            ft.commit()
        }
    }

    override fun getItemCount(): Int {
        return this.nData.size
    }

    fun setList(list:List<Article>){
        nData = list
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int){
        var list = mutableListOf<Article>()
        itemDeletedPosition=position
        itemDeleted=nData.get(position)
        list.addAll(nData)
        list.removeAt(position)
        nData=list.toList()
        notifyItemRemoved(position)
        deleteArticleById(itemDeleted!!._id!!)
        Log.i("Position Save","${position},${itemDeletedPosition}")
        Snackbar.make(root!!.itemView,"Articulo Eliminado",Snackbar.LENGTH_LONG).setAction("Deshacer"){
            Log.i("On Snackbar","${itemDeleted}")
            list.add(itemDeletedPosition,itemDeleted!!)
            nData=list.toList()
            notifyItemInserted(itemDeletedPosition)
            postArticle(itemDeleted!!)
        }.show()
    }

    fun postArticle(article: Article){
        scope.launch {
            var responses = ArticleResponses()
            var data = responses.articlePost(itemDeleted!!)
        }
    }

    fun deleteArticleById(articleId:String){
        scope.launch {
            var responses = ArticleResponses()
            var data = responses.deleteArticleById(articleId)
        }
    }

    fun getList():List<Article>{ return nData }

    inner class ViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        var card: CardView
        var title: TextView
        var content: TextView

        fun bindData(item: Article){
            title.text=item.title
            if(item.content.length>150){
                var content150 = item.content.chunked(150)
                content.text=content150.get(0).plus("...")
            }else{
                content.text = item.content
            }
        }

        init{
            card = itemView.findViewById(R.id.card_container)
            title = itemView.findViewById(R.id.card_Title)
            content= itemView.findViewById(R.id.card_Content)
            title.setTypeface(title.typeface, Typeface.BOLD)
            content.setTypeface(content.typeface,Typeface.ITALIC)
        }
    }


}