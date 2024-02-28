package com.sdapps.entres.main.login.view

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.RelativeLayout

object SlideupTransition {

    fun slideUp(view: View, duration: Long){
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        animation.duration = duration
        animation.fillAfter = false
        view.startAnimation(animation)
        view.visibility = View.VISIBLE
    }

    fun slideDownFadeIn(view: View, duration : Long) {
        val animate = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f
        )
        animate.duration = duration
        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = duration




        val animationSet = AnimationSet(true)
        animationSet.addAnimation(animate)
        animationSet.addAnimation(fadeInAnimation)


        animationSet.fillAfter  = true
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        view.startAnimation(animationSet)
    }
}