package com.yuaihen.redpacketrain.redpacket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuaihen.redpacketrain.util.AnimUtil;
import com.yuaihen.redpacketrain.R;

import java.text.MessageFormat;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Yuaihen.
 * on 2019/1/22
 */
public class RedPacketControlView extends LinearLayout {

    public static final int RED_PACKET_ALL_EMPTY = 0;
    public static final int RED_PACKET_BEFORE = 1;
    public static final int RED_PACKET_YET_GET = 2;
    public static final int RED_PACKET_SCAN_QRCODE = 3;
    @BindView(R.id.red_packet_test)
    RedPacketView mRedPacketView;
    @BindView(R.id.tv_red_packet_title)
    AppCompatTextView mTvRedPacketTitle;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.rl_red_packet_running_tip)
    RelativeLayout mRlRedPacketRunningTip;
    @BindView(R.id.tv_time_count)
    TextView mTvTimeCount;
    @BindView(R.id.rl_count_down)
    RelativeLayout mRlCountDown;
    @BindView(R.id.iv_gold)
    ImageView mIvGold;
    @BindView(R.id.iv_red1)
    ImageView mIvRed1;
    @BindView(R.id.iv_red2)
    ImageView mIvRed2;
    @BindView(R.id.tv_red_packet_count_down_tip)
    TextView mTvRedPacketCountDownTip;
    @BindView(R.id.ll_red_packet_fail)
    LinearLayout mLlGetRedPacketFail;
    @BindView(R.id.ll_red_packet_success)
    LinearLayout mLlGetRedPacketSuccess;
    @BindView(R.id.qrcode_red_packer_view)
    QrcodeRedPkgView mQrcodeRedPackerView;

    private Unbinder mBind;
    private CountDownTimer mCountDownTimer;
    private ObjectAnimator mAnimator1;
    private ObjectAnimator mAnimator2;
    private ObjectAnimator mAnimator3;

    public RedPacketControlView(Context context) {
        this(context, null);
    }

    public RedPacketControlView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedPacketControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_red_packet, this);
        mBind = ButterKnife.bind(this, view);
    }

    public RedPacketView getRedPacketView() {
        return mRedPacketView;
    }

    /**
     * 显示提示语
     * RED_PACKET_ALL_EMPTY
     * RED_PACKET_BEFORE
     * RED_PACKET_YET_GET
     */
    public void showTitle(int index) {
        mRlRedPacketRunningTip.setVisibility(GONE);
        mTvRedPacketTitle.setVisibility(VISIBLE);
        if (index == RED_PACKET_ALL_EMPTY) {
            mTvRedPacketTitle.setText(getResources().getString(R.string.red_packet_empty));
        } else if (index == RED_PACKET_BEFORE) {
            mTvRedPacketTitle.setText(getResources().getString(R.string.red_packet_before));
        } else if (index == RED_PACKET_YET_GET) {
            mTvRedPacketTitle.setText(getResources().getString(R.string.red_packet_after));
        } else if (index == RED_PACKET_SCAN_QRCODE) {
            mTvRedPacketTitle.setText(getResources().getString(R.string.red_packet_scan_qrcode));
        }
    }


    /**
     * 显示提示语：点击掉落的红包 和倒计时开始
     */
    public void start() {
        mLlGetRedPacketFail.setVisibility(GONE);
        mLlGetRedPacketSuccess.setVisibility(GONE);
        mTvRedPacketTitle.setVisibility(GONE);
        mRlRedPacketRunningTip.setVisibility(VISIBLE);
        mTvTimeCount.setVisibility(VISIBLE);
        mRlCountDown.setVisibility(VISIBLE);
        AnimatorSet set = AnimUtil.scaleShowRedPacketTip(mRlCountDown);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //3秒倒计时动画
                mAnimator1 = AnimUtil.rotateAnim(mIvGold, 5000, true);
                mAnimator2 = AnimUtil.rotateAnim(mIvRed1, 5000, false);
                mAnimator3 = AnimUtil.rotateAnim(mIvRed2, 5000, false);
                mCountDownTimer = new CountDownTimer(3 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mTvTimeCount.setText(MessageFormat.format("{0}", millisUntilFinished / 1000));
                        AnimUtil.scaleBlowUp(mTvTimeCount);
                    }

                    @Override
                    public void onFinish() {
                        mRlCountDown.setVisibility(GONE);
                        //开始红包雨  并且倒计时10S
                        startRedPacketRain();
                    }
                }.start();
            }
        });
    }

    /**
     * 开启红包雨
     */
    private void startRedPacketRain() {
        mRedPacketView.startRain();
        mCountDownTimer = new CountDownTimer(10 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTvTime.setText(MessageFormat.format("剩余：{0}s", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mRedPacketView.stopRainNow();
                showQrCodeOrTip();
            }
        }.start();
    }


    /**
     * 显示扫码提取红包或者没有获得红包/提示已经领取过
     */
    private void showQrCodeOrTip() {
        boolean clickRedPacket = mRedPacketView.getClickRedPacket();
        if (clickRedPacket) {
            showQrCodeDialog();
        } else {
            //没有点击过红包  显示没有获得红包界面
            showGetRedPacketFail();
        }
    }


    /**
     * 显示扫码界面
     */
    private void showQrCodeDialog() {
        showTitle(RED_PACKET_SCAN_QRCODE);
        mTvRedPacketCountDownTip.setVisibility(GONE);
        mTvTimeCount.setVisibility(GONE);

        mQrcodeRedPackerView.show();
        mQrcodeRedPackerView.setOnJumpListener(new QrcodeRedPkgView.OnJumpListener() {
            @Override
            public void onJump() {
                mQrcodeRedPackerView.hide();
                showScanSuccess();
            }

            @Override
            public void onTimerOver() {
                mQrcodeRedPackerView.hide();
            }
        });
    }


    /**
     * 扫码成功之后显示领取成功界面
     */
    private void showScanSuccess() {
        mRlCountDown.setVisibility(VISIBLE);

        //显示领取成功界面
        mLlGetRedPacketSuccess.setVisibility(VISIBLE);
        AnimUtil.scaleShowRedPacketTip(mRlCountDown);
    }


    /**
     * 没有点击红包  显示领取失败界面
     */
    private void showGetRedPacketFail() {
        showTitle(RED_PACKET_SCAN_QRCODE);
        mTvRedPacketCountDownTip.setVisibility(GONE);
        mTvTimeCount.setVisibility(GONE);
        mRlCountDown.setVisibility(VISIBLE);

        //显示领取失败界面
        mLlGetRedPacketFail.setVisibility(VISIBLE);
        AnimatorSet set = AnimUtil.scaleShowRedPacketTip(mRlCountDown);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBind != null) {
            mBind.unbind();
        }

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        if (mAnimator1 != null) {
            mAnimator1.cancel();
        }
        if (mAnimator2 != null) {
            mAnimator2.cancel();
        }
        if (mAnimator3 != null) {
            mAnimator3.cancel();
        }

    }
}
