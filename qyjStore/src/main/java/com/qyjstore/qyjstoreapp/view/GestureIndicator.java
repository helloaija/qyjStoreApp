package com.qyjstore.qyjstoreapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.qyjstore.qyjstoreapp.R;

/**
 * @Author shitl
 * @Description 设置手势密码-手势标志
 * @date 2019-05-21
 */
public class GestureIndicator extends View {

    /** 行列数 */
    private int rowNum = 3;
    private int colNum = 3;

    private Drawable patternNoraml = null;
    private Drawable patternPressed = null;

    /** 图标宽、高 */
    private int patternWidth = 0;
    private int patternHeight = 0;

    private String password = "";

    public GestureIndicator(Context context) {
        super(context);
    }

    public GestureIndicator(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        patternNoraml = getResources().getDrawable(R.drawable.gesture_pattern_normal, null);
        patternPressed = getResources().getDrawable(R.drawable.gesture_pattern_pressed, null);
        patternWidth = patternPressed.getIntrinsicWidth();
        patternHeight = patternPressed.getIntrinsicHeight();
        patternPressed.setBounds(0, 0, patternWidth, patternHeight);
        patternNoraml.setBounds(0, 0, patternWidth, patternHeight);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int patternWidth = patternNoraml.getIntrinsicWidth();
        int patternHeight = patternNoraml.getIntrinsicHeight();
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                // 横向坐标，宽度 + 间隔
                int dx = (int) (i * (patternWidth + patternWidth * 0.5));
                // 横向坐标，高度 + 间隔
                int dy = (int) (j * (patternHeight + patternHeight * 0.5));
                canvas.save();
                canvas.translate(dx, dy);

                String curNum = String.valueOf((i + 1) + j * colNum);
                if (TextUtils.isEmpty(password)) {
                    patternNoraml.draw(canvas);
                } else {
                    if (password.contains(curNum)) {
                        patternPressed.draw(canvas);
                    } else {
                        patternNoraml.draw(canvas);
                    }
                }

                canvas.restore();
            }
        }
    }

    @Override
    protected void onMeasure(int paramInt1, int paramInt2) {
        // 设置view宽高，宽=模型宽*列数+间隔*（列数-1），高=模型高*行数+间隔*（行数-1）
        setMeasuredDimension((int)(patternWidth * (colNum + 0.5 * (colNum - 1))), (int)(patternHeight * (rowNum + 0.5 * (rowNum - 1))));
    }

    /**
     * 请求重新绘制
     * @param password 手势密码
     */
    public void setPath(String password) {
        this.password = password;
        invalidate();
    }

}
