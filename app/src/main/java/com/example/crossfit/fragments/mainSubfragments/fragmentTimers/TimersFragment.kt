package com.example.crossfit.fragments.mainSubfragments.fragmentTimers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.crossfit.NAV_CONTROLLER
import com.example.crossfit.R
import com.example.crossfit.databinding.ButtomSheetDialogBinding
import com.example.crossfit.databinding.CustomMenuBinding
import com.example.crossfit.databinding.FragmentTimersBinding
import com.example.crossfit.utils.formateTime
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

//Я в своем познании настолько преисполнился...
class TimersFragment : Fragment() {
    // Основной биндинг
    private lateinit var binding:FragmentTimersBinding

    // Биндинги менюшек которые добавляем в наши блоки
    private lateinit var menuForTime: CustomMenuBinding
    private lateinit var menuAMRAP:CustomMenuBinding
    private lateinit var menuEmom:CustomMenuBinding
    private lateinit var menuTabata:CustomMenuBinding

    // Нижний лист который будет вызыватся при нажатии на отдельный элемент меню
    private lateinit var bottomSheetBinding:ButtomSheetDialogBinding

    // Значение на которые мы меняем высоту блоков(CardView)
    // Это значение инициализируется высотой того меню которое мы добавили (ПИЗДЕЦ)
    private var heightChangeValueMenuForTime = 0
    private var heightChangeValueMenuAMRAP = 0
    private var heightChangeValueMenuEmom = 0
    private var heightChangeValueMenuTabata = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTimersBinding.inflate(inflater, container, false)
        val dialog = activity?.let { it1 -> BottomSheetDialog(it1) }
        val viewModel = ViewModelProvider(this).get(TimersFragmentViewModel::class.java)
        menuForTime = CustomMenuBinding.inflate(inflater, container, false)
        menuAMRAP = CustomMenuBinding.inflate(inflater, container, false)
        menuEmom = CustomMenuBinding.inflate(inflater, container, false)
        menuTabata = CustomMenuBinding.inflate(inflater, container, false)

        // Следим за состоянием вью модели
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                // Это настойка менюшек которые потом добавляем в каждый блок (CardView)
                // Тут мы настраиваем блок для каждой тренеровки. В этой настройке так же происходит настройка диалогов.
                menuForTime.apply {
                    val titleStr = "Время обратного отчета"
                    val titleStr2 = "Автоматическое завершение через"
                    title1.text = titleStr
                    // тут просто секунды
                    value1.text = state.stateCardForTime.countDownTime.toString() + " секунд"
                    title2.text = titleStr2
                    // берем секунды из вьюв модели и форматируем в строку по типу: 2 минуты 3 секунды
                    value2.text = state.stateCardForTime.autoEndTime.formateTime()
                    menu3.visibility = GONE
                    menu1.setOnClickListener{
                        // При нажатии на отдельный элемент меню вызывается нижний диалог
                        bottomSheetBinding = ButtomSheetDialogBinding.inflate(inflater, container, false)
                        // Тут происходит настройка диалога.
                        // Так как эта таже самая вьюшка придется настраивать для кажого элемента меню меню
                        bottomSheetBinding.apply {
                            pickerContainerMin.visibility = GONE
                            numberPickerSek.maxValue = 100
                            numberPickerSek.minValue = 1

                            numberPickerSek.value = state.stateCardForTime.countDownTime.toInt()
                            title.text = titleStr
                            dialog?.setContentView(root)
                            Ok.setOnClickListener{
                                dialog?.dismiss()
                                // при нажатии на ок диалога обновляем данные во вью модели
                                viewModel.updateCardForTimeCountDownTime(numberPickerSek.value.toLong())
                            }
                            dialog?.show()
                        }
                    }
                    menu2.setOnClickListener{
                        // При нажатии на отдельный элемент меню вызывается нижний диалог
                        bottomSheetBinding = ButtomSheetDialogBinding.inflate(inflater, container, false)
                        // Тут происходит настройка диалога.
                        // Так как эта таже самая вьюшка придется настраивать для кажого элемента меню меню
                        bottomSheetBinding.apply {
                            title.text = titleStr2
                            numberPickerMin.apply {
                                maxValue = 59
                                minValue = 0
                                // Получаем минуты установленные в данный момент
                                numberPickerMin.value = (state.stateCardForTime.autoEndTime / 60).toInt()
                            }
                            numberPickerSek.apply {
                                maxValue = 59
                                minValue = 0
                                // Получаем секунды установленные в данный момент
                                value = (state.stateCardForTime.autoEndTime % 60).toInt()
                            }

                            // Данные отслеживатели необходимы чтобы не дать пользователю установить нулевое значение
                            numberPickerMin.setOnValueChangedListener { _, p1, p2 ->
                                if (p2 == 0) {
                                    if (numberPickerSek.value == 0) {
                                        numberPickerSek.value ++
                                    }
                                }
                            }
                            numberPickerSek.setOnValueChangedListener { _, p1, p2 ->
                                if (p2 == 0) {
                                    if (numberPickerMin.value == 0) {
                                        numberPickerMin.value ++
                                    }
                                }
                            }

                            dialog?.setContentView(root)
                            Ok.setOnClickListener{
                                dialog?.dismiss()
                                // при нажатии на ок диалога обновляем данные во вью модели
                                // необходимо сложить значение двух пиккеров и преобразовать все в секунды
                                val autoendTime = numberPickerMin.value * 60 + numberPickerSek.value
                                viewModel.updateCardForTimeAutoEndTime(autoendTime.toLong())
                            }
                            dialog?.show()
                        }
                    }
                    btnStart.setOnClickListener{
                        // При навигации передаем все необходимые значения
                        val bundle = Bundle()
                        bundle.putLong("countdownTime", state.stateCardForTime.countDownTime)
                        bundle.putLong("timeEnd", state.stateCardForTime.autoEndTime)
                        NAV_CONTROLLER.navigate(R.id.timerFragment, bundle)
                    }
                }
                menuAMRAP.apply {
                    val titleStr = "Время обратного отчета"
                    val titleStr2 = "Время тренеровки" //По сути тоже самое что и атозавершение
                    title1.text = titleStr
                    value1.text = state.stateCardAmrap.countDownTime.toString() + " секунд"
                    title2.text = titleStr2
                    value2.text = state.stateCardAmrap.autoEndTime.formateTime()
                    menu3.visibility = GONE

                    menu1.setOnClickListener{
                        bottomSheetBinding = ButtomSheetDialogBinding.inflate(inflater, container, false)
                        bottomSheetBinding.apply {
                            pickerContainerMin.visibility = GONE
                            numberPickerSek.apply {
                                maxValue = 100
                                minValue = 1
                                value = (state.stateCardAmrap.countDownTime).toInt()
                            }
                            title.text = titleStr

                            Ok.setOnClickListener{
                                dialog?.dismiss()
                                // при нажатии на ок обновляем данные во вью модели
                                viewModel.updateCardAmrapCountDownTime(numberPickerSek.value.toLong())
                            }

                            dialog?.setContentView(root)
                            dialog?.show()
                        }
                    }
                    menu2.setOnClickListener{
                        bottomSheetBinding = ButtomSheetDialogBinding.inflate(inflater, container, false)
                        bottomSheetBinding.apply{
                            numberPickerMin.apply {
                                maxValue = 59
                                minValue = 0
                                value = (state.stateCardAmrap.autoEndTime / 60).toInt()
                            }
                            numberPickerSek.apply {
                                maxValue = 59
                                minValue = 0
                                value = (state.stateCardAmrap.autoEndTime % 60).toInt()
                            }

                            // Данные отслеживатели необходимы чтобы не дать пользователю установить нулевое значение
                            numberPickerMin.setOnValueChangedListener { _, p1, p2 ->
                                if (p2 == 0) {
                                    if (numberPickerSek.value == 0) {
                                        numberPickerSek.value ++
                                    }
                                }
                            }
                            numberPickerSek.setOnValueChangedListener { _, p1, p2 ->
                                if (p2 == 0) {
                                    if (numberPickerMin.value == 0) {
                                        numberPickerMin.value ++
                                    }
                                }
                            }

                            title.text = titleStr2

                            Ok.setOnClickListener{
                                dialog?.dismiss()
                                // при нажатии на ок диалога обновляем данные во вью модели
                                // необходимо сложить значение двух пиккеров и преобразовать все в секунды
                                val autoendTime = numberPickerMin.value * 60 + numberPickerSek.value
                                viewModel.updateCardAmrapAutoEndTime(autoendTime.toLong())
                            }

                            dialog?.setContentView(root)
                            dialog?.show()
                        }
                    }

                    btnStart.setOnClickListener{
                        val bundle = Bundle()
                        bundle.putInt("countdownTime", state.stateCardAmrap.countDownTime.toInt())
                        bundle.putLong("timeStart", state.stateCardAmrap.autoEndTime)
                        NAV_CONTROLLER.navigate(R.id.timerFragmentAmrap, bundle)
                    }

                }
                menuEmom.apply {
                    val titleStr = "Время обратного отчета"
                    val titleStr2 = "Время интервала"
                    val titleStr3 = "Раундов"
                    title1.text = titleStr
                    value1.text = state.stateCardEmom.countDownTime.toString() + " секунд"
                    title2.text = titleStr2
                    value2.text = state.stateCardEmom.timeInterval.formateTime()
                    title3.text = titleStr3
                    value3.text = state.stateCardEmom.rounds.toString()
                    menu1.setOnClickListener{
                        bottomSheetBinding = ButtomSheetDialogBinding.inflate(inflater, container, false)
                        bottomSheetBinding.apply{
                            pickerContainerMin.visibility = GONE
                            numberPickerSek.apply{
                                maxValue = 100
                                minValue = 1
                                value = (state.stateCardEmom.countDownTime).toInt()
                            }
                            title.text = titleStr

                            Ok.setOnClickListener{
                                dialog?.dismiss()
                                // при нажатии на ок диалога обновляем данные во вью модели
                                viewModel.updateCardEmomCountDownTime(numberPickerSek.value.toLong())
                            }
                            dialog?.setContentView(root)
                            dialog?.show()
                        }



                    }
                    menu2.setOnClickListener{
                        bottomSheetBinding = ButtomSheetDialogBinding.inflate(inflater, container, false)
                        bottomSheetBinding.apply{
                            title.text = titleStr2
                            numberPickerMin.apply{
                                maxValue = 59
                                minValue = 0
                                value = (state.stateCardEmom.timeInterval / 60).toInt()
                            }
                            numberPickerSek.apply{
                                maxValue = 59
                                minValue = 0
                                value = (state.stateCardEmom.timeInterval % 60).toInt()
                            }
                            // Данные отслеживатели необходимы чтобы не дать пользователю установить нулевое значение
                            numberPickerMin.setOnValueChangedListener { _, p1, p2 ->
                                if (p2 == 0) {
                                    if (numberPickerSek.value == 0) {
                                        numberPickerSek.value ++
                                    }
                                }
                            }
                            numberPickerSek.setOnValueChangedListener { _, p1, p2 ->
                                if (p2 == 0) {
                                    if (numberPickerMin.value == 0) {
                                        numberPickerMin.value ++
                                    }
                                }
                            }
                            dialog?.setContentView(root)
                            Ok.setOnClickListener{
                                dialog?.dismiss()
                                // при нажатии на ок обновляем данные во вью модели
                                // необходимо сложить значение двух пиккеров и преобразовать все в секунды
                                val autoendTime = numberPickerMin.value * 60 + numberPickerSek.value
                                viewModel.updateCardEmomAutoEndTime(autoendTime.toLong())
                            }

                            dialog?.show()
                        }

                    }
                    menu3.setOnClickListener{
                        bottomSheetBinding = ButtomSheetDialogBinding.inflate(inflater, container, false)
                        bottomSheetBinding.apply{
                            title.text = titleStr3
                            pickerContainerMin.visibility = GONE
                            pickerContainerSek.visibility = GONE
                            pickerContainerNum.visibility = VISIBLE
                            numberPickerNum.apply{
                                maxValue = 100
                                minValue = 1
                                value = state.stateCardEmom.rounds.toInt()
                            }
                            dialog?.setContentView(root)
                            Ok.setOnClickListener{
                                dialog?.dismiss()
                                viewModel.updateCardEmomCountRounds(numberPickerNum.value.toLong())
                            }

                            dialog?.show()
                        }
                    }
                    btnStart.setOnClickListener{
                        val bundle = Bundle()
                        bundle.putLong("countdownTime", state.stateCardEmom.countDownTime)
                        bundle.putLong("timeInterval", state.stateCardEmom.timeInterval)
                        bundle.putLong("intervals", state.stateCardEmom.rounds)
                        NAV_CONTROLLER.navigate(R.id.timerFragmentEmom, bundle)
                    }
                }
                menuTabata.apply {
                    val titleStr = "Время обратного отчета"
                    val titleStr2 = "Время тренеровки"
                    val titleStr3 = "Время отдыха"
                    val titleStr4 = "Для интрервалов"
                    menu4.visibility = VISIBLE
                    title1.text = titleStr
                    value1.text = state.stateCardTabata.countDownTime.toString() + " секунд"
                    title2.text = titleStr2
                    value2.text = state.stateCardTabata.timeIntervalWork.formateTime()
                    title3.text = titleStr3
                    value3.text = state.stateCardTabata.timeIntervalRest.formateTime()
                    title4.text = titleStr4
                    value4.text = state.stateCardTabata.rounds.toString()

                    menu1.setOnClickListener{
                        bottomSheetBinding = ButtomSheetDialogBinding.inflate(inflater, container, false)
                        bottomSheetBinding.apply{
                            pickerContainerMin.visibility = GONE
                            numberPickerSek.apply{
                                maxValue = 100
                                minValue = 1
                                value = (state.stateCardEmom.countDownTime).toInt()
                            }
                            title.text = titleStr

                            Ok.setOnClickListener{
                                dialog?.dismiss()
                                // при нажатии на ок диалога обновляем данные во вью модели
                                viewModel.updateCardTabataCountDownTime(numberPickerSek.value.toLong())
                            }
                            dialog?.setContentView(root)
                            dialog?.show()
                        }



                    }
                    menu2.setOnClickListener{
                        bottomSheetBinding = ButtomSheetDialogBinding.inflate(inflater, container, false)
                        bottomSheetBinding.apply{
                            numberPickerMin.apply {
                                maxValue = 59
                                minValue = 0
                                value = (state.stateCardTabata.timeIntervalWork / 60).toInt() // Минуты это целочисл деление
                            }
                            numberPickerSek.apply {
                                maxValue = 59
                                minValue = 0
                                value = (state.stateCardTabata.timeIntervalWork % 60).toInt() // Секунды мы получаем из остадка
                            }

                            // Данные отслеживатели необходимы чтобы не дать пользователю установить нулевое значение
                            numberPickerMin.setOnValueChangedListener { _, p1, p2 ->
                                if (p2 == 0) {
                                    if (numberPickerSek.value == 0) {
                                        numberPickerSek.value ++
                                    }
                                }
                            }
                            numberPickerSek.setOnValueChangedListener { _, p1, p2 ->
                                if (p2 == 0) {
                                    if (numberPickerMin.value == 0) {
                                        numberPickerMin.value ++
                                    }
                                }
                            }

                            title.text = titleStr2

                            Ok.setOnClickListener{
                                dialog?.dismiss()
                                // при нажатии на ок диалога обновляем данные во вью модели
                                // необходимо сложить значение двух пиккеров и преобразовать все в секунды
                                val intervalWork = numberPickerMin.value * 60 + numberPickerSek.value
                                viewModel.updateCardTabataTimeIntervalWork(intervalWork.toLong())
                            }

                            dialog?.setContentView(root)
                            dialog?.show()
                        }
                    }
                    menu3.setOnClickListener{
                        bottomSheetBinding = ButtomSheetDialogBinding.inflate(inflater, container, false)
                        bottomSheetBinding.apply{
                            numberPickerMin.apply {
                                maxValue = 59
                                minValue = 0
                                value = (state.stateCardTabata.timeIntervalRest / 60).toInt() // Минуты это целочисл деление
                            }
                            numberPickerSek.apply {
                                maxValue = 59
                                minValue = 0
                                value = (state.stateCardTabata.timeIntervalRest % 60).toInt() // Секунды мы получаем из остадка
                            }

                            // Данные отслеживатели необходимы чтобы не дать пользователю установить нулевое значение
                            numberPickerMin.setOnValueChangedListener { _, p1, p2 ->
                                if (p2 == 0) {
                                    if (numberPickerSek.value == 0) {
                                        numberPickerSek.value ++
                                    }
                                }
                            }
                            numberPickerSek.setOnValueChangedListener { _, p1, p2 ->
                                if (p2 == 0) {
                                    if (numberPickerMin.value == 0) {
                                        numberPickerMin.value ++
                                    }
                                }
                            }

                            title.text = titleStr2

                            Ok.setOnClickListener{
                                dialog?.dismiss()
                                // при нажатии на ок диалога обновляем данные во вью модели
                                // необходимо сложить значение двух пиккеров и преобразовать все в секунды
                                val intervalRest = numberPickerMin.value * 60 + numberPickerSek.value
                                viewModel.updateCardTabataTimeIntervalRest(intervalRest.toLong())
                            }

                            dialog?.setContentView(root)
                            dialog?.show()
                        }
                    }
                    menu4.setOnClickListener{
                        bottomSheetBinding = ButtomSheetDialogBinding.inflate(inflater, container, false)
                        bottomSheetBinding.apply{
                            pickerContainerMin.visibility = GONE
                            numberPickerSek.apply{
                                maxValue = 100
                                minValue = 1
                                value = (state.stateCardTabata.rounds).toInt()
                            }
                            title.text = titleStr

                            Ok.setOnClickListener{
                                dialog?.dismiss()
                                // при нажатии на ок диалога обновляем данные во вью модели
                                viewModel.updateCardTabataRounds(numberPickerSek.value.toLong())
                            }
                            dialog?.setContentView(root)
                            dialog?.show()
                        }
                    }
                }
            }
        }

        binding.apply {
            // Непосредственно добавляем наши меню
            linearForTime.addView(menuForTime.root)
            linearAMRAP.addView(menuAMRAP.root)
            linearEmom.addView(menuEmom.root)
            linearTabata.addView(menuTabata.root)

            // Здесь происходит получение высоты менюшки, а потом его скрытие
            // Это необходимо так как невозможно получить высоту скрытой(GONE) вьюшки
            menuForTime.root.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                        // gets called after layout has been done but before display
                        // so we can get the height then hide the view
                        // вызывается после выполнения макета, но перед отображением
                        // таким образом, мы можем получить высоту, а затем скрыть вид
                        heightChangeValueMenuForTime = menuForTime.root.height
                        menuForTime.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        menuForTime.root.visibility = GONE
                    } })
            menuAMRAP.root.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // gets called after layout has been done but before display
                    // so we can get the height then hide the view
                    // вызывается после выполнения макета, но перед отображением
                    // таким образом, мы можем получить высоту, а затем скрыть вид
                    heightChangeValueMenuAMRAP = menuAMRAP.root.height
                    menuAMRAP.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    menuAMRAP.root.visibility = GONE
                }
            })
            menuEmom.root.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // gets called after layout has been done but before display
                    // so we can get the height then hide the view
                    // вызывается после выполнения макета, но перед отображением
                    // таким образом, мы можем получить высоту, а затем скрыть вид
                    heightChangeValueMenuEmom = menuEmom.root.height
                    menuEmom.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    menuEmom.root.visibility = GONE
                }
            })
            menuTabata.root.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // gets called after layout has been done but before display
                    // so we can get the height then hide the view
                    // вызывается после выполнения макета, но перед отображением
                    // таким образом, мы можем получить высоту, а затем скрыть вид
                    heightChangeValueMenuTabata = menuTabata.root.height
                    menuTabata.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    menuTabata.root.visibility = GONE
                }
            })

            // Устанавливаем клики на блоки(CardView)
            cardViewForTime.setOnClickListener{
                // Проверяем видно ли наше меню если нет значит оно закрыто
                if(menuForTime.root.visibility == VISIBLE){
                    // Закрытие меню
                    increaseViewSize(it, -heightChangeValueMenuForTime) // метод запуска анимации
                    menuForTime.root.visibility = GONE
                }
                else{
                    // Раскрытие меню
                    increaseViewSize(it, heightChangeValueMenuForTime) // метод запуска анимации
                    menuForTime.root.visibility = VISIBLE
                }
            }

            cardViewAMRAP.setOnClickListener {
                // Проверяем видно ли наше меню если нет значит оно закрыто
                if( menuAMRAP.root.visibility == VISIBLE){
                    // Закрытие меню
                    increaseViewSize(it, -heightChangeValueMenuAMRAP) // метод запуска анимации
                    menuAMRAP.root.visibility = GONE
                }
                else{
                    // Раскрытие меню
                    increaseViewSize(it, heightChangeValueMenuAMRAP) // метод запуска анимации
                    menuAMRAP.root.visibility = VISIBLE
                }

            }

            cardViewEmom.setOnClickListener {
                // Проверяем видно ли наше меню если нет значит оно закрыто
                if(menuEmom.root.visibility == VISIBLE){
                    // Закрытие меню
                    increaseViewSize(it, -heightChangeValueMenuEmom) // метод запуска анимации
                    menuEmom.root.visibility = GONE
                }
                else{
                    // Раскрытие меню
                    increaseViewSize(it, heightChangeValueMenuEmom) // метод запуска анимации
                    menuEmom.root.visibility = VISIBLE
                }

            }

            cardViewTabata.setOnClickListener {
                // Проверяем видно ли наше меню если нет значит оно закрыто
                if(menuTabata.root.visibility == VISIBLE){
                    // Закрытие меню
                    increaseViewSize(it, -heightChangeValueMenuTabata) // метод запуска анимации
                    menuTabata.root.visibility = GONE
                }
                else{
                    // Раскрытие меню
                    increaseViewSize(it, heightChangeValueMenuTabata) // метод запуска анимации
                    menuTabata.root.visibility = VISIBLE
                }

            }
        }

        return binding.root

    }

    private fun increaseViewSize(view: View, size: Int) {
        // Запуская анимацию блокируем нажатия чтобы избежать рассинхрона
        view.isClickable = false
        val valueAnimator = ValueAnimator.ofInt(view.measuredHeight, view.measuredHeight+size)
        valueAnimator.duration = 120L // Длительность анимации
        valueAnimator.addUpdateListener {
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.height = animatedValue
            view.layoutParams = layoutParams
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.isClickable = true
                // При завершении анимации разблокируем нажития
            }
        })
        valueAnimator.start()
    }

}