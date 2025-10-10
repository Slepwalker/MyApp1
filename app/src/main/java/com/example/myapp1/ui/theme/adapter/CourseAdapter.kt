package com.example.myapp1.ui.theme.adapter

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp1.ProductDetailFragment
import com.example.myapp1.databinding.ItemCourseBinding
import com.example.myapp1.model.CourseItem
import com.example.myapp1.R

class CourseAdapter(
    private val items: List<CourseItem>,
    private val onClick: (CourseItem) -> Unit ={}
) : RecyclerView.Adapter<CourseAdapter.VH>() {

    inner class VH(private val b: ItemCourseBinding) : RecyclerView.ViewHolder(b.root){
        fun bind(item: CourseItem){
            b.tvTitle.text = item.title
            b.tvDescription.text = item.description
            b.tvPrice.text = "S/. ${item.price}"
            b.tvStock.text ="${item.stock} en stock"
            if(item.imageUri!=null){
                b.ivImage.setImageURI(Uri.parse(item.imageUri))
            }else if(item.imageRes != null){
                b.ivImage.setImageResource(item.imageRes)
            }else{
                b.ivImage.setImageResource(R.mipmap.ic_launcher_round)
            }

            b.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}