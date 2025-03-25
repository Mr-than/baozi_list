package com.example.baozi_list.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baozi_list.R
import com.example.baozi_list.base.APP
import com.example.baozi_list.base.BaseBindingWithVMActivity
import com.example.baozi_list.bean.Car
import com.example.baozi_list.bean.CarManager
import com.example.baozi_list.bean.Disinfection
import com.example.baozi_list.databinding.ActivityMainBinding
import com.example.baozi_list.ui.adapter.DisinfectionAdapter
import com.example.baozi_list.ui.adapter.HomeCarAdapter
import com.example.baozi_list.ui.adapter.TopRvAdapter
import com.example.baozi_list.ui.view.DeleteConstraintLayout
import com.example.baozi_list.ui.view.LogLayoutManager
import com.example.baozi_list.ui.view.MainActivityFloatButtonAnimatorSetter
import com.example.baozi_list.ui.view.TextDrawable
import com.google.android.material.internal.TextDrawableHelper


class MainActivity : BaseBindingWithVMActivity<MainViewModel, ActivityMainBinding>() {


    private val carDrawable = TextDrawable("车")
    private val disinfectionDrawable = TextDrawable("消毒")
    private val cleanDrawable = TextDrawable("清洁")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        liveDataObserve()
        viewModel.getInitCars()
        initView()
        initTop()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun liveDataObserve() {

        val carAdapter = HomeCarAdapter(binding.activityMainRecyclerView, 0, viewModel = viewModel)
        val disinfectionAdapter= DisinfectionAdapter(0,viewModel,0,binding.activityMainRecyclerView)



        val layoutManager = LinearLayoutManager(this)
        binding.activityMainRecyclerView.adapter = carAdapter
        binding.activityMainRecyclerView.layoutManager = layoutManager
        binding.activityMainRecyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                val f = layoutManager.findFirstVisibleItemPosition()
                val l = layoutManager.findLastVisibleItemPosition()
                for (i in f until l) {
                    val view = binding.activityMainRecyclerView.getChildAt(i)
                    (view as? DeleteConstraintLayout)?.recover()
                }
            }
            false
        }





        viewModel.carsLiveData.observe(this) {
            val finalList = ArrayList<Car>()
            var time = ""
            for (i in 0 until it.size) {
                if (it[i].addTime != time) {
                    finalList.add(
                        CarManager.getTimeBean(it[i].addTime)
                    )
                    finalList.add(it[i])
                    time = it[i].addTime
                } else {
                    finalList.add(it[i])
                }
            }
            binding.activityMainRecyclerView.removeAllViews()
            binding.activityMainRecyclerView.removeAllViewsInLayout()
            binding.activityMainRecyclerView.recycledViewPool.clear()
            binding.activityMainRecyclerView.swapAdapter(carAdapter,false)
            carAdapter.refresh(finalList)

            binding.fragmentListFab.overlay.clear()
            binding.fragmentListFab.overlay.add(carDrawable)
        }


        viewModel.disinfectionLveData.observe(this) {
            val finalList = ArrayList<Disinfection>()
            var time = ""
            for (i in it.indices) {
                if (it[i].date != time) {
                    finalList.add(
                        Disinfection(0, it[i].date, "", isFinish = false, isTail = false, isHead = true)
                    )
                    finalList.add(it[i])
                    time = it[i].date
                } else {
                    finalList.add(it[i])
                }
            }
            binding.activityMainRecyclerView.removeAllViews()
            binding.activityMainRecyclerView.removeAllViewsInLayout()
            binding.activityMainRecyclerView.recycledViewPool.clear()
            binding.activityMainRecyclerView.swapAdapter(disinfectionAdapter,false)
            disinfectionAdapter.refresh(finalList)
            binding.fragmentListFab.overlay.clear()
            binding.fragmentListFab.overlay.add(disinfectionDrawable)
        }

        viewModel.singleCarLiveData.observe(this) {
            val log = UpdateDialog(null)
            log.show(this.supportFragmentManager, "")
        }


    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initTop() {
        binding.topRecyclerView.adapter = TopRvAdapter(40)
        binding.topRecyclerView.layoutManager = LogLayoutManager(this)
    }

    private fun initView() {

        binding.fragmentListFab.post {
            carDrawable.setBounds(0, 0, binding.fragmentListFab.width, binding.fragmentListFab.height)
            disinfectionDrawable.setBounds(0, 0, binding.fragmentListFab.width, binding.fragmentListFab.height)
            cleanDrawable.setBounds(0, 0, binding.fragmentListFab.width, binding.fragmentListFab.height)
        }

        binding.activityMainScrollDownView.setTopView(R.id.fragment_test_fl.findView())

        binding.activityMainSearch.setOnClickListener {
            val intent = Intent(
                this@MainActivity,
                SearchActivity::class.java
            )
            val pair1: androidx.core.util.Pair<View, String> =
                androidx.core.util.Pair(binding.activityMainSearch as View, "shared")
            val pair2: androidx.core.util.Pair<View, String> =
                androidx.core.util.Pair(binding.activityMainToolbar as View, "sharedImage")
            val activityOptionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, pair1, pair2)
            // 启动目标Activity
            startActivity(intent, activityOptionsCompat.toBundle())
        }

        val setter = MainActivityFloatButtonAnimatorSetter(
            resources,
            binding.fragmentListFab,
            binding.carList,
            binding.cleaningList,
            binding.disinfectionList
        )
        binding.fragmentListFab.setAnimatorSetter(setter)

        binding.fragmentListFab.setOnClickListener {
            setter.startAnimation()
        }


        binding.fragmentListFab.setOnLongClickListener {
            val log = AddDialog()
            log.show(this.supportFragmentManager, "")
            false
        }

        binding.carList.setOnClickListener {
            APP.listType=APP.CAR
            viewModel.getInitCars()
            //Toast.makeText(this, "carList", Toast.LENGTH_SHORT).show()
        }

        binding.cleaningList.setOnClickListener {
            // TODO: 填充清洁数据
            //Toast.makeText(this, "cleaningList", Toast.LENGTH_SHORT).show()
        }

        binding.disinfectionList.setOnClickListener {
            APP.listType=APP.DI
            viewModel.getInitCars()
        }
    }


    override fun onStart() {
        super.onStart()
        viewModel.getInitCars()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v: View? = currentFocus
        v?.let {
            if (isShouldHideInput(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (window.superDispatchTouchEvent(ev)) {
            return true
        }
        return onTouchEvent(ev)
    }

    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && (v is EditText)) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (binding.activityMainScrollDownView.isDown) {
                binding.activityMainScrollDownView.backDown()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}