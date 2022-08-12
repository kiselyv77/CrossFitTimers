package com.example.crossfit.fragments.mainSubfragments.fragmentHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crossfit.adapters.RcAdapter2
import com.example.crossfit.dataBase.WorkoutDataBase
import com.example.crossfit.databinding.FragmentHistoryBinding
import com.example.crossfit.fragments.MyViewModelFactory
import com.example.crossfit.fragments.mainSubfragments.fragmentTimers.TimersFragmentViewModel
import com.example.crossfit.models.toWorkout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HistoryFragment : Fragment() {

    @Inject
    lateinit var room: WorkoutDataBase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this).get(HistoryFragmentViewModel::class.java)

        val adapter = RcAdapter2{ id ->
            viewModel.deleteWorkout(id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                if(state.workoutList.size > 0){
                    adapter.setList(state.workoutList)
                    binding.textIsListEmphy.visibility = GONE
                }
                else{
                    binding.textIsListEmphy.visibility = VISIBLE
                }
            }
        }

        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        binding.rcView.adapter = adapter
        return binding.root
    }

}