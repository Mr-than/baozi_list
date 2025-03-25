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
import com.example.baozi_list.base.APP
import com.example.baozi_list.bean.Car
import com.example.baozi_list.bean.Disinfection
import com.example.baozi_list.databinding.FragmentAddLayoutBinding
import com.example.baozi_list.util.WxScanUtil


class AddDialog : DialogFragment() {

    private val dataList = ArrayList<Car>()
    private val disinfectionDataList = ArrayList<Disinfection>()
    private val viewModel: MainViewModel by lazy { ViewModelProvider(requireActivity())[MainViewModel::class.java] }

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
            attributes.height = height - 200
            attributes.gravity = Gravity.CENTER
            window.setAttributes(attributes)

        }
        val binding = FragmentAddLayoutBinding.inflate(inflater, container, false)


        binding.fragmentAddSingleButton.setOnClickListener {
            viewModel.startUpdate()
            dismiss()
        }


        binding.fragmentAddClearButton.setOnClickListener {
            binding.fragmentAddEditText.setText("")
        }

        binding.fragmentAddParseButton.setOnClickListener {
            val text = binding.fragmentAddEditText.text.toString()
            if (text.isEmpty()) {
                Toast.makeText(requireContext(), "请输入内容", Toast.LENGTH_SHORT).show()
            } else {

                when (APP.listType) {
                    APP.CAR -> {
                        val list = WxScanUtil.parseWxString(text)
                        dataList.clear()
                        dataList.addAll(list)
                        var txt = ""
                        for (car in dataList) {
                            txt += car.toString() + "\n"
                        }
                        binding.fragmentAddTextView.text = txt
                    }

                    APP.DI -> {
                        val list = WxScanUtil.parseDisinfectionString(text)
                        disinfectionDataList.clear()
                        disinfectionDataList.addAll(list)
                        var txt = ""
                        for (car in disinfectionDataList) {
                            txt += car.toString() + "\n"
                        }
                        binding.fragmentAddTextView.text = txt
                    }
                }


            }
        }

        binding.root.setOnTouchListener { _, event ->

            if (isSoftShowing()) {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.fragmentAddEditText.windowToken, 0)
                }
                return@setOnTouchListener true
            } else {
                return@setOnTouchListener false
            }


        }

        binding.fragmentAddFirmButton.setOnClickListener {

            when (APP.listType) {
                APP.CAR -> {
                    if (dataList.isEmpty()) {
                        Toast.makeText(requireContext(), "请先解析数据", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.insertCars(dataList)
                        dismiss()
                    }
                }

                APP.DI -> {
                    if (disinfectionDataList.isEmpty()) {
                        Toast.makeText(requireContext(), "请先解析数据", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.insertDisinfections(disinfectionDataList)
                        dismiss()
                    }
                }
            }


        }

        binding.recyclerView.setActivity(requireContext() as MainActivity)
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