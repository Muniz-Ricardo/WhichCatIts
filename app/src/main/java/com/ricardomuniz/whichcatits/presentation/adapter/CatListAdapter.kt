package com.ricardomuniz.whichcatits.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ricardomuniz.whichcatits.data.model.Cat
import com.ricardomuniz.whichcatits.databinding.ItemCatListBinding
import com.ricardomuniz.whichcatits.util.OnItemClickListener

class CatListAdapter(
    private val context: Context,
    private val catMutableList: MutableList<Cat>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CatListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatListAdapter.ViewHolder {
        val itemView = ItemCatListBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return ViewHolder(itemView, context)
    }

    override fun onBindViewHolder(holder: CatListAdapter.ViewHolder, position: Int) {
        val catItem = catMutableList[position]
        holder.bind(catItem)
    }

    override fun getItemCount() = catMutableList.size

    inner class ViewHolder(
        private val itemBinding: ItemCatListBinding, val ctx: Context
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(cat: Cat) {
            itemBinding.tvNameCat.text = cat.id
            itemBinding.root.setOnClickListener { onItemClickListener.onClick(cat.id) }
        }
    }
}