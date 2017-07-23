package com.example.zzh.freeradarchartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.graphics.Color.parseColor;

/**
 * Created by chen on 2017-6-28.
 */

public class RadarChartView extends View {

    private Context context;
    private Paint axisPaint;
    private Paint valuePaint;
    private Paint textPaint;
    private Paint dotPaint;
    private Paint fillPaint;
    private Paint strokePaint;
    private Paint valueStrokePaint;
    private Path path;
    private Path src;

    private int centerX;
    private int centerY;
    private float radius;//最大半径
    private int countX = 3;//多边形边个数
    private int countY;//刻度
    private float angle = (float) (Math.PI * 2 / countX);//单位角度
    private float axisMax;//最大值
    private boolean isCircle;
    private boolean dotVisiable;
    private boolean fillAndStrock;
    private int dotColor;//点颜色
    private int dataAreaColor;//数据区域颜色
    private int bgColor;//网格背景颜色
    private int griddingColor;//网格线颜色
    private int valueStrokeColor;//数据描边颜色
    private float dotRadius;//点半径
    private int textSize;//字体大小
    private int textColor;//字体颜色
    private float textSpacing;//字体间距
    private boolean griddingVisiable;//网格是否显示
    private HashMap<String, Float> valueHash;

    public RadarChartView(Context context) {
        this(context, null);
    }

    public RadarChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        valueHash = new LinkedHashMap<>();
        iniParams(attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 各种属性参数
     */
    private void iniParams(AttributeSet attrs, int defStyleAttr) {
        TypedArray colors = context.obtainStyledAttributes(attrs, new int[]{
                R.attr.bgColor, R.attr.dotColor, R.attr.dataAreaColor, R.attr.griddingColor
        }, defStyleAttr, 0);
        final int bgColor = colors.getColor(0, parseColor("#30349DFF"));
        final int dotColor = colors.getColor(1, parseColor("#FFFF0000"));
        final int dataAreaColor = colors.getColor(2, parseColor("#9079C930"));
        final int griddingColor = colors.getColor(3, parseColor("#FFFFFFFF"));
        final int textColor = colors.getColor(3, parseColor("#FFFFFFFF"));
        final int valueStrokeColor = colors.getColor(3, parseColor("#FFFFFFFF"));
        colors.recycle();

        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.RadarChartView, defStyleAttr, 0);
        this.bgColor = values.getColor(R.styleable.RadarChartView_bgColor, bgColor);
        this.dotColor = values.getColor(R.styleable.RadarChartView_bgColor, dotColor);
        this.dataAreaColor = values.getColor(R.styleable.RadarChartView_bgColor, dataAreaColor);
        this.griddingColor = values.getColor(R.styleable.RadarChartView_bgColor, griddingColor);
        this.textColor = values.getColor(R.styleable.RadarChartView_bgColor, textColor);
        this.valueStrokeColor = values.getColor(R.styleable.RadarChartView_valueStrokeColor, valueStrokeColor);
        dotRadius = values.getFloat(R.styleable.RadarChartView_dotRadius, 5F);
        textSize = values.getDimensionPixelSize(R.styleable.RadarChartView_textSzie, 20);
        textSpacing = values.getDimension(R.styleable.RadarChartView_textSpacing, 20F);
        countY = values.getInt(R.styleable.RadarChartView_axisNumb, 4);
        griddingVisiable = values.getBoolean(R.styleable.RadarChartView_griddingVisiable, true);
        axisMax = values.getFloat(R.styleable.RadarChartView_axisMax, 100F);
        dotVisiable = values.getBoolean(R.styleable.RadarChartView_dotVisiable, false);
        values.recycle();
    }

    private void initPaint() {
        //网格竖线
        axisPaint = creatPaint(griddingColor, 0, 2, 0);

        //数据区域填充
        valuePaint = creatPaint(dataAreaColor, 1, 2, 0);

        //数据区域描边
        valueStrokePaint = creatPaint(valueStrokeColor, 0, 3, 0);

        //字体标签
        textPaint = creatPaint(textColor, 2, 0.5F, textSize);

        //点
        dotPaint = creatPaint(dotColor, 2, 1, 0);
//
        //网格横线填充
        fillPaint = creatPaint(bgColor, 1, 2, 0);

        //网格横线描边
        strokePaint = creatPaint(griddingColor, 0, 3, 0);
    }

    /**
     * 创建画笔
     */
    private Paint creatPaint(int color, int style, float width, float textSize) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        switch (style) {
            case 0:
                paint.setStyle(Paint.Style.STROKE);
                break;
            case 1:
                paint.setStyle(Paint.Style.FILL);
                break;
            case 2:
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                break;
        }
        paint.setStrokeWidth(width);
        paint.setTextSize(textSize);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        angle = (float) (Math.PI * 2 / countX);

        int width = getWidth();
        int height = getHeight();
        canvas.translate(width / 2, height / 2);

        path = new Path();
        src = new Path();

        drawGridding(canvas);
        drawData(canvas);
        drawText(canvas);
    }

    /**
     * 外围标签
     */
    private void drawText(Canvas canvas) {
        Iterator<Map.Entry<String, Float>> iterator = valueHash.entrySet().iterator();
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        float r = radius * (countX - 1) / countX;//半径大小
        for (int i = 0; i < countX; i++) {
            Map.Entry<String, Float> next = iterator.next();
            float x = (float) ((textSpacing + radius) * Math.cos(-Math.PI / 2 + angle * i));
            float y = (float) ((textSpacing + radius) * Math.sin(-Math.PI / 2 + angle * i));

            double angle2 = angle * i;
            float height = fontHeight / 2;//文本高度一半
            if (angle2 >= 0 && angle2 <= Math.PI / 2) {//第1象限//
                float dis = textPaint.measureText(next.getKey()) / 2;//文本长度一半
                canvas.drawText(next.getKey(), x - dis, y, textPaint);
            } else if (angle2 > Math.PI / 2 && angle2 <= Math.PI) {//第2象限
                float dis = textPaint.measureText(next.getKey()) / 2;//文本长度一半
                canvas.drawText(next.getKey(), x - dis, y + height, textPaint);
            } else if (angle2 >= Math.PI && angle2 < 3 * Math.PI / 2) {//第3象限
                float dis = textPaint.measureText(next.getKey()) / 2;//文本长度一半
                canvas.drawText(next.getKey(), x - dis, y + height, textPaint);
            } else if (angle2 >= 3 * Math.PI / 2 && angle2 <= Math.PI * 2) {//第4象限
                float dis = textPaint.measureText(next.getKey()) / 2;//文本长度一半
                canvas.drawText(next.getKey(), x - dis, y, textPaint);
            }
        }
    }

    /**
     * 绘制网格
     */
    private void drawGridding(Canvas canvas) {
        float r = radius / countY;
        for (int i = 1; i < countY + 1; i++) {
            float eachR = r * i;
            this.path.reset();
            for (int j = 0; j < countX; j++) {//
                float x = (float) (eachR * Math.cos(-Math.PI / 2 + angle * j));//-pi/2，是因为第一条线以正Y轴开始
                float y = (float) (eachR * Math.sin(-Math.PI / 2 + angle * j));
                if (i == countY) {
                    src.lineTo(x, y);
                    canvas.drawPath(src, axisPaint);
                    src.reset();
                }

                if (isCircle) {
                    float radius = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                    path.addCircle(0, 0, radius, Path.Direction.CW);
                } else {
                    if (j == 0) {
                        this.path.moveTo(x, y);
                    } else {
                        this.path.lineTo(x, y);
                    }
                }
            }
            this.path.close();
            canvas.drawPath(this.path, fillPaint);
            if (griddingVisiable) {
                canvas.drawPath(this.path, strokePaint);
            }
        }
    }

    /**
     * 绘制数据区域图形
     */
    private void drawData(Canvas canvas) {
        Iterator<Map.Entry<String, Float>> iterator = valueHash.entrySet().iterator();
        Path dataPath = new Path();
        float r = radius / axisMax;//半径大小
        for (int i = 0; i < countX; i++) {
            Map.Entry<String, Float> next = iterator.next();
            float x = (float) Math.cos(-Math.PI / 2 + angle * i) * r * next.getValue();
            float y = (float) Math.sin(-Math.PI / 2 + angle * i) * r * next.getValue();
            if (i == 0) {
                dataPath.moveTo(x, y);//
            } else {
                dataPath.lineTo(x, y);
            }
            if (dotVisiable) {
                canvas.drawCircle(x, y, dotRadius, dotPaint);
            }
        }
        dataPath.close();
        canvas.drawPath(dataPath, valuePaint);
        if (fillAndStrock) {
            canvas.drawPath(dataPath, valueStrokePaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(w, h) / 2 * 0.8f;
        centerX = w / 2;
        centerY = h / 2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 增加一组数据
     */
    public void addData(HashMap<String, Float> data) {
        valueHash.clear();
        valueHash.putAll(data);
        countX = valueHash.size();
    }

    /**
     * 将View刷新单独拿出来，方便操作
     */
    public void refreshView() {
        postInvalidate();
    }

    /**
     * 蜘蛛图最大值，默认为100
     */
    public void setAxisMax(float axisMax) {
        this.axisMax = axisMax;
    }

    /**
     * View中心横坐标
     */
    public int getViewCenterX() {
        return centerX;
    }

    /**
     * View中心纵坐标
     */
    public int getViewCenterY() {
        return centerY;
    }

    /**
     * 背景网格是否填充
     */
    public void setStroke(boolean isStroke) {
        fillPaint.setStyle(isStroke ? Paint.Style.STROKE : Paint.Style.FILL);
    }

    /**
     * 数据围成的区域描边和填充
     */
    public void setValeStroke(boolean isStroke) {
        valuePaint.setStyle(isStroke ? Paint.Style.STROKE : Paint.Style.FILL);
    }

    /**
     * 多边形边数
     */
    public void setAxisTick(int axisTick) {
        countY = axisTick;
    }

    /**
     * 刻度个数
     */
    public void setAxisNumb(int axisNumb) {
        countX = axisNumb;
    }

    /**
     * 圆形或者多边形设置
     */
    public void setCircle(boolean isCircle) {
        this.isCircle = isCircle;
    }

    /**
     * 标签字体大小
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
    }

    /**
     * 字体间距
     */
    public void setSpacing(int textSpacing) {
        this.textSpacing = textSpacing;
    }

    /**
     * 数据区域单独描边
     */
    public void setFillAndStrock(boolean fillAndStrock) {
        this.fillAndStrock = fillAndStrock;
    }

    /**
     * 点的颜色
     */
    public void setDotColor(int color) {
        this.dotColor = color;
    }

    /**
     * 是否显示点
     */
    public void setDot(boolean dotVisiable) {
        this.dotVisiable = dotVisiable;
    }

    /**
     * 点的半径
     */
    public void setDotRadius(float dotRadius) {
        if (dotRadius < 0) {
            this.dotRadius = 0;
        } else {
            this.dotRadius = dotRadius;
        }
    }

    /**
     * 是否绘出网格
     */
    public void setGridding(boolean griddingVisiable) {
        this.griddingVisiable = griddingVisiable;
    }

    /**
     * 网格颜色
     */
    public void setGriddingColor(int color) {
        griddingColor = color;
    }

    /**
     * 参数变化
     */
    public void changeParams(String key, float value) {
        valueHash.put(key, value);
    }


}
