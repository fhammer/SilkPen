package play.club.skecher;

/**
 * 项目名称：SilkPaints
 * 类描述：
 * 创建人：fuzh2
 * 创建时间：2016/7/10 16:34
 * 修改人：fuzh2
 * 修改时间：2016/7/10 16:34
 * 修改备注：
 */
public class Node {
    public static final int ACTION_DOWN = 1;
    public static final int ACTION_UP = 2;
    public static final int ACTION_MPVE = 3;
    private float x;
    private float y;
    private int type = ACTION_DOWN;

    public Node(float pX, float pY, int pType) {
        x = pX;
        y = pY;
        type = pType;
    }

    public int getType() {
        return type;
    }

    public void setType(int pType) {
        type = pType;
    }

    public float getY() {
        return y;
    }

    public void setY(float pY) {
        y = pY;
    }

    public float getX() {
        return x;
    }

    public void setX(float pX) {
        x = pX;
    }
}
