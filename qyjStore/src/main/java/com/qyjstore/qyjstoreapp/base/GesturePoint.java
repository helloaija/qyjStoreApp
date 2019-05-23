package com.qyjstore.qyjstoreapp.base;

import android.content.Context;
import android.widget.ImageView;
import com.qyjstore.qyjstoreapp.R;

/**
 * @Author shitl
 * @Description 手势节点信息
 * @date 2019-05-22
 */
public class GesturePoint {
    /** 节点状态-初始 */
    public static final String STATUS_NORMAL = "normal";
    /** 节点状态-按压 */
    public static final String STATUS_PRESSED = "pressed";
    /** 节点状态-错误 */
    public static final String STATUS_WRONG = "wrong";

    private Context context;
    /** 左边x坐标值 */
    private int left;
    /** 顶部y坐标值 */
    private int top;
    /** 右边x坐标值 */
    private int right;
    /** 底部y坐标值 */
    private int botton;
    /** 节点图标 */
    private ImageView image;
    /** 节点的值 */
    private int value;
    /** 节点状态 */
    private String status;

    public GesturePoint(Context context, int left, int top, int right, int botton, int value, String status) {
        this.context = context;
        this.left = left;
        this.top = top;
        this.right = right;
        this.botton = botton;
        this.value = value;
        this.setStatus(status);
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBotton() {
        return botton;
    }

    public void setBotton(int botton) {
        this.botton = botton;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;

        if (image == null) {
            image = new ImageView(this.context);
        }

        switch (status) {
            case STATUS_NORMAL:
                this.image.setImageResource(R.drawable.gesture_node_normal);
                break;
            case STATUS_PRESSED:
                this.image.setImageResource(R.drawable.gesture_node_pressed);
                break;
            case STATUS_WRONG:
                this.image.setImageResource(R.drawable.gesture_node_wrong);
                break;
            default:
                break;
        }

        this.image.layout(this.left, this.top, this.right, this.botton);
    }

    /**
     * 获取中心点x值
     * @return
     */
    public int getCenterX() {
        return this.left + (this.right - this.left) / 2;
    }

    /**
     * 获取中心点y值
     * @return
     */
    public int getCenterY() {
        return this.top + (this.botton - this.top) / 2;
    }
}
