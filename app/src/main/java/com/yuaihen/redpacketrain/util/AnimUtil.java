package com.yuaihen.redpacketrain.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Yuaihen.
 * on 2019/1/23
 */
public class AnimUtil {

    public static ObjectAnimator rotateAnim(View view, int duration, boolean clockwise) {
        ObjectAnimator rotate;
        if (clockwise) {
            rotate = ObjectAnimator.ofFloat(view, "rotation", 0, 360);
        } else {
            rotate = ObjectAnimator.ofFloat(view, "rotation", 0, -360);
        }
        rotate.setDuration(duration);
        rotate.setRepeatCount(ValueAnimator.INFINITE);
        rotate.setRepeatMode(ValueAnimator.RESTART);
        //旋转动画不停顿
        rotate.setInterpolator(new LinearInterpolator());
        rotate.start();
        return rotate;
        //        rotate.cancel();
    }

    public static AnimatorSet scaleShowRedPacketTip(View view) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.3f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.3f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.3f, 1f);
        set.playTogether(scaleX, scaleY, alpha);
        set.setDuration(400);
        set.setInterpolator(new LinearInterpolator());
        set.start();
        return set;
    }

    public static void scaleBlowUp(View view) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f, 0.7f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f, 0.7f);
        set.playTogether(scaleX, scaleY);
        set.setInterpolator(new LinearInterpolator());
        set.setDuration(1000);
        set.start();
    }
}
