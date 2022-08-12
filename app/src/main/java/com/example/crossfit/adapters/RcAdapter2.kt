package com.example.crossfit.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crossfit.NAV_CONTROLLER
import com.example.crossfit.R
import com.example.crossfit.databinding.ItemWorkoutBinding
import com.example.crossfit.models.Workout
import com.example.crossfit.models.WorkoutType.TYPE_AMRAP
import com.example.crossfit.models.WorkoutType.TYPE_EMOM
import com.example.crossfit.models.WorkoutType.TYPE_TIME


class RcAdapter2(val delete : (id : String) -> Unit): RecyclerView.Adapter<RcAdapter2.Holder>() {
    val list = mutableListOf<Workout>()

    class Holder(item: ItemWorkoutBinding): RecyclerView.ViewHolder(item.root){
        val title = item.title
        val type = item.type
        val time = item.time
        val date = item.date
        val btnDelete = item.btnDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val item = ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = Holder(item)
        return holder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = list[position].title
        holder.date.text = list[position].dateTime
        holder.btnDelete.setOnClickListener {
            delete(list[holder.adapterPosition].id)
            list.removeAt(holder.adapterPosition)
            notifyItemRemoved(position)
        }

        Log.d("type", list[position].type)
        when(list[position].type){
            TYPE_TIME ->{holder.type.text = "Тренеровка на время"}
            TYPE_AMRAP ->{holder.type.text = "Тренеровка AMRAP"}
            TYPE_EMOM ->{holder.type.text = "Тренеровка EMOM"}
        }
        holder.time.text = list[position].time

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", list[position].id)
            NAV_CONTROLLER.navigate(R.id.detailFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(newList:List<Workout>){
        list.clear()
        list.addAll(newList)
        notifyItemRangeInserted(0, list.size)
    }
}
