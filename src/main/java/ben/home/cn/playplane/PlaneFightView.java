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
    private BackGround _background;
    private SceneControl director = SceneControl.START;
    AstroidRock astroidRock;

    GameMainThread gameThread;

    public PlaneFightView(Context context) {
        super(context);
        // get the Surface Holder
        _holder = getHolder();
        _holder.addCallback(this);      // 注册回调函数
        director = SceneControl.START;
        gameInit();
    }

    private void gameInit() {
        _background = new BackGround(BitmapFactory.decodeResource(getResources(), R.drawable.bluespace));
        astroidRock = new AstroidRock(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1));
    }

    private void gameProcessing(){
        _background.refresh();              // Always keep running
        switch(director){
            case START:
                astroidRock.refreshStatus();
                break;
            case END:
                break;
            default:
                break;
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameThread = new GameMainThread();
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        return;
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
        switch(director){
            case START:
                canvas.drawBitmap(astroidRock.getImage(), astroidRock.getCoordinates().get_x(),
                        astroidRock.getCoordinates().get_y(), null);
                break;
            case END:
                break;
            default:
                break;
        }
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
                    gameProcessing();
                    onDraw(canvas);
                }
                surfaceHolder.unlockCanvasAndPost(canvas);

                try {
                    //Thread.sleep(1000/60);
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
