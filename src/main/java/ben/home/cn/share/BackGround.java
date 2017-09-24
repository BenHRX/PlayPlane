package ben.home.cn.share;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by benhuang on 17-9-20.
 */

public class BackGround extends Spirit {

    private Bitmap bigBitmap;
    private int bgWidth;
    private int bgHeight;

    public BackGround(Bitmap background){
        super();
        if (background != null) {
            bgWidth = background.getWidth();
            bgHeight = background.getHeight();
            bigBitmap = Bitmap.createBitmap(bgWidth, bgHeight*2, background.getConfig());
            Canvas canvas = new Canvas(bigBitmap);
            canvas.drawBitmap(background, 0, 0, null);
            canvas.drawBitmap(background, 0, bgHeight, null);
            this.getCoordinates().set_y(0 - bgHeight);
            this.getSpeed().set_vely(2);
            this.getSpeed().set_velyDirection(1);
        }
    }

    public void refresh(){
        this.getCoordinates().set_y(this.getCoordinates().get_y() +
                this.getSpeed().get_vely() * this.getSpeed().get_velyDirection());
        if(this.getCoordinates().get_y() >= 0){
            this.getCoordinates().set_y(0 - bgHeight);
        }
    }

    @Override
    public Bitmap getImage() {
        return this.bigBitmap;
    }

    @Override
    public String toString() {
        return "BackGround{" +
                "bigBitmap=" + bigBitmap +
                ", bgWidth=" + bgWidth +
                ", bgHeight=" + bgHeight +
                '}';
    }
}
