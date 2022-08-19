package com.example.crossfit.fragments.saveFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.crossfit.NAV_CONTROLLER
import com.example.crossfit.R
import com.example.crossfit.dataBase.WorkoutDataBase
import com.example.crossfit.databinding.FragmentSaveBinding
import com.example.crossfit.databinding.ItemRoundBinding
import com.example.crossfit.fragments.mainSubfragments.fragmentHistory.HistoryFragmentViewModel
import com.example.crossfit.models.Workout
import com.example.crossfit.models.WorkoutType.TYPE_TIME
import com.example.crossfit.models.toWorkoutEntity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SaveFragment: Fragment() {
    @Inject lateinit var room: WorkoutDataBase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentSaveBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this).get(SaveFragmentViewModel::class.java)


        val list = arguments?.getStringArrayList("rounds") ?: emptyList()
        val time = arguments?.getString("time") ?: ""
        val type = arguments?.getString("type") ?: TYPE_TIME
        Toast.makeText(requireContext(), list.size.toString(), Toast.LENGTH_SHORT).show()

        list.forEachIndexed { index, it ->
            val item = ItemRoundBinding.inflate(inflater)
            item.number.setTextColor(R.color.darkBlue)
            item.type.setTextColor(R.color.darkBlue)
            item.time.setTextColor(R.color.darkBlue)
            item.number.text = (index + 1).toString()
            item.type.text = "Раунд"
            item.time.text = it
            binding.roundList.addView(item.root)
        }
        val titleEditText = binding.title.editText
        val descEditText = binding.desc.editText
        val numberEditText= binding.number.editText
        binding.button.setOnClickListener{
            if(titleEditText?.text?.isNotEmpty() == true && descEditText?.text?.isNotEmpty() == true && numberEditText?.text?.isNotEmpty() == true){
                val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
                val currentDateandTime: String = sdf.format(Date())
                viewModel.saveWorkout(
                    title = titleEditText.text.toString(),
                    type = type,
                    description = descEditText.text.toString(),
                    time = time,
                    number= numberEditText.text.toString(),
                    dateTime = currentDateandTime,
                    rounds = list,
                )

                NAV_CONTROLLER.navigate(R.id.action_saveFragment_to_mainFragment)

            }
            else{
                Toast.makeText(requireContext(),"Пожалуйста заплните все поля", Toast.LENGTH_SHORT).show()
            }

        }
        return binding.root
    }


}