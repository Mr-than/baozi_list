package com.example.baozi_list.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.baozi_list.base.APP
import com.example.baozi_list.bean.Bean
import com.example.baozi_list.bean.Car
import com.example.baozi_list.bean.Disinfection
import com.example.baozi_list.databinding.FragmentDisinfectionUpdateLayoutBinding
import com.example.baozi_list.databinding.FragmentUpdateLayoutBinding
import com.example.baozi_list.util.getDate

class UpdateDialog(val bean: Bean?): DialogFragment() {

    private val viewModel:MainViewModel by lazy { ViewModelProvider(requireActivity())[MainViewModel::class.java] }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val window = dialog?.window
        window?.decorView?.setPadding(0, 0, 0, 0)

        val attributes = window?.attributes

        val display = requireActivity().windowManager.defaultDisplay

        val outSize = Point()
        display.getSize(outSize) //不能省略,必须有
        val height = outSize.y //得到屏幕的高度

        attributes?.run {
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            attributes.height = height-200
            attributes.gravity = Gravity.CENTER
            window.setAttributes(attributes)

        }
        val binding:ViewBinding= when(APP.listType){
            APP.CAR->{
                val binding=FragmentUpdateLayoutBinding.inflate(inflater,container,false)
                val car=bean as? Car
                if (car != null) {
                    binding.updateTrainEdit.setText(car.backTrain)
                    binding.updateTrainInTimeEdit .setText(car.inTime)
                    binding.updateTrainOutTimeEdit.setText(car.outTime)
                    binding.updateTrainLaneEdit .setText(car.pickUpLane)
                    binding.updateTrainNumberEdit.setText(car.trainNumber)

                    binding.updateFirm.setOnClickListener {
                        val c = Car(
                            id = car.id,
                            trainNumber = binding.updateTrainNumberEdit.text.toString(),
                            backTrain = binding.updateTrainEdit.text.toString(),
                            inTime = binding.updateTrainInTimeEdit.text.toString(),
                            outTime = binding.updateTrainOutTimeEdit.text.toString(),
                            pickUpLane = binding.updateTrainLaneEdit.text.toString(),
                            addTime = car.addTime,
                            foldDetail = car.foldDetail,
                            isDetail = car.isDetail,
                            isTime = car.isTime,
                            isTail = car.isTail,
                            isFinish = car.isFinish
                        )

                        viewModel.updateCar(c)
                        dismiss()
                    }
                }else{
                    binding.updateFirm.setOnClickListener {
                        val number=binding.updateTrainNumberEdit.text.toString()
                        val lane=binding.updateTrainLaneEdit.text.toString()

                        if(number.isEmpty()||lane.isEmpty()){
                            Toast.makeText(requireContext(),"车号和股道不能为空",Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        val c = Car(
                            trainNumber = number,
                            backTrain = binding.updateTrainEdit.text.toString(),
                            inTime = binding.updateTrainInTimeEdit.text.toString(),
                            outTime = binding.updateTrainOutTimeEdit.text.toString(),
                            pickUpLane = lane,
                            addTime = getDate(),
                            foldDetail = false,
                            isDetail = false,
                            isTime = false,
                            isTail = false,
                            isFinish = false
                        )
                        viewModel.insertCar(c)
                        dismiss()
                    }
                }
                binding.updateCancel.setOnClickListener {
                    dismiss()
                }
                binding
            }
            APP.DI->{
                val binding=FragmentDisinfectionUpdateLayoutBinding.inflate(inflater,container,false)
                val disinfection=bean as? Disinfection

                if(disinfection!=null){
                    binding.updateDisinfectionEdit.setText(disinfection.carNumber)

                    binding.disinfectionUpdateFirm.setOnClickListener {

                        val s=binding.updateDisinfectionEdit.text.toString()
                        if(s.isEmpty()){
                            Toast.makeText(requireContext(),"不能为空",Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        val bean=Disinfection(
                            disinfection.id,
                            disinfection.date,
                            s,
                            disinfection.isFinish,
                            disinfection.isTail,
                            disinfection.isHead
                        )
                        viewModel.updateDisinfection(bean)
                        dismiss()
                    }
                }else{
                    binding.disinfectionUpdateFirm.setOnClickListener {

                        val s=binding.updateDisinfectionEdit.text.toString()
                        if(s.isEmpty()){
                            Toast.makeText(requireContext(),"不能为空",Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        val bean=Disinfection(
                            0,
                            getDate(),
                            s,
                            isFinish = false,
                            isTail = false,
                            isHead = false
                        )
                        viewModel.insertDisinfection(bean)
                        dismiss()
                    }
                }

                binding.disinfectionUpdateCancel .setOnClickListener {
                    dismiss()
                }

                binding
            }
            else->{
                val binding=FragmentUpdateLayoutBinding.inflate(inflater,container,false)
                binding
            }
        }




        binding.root.setOnTouchListener { _, event ->
            if(isSoftShowing()){
                if (event.action==MotionEvent.ACTION_DOWN){
                    val imm=requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    when(binding){
                        is FragmentUpdateLayoutBinding->{
                            imm.hideSoftInputFromWindow(binding.updateTrainEdit .windowToken,0)
                            imm.hideSoftInputFromWindow(binding.updateTrainLaneEdit .windowToken,1)
                            imm.hideSoftInputFromWindow(binding.updateTrainInTimeEdit .windowToken,2)
                            imm.hideSoftInputFromWindow(binding.updateTrainOutTimeEdit .windowToken,3)
                            imm.hideSoftInputFromWindow(binding.updateTrainNumberEdit .windowToken,4)
                        }
                        is FragmentDisinfectionUpdateLayoutBinding->{
                            imm.hideSoftInputFromWindow(binding.updateDisinfectionEdit .windowToken,0)
                        }
                    }

                }

                return@setOnTouchListener true
            }else{
                return@setOnTouchListener false
            }


        }

        return binding.root
    }


    private fun isSoftShowing(): Boolean {
        //获取当前屏幕内容的高度
        val screenHeight: Int = (requireContext() as MainActivity).window.decorView.height
        //获取View可见区域的bottom
        val rect = Rect()
        (requireContext() as MainActivity).window.decorView.getWindowVisibleDisplayFrame(rect)

        return screenHeight - rect.bottom != 0
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        viewModel.getInitCars()
    }

}