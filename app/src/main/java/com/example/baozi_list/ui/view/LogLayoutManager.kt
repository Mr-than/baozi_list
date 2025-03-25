package com.example.baozi_list.ui.view

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.baozi_list.R
import kotlin.math.abs
import kotlin.math.min


class LogLayoutManager(context: Context) : RecyclerView.LayoutManager() {


    private val screenWidth: Int
    private var mHorizontalOffset: Long = 0
    private var arriveEnd = false
    private val list= listOf(R.drawable.color_1,R.drawable.color_2,R.drawable.color_3,R.drawable.color_4)

    init {
        // 获取屏幕的宽高
        val metrics = context.resources.displayMetrics
        screenWidth = metrics.widthPixels
    }


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    private fun getScale(x: Float, offset: Float): Float {
        val t = 1f - (0.05f / offset) * x
        if (t > 1f) {
            return 1f
        }
        return t
    }

    private fun getRotation(x: Float, offset: Float): Float {
        val t = -4 - (4f / offset) * x
        if (t > 0) {
            return 0f
        }
        return t
    }


    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            return
        }
        if (childCount == 0 && state.itemCount > 0) {
            fill(recycler, 0)
        }
    }

    private fun fill(recycler: RecyclerView.Recycler, dx: Int) {
        val offset = screenWidth / 6.0f
        var offsetX = offset - screenWidth - mHorizontalOffset
        val startItem = if (mHorizontalOffset > 0) (mHorizontalOffset / offset).toInt() else 0
        offsetX += offset * startItem
        val endItem = min(startItem + (screenWidth / offset).toInt() + 4, itemCount)
        recycleChildren(recycler, dx, startItem, endItem)
        detachAndScrapAttachedViews(recycler)
        for (i in startItem until endItem) {
            val itemView = recycler.getViewForPosition(i)
            addView(itemView)
            measureChildWithMargins(itemView, 0, 0)
            val width = getDecoratedMeasuredWidth(itemView)
            val height = getDecoratedMeasuredHeight(itemView)
            layoutDecorated(itemView, offsetX.toInt(), 0, width + offsetX.toInt(), height)
            offsetX += width / 6

            itemView.translationZ = (itemCount + 1.toFloat() * 10 - i)
            val p = itemView.x + itemView.width - offset
            val scale = getScale(p, offset)
            val rotation = getRotation(p, offset)
            itemView.scaleX = scale
            itemView.scaleY = scale
            itemView.rotation = rotation
            itemView.background= itemView.resources.getDrawable(list[i%4],null)
            itemView.alpha=0.95f
        }
    }


    private fun recycleChildren(
        recycler: RecyclerView.Recycler,
        dx: Int,
        startItem: Int,
        endItem: Int
    ) {
        // 用于跟踪需要回收的视图
        val scrapList = mutableListOf<View>()
        if (dx > 0) {
            for (i in 0 until childCount) {
                val child = getChildAt(i) ?: continue
                val position = getPosition(child)
                if (position < startItem) {
                    scrapList.add(child)
                }
            }
        } else if (dx < 0) {
            for (i in 0 until childCount) {
                val child = getChildAt(i) ?: continue
                val position = getPosition(child)
                if (position >= endItem) {
                    scrapList.add(child)
                }
            }
        }
        for (child in scrapList) {
            removeAndRecycleView(child, recycler)
        }
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        if (dx == 0 || childCount == 0) {
            return 0
        }
        val realDx = dx / 1.0f
        if (abs(realDx) < 0.00000001f) {
            return 0
        }
        mHorizontalOffset += dx
        if (dx < 0 && mHorizontalOffset < 0) {
            mHorizontalOffset = 0
        }
        if (dx < 0 && arriveEnd) {
            mHorizontalOffset =
                (itemCount * screenWidth / 6 - (screenWidth / (screenWidth / 6)) * (screenWidth / 6)).toLong()
            arriveEnd = false
        }
        if (mHorizontalOffset > itemCount * screenWidth / 6 - (screenWidth / (screenWidth / 6)) * (screenWidth / 6)) {
            mHorizontalOffset =
                (itemCount * screenWidth / 6 - (screenWidth / (screenWidth / 6)) * (screenWidth / 6)).toLong() + 1
            arriveEnd = true
            fill(recycler, dx)
            return 0
        }
        fill(recycler, dx)
        return dx
    }


    override fun canScrollHorizontally(): Boolean {
        return true
    }
}
