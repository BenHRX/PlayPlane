package ben.home.cn.playplane;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ben.home.cn.share.BackGround;

/**
 * Created by benhuang on 17-9-20.
 */

public class PlaneFightView extends SurfaceView implements SurfaceHolder.Callback{

    private final SurfaceHolder _holder;
    BackGround _background;

    GameMainThread gameThread;

    public PlaneFightView(Context context) {
        super(context);
        // get the Surface Holder
        _holder = getHolder();
        _holder.addCallback(this);      // 注册回调函数
        gameInit();
    }

    private void gameInit() {
        _background = new BackGround(BitmapFactory.decodeResource(getResources(), R.drawable.bluespace));
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameThread = new GameMainThread();
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        gameThread.requestWaitAndExit();
        gameThread = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(_background.getImage(), _background.getCoordinates().get_x(),
                _background.getCoordinates().get_y(), null);
    }

    public class GameMainThread extends Thread{

        private boolean _threadDone;

        GameMainThread(){
            super();
            this._threadDone = false;
        }

        @Override
        public void run() {
            super.run();
            SurfaceHolder surfaceHolder = _holder;

            while(!this._threadDone)
            {
                Canvas canvas = surfaceHolder.lockCanvas();
                synchronized (canvas){
                    onDraw(canvas);
                }
                surfaceHolder.unlockCanvasAndPost(canvas);

                try {
                    Thread.sleep(1000/60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void requestWaitAndExit(){
            this._threadDone = true;
            try {
                join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
