package com.example.crossfit.fragments.fragmentDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.crossfit.NAV_CONTROLLER
import com.example.crossfit.databinding.FragmentDetailBinding
import com.example.crossfit.databinding.ItemRoundBinding
import com.example.crossfit.models.WorkoutType.TYPE_AMRAP
import com.example.crossfit.models.WorkoutType.TYPE_EMOM
import com.example.crossfit.models.WorkoutType.TYPE_TIME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailFragment : Fragment() {

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this).get(DetailFragmentViewModel::class.java)
        val id = arguments?.getString("id") ?: ""
        viewModel.getWorkout(id)

        binding.back.setOnClickListener{
            NAV_CONTROLLER.popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.apply {
                    title.text = state.workout.title
                    roundsCount.text = state.workout.rounds.size.toString()
                    desc.text = state.workout.description
                    date.text = state.workout.dateTime
                    time.text = state.workout.time
                    when(state.workout.type){
                        TYPE_TIME -> type.text = "Тренеровка на время"
                        TYPE_AMRAP -> type.text = "Amrap тренеровка"
                        TYPE_EMOM -> type.text = "Emom Тренеровка"
                    }

                    state.workout.rounds.forEachIndexed{ index, it ->
                        val item = ItemRoundBinding.inflate(inflater, container, false)
                        item.number.text = (index+1).toString()
                        item.type.text = "раунд"
                        item.time.text = it
                        binding.roundsList.addView(item.root)
                    }
                }
            }
        }

        return binding.root
    }

}