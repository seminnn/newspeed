package com.newspeed.myapplication.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.newspeed.myapplication.ApiService
import com.newspeed.myapplication.BookmarkRequest
import com.newspeed.myapplication.BookmarkResponse
import com.newspeed.myapplication.databinding.BookmarkitemViewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarkAdapter(private val dataSet: List<BookmarkData>,
                      private val apiService: ApiService,
                      private val authToken: String) :
    RecyclerView.Adapter<BookmarkAdapter.MyViewHolder>() {

    private var onItemClickListener: ((BookmarkData) -> Unit)? = null

    class MyViewHolder(private val binding: BookmarkitemViewBinding,
                       private val apiService: ApiService,
                       private val authToken: String) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookmarkData, onItemClickListener: ((BookmarkData) -> Unit)?) {
            binding.bookmarknewstitle.text = item.title
            binding.bookmarkBtn.isChecked = true // 기본적으로 체크되어 있음

            binding.bookmarkBtn.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    // CheckBox가 해제될 때 북마크 삭제 API 호출
                    val authorizationHeader = "Bearer $authToken"
                    val bookmarkRequest = BookmarkRequest(item.nid)
                    apiService.deleteBookmark(authorizationHeader, bookmarkRequest).enqueue(object :
                        Callback<BookmarkResponse> {
                        override fun onResponse(call: Call<BookmarkResponse>, response: Response<BookmarkResponse>) {
                            if (response.isSuccessful) {
                                Toast.makeText(binding.root.context, "북마크에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(binding.root.context, "Failed to delete bookmark", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<BookmarkResponse>, t: Throwable) {
                            Toast.makeText(binding.root.context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            // 아이템 전체 클릭 리스너 설정
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = BookmarkitemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, apiService, authToken) // 여기에 apiService와 authToken을 전달합니다.
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