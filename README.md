# FreeRadarChartView
##图表展示
![](https://github.com/SaltedFishHan/FreeRadarChartView/raw/master/app/1111.gif)

##添加数据
```java
  radarView = (RadarChartView) findViewById(R.id.radarView);

valueHash = new LinkedHashMap<>();
valueHash.put("I", 33F);
valueHash.put("II", 100F);
valueHash.put("III", 66F);
valueHash.put("Ⅳ", 77F);
valueHash.put("Ⅴ", 88F);
valueHash.put("Ⅵ", 100F);
valueHash.put("Ⅶ", 22F);
valueHash.put("Ⅷ", 22F);
valueHash.put("Ⅸ", 62F);
valueHash.put("Ⅹ", 82F);
valueHash.put("Ⅺ", 22F);
valueHash.put("Ⅻ", 92F);
valueHash.put("XIII", 72F);
radarView.addData(valueHash);
radarView.setAxisNumb(6);
```

##所有可设置图表属性
```java
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
   * 参数改变
   */
  public void changeParams(String key, float value) {
      valueHash.put(key, value);
  }
  ```
