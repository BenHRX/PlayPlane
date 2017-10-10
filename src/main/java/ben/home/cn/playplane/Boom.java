package ben.home.cn.playplane;

import android.graphics.Bitmap;

import ben.home.cn.share.AnimateSpirit;

/**
 * Created by benhuang on 17-10-10.
 */

public class Boom extends AnimateSpirit {
    private static int BOOMFRAMETIME = 2;

    public Boom(Bitmap animateBitmap, int columnCount, int rowCount, int pos_X, int pos_Y) {
        super(animateBitmap, columnCount, rowCount, BOOMFRAMETIME);
        this.set_alive(true);
        this.getCoordinates().set_x(pos_X);
        this.getCoordinates().set_y(pos_Y);
    }

    public void boomStatusUpdate(){
        runOnce(this.getFrameTime());
    }
}
