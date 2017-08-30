package com.lincolnwdaniel.ltrain.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

/**
 * Created by lwdthe1 on 8/19/2015.
 */
public class LView {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static View drawCircularBackground(Context context, View view, String bgColor, int width, int height){
        Paint paint = new Paint();
        paint.setColor(Color.parseColor(bgColor));
        Bitmap bg = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        canvas.drawCircle(width, height, 100, paint);

        view.setBackgroundDrawable(new BitmapDrawable(bg));

        return view;
    }

    public static class CircleView<T> extends View {
        String colorHex;
        public CircleView(Context context, String colorHex) {
            super(context);
            this.colorHex = colorHex;
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
            int x = getWidth();
            int y = getHeight();
            int radius;
            radius = 100;
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#16a085"));
            canvas.drawCircle(x / 2, y / 2, radius, paint);
        }


        public View draw() {
            super.draw(new Canvas());
            return this;
        }
    }

    public static void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
