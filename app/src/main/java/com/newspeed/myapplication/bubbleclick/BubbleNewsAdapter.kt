package com.newspeed.myapplication.bubbleclick

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newspeed.myapplication.databinding.BubbleclickitemViewBinding

//버블 클릭시 뜨는 카드뉴스에 대한 어댑터 코드
class BubbleNewsAdapter(private val bubbledataSet: List<BubbleNewsData>) :
    RecyclerView.Adapter<BubbleNewsAdapter.BubbleViewHolder>() {

    class BubbleViewHolder(private val binding: BubbleclickitemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BubbleNewsData, onItemClicked: (BubbleNewsData) -> Unit) {
            binding.bubblenewsTitle.text = item.title
            binding.bubblenewsContent.text = item.content
            binding.bubblenewsNid.text = item.nid.toString()
            // 클릭 이벤트 설정
            binding.root.setOnClickListener {
                onItemClicked(item)
            }
        }
    }

    private var onItemClicked: (BubbleNewsData) -> Unit = {}

    fun setOnItemClickListener(listener: (BubbleNewsData) -> Unit) {
        onItemClicked = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BubbleViewHolder {
        val binding = BubbleclickitemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BubbleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BubbleViewHolder, position: Int) {
        val item = bubbledataSet[position]
        holder.bind(item, onItemClicked)
    }

    override fun getItemCount(): Int {
        return bubbledataSet.size
    }
}
