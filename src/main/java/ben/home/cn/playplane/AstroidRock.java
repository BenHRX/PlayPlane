package ben.home.cn.playplane;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

import java.util.Random;

import ben.home.cn.share.ElementStatus;
import ben.home.cn.share.Spirit;

/**
 * Created by benhuang on 17-9-22.
 */

public class AstroidRock extends Spirit implements ElementStatus{

    private static final int MAXROTATEINFRAME = 5;
    private static final int MAXBIGROCKSPEED = 7;
    private int rangeOfRockMin;
    private int rangeOfRockMax;

    private Bitmap originBitmap;
    private Bitmap changedBitmap;
    private Bitmap realBitmap;
    private int rotateInFrame;
    private float totalAngle;
    private float originBitmapWidth;
    private float originBitmapHeight;
    private double realBitmapLength;             // Must be rectangle
    private float deltaX;
    private float deltaY;

    private RectF collisonRectangle;

    private int HealPower;
    private int AttackValue;

    private static String TAG = "Astroid inside >>";

    /*String TAG = "Astroid use >";*/

    AstroidRock(Bitmap astroidBitmap){
        super();
        originBitmap = astroidBitmap;
        Random rand = new Random(System.currentTimeMillis());
        rotateInFrame = rand.nextInt() % MAXROTATEINFRAME;          // Use random degree for init, '+/-' also ok
        getSpeed().set_vely(Math.abs(rand.nextInt() % MAXBIGROCKSPEED));
        while(getSpeed().get_vely() <= 3){
            getSpeed().set_vely(Math.abs(rand.nextInt() % MAXBIGROCKSPEED));  // Next speed;
        }
        getSpeed().set_velyDirection(1);
        /*getCoordinates().set_y(0);
        getCoordinates().set_x(100);*/      // Random set the rock position
        totalAngle = 0.0f;
        originBitmapWidth = originBitmap.getWidth();
        originBitmapHeight = originBitmap.getHeight();
        realBitmapLength = Math.sqrt(originBitmapWidth*originBitmapWidth +
                originBitmapHeight*originBitmapHeight);
        deltaX = (float) (realBitmapLength - originBitmapWidth);
        deltaY = (float) (realBitmapLength - originBitmapHeight);
        // Todo
        /*rangeOfRockMax = screenSize - deltaX;   --- Further need to get the screen size for initilize.*/
        rangeOfRockMax = Math.round(PlaneFightView.SCREEN_WIDTH - deltaX);
        rangeOfRockMin = 0;
        collisonRectangle = new RectF((float)(realBitmapLength - originBitmapWidth)/2,
                (float)(realBitmapLength - originBitmapHeight)/2,
                (float)(realBitmapLength + originBitmapWidth)/2,
                (float)(realBitmapLength + originBitmapHeight)/2);
        getCoordinates().set_y(0 - Math.round((float) realBitmapLength));
        getCoordinates().set_x(Math.abs(rand.nextInt() % rangeOfRockMax));
        this.set_alive(true);
        this.setHealPower(1);
        this.setAttackValue(1);
    }

    /*public int getRotateInFrame() {
        return rotateInFrame;
    }*/

    /*public void setRotateInFrame(int rotateInFrame) {
        this.rotateInFrame = rotateInFrame;
    }*/

    public void refreshStatus(){

        super.statusUpdate();

        if(getCoordinates().get_x() < (0 - realBitmapLength / 2) ||
                getCoordinates().get_x() > PlaneFightView.SCREEN_WIDTH + realBitmapLength /2){
            this.set_alive(false);
            return;
        }
        if(getCoordinates().get_y() > PlaneFightView.SCREEN_HEIGHT + realBitmapLength){
            this.set_alive(false);
            return;
        }

        int AsdWidth = originBitmap.getWidth();
        int AsdHeight = originBitmap.getHeight();
        totalAngle = totalAngle + rotateInFrame;
        if(totalAngle >= 360){
            totalAngle = totalAngle - 360;
        }
        if(totalAngle <= -360){
            totalAngle = totalAngle + 360;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(totalAngle, originBitmap.getWidth()/2, originBitmap.getHeight()/2);            // Set rotation center point
        matrix.postRotate(totalAngle);
        changedBitmap = Bitmap.createBitmap(originBitmap, 0, 0, AsdWidth, AsdHeight, matrix, true);
        realBitmap = Bitmap.createBitmap((int)Math.ceil(realBitmapLength), (int)Math.ceil(realBitmapLength),
                originBitmap.getConfig());
        Canvas canvas = new Canvas(realBitmap);
        canvas.drawBitmap(changedBitmap, (float)(realBitmapLength - changedBitmap.getWidth())/2,
                (float)(realBitmapLength - changedBitmap.getHeight())/2, null);
        /*Log.v(TAG, "The astroid is at (" + getCoordinates().get_x() + ", " + getCoordinates().get_y() + ")");
        Log.v(TAG, "The astroid pic size is (" + changedBitmap.getWidth() + ", " + changedBitmap.getHeight() + ")");*/
    }

    @Override
    public Bitmap getImage() {
        //return this.changedBitmap;
        return realBitmap;
    }

    public void recycleBitmap(){
        if(!is_alive()){
            originBitmap = null;
            changedBitmap = null;
            realBitmap = null;
            System.gc();
        }
    }

    public boolean checkCollison(RectF rect){
        Log.v(TAG, "The checkcollison (left,top) = (" + getCollisonRectangle().left + "," + getCollisonRectangle().top +
                ") and (right,bottom) = (" + getCollisonRectangle().right + "," + getCollisonRectangle().bottom + ")");
        Log.v(TAG, "The outter (left,top) = (" + rect.left + "," + rect.top +
                ") and (right,bottom) = (" + rect.right + "," + rect.bottom + ")");
        return this.collisonRectangle.intersect(rect);
//        return false;
    }

    public RectF getCollisonRectangle(){
        collisonRectangle.offsetTo(0.0f, 0.0f);
        collisonRectangle.offset(this.getCoordinates().get_x() + deltaX / 2,
                this.getCoordinates().get_y() + deltaY / 2);
        return collisonRectangle;
    }

    @Override
    public String toString() {
        return "AstroidRock{" +
                "originBitmap=" + originBitmap +
                ", changedBitmap=" + changedBitmap +
                ", realBitmap=" + realBitmap +
                ", rotateInFrame=" + rotateInFrame +
                ", totalAngle=" + totalAngle +
                ", originBitmapWidth=" + originBitmapWidth +
                ", originBitmapHeight=" + originBitmapHeight +
                ", realBitmapLength=" + realBitmapLength +
                '}';
    }

    @Override
    public int getHealPower() {
        return this.HealPower;
    }

    @Override
    public void setHealPower(int heal) {
        this.HealPower = heal;
    }

    @Override
    public int getAttackValue() {
        return this.AttackValue;
    }

    @Override
    public void setAttackValue(int value) {
        this.AttackValue = value;
    }

    @Override
    public int checkDestroyStatus(int outterDamage) {
        if(this.getHealPower() - outterDamage <= 0){
            return TARGETDESTROYED;
        }
        else{
            this.setHealPower(this.getHealPower() - outterDamage);
            return TARGETALIVE;
        }
    }
}
