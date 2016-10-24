package play.club.skecher.style;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import play.club.skecher.Point;
import play.club.skecher.Style;

/**
 * 项目名称：SilkPaints
 * 类描述：
 * 创建人：fuzh2
 * 创建时间：2016/7/10 23:27
 * 修改人：fuzh2
 * 修改时间：2016/7/10 23:27
 * 修改备注：
 */
public class HackStyle implements Style {

    private final int FINAL_LINE_RADIUO = 100;

    private Vector<Point> mPoints;//保存所有的数据点

    private Path mPath;//当前设置色值的的绘制路径

    private Paint mPaint;
    private int foregroudColor = Color.RED;//前景色
    private int bgColor = Color.WHITE;//背景色
    private float paintWidth = 0.5f;//画笔宽度发
    private int radiuo = FINAL_LINE_RADIUO;

    private ArrayList<Path> mPaths = new ArrayList<>();//历史路径
    private ArrayList<Integer> colors = new ArrayList<>();//历史路径对应的颜色
    private ArrayList<Float> lineWidths = new ArrayList<>();//历史路径对应的坏宽度

    public HackStyle() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(foregroudColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(paintWidth);
        mPath = new Path();
        mPoints = new Vector<>();
    }

    @Override
    public void strokeStart(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        newLine(x, y);
    }

    @Override
    public void stroke(Canvas c, float x, float y) {
        mPath.lineTo(x, y);
        newLine(x, y);
    }

    @Override
    public void draw(Canvas canvas) {
//        canvas.drawColor(bgColor);//画背景
        for (int i = 0; i < mPaths.size(); i++) {
            mPaint.setColor(colors.get(i));
            mPaint.setStrokeWidth(lineWidths.get(i));
            canvas.drawPath(mPaths.get(i), mPaint);
        }
        mPaint.setColor(foregroudColor);
        mPaint.setStrokeWidth(paintWidth);
        canvas.drawPath(mPath, mPaint);//画路径
    }

    @Override
    public void setColor(int color) {
        savePreStats();
        this.foregroudColor = color;
    }

    @Override
    public void saveState(HashMap<Integer, Object> state) {

    }

    @Override
    public void restoreState(HashMap<Integer, Object> state) {

    }

    @Override
    public void setColorBrige(String startColor, String endColor) {

    }

    @Override
    public void startPlayRecode(Canvas c) {

    }

    /**
     * 清屏
     */
    public void clearPath() {
        mPoints.clear();
        mPaths.clear();
        colors.clear();
        lineWidths.clear();
        mPath.reset();
    }


    /**
     * 连接半径在指定范围的所有点
     *
     * @param x
     * @param y
     */
    private void newLine(float x, float y) {
        if (mPoints.size() > 0) {
            for (Point p : mPoints) {
                double r = Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
                if ((r <= radiuo)) {
                    mPath.moveTo(x, y);
                    mPath.lineTo(p.x, p.y);
                }
            }
        } else {
        }
        mPath.moveTo(x, y);//移动到最新点，防止错误交叉
        mPoints.add(new Point(x, y));
    }

    /**
     * 保存之前的绘制状态
     */
    private void savePreStats() {
        mPaths.add(mPath);
        Float flw = Float.parseFloat(paintWidth + "");
        lineWidths.add(flw);
        Integer integer = Integer.parseInt(foregroudColor + "");
        colors.add(integer);
        mPath = new Path();
    }
}
