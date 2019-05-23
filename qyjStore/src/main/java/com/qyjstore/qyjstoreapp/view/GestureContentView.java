package com.qyjstore.qyjstoreapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import com.qyjstore.qyjstoreapp.base.GesturePoint;
import com.qyjstore.qyjstoreapp.utils.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author shitl
 * @Description 手势绘制
 * @date 2019-05-22
 */
public class GestureContentView extends ViewGroup {
    public static final String STATUS_NORMAL = "normal";
    public static final String STATUS_PRESSED = "pressed";
    public static final String STATUS_WRONG = "wrong";

    /** 行列数 */
    private int rowNum = 3;
    private int colNum = 3;

    /** 图标宽、高 */
    private int nodeWidth;
    private int nodeHeight;
    /** 图标水平间隔、垂直间隔 */
    private int nodeHorizonSpace;
    private int nodeVerticalSpace;

    private Context mContext;

    /** 节点列表 */
    private List<GesturePoint> pointList = new ArrayList<>();

    /** 是否可画图 */
    private boolean isDrawEnable = true;

    /** 画笔 */
    private Paint paint;
    /** 画布 */
    private Canvas canvas;
    /** 位图 */
    private Bitmap bitmap;

    /** 最后绘制的节点 */
    private GesturePoint lastPoint;

    /** 两点之间经过的点，例如点1和9间经过点5：<"19", Point(5)> */
    private Map<String, GesturePoint> middlePointMap;

    /** 记录画过的线 */
    private List<GesturePoint> drawPointList = new LinkedList<>();

    /** 监听器 */
    private GestureContentListener listener = null;

    public GestureContentView(Context context) {
        super(context);
    }

    public GestureContentView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        // 屏幕宽度
        int screenWidth = AppUtil.getWindowDisplayMetrics(context).widthPixels;
        this.nodeWidth = 2 * screenWidth / (3 * this.colNum + 1);
        this.nodeHeight = 2 * screenWidth / (3 * this.colNum + 1);
        this.nodeHorizonSpace = screenWidth / (3 * this.colNum + 1);
        this.nodeVerticalSpace = screenWidth / (3 * this.colNum + 1);

        this.mContext = context;
        // 添加子节点
        this.addChildViews();
        // 初始化中间节点
        this.initMiddlePointMap();

        // 允许画图
        this.setWillNotDraw(false);
        // 设置画笔
        this.setPaint();
        this.bitmap = Bitmap.createBitmap(this.nodeWidth * this.colNum + this.nodeHorizonSpace * (this.colNum - 1),
                this.nodeHeight * this.rowNum + this.nodeVerticalSpace * (this.colNum - 1), Bitmap.Config.ARGB_8888);
        // 创建画布
        this.canvas = new Canvas(this.bitmap);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 设置view宽高，宽=模型宽*列数+间隔*（列数-1），高=模型高*行数+间隔*（行数-1）
        setMeasuredDimension(this.nodeWidth * this.colNum + this.nodeHorizonSpace * (this.colNum - 1),
                this.nodeHeight * this.rowNum + this.nodeVerticalSpace * (this.colNum - 1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.bitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isDrawEnable) {
            return true;
        }
        this.paint.setColor(Color.rgb(76, 109, 180));
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performClick();
                GesturePoint point1 = this.getPointByCoordinate(event.getX(), event.getY());
                if (point1 == null || this.isPointSelected(point1)) {
                    return true;
                }

                this.drawPointList.add(point1);
                point1.setStatus(GesturePoint.STATUS_PRESSED);
                this.lastPoint = point1;
                break;
            case MotionEvent.ACTION_MOVE:
                this.clearScreenAndDrawList();

                GesturePoint point2 = this.getPointByCoordinate(event.getX(), event.getY());

                if (point2 == null || this.isPointSelected(point2)) {
                    // 没有绘制到节点，或者节点已经绘制过
                    if (this.lastPoint != null) {
                        // 如果存在最后节点，就从上一个节点画到当前滑动位置
                        this.canvas.drawLine(this.lastPoint.getCenterX(), this.lastPoint.getCenterY(),
                                event.getX(), event.getY(), this.paint);
                    }
                } else {
                    // 如果绘制到节点
                    if (this.lastPoint == null) {
                        // 如果最后节点为空，就设置当前节点为最后节点
                        this.lastPoint = point2;
                        point2.setStatus(GesturePoint.STATUS_PRESSED);
                        return true;
                    }
                    if (this.lastPoint.getValue() == point2.getValue()) {
                        // 如果还没有滑出最后节点，也划线
                        this.canvas.drawLine(this.lastPoint.getCenterX(), this.lastPoint.getCenterY(),
                                event.getX(), event.getY(), paint);
                    } else {
                        // 如果滑出最后节点到另一个节点
                        // 获取中间节点
                        GesturePoint middlePoint = this.getMiddlePoint(this.lastPoint, point2);

                        if (middlePoint != null && !this.isPointSelected(middlePoint)) {
                            middlePoint.setStatus(GesturePoint.STATUS_PRESSED);
                            this.drawPointList.add(middlePoint);
                            this.canvas.drawLine(this.lastPoint.getCenterX(), this.lastPoint.getCenterY(),
                                    middlePoint.getCenterX(), middlePoint.getCenterY(), this.paint);
                            this.canvas.drawLine(middlePoint.getCenterX(), middlePoint.getCenterY(),
                                    point2.getCenterX(), point2.getCenterY(), this.paint);
                        } else {
                            this.canvas.drawLine(this.lastPoint.getCenterX(), this.lastPoint.getCenterY(),
                                    point2.getCenterX(), point2.getCenterY(), this.paint);
                        }

                        this.drawPointList.add(point2);
                    }

                    point2.setStatus(GesturePoint.STATUS_PRESSED);
                    this.lastPoint = point2;
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (listener != null) {
                    listener.onDrawFinished(this.getPassword());
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 重置
     */
    public void reset() {
        for (GesturePoint point : this.pointList) {
            point.setStatus(GesturePoint.STATUS_NORMAL);
        }
        this.drawPointList.clear();
        this.clearScreenAndDrawList();
        invalidate();
    }

    /**
     * 延迟重置
     * @param delayMillis 延迟时间
     */
    public void reset(long delayMillis) {
        this.setDrawEnable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
               setDrawEnable(true);
            }
        }, delayMillis);
    }

    /**
     * 设置连线状态
     * @param status
     */
    public void setStatus(String status) {
        String pointStatus = GesturePoint.STATUS_NORMAL;
        if (STATUS_PRESSED.equals(status)) {
            pointStatus = GesturePoint.STATUS_PRESSED;
            this.paint.setColor(Color.rgb(76, 109, 180));
        } else if (STATUS_WRONG.equals(status)) {
            pointStatus = GesturePoint.STATUS_WRONG;
            this.paint.setColor(Color.rgb(252, 110, 81));
        } else if (STATUS_NORMAL.equals(status)) {
            this.paint.setColor(Color.TRANSPARENT);
        } else {
            this.paint.setColor(Color.TRANSPARENT);
        }

        for (GesturePoint point : this.drawPointList) {
            // 设置节点状态颜色
            point.setStatus(pointStatus);
        }
        this.clearScreenAndDrawList();
        invalidate();
    }

    /**
     * 获取密码
     * @return
     */
    public String getPassword() {
        StringBuilder sb = new StringBuilder();
        for (GesturePoint point : this.drawPointList) {
            sb.append(point.getValue());
        }
        return sb.toString();
    }

    /**
     * 设置监听器
     * @param listener
     */
    public void setGestureContentListener(GestureContentListener listener) {
        this.listener = listener;
    }

    /**
     * 设置是否可画图
     * @param flag
     */
    public void setDrawEnable(boolean flag) {
       this.isDrawEnable = flag;
    }

    /**
     * 获取坐标点的节点
     * @param x
     * @param y
     * @return
     */
    private GesturePoint getPointByCoordinate(float x, float y) {
        for (GesturePoint point : this.pointList) {
            if (x > point.getLeft() && x < point.getRight() && y > point.getTop() && y < point.getBotton()) {
                return point;
            }
        }
        return null;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * 添加子组件
     */
    private void addChildViews() {
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum; j++) {
                int pointValue = i + 1 + (this.colNum * j);
                int left = (this.nodeHorizonSpace + this.nodeWidth) * i;
                int top = (this.nodeVerticalSpace + this.nodeHeight) * j;
                int right = left + this.nodeWidth;
                int bottom = top + this.nodeHeight;
                GesturePoint point = new GesturePoint(this.mContext, left, top, right, bottom, pointValue, GesturePoint.STATUS_NORMAL);

                this.pointList.add(point);
                this.addView(point.getImage());
            }
        }
    }

    /**
     * 设置画笔
     */
    private void setPaint() {
        this.paint = new Paint(Paint.DITHER_FLAG);
        // 设置非填充
        this.paint.setStyle(Paint.Style.STROKE);
        // 笔宽像素
        this.paint.setStrokeWidth(10);
        // 设置默认连线颜色
        this.paint.setColor(Color.rgb(76, 109, 180));
        // 不显示锯齿
        this.paint.setAntiAlias(true);
    }

    /**
     * 初始化中间节点
     */
    private void initMiddlePointMap() {
        this.middlePointMap = new HashMap<>();
        this.middlePointMap.put("13", getGesturePointByValue(2));
        this.middlePointMap.put("17", getGesturePointByValue(4));
        this.middlePointMap.put("19", getGesturePointByValue(5));
        this.middlePointMap.put("28", getGesturePointByValue(5));
        this.middlePointMap.put("37", getGesturePointByValue(5));
        this.middlePointMap.put("39", getGesturePointByValue(6));
        this.middlePointMap.put("46", getGesturePointByValue(5));
        this.middlePointMap.put("79", getGesturePointByValue(8));
    }

    /**
     * 根据节点值获取节点
     * @param value
     * @return
     */
    private GesturePoint getGesturePointByValue(int value) {
        for (GesturePoint point : this.pointList) {
            if (value == point.getValue()) {
                return point;
            }
        }

        return null;
    }

    /**
     * 获取两个节点之间的节点
     * @param point1
     * @param point2
     * @return
     */
    private GesturePoint getMiddlePoint(GesturePoint point1, GesturePoint point2) {
        return point1.getValue() < point2.getValue() ?
                this.middlePointMap.get(String.valueOf(point1.getValue()) + point2.getValue()) :
                this.middlePointMap.get(String.valueOf(point2.getValue()) + point1.getValue());
    }

    /**
     * 清掉屏幕上所有的线，然后画出集合里面的线
     */
    private void clearScreenAndDrawList() {
        this.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (drawPointList.size() > 1) {
            for (int i = 1; i < drawPointList.size(); i ++) {
                GesturePoint lastPoint = drawPointList.get(i - 1);
                GesturePoint point = drawPointList.get(i);
                // 画线
                this.canvas.drawLine(lastPoint.getCenterX(), lastPoint.getCenterY(),
                        point.getCenterX(), point.getCenterY(), paint);
            }
        }
    }

    /**
     * 验证节点是否已连接
     * @param point
     * @return
     */
    private boolean isPointSelected(GesturePoint point) {
        for (GesturePoint selectedPoint : this.drawPointList) {
            if (selectedPoint.getValue() == point.getValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 监听器接口
     */
    public interface GestureContentListener {
        /**
         * 绘制完成时调用
         * @param password
         */
        void onDrawFinished(String password);
    }
}
