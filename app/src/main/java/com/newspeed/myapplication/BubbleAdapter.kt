package com.newspeed.myapplication

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.newspeed.myapplication.databinding.BubbleitemViewBinding

class BubbleAdapter(
    private val context: Context,
    private var data: List<BubbleItem>,
    private val onItemClick: (BubbleItem) -> Unit

) : RecyclerView.Adapter<BubbleAdapter.ViewHolder>() {

    private val itemColors = intArrayOf(
        R.color.color1,
        R.color.color2,
        R.color.color3,
        R.color.color4,
        R.color.color5,
        R.color.color6,
        R.color.color7,
        R.color.color8,
        R.color.color9,
        R.color.color10
    )

    inner class ViewHolder(val binding: BubbleitemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick(data[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = BubbleitemViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.binding.newstitle.text = currentItem.keyword
        holder.binding.newscid.text = currentItem.cid
        holder.binding.newssum.text = currentItem.sum_com.toString()

        val colorRes = itemColors[position % itemColors.size]
        val backgroundColor = ContextCompat.getColor(context, colorRes)

        val shapeDrawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setSize(100.dp, 100.dp)
            setColor(backgroundColor)
        }

        holder.binding.newsbubble.background = shapeDrawable
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(newData: List<BubbleItem>) {
        data = newData
        notifyDataSetChanged()
    }

    private val Int.dp: Int
        get() = (this * context.resources.displayMetrics.density).toInt()
}