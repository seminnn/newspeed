package com.newspeed.myapplication.cardnews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newspeed.myapplication.databinding.NewsitemViewBinding

class MyAdapter(private val carddataSet: List<ItemData>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: NewsitemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemData, onItemClicked: (ItemData) -> Unit) {
            binding.cardnewsTitle.text = item.title
            binding.cardnewsContent.text = item.content
            binding.cardnewsNid.text = item.nid.toString()
            // 클릭 이벤트 설정
            binding.root.setOnClickListener {
                onItemClicked(item)
            }
        }
    }
    private var onItemClicked: (ItemData) -> Unit = {}

    fun setOnItemClickListener(listener: (ItemData) -> Unit) {
        onItemClicked = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = NewsitemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = carddataSet[position]
        holder.bind(item, onItemClicked)
    }

    override fun getItemCount(): Int {
        return carddataSet.size
    }
}
