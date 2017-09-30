package ben.home.cn.playplane;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

import ben.home.cn.share.AnimateSpirit;

/**
 * Created by benhuang on 17-9-22.
 */


public class SpaceShip extends AnimateSpirit {

    private RectF collisonRectangle;
    private static String TAG = "SpaceShip in >>";
    private float bitmapSizeW;
    private float bitmapSizeH;

    SpaceShip(ArrayList<Bitmap> tmpList, int frameTime){
        super(tmpList, frameTime);               // Default set to 1s
        bitmapSizeW = tmpList.get(0).getWidth();
        bitmapSizeH = tmpList.get(0).getHeight();
        collisonRectangle = new RectF(0, 0, bitmapSizeW, bitmapSizeH);
        this.getCoordinates().set_x(Math.round(PlaneFightView.SCREEN_WIDTH / 2 - bitmapSizeW / 2));
        this.getCoordinates().set_y(Math.round(PlaneFightView.SCREEN_HEIGHT - bitmapSizeH));
    }

    public boolean checkCollison(RectF rect){
        Log.v(TAG, "The spaceship (left,top) = (" + getCollisonRectangle().left + "," + getCollisonRectangle().top +
                ") and (right,bottom) = (" + getCollisonRectangle().right + "," + getCollisonRectangle().bottom + ")");
        Log.v(TAG, "The outter (left,top) = (" + rect.left + "," + rect.top +
                ") and (right,bottom) = (" + rect.right + "," + rect.bottom + ")");
        return this.collisonRectangle.intersect(rect);
    }

    public RectF getCollisonRectangle(){
        collisonRectangle.offsetTo(0.0f, 0.0f);
        collisonRectangle.offset(this.getCoordinates().get_x(), this.getCoordinates().get_y());
        return collisonRectangle;
    }

    public void statusUpdate(){
        this.runBySequence(this.getFrameTime());
    }
}
