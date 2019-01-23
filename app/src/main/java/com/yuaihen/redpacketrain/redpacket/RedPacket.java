package com.yuaihen.redpacketrain.redpacket;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.Random;

/**
 * Created by Yuaihen.
 * on 2019/1/21
 */
public class RedPacket {

    public float x, y;
    public float rotation;
    public float speed;
    public float rotationSpeed;
    public int redPacketWidth, redPacketHeight;
    public Bitmap bitmap;
    public int money;
    public boolean isRealRed;
    public int index;

    public RedPacket(Context context, Bitmap originalBitmap, int speed, float maxSize, float minSize, int index) {
        //获取一个显示红包大小的倍数
        double widthRandom = Math.random();
        if (widthRandom < minSize || widthRandom > maxSize) {
            widthRandom = maxSize;
        }
        //红包的宽度
        redPacketWidth = (int) (originalBitmap.getWidth() * widthRandom);
        //红包的高度
        redPacketHeight = redPacketWidth * originalBitmap.getHeight() / originalBitmap.getWidth();

        int mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;

        //生成红包bitmap
        bitmap = Bitmap.createScaledBitmap(originalBitmap, redPacketWidth, redPacketHeight, true);
        originalBitmap.recycle();
        Random random = new Random();
        //红包起始位置x:[0,mWidth-redPacketWidth]
        int rx = random.nextInt(mScreenWidth) - redPacketWidth;
        x = rx <= 0 ? 0 : rx;
        //红包起始位置y   在(-getHeight(),0] 之间，即一开始位于屏幕上方以外
        y = -random.nextInt(mScreenHeight);
        //初始化该红包的下落速度
        this.speed = speed + (float) Math.random() * 1000;
        //初始化该红包的初始旋转角度
        //            rotation = (float) Math.random() * 180 - 90;
        //初始化该红包的旋转速度
        //            rotationSpeed = (float) Math.random() * 90 - 45;
        //初始化是否为中奖红包
        this.index = index;
        if (index == 1 || index == 3) {
            //是红包
            isRealRed = true;
        } else {
            //是金币
            isRealRed = false;
        }

    }

    /**
     * 判断当前点是否包含在区域内
     */
    public boolean isContains(float x, float y) {
        //稍微扩大下点击的区域
        return this.x - 50 < x && this.x + 50 + redPacketWidth > x
                && this.y - 50 < y && this.y + 50 + redPacketHeight > y;
    }

    /**
     * 随机 是否为中奖红包
     */
    public boolean isRealRedPacket() {
        return isRealRed;
        //        Random random = new Random();
        //        int num = random.nextInt(10) + 1;
        //        //如果[1,10]随机出的数字是2的倍数 为中奖红包
        //        if (num % 2 == 0) {
        //            money = num * 2;//中奖金额
        //            return true;
        //        }
        //        return false;
    }

    /**
     * 回收图片
     */
    public void recycle() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}

