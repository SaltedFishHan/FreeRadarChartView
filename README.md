# FreeRadarChartView
图表展示
![](https://github.com/SaltedFishHan/FreeRadarChartView/raw/master/app/1111.gif)
添加数据
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
