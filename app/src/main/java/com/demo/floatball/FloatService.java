package com.demo.floatball;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class FloatService extends Service {
    private View floatView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private float downX;
    private float downY;
    private int floatBallX;
    private int floatBallY;

    private void addView() {
        floatView = new View(getApplicationContext());
        floatView.setBackgroundResource(R.mipmap.ic_launcher_round);
        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatService.this, "触发了click事件", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                FloatService.this.startActivity(intent);
            }
        });
        floatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = motionEvent.getRawX();
                        downY = motionEvent.getRawY();
                        floatBallX = layoutParams.x;
                        floatBallY = layoutParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        layoutParams.x = (int) (floatBallX + motionEvent.getRawX() - downX);
                        layoutParams.y = (int) (floatBallY + motionEvent.getRawY() - downY);
                        windowManager.updateViewLayout(floatView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(downX - motionEvent.getRawX()) > 5 || Math.abs(downX - motionEvent.getRawX()) > 5) {
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.format = PixelFormat.RGBA_8888;// 背景透明
        layoutParams.width = 150; //设置宽度属性
        layoutParams.height = 150; //设置高度属性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        // 允许将window范围之外的事件传递到底下的应用上
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.addView(floatView, layoutParams);
            windowManager.updateViewLayout(floatView, layoutParams); // 更新相关的属性，使其生效
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addView();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        windowManager.removeView(floatView);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
