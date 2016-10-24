package play.club.skecher.style;

import play.club.skecher.Style;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 项目名称：HackArt
 * 类描述：
 * 创建人：fuzh2
 * 创建时间：2016/6/30 16:29
 * 修改人：fuzh2
 * 修改时间：2016/6/30 16:29
 * 修改备注：
 */
public class StylesFactory {
    public static final int SKETCHY = 0x1001;
    public static final int SHADED = 0x1002;
    public static final int CHROME = 0x1003;
    public static final int FUR = 0x1004;
    public static final int LONGFUR = 0x1005;
    public static final int WEB = 0x1006;
    public static final int SQUARES = 0x1007;
    public static final int RIBBON = 0x1008;
    public static final int CIRCLES = 0x1009;
    public static final int GRID = 0x1010;
    public static final int SIMPLE = 0x1011;
    public static final int ERASER = 0x1012;
    public static final int HACKPEN = 0x1013;

    private static Map<Integer, Style> cache = new HashMap<Integer, Style>();
    private static int currentStyle = RIBBON;

    public static Style getStyle(int id) {
        if (!cache.containsKey(id)) {
            cache.put(id, getStyleInstance(id));
        }
        currentStyle = id;
        return cache.get(id);
    }

    public static Style getCurrentStyle() {
        return getStyle(currentStyle);
    }

    public static void clearCache() {
        cache.clear();
    }

    private static Style getStyleInstance(int id) {
        switch (id) {
            case SKETCHY:
                return new SketchyStyle();
            case SHADED:
                return new ShadedStyle();
            case CHROME:
                return new ChromeStyle();
            case FUR:
                return new FurStyle();
            case LONGFUR:
                return new LongfurStyle();
            case WEB:
                return new WebStyle();
            case SQUARES:
                return new SquaresStyle();
            case RIBBON:
                return new RibbonStyle();
            case CIRCLES:
                return new CirclesStyle();
            case GRID:
                return new GridStyle();
            case SIMPLE:
                return new SimpleStyle();
            case ERASER:
                return new EraserStyle();
            case HACKPEN:
                return new HackStyle();

            default:
                throw new RuntimeException("Invalid style ID");
        }
    }

    public static void saveState(HashMap<Integer, Object> state) {
        Collection<Style> values = cache.values();
        for (Style style : values) {
            style.saveState(state);
        }
    }

    public static void restoreState(HashMap<Integer, Object> state) {
        Set<Integer> keySet = state.keySet();
        for (int id : keySet) {
            Style style = getStyle(id);
            style.restoreState(state);
        }
    }
}
