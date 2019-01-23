package com.yuaihen.redpacketrain;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yuaihen.redpacketrain.redpacket.RedPacket;
import com.yuaihen.redpacketrain.redpacket.RedPacketControlView;
import com.yuaihen.redpacketrain.redpacket.RedPacketView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.rl_container)
    RelativeLayout mRlContainer;
    @BindView(R.id.red_packet_view)
    RedPacketControlView mRedPacketControlView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_fireworks);

        mBtnStart.setOnClickListener(v -> mRedPacketControlView.start());

        mRedPacketControlView.getRedPacketView().setOnRedPacketClickListener(new RedPacketView.OnRedPacketClickListener() {
            @Override
            public void onRedPacketClickListener(RedPacket redPacket, float oldY) {
                final ImageView iv = new ImageView(MainActivity.this);
                mRlContainer.addView(iv);
                iv.setImageBitmap(bitmap);
                iv.setX(redPacket.x);
                iv.setY(oldY);
                AnimatorSet set = new AnimatorSet();
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv, "scaleY", 0, 1.2f);
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv, "scaleX", 0, 1.2f);
                set.playTogether(scaleX, scaleY);
                set.setInterpolator(new DecelerateInterpolator());
                set.setDuration(500);
                set.start();

                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mRlContainer.removeView(iv);
                    }
                });
            }
        });
    }
}
