package ben.home.cn.playplane;

import android.graphics.Bitmap;
import android.graphics.Matrix;
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
    private int rotateInFrame;
    private float totalAngle;

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

        Log.v(TAG, "The astroid is at (" + getCoordinates().get_x() + ", " + getCoordinates().get_y() + ")");
        Log.v(TAG, "The astroid pic size is (" + changedBitmap.getWidth() + ", " + changedBitmap.getHeight() + ")");
        /*this.getCoordinates().set_y(this.getCoordinates().get_y() +
                this.getSpeed().get_vely()*this.getSpeed().get_velyDirection());
        this.getCoordinates().set_x(this.getCoordinates().get_x() +
                this.getSpeed().get_velx() * this.getSpeed().get_velxDirection());*/

    }

    @Override
    public Bitmap getImage() {
        return this.changedBitmap;
    }
}
