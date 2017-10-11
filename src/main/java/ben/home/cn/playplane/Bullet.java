package ben.home.cn.playplane;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import ben.home.cn.share.ElementStatus;
import ben.home.cn.share.Spirit;

/**
 * Created by benhuang on 17-10-1.
 */

public class Bullet extends Spirit implements ElementStatus{

    private float bitmapSizeW;
    private float bitmapSizeH;
    RectF collisonRectangle;
    private int healPower;
    private int attackValue;
    String TAG = "Bullet inside >>";

    Bullet(Bitmap bulletBitmap, int posX, int posY){
        super(bulletBitmap);
        bitmapSizeW = bulletBitmap.getWidth();
        bitmapSizeH = bulletBitmap.getHeight();
        this.getCoordinates().set_x(posX - (int)bitmapSizeW/2);
        this.getCoordinates().set_y(posY);
        this.getSpeed().set_vely(30);
        this.getSpeed().set_velx(0);
        this.getSpeed().set_velyDirection(Speed.Y_DIRECTION_UP);
        this.getSpeed().set_velxDirection(Speed.X_DIRECTION_LEFT);

        collisonRectangle = new RectF(0, 0, bitmapSizeW, bitmapSizeH);

        this.set_alive(true);
        this.setHealPower(0);
        this.setAttackValue(1);
    }

    public boolean checkCollison(RectF rect){
        Log.v(TAG, "The bullet (left,top) = (" + getCollisonRectangle().left + "," + getCollisonRectangle().top +
                ") and (right,bottom) = (" + getCollisonRectangle().right + "," + getCollisonRectangle().bottom + ")");
        Log.v(TAG, "The outter bullet (left,top) = (" + rect.left + "," + rect.top +
                ") and (right,bottom) = (" + rect.right + "," + rect.bottom + ")");
        return this.collisonRectangle.intersect(rect);
    }

    public RectF getCollisonRectangle(){
        collisonRectangle.offsetTo(0.0f, 0.0f);
        collisonRectangle.offset(this.getCoordinates().get_x(), this.getCoordinates().get_y());
        return collisonRectangle;
    }

    @Override
    public void statusUpdate() {
        super.statusUpdate();
        /*Log.v(TAG, "The bullet vel (x,y) is (" + this.getSpeed().get_velx() + ","
                + this.getSpeed().get_vely() + ")");*/
        /*Log.v(TAG, "The bullet posistion (x,y) is (" + this.getCoordinates().get_x() + ","
                + this.getCoordinates().get_y() + ")");*/
        if(this.getCoordinates().get_x() < 0 ||
                this.getCoordinates().get_x() > PlaneFightView.SCREEN_WIDTH ||
                this.getCoordinates().get_y() < 0 ||
                this.getCoordinates().get_y() > PlaneFightView.SCREEN_HEIGHT){
            this.set_alive(false);
        }
    }

    @Override
    public int getHealPower() {
        return healPower;
    }

    @Override
    public void setHealPower(int heal) {
        this.healPower = heal;
    }

    @Override
    public int getAttackValue() {
        return attackValue;
    }

    @Override
    public void setAttackValue(int value) {
        attackValue = value;
    }

    @Override
    public int checkDestroyStatus(int outterDamage) {
        /*if(this.getHealPower() - outterDamage <= 0){
            return TARGETDESTROYED;
        }
        else{
            return TARGETALIVE;
        }*/
        // Bullet sure should disappear
        return this.TARGETDESTROYED;
    }
}
