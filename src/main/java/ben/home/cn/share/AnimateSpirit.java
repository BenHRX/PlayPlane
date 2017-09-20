package ben.home.cn.share;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by benhuang on 17-9-13.
 */

public class AnimateSpirit extends Spirit {
    private Bitmap _animateBitmap;
    private int columnCount;
    private int rowCount;
    private ArrayList<Bitmap> _animateList = new ArrayList<Bitmap>();
    private int runningCount = 0;
    private int pointer;
    private int frameTime;


    public AnimateSpirit(Bitmap animateBitmap, int columnCount, int frameTime){
        super();
        if(columnCount <= 0){
            this.columnCount = 1;
        }
        else{
            this.columnCount = columnCount;
        }
        rowCount = 1;
        this._animateBitmap = animateBitmap;
        SplitBitmap();
        setFrameTime(frameTime);
    }

    public AnimateSpirit(Bitmap animateBitmap, int columnCount, int rowCount, int frameTime){
        super();
        if(columnCount <= 0){
            this.columnCount = 1;
        }
        else{
            this.columnCount = columnCount;
        }
        if(rowCount <= 0){
            this.rowCount = 1;
        }
        else{
            this.rowCount = rowCount;
        }
        this._animateBitmap = animateBitmap;
        SplitBitmap();
        setFrameTime(frameTime);
    }

    public AnimateSpirit(ArrayList<Bitmap> tmpList, int frameTime){
        super();
        for (Bitmap bitmap : tmpList) {
            _animateList.add(bitmap);
        }
        setFrameTime(frameTime);
    }


    private void SplitBitmap(){
        Bitmap tmpBitmap;
        int width = this._animateBitmap.getWidth() / columnCount;
        int height = this._animateBitmap.getHeight() / rowCount;

        for(int i=0; i < (columnCount * rowCount); i++){
            tmpBitmap = Bitmap.createBitmap(this._animateBitmap, width * (i % columnCount), height * (i / columnCount), width, height);
            _animateList.add(i, tmpBitmap);
        }
    }

    public void runBySequence(int ms){       // Every ms to switch
        runningCount++;
        if(runningCount > (60 * ms / 1000)){
            pointer++;
            if(pointer >= _animateList.size()){
                pointer = 0;
            }
            runningCount = 0;
        }
    }


    public int getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(int frameTime) {
        this.frameTime = frameTime;
    }

    @Override
    public Bitmap getImage() {
        return _animateList.get(pointer);
    }

    @Override
    public void statusUpdate() {
        super.statusUpdate();
        if(this.getCoordinates().get_x() >= 600 || this.getCoordinates().get_x() <= 20){
            this.getSpeed().toggleXDirection();
        }
    }
}
