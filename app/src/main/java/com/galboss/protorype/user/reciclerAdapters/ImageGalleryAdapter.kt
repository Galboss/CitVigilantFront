package com.galboss.protorype.user.reciclerAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.galboss.protorype.R
import com.galboss.protorype.model.entities.ArticleGalleryItem
import com.squareup.picasso.Picasso

class ImageGalleryAdapter(
    private var nData : ArrayList<ArticleGalleryItem>,
    private var mInflater: LayoutInflater,
    private var context:Context
):RecyclerView.Adapter<ImageGalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item_recycler,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(nData.get(position))
    }

    fun setItems(data:ArrayList<ArticleGalleryItem>){
        nData = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return nData.size
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private var image: ImageView

        fun bindData(item:ArticleGalleryItem){
            Picasso.get().load(item.route).into(image)
        }

        init{
            image= itemView.findViewById(R.id.imageViewRecyclerItem)
        }
    }
}