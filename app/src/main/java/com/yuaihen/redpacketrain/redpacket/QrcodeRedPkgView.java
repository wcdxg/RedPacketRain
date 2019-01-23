package com.yuaihen.redpacketrain.redpacket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuaihen.redpacketrain.R;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Yuaihen.
 * on 2019/1/21
 * 扫码界面
 */
public class QrcodeRedPkgView extends RelativeLayout {

    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.tv_jump_timer)
    TextView tvJumpTimer;
    @BindView(R.id.iv_firework_small)
    ImageView ivFireworkSmall;
    @BindView(R.id.iv_firework_big)
    ImageView ivFireworkBig;
    private CountDownTimer timer;

    public QrcodeRedPkgView(Context context) {
        this(context, null);
    }

    public QrcodeRedPkgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QrcodeRedPkgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public interface OnJumpListener {
        void onJump();

        void onTimerOver();
    }

    private OnJumpListener onJumpListener;

    public void setOnJumpListener(OnJumpListener onJumpListener) {
        this.onJumpListener = onJumpListener;
    }


    private void init() {
        View view = View.inflate(getContext(), R.layout.layout_scan_red_pkg, this);
        ButterKnife.bind(view);

        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvJumpTimer.setText(MessageFormat.format("跳过（{0}s）", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (onJumpListener != null) {
                    onJumpListener.onTimerOver();
                }
            }
        };

        ivFireworkBig.setVisibility(View.INVISIBLE);
        ivFireworkSmall.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (timer != null) {
            timer.cancel();
        }
    }


    @OnClick(R.id.btn_jump_over)
    public void onViewClicked() {
        if (onJumpListener != null) {
            onJumpListener.onJump();
        }
    }

    /**
     * 以动画效果显示
     */
    public void show() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 0, 1);
        AnimatorSet anim = new AnimatorSet();
        scaleX.setInterpolator(new OvershootInterpolator(1.2f));
        scaleY.setInterpolator(new OvershootInterpolator(1.2f));
        anim.setDuration(1400);
        anim.playTogether(scaleX, scaleY);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startFireWorkAnim(ivFireworkBig, 400);
                startFireWorkAnim(ivFireworkSmall, 700);
                timer.start();
            }
        });
        anim.start();
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1, 0);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1, 0);
        AnimatorSet anim = new AnimatorSet();
        scaleX.setInterpolator(new AccelerateInterpolator());
        scaleY.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(1500);
        anim.playTogether(scaleX, scaleY);
        anim.start();

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (timer != null) {
                    timer.cancel();
                }
            }
        });


    }

    /**
     * 烟花
     */
    private void startFireWorkAnim(final View fireworkView, long delay) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(fireworkView, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(fireworkView, "scaleY", 0, 1);
        scaleX.setInterpolator(new DecelerateInterpolator());
        scaleY.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(fireworkView, "scaleX", 1, 0.8f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(fireworkView, "scaleY", 1, 0.8f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(fireworkView, "alpha", 1, 0);

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(scaleX, scaleY);
        scaleUp.setDuration(1800);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(scaleDownX, scaleDownY, alpha);
        scaleDown.setDuration(2000);

        AnimatorSet anim = new AnimatorSet();
        anim.playSequentially(scaleUp, scaleDown);
        anim.setStartDelay(delay);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                fireworkView.setVisibility(View.VISIBLE);
                fireworkView.setAlpha(1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fireworkView.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    /**
     * 设置红包金额
     */
    @SuppressLint("DefaultLocale")
    public void setMoney(float money) {
        tvMoney.setText(String.format("%.2f", money));
    }


    /**
     * 设置二维码链接
     */
    public void setQrcode(String qrcodeUrl) {
        //        ivQrcode.setImageBitmap();
    }

}
