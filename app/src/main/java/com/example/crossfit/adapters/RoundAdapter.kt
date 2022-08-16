package com.example.crossfit.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crossfit.databinding.ItemRoundBinding


class RoundAdapter: RecyclerView.Adapter<RoundAdapter.Holder>() {
    val list = mutableListOf<String>()

    class Holder(chatItem:ItemRoundBinding): RecyclerView.ViewHolder(chatItem.root){
        val number = chatItem.number
        val type = chatItem.type
        val time = chatItem.time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val item = ItemRoundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = Holder(item)
        return holder

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.number.text = (position+1).toString()
        holder.type.text = "Раунд"
        holder.time.text = list[position]

        holder.number.setTextColor(Color.WHITE)
        holder.type.setTextColor(Color.WHITE)
        holder.time.setTextColor(Color.WHITE)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(newList:List<String>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
