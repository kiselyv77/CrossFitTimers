package com.example.crossfit.adapters

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.crossfit.NAV_CONTROLLER
import com.example.crossfit.R
import com.example.crossfit.databinding.ItemWorkoutBinding
import com.example.crossfit.models.Workout
import com.example.crossfit.models.WorkoutType.TYPE_AMRAP
import com.example.crossfit.models.WorkoutType.TYPE_EMOM
import com.example.crossfit.models.WorkoutType.TYPE_TABATA
import com.example.crossfit.models.WorkoutType.TYPE_TIME
import com.example.crossfit.utils.formateDateTime

class WorkoutAdapter(val delete : (id : String) -> Unit): RecyclerView.Adapter<WorkoutAdapter.Holder>() {
    var list = listOf<Workout>()

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = list[position].title
        holder.date.text = list[position].dateTime.formateDateTime()
        holder.btnDelete.setOnClickListener {
            delete(list[holder.adapterPosition].id)
        }

        when(list[position].type){
            TYPE_TIME ->{holder.type.text = "Тренеровка на время"}
            TYPE_AMRAP ->{holder.type.text = "Тренеровка AMRAP"}
            TYPE_EMOM ->{holder.type.text = "Тренеровка EMOM"}
            TYPE_TABATA ->{holder.type.text = "Тренеровка TABATA"}

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

    fun updateList(newList:List<Workout>){
        Log.d("update", "new:->$newList")
        Log.d("update", "old:->$list")
        val diffCallback = WorkoutDiffUtillCallBack(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

}
