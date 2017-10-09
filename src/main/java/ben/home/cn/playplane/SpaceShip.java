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
    private static float VELMAX = 15.0f;
    private float bitmapSizeW;
    private float bitmapSizeH;
    private float _targetX;
    private float _targetY;
    private float _vel;

    SpaceShip(ArrayList<Bitmap> tmpList, int frameTime) {
        super(tmpList, frameTime);               // Default set to 1s
        bitmapSizeW = tmpList.get(0).getWidth();
        bitmapSizeH = tmpList.get(0).getHeight();
        collisonRectangle = new RectF(0, 0, bitmapSizeW, bitmapSizeH);
        this.getCoordinates().set_x(Math.round(PlaneFightView.SCREEN_WIDTH / 2 - bitmapSizeW / 2));
        this.getCoordinates().set_y(Math.round(PlaneFightView.SCREEN_HEIGHT - bitmapSizeH));
        this.set_alive(true);
    }

    public boolean checkCollison(RectF rect) {
        Log.v(TAG, "The spaceship (left,top) = (" + getCollisonRectangle().left + "," + getCollisonRectangle().top +
                ") and (right,bottom) = (" + getCollisonRectangle().right + "," + getCollisonRectangle().bottom + ")");
        Log.v(TAG, "The outter (left,top) = (" + rect.left + "," + rect.top +
                ") and (right,bottom) = (" + rect.right + "," + rect.bottom + ")");
        return this.collisonRectangle.intersect(rect);
    }

    public RectF getCollisonRectangle() {
        collisonRectangle.offsetTo(0.0f, 0.0f);
        collisonRectangle.offset(this.getCoordinates().get_x(), this.getCoordinates().get_y());
        return collisonRectangle;
    }

    public void statusUpdate() {
        float deltaX = 0.0f;
        float deltaY = 0.0f;
        deltaX = getCoordinates().get_x() - _targetX;
        deltaY = getCoordinates().get_y() - _targetY;
        super.statusUpdate();
        if(Math.sqrt(deltaX*deltaX + deltaY*deltaY) < (double)_vel){
            this.getSpeed().set_velx(0);
            this.getSpeed().set_vely(0);
            this.getCoordinates().set_x((int)_targetX);
            this.getCoordinates().set_y((int)_targetY);
        }
        this.runBySequence(this.getFrameTime());
    }

    public void setTarget(float targetX, float targetY) {
        float distanceX = 0.0f;
        float distanceY = 0.0f;
        double yvsvel = 0.0d;
        double xvsvel = 0.0d;
        _vel = VELMAX;
        this._targetX = targetX;
        this._targetY = targetY;
        distanceX = _targetX - getCoordinates().get_x();
        distanceY = _targetY - getCoordinates().get_y();
        yvsvel = distanceY / Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        xvsvel = distanceX / Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        this.getSpeed().set_vely((int) Math.abs((_vel * yvsvel)));
        this.getSpeed().set_velx((int) Math.abs((_vel * xvsvel)));
        if (_targetX > getCoordinates().get_x()) {
            getSpeed().set_velxDirection(Speed.X_DIRECTION_RIGHT);
        }
        getSpeed().set_velxDirection(_targetX > getCoordinates().get_x() ? Speed.X_DIRECTION_RIGHT
                : Speed.X_DIRECTION_LEFT);
        getSpeed().set_velyDirection(_targetY > getCoordinates().get_y() ? Speed.Y_DIRECTION_DOWN
                : Speed.Y_DIRECTION_UP);
    }
}
