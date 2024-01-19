package com.aa.fittracker.presentation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class AnimationHelper {
    public static void centerpieceClick(ImageView view){
        //size values
        float startScale = 1.0f;
        float endScale = 0.95f;
        int animationDuration = 500;
        //ObjectAnimators
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view,"scaleX",startScale,endScale);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view,"scaleY",startScale,endScale);
        //interpolation's
        scaleDownX.setInterpolator(new DecelerateInterpolator());
        scaleDownY.setInterpolator(new DecelerateInterpolator());
        //set duration
        scaleDownX.setDuration(animationDuration);
        scaleDownY.setDuration(animationDuration);
        //COMBINE INTO 1 ANIMATOR
        AnimatorSet scale = new AnimatorSet();
        scale.play(scaleDownX).with(scaleDownY);
        //scaled down
        scale.start();
        //SCALE BACK UP
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view,"scaleX",endScale,startScale);
        ObjectAnimator scaleUpY =ObjectAnimator.ofFloat(view,"scaleY",endScale,startScale);

        scaleUpX.setInterpolator(new DecelerateInterpolator());
        scaleUpX.setDuration(animationDuration);
        scaleUpY.setInterpolator(new DecelerateInterpolator());
        scaleUpY.setDuration(animationDuration);
        AnimatorSet up = new AnimatorSet();
        up.play(scaleUpX).with(scaleUpY);
        up.start();
    }
    public static void shakeView(View view) {
        // Create ObjectAnimators for translationX
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "translationX", -20);
        anim1.setDuration(100);

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "translationX", 20);
        anim2.setDuration(100);

        ObjectAnimator anim3 = ObjectAnimator.ofFloat(view, "translationX", -10);
        anim3.setDuration(100);

        // Create an AnimatorSet to play the animations sequentially
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(anim1, anim2, anim3);
        animatorSet.start();
    }
    public static void fadeIn(ImageView element){
        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(element,View.ALPHA,0f,1f);
        fadeAnimator.setDuration(1500);
        fadeAnimator.setStartDelay(500);
        fadeAnimator.start();
    }
    public static void fadeUsername(View view){
        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(view,View.ALPHA,0f,1f);
        fadeAnimator.setDuration(1000);
        fadeAnimator.start();
    }
}
