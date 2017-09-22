package ben.home.cn.playplane;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

import java.util.Random;

import ben.home.cn.share.Spirit;

/**
 * Created by benhuang on 17-9-22.
 */

public class AstroidRock extends Spirit {

    private static final int MAXROTATEINFRAME = 5;
    private Bitmap originBitmap;
    private Bitmap changedBitmap;
    private Bitmap realBitmap;
    private int rotateInFrame;
    private float totalAngle;
    private float originBitmapWidth;
    private float originBitmapHeight;
    private double realBitmapLength;             // Must be rectangle

    private RectF collisonRectangle;


    String TAG = "Astroid use >";

    AstroidRock(Bitmap astroidBitmap){
        super();
        originBitmap = astroidBitmap;
        Random rand = new Random(System.currentTimeMillis());
        rotateInFrame = Math.abs(rand.nextInt() % MAXROTATEINFRAME);          // Use random degree for init
        getSpeed().set_vely(1);
        getSpeed().set_velyDirection(1);
        getCoordinates().set_y(0);
        getCoordinates().set_x(100);
        totalAngle = 0.0f;
        originBitmapWidth = originBitmap.getWidth();
        originBitmapHeight = originBitmap.getHeight();
        realBitmapLength = Math.sqrt(originBitmapWidth*originBitmapWidth +
                originBitmapHeight*originBitmapHeight);
        collisonRectangle = new RectF((float)(realBitmapLength - originBitmapWidth)/2,
                (float)(realBitmapLength - originBitmapHeight)/2,
                (float)(realBitmapLength + originBitmapWidth)/2,
                (float)(realBitmapLength + originBitmapHeight)/2);
    }

    /*public int getRotateInFrame() {
        return rotateInFrame;
    }*/

    /*public void setRotateInFrame(int rotateInFrame) {
        this.rotateInFrame = rotateInFrame;
    }*/

    public void refreshStatus(){

        super.statusUpdate();

        // changedBitmap.recycle();

        int AsdWidth = originBitmap.getWidth();
        int AsdHeight = originBitmap.getHeight();
        totalAngle = totalAngle + rotateInFrame;
        if(totalAngle >=360){
            totalAngle = totalAngle - 360;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(totalAngle, originBitmap.getWidth()/2, originBitmap.getHeight()/2);
        matrix.postRotate(totalAngle);
        // matrix.postRotate(rotateInFrame);
        changedBitmap = Bitmap.createBitmap(originBitmap, 0, 0, AsdWidth, AsdHeight, matrix, true);
        // originBitmap = changedBitmap;
        // changedBitmap = null;
        //changedBitmap.recycle();
        realBitmap = Bitmap.createBitmap((int)Math.ceil(realBitmapLength), (int)Math.ceil(realBitmapLength),
                originBitmap.getConfig());
        Canvas canvas = new Canvas(realBitmap);
        canvas.drawBitmap(changedBitmap, (float)(realBitmapLength - changedBitmap.getWidth())/2,
                (float)(realBitmapLength - changedBitmap.getHeight())/2, null);

        Log.v(TAG, "The astroid is at (" + getCoordinates().get_x() + ", " + getCoordinates().get_y() + ")");
        Log.v(TAG, "The astroid pic size is (" + changedBitmap.getWidth() + ", " + changedBitmap.getHeight() + ")");
        /*this.getCoordinates().set_y(this.getCoordinates().get_y() +
                this.getSpeed().get_vely()*this.getSpeed().get_velyDirection());
        this.getCoordinates().set_x(this.getCoordinates().get_x() +
                this.getSpeed().get_velx() * this.getSpeed().get_velxDirection());*/

    }

    @Override
    public Bitmap getImage() {
        //return this.changedBitmap;
        return realBitmap;
    }

    public boolean checkCollison(RectF rect){
        return this.collisonRectangle.contains(rect);
    }

    public RectF getCollisonRectangle(){
        collisonRectangle.offsetTo(0.0f, 0.0f);
        collisonRectangle.offset(this.getCoordinates().get_x() + (float)(realBitmapLength - originBitmapWidth)/2,
                this.getCoordinates().get_y() + (float)(realBitmapLength - originBitmapHeight)/2);
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
}
