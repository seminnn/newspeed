package com.newspeed.myapplication

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.newspeed.myapplication.databinding.BubbleitemViewBinding


class BubbleAdapter(
    private val context: Context,
    private var data: List<String>,
    private val onItemClick: (String) -> Unit
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bubbleLayout: LinearLayout = itemView.findViewById(R.id.newsbubble)
        val itemText: TextView = itemView.findViewById(R.id.newstitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = BubbleitemViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemText.text = data[position]

        val colorRes = itemColors[position % itemColors.size]
        val backgroundColor = ContextCompat.getColor(context, colorRes)

        val shapeDrawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setSize(100.dp, 100.dp)
            setColor(backgroundColor)
        }

        holder.bubbleLayout.background = shapeDrawable

        holder.itemView.setOnClickListener {
            onItemClick(data[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(newData: List<String>) {
        data = newData
        notifyDataSetChanged()
    }

    private val Int.dp: Int
        get() = (this * context.resources.displayMetrics.density).toInt()
}

