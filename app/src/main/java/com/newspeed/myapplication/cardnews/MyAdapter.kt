package com.newspeed.myapplication.cardnews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newspeed.myapplication.cardnews.ItemData
import com.newspeed.myapplication.databinding.NewsitemViewBinding

class MyAdapter(private val dataSet: List<ItemData>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var onItemClickListener: ((ItemData) -> Unit)? = null

    class MyViewHolder(private val binding: NewsitemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemData, onItemClickListener: ((ItemData) -> Unit)?) {
            binding.newstitle.text = item.title
            //binding.imageView.setImageResource(item.imageResourceId)

            // Set click listener
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = NewsitemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataSet[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setOnItemClickListener(listener: (ItemData) -> Unit) {
        onItemClickListener = listener
    }
}
