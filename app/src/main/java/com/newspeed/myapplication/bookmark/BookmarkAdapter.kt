package com.newspeed.myapplication.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newspeed.myapplication.databinding.BookmarkitemViewBinding

class BookmarkAdapter(private val dataSet: List<BookmarkData>) :
    RecyclerView.Adapter<BookmarkAdapter.MyViewHolder>() {

    private var onItemClickListener: ((BookmarkData) -> Unit)? = null

    class MyViewHolder(private val binding: BookmarkitemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookmarkData, onItemClickListener: ((BookmarkData) -> Unit)?) {
            binding.bookmarknewstitle.text = item.title
            //binding.imageView.setImageResource(item.imageResourceId)

            // Set click listener
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = BookmarkitemViewBinding.inflate(
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

    fun setOnItemClickListener(listener: (BookmarkData) -> Unit) {
        onItemClickListener = listener
    }
}