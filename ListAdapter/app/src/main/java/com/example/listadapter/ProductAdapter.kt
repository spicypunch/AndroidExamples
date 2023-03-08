package com.example.listadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listadapter.databinding.ProductItemBinding

class ProductAdapter : ListAdapter<ProductModel, ProductAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(var binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductModel) {
            binding.apply {
                ivThumbnail.setImageDrawable(item.thumbnail)
                tvTitle.text = item.title
                tvPrice.text = item.price
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // currentList: 해당 Adapter에 "submitList()"를 통해 삽입한 아이템 리스트
        holder.bind(currentList[position])
    }

    companion object {
        // diffUtil: currentList에 있는 각 아이템들을 비교하여 최신 상태를 유지하도록 한다.
        val diffUtil = object : DiffUtil.ItemCallback<ProductModel>() {
            override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}