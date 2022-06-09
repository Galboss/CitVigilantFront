package com.galboss.protorype.user.reciclerAdapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.galboss.protorype.R
import com.galboss.protorype.model.entities.Article
import java.lang.StringBuilder

class ListaArticulosAdapter(
    private var nData: List<Article>,
    private val mInflater: LayoutInflater,
    private val context: Context
) : RecyclerView.Adapter<ListaArticulosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.list_article_item,null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(nData.get(position))
        var item = nData.get(position)
        /*holder.itemView.setOnClickListener{
            holder.itemView.setBackgroundColor(Color.GREEN)
        }*/
    }

    override fun getItemCount(): Int {
        return this.nData.size
    }

    fun setList(list:List<Article>){ nData = list }

    fun getList():List<Article>{ return nData }

    inner class ViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        var card: CardView
        var title: TextView
        var content: TextView

        fun bindData(item: Article){
            var title50 = item.title.plus(generateSpaces(item.title.length))
            title.text=title50
            if(item.content.length>150){
                var content150 = item.content.chunked(150)
                content.text=content150.get(0).plus("...")
            }else{
                content.text = item.content
            }
        }
        fun generateSpaces(size:Int):String{
            var spaces: StringBuilder= StringBuilder()
            for (i in size..70)
                spaces.append(" ")
            return spaces.toString()
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