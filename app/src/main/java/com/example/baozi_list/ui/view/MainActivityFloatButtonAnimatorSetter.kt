package com.example.baozi_list.ui.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import com.example.baozi_list.util.dp2px
import kotlin.math.sqrt

class MainActivityFloatButtonAnimatorSetter(
    private val resources: Resources,
    private val hostFloatButton: View,
    private vararg val views: View
) {
    private var isAnimated = false
    private val animatorInitY by lazy { views[0].y }
    private val animatorInitX by lazy { views[0].x }
    private val displayMetrics by lazy { resources.displayMetrics }
    private val screenWidth by lazy { displayMetrics.widthPixels }

    init {
        views[0].post {
            animatorInitX
            animatorInitY
        }
    }


    fun startAnimation() {
        if (!isAnimated) {

            for (view in views) {
                view.x = hostFloatButton.x
                view.y = hostFloatButton.y
            }

            showButtons()
        } else {
            hideButtons()
        }
        isAnimated = !isAnimated
    }


    private fun showButtons() {

        val moveX1 = if (hostFloatButton.x > screenWidth / 2) {
            -dp2px(resources, 90f).toFloat()
        } else {
            hostFloatButton.x - animatorInitX + dp2px(resources, 90f).toFloat()
        }

        val moveX2 = if (hostFloatButton.x > screenWidth / 2) {
            -(dp2px(resources, 90f).toFloat() / sqrt(2f))
        } else {
            (dp2px(resources, 90f).toFloat() / sqrt(2f)) + hostFloatButton.x - animatorInitX
        }


        // 显示按钮
        for (view in views) {
            view.visibility = View.VISIBLE
        }


        // 创建显示动画
        val anim1 = createShowAnimator(views[0], moveX1, hostFloatButton.y - animatorInitY)

        val anim2 = createShowAnimator(
            views[1],
            moveX2,
            hostFloatButton.y - animatorInitY + (-dp2px(resources, 90f).toFloat() / sqrt(2f))
        )
        val anim3 = createShowAnimator(
            views[2],
            moveX2,
            (dp2px(resources, 90f).toFloat() / sqrt(2f)) + hostFloatButton.y - animatorInitY
        )
        // 同时显示
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(anim1, anim2, anim3)
        animatorSet.start()
    }

    private fun hideButtons() {


        val moveX = if (hostFloatButton.x > screenWidth / 2) {
            0f
        } else {
            hostFloatButton.x - animatorInitX
        }


        // 创建隐藏动画
        val anim1 = createHideAnimator(views[0], moveX, hostFloatButton.y - animatorInitY)
        val anim2 =
            createHideAnimator(views[1], moveX, hostFloatButton.y - animatorInitY)
        val anim3 =
            createHideAnimator(views[2], moveX, hostFloatButton.y - animatorInitY)

        // 同时隐藏
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(anim1, anim2, anim3)
        animatorSet.start()

        animatorSet.doOnEnd {
            // 隐藏按钮
            for (view in views) {
                view.visibility = View.INVISIBLE
            }
        }
    }

    private fun createShowAnimator(
        view: View,
        translationX: Float,
        translationY: Float
    ): AnimatorSet {
        val duration = 100L // 动画持续时间
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f).setDuration(duration)
        val scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f).setDuration(duration)
        val translationXAnimator =
            ObjectAnimator.ofFloat(view, "translationX", translationX).setDuration(duration)
        val translationYAnimator =
            ObjectAnimator.ofFloat(view, "translationY", translationY).setDuration(duration)

        // 插值器
        scaleX.interpolator = AccelerateDecelerateInterpolator()
        scaleYAnimator.interpolator = AccelerateDecelerateInterpolator()
        translationXAnimator.interpolator = AccelerateDecelerateInterpolator()
        translationYAnimator.interpolator = AccelerateDecelerateInterpolator()

        // 合并动画
        val animatorSet = AnimatorSet()
        animatorSet.play(scaleX).with(scaleYAnimator).with(translationXAnimator)
            .with(translationYAnimator)
        return animatorSet
    }

    private fun createHideAnimator(
        view: View,
        translationX: Float,
        translationY: Float,
    ): AnimatorSet {
        val duration = 100L // 动画持续时间
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f).setDuration(duration) // 从原始大小到0
        val scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f).setDuration(duration)
        val translationXAnimator =
            ObjectAnimator.ofFloat(view, "translationX", translationX).setDuration(duration)
        val translationYAnimator =
            ObjectAnimator.ofFloat(view, "translationY", translationY).setDuration(duration)

        // 插值器
        scaleX.interpolator = AccelerateDecelerateInterpolator()
        scaleYAnimator.interpolator = AccelerateDecelerateInterpolator()
        translationXAnimator.interpolator = AccelerateDecelerateInterpolator()
        translationYAnimator.interpolator = AccelerateDecelerateInterpolator()

        // 合并动画
        val animatorSet = AnimatorSet()
        animatorSet.play(scaleX).with(scaleYAnimator).with(translationXAnimator)
            .with(translationYAnimator)
        return animatorSet
    }

    fun closeButton() {
        if (isAnimated) {
            hideButtons()
            isAnimated = !isAnimated
        }
    }


}