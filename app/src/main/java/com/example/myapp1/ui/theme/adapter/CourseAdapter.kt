package com.example.myapp1.ui.theme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp1.databinding.ItemCourseBinding
import com.example.myapp1.model.CourseItem

class CourseAdapter(
    private val items: List<CourseItem>,
    private val onClick: (CourseItem) -> Unit ={}
) : RecyclerView.Adapter<CourseAdapter.VH>() {

    inner class VH(private val b: ItemCourseBinding) : RecyclerView.ViewHolder(b.root){
        fun bind(item: CourseItem){
            b.ivImage.setImageResource(item.imageRes)
            b.tvTitle.text = item.title
            b.tvDescription.text = item.description
            b.tvPrice.text = "S/. ${item.price}"
            b.tvStock.text ="${item.stock} en stock"
            b.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}