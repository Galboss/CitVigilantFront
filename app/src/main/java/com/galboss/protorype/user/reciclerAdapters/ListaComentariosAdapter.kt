package com.galboss.protorype.user.reciclerAdapters

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.galboss.protorype.R
import com.galboss.protorype.model.entities.CommentaryWithUser

class ListaComentariosAdapter(
    private var nData: ArrayList<CommentaryWithUser>,
    private var mInflater:LayoutInflater,
    private var context:Context
): RecyclerView.Adapter<ListaComentariosAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.commentary_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(nData.get(position))
    }

    override fun getItemCount(): Int {
        return nData.size
    }

    fun setItem(list: ArrayList<CommentaryWithUser>){
        nData=list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private var userComen: TextView
        private var contentComen:TextView
        fun bindData(item: CommentaryWithUser){
            userComen.text=item.user?.userName
            contentComen.text=item.content
        }
        init {
            userComen=itemView.findViewById(R.id.commentary_user)
            contentComen=itemView.findViewById(R.id.commentary_content)
        }
    }

}