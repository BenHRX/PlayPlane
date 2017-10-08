package ben.home.cn.playplane;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import ben.home.cn.share.BackGround;

/**
 * Created by benhuang on 17-9-20.
 */

public class PlaneFightView extends SurfaceView implements SurfaceHolder.Callback{

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    private int timeTick = 0;
    private final SurfaceHolder _holder;
    private BackGround _background;
    private SceneControl director = SceneControl.START;
    // AstroidRock astroidRock;
    ArrayList<AstroidRock> astroidRocks = new ArrayList<>();
    SpaceShip spaceShip;
    LinkedList<Bullet> bullets = new LinkedList<>();
    private Bitmap bulletBitmap;

    GameMainThread gameThread;

    String TAG = "Main View (Test) >";

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
        /*astroidRock = new AstroidRock(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1));*/
        /*astroidRocks.add(new AstroidRock(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1)));          // rock 01
        astroidRocks.add(new AstroidRock(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1)));          // rock 02
        astroidRocks.add(new AstroidRock(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1)));          // rock 03*/
        ArrayList<Bitmap> spaceShipBitmaps = new ArrayList<>();
        spaceShipBitmaps.add(0, BitmapFactory.decodeResource(getResources(),R.drawable.spaceship));
        spaceShipBitmaps.add(1, BitmapFactory.decodeResource(getResources(), R.drawable.ship_thrust));
        spaceShip = new SpaceShip(spaceShipBitmaps, 60);
        timeTick = 0;
        bulletBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plasmashot);
        bulletUpdate();
        rebornNewRock();
        rebornSpaceShip();
    }

    private void gameProcessing(){
        _background.refresh();              // Always keep running
        switch(director){
            case START:
                for(AstroidRock astroidRock : astroidRocks) {
                    if(astroidRock.is_alive()){
                        astroidRock.refreshStatus();
                    }
                }
                spaceShip.statusUpdate();
                bulletUpdate();
                break;
            case END:
                break;
            default:
                break;
        }
    }


    private void cleanNotAlive() {
        for (int i = astroidRocks.size()-1; i >= 0; i--) {
            if(!astroidRocks.get(i).is_alive()){
                astroidRocks.remove(i);
            }
        }
    }

    private void rebornNewRock(){
        AstroidRock tmpAstroidRock;
        boolean validAstroid;
        while(astroidRocks.size() < 3){
//            astroidRocks.add(new AstroidRock(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1)));
//            图片太大，空间不大，要修改图片大小才能使用检测
            tmpAstroidRock = new AstroidRock(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1));
            if(astroidRocks.size() == 0){
                astroidRocks.add(tmpAstroidRock);
                continue;
            }
            validAstroid = true;
            for (AstroidRock a: astroidRocks) {
                if(tmpAstroidRock.checkCollison(a.getCollisonRectangle())){
                    validAstroid = false;
                }
            }
            if(validAstroid){
                astroidRocks.add(tmpAstroidRock);
            }
        }
    }

    private void rebornSpaceShip() {
        return;
    }

    private void bulletUpdate() {
        timeTick++;
        if(timeTick > 10){
            Bullet bullet = new Bullet(bulletBitmap, spaceShip.getCoordinates().get_centerX(),
                    spaceShip.getCoordinates().get_y());
            bullets.add(bullet);
            timeTick = 0;
        }
        for(Bullet tmpBullet : bullets){
            tmpBullet.statusUpdate();
        }
        // Clear not alive bullet
        /*for (int i = bullets.size()-1; i >= 0; i--) {
            if(!bullets.get(i).is_alive()){
                bullets.remove(i);
            }
        }*/
        Iterator<Bullet> bulletIter = bullets.listIterator();
        while(bulletIter.hasNext()){
            Bullet tmpBulletStatus = bulletIter.next();
            if(!tmpBulletStatus.is_alive()){
                bulletIter.remove();
            }
        }
//        Log.v(TAG, "The bullets size is " + bullets.size());
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
        Paint paint = new Paint();
        super.onDraw(canvas);
        canvas.drawBitmap(_background.getImage(), _background.getCoordinates().get_x(),
                _background.getCoordinates().get_y(), null);
        switch(director){
            case START:
                for(AstroidRock astroidRock : astroidRocks){
                    if(astroidRock.is_alive()){
                        canvas.drawBitmap(astroidRock.getImage(), astroidRock.getCoordinates().get_x(),
                                astroidRock.getCoordinates().get_y(), null);
                        paint.setColor(Color.RED);
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawRect(astroidRock.getCollisonRectangle(), paint);
                    }
                }
                for(Bullet tmpBullet : bullets){
                    if(tmpBullet.is_alive()){
                        canvas.drawBitmap(tmpBullet.getImage(), tmpBullet.getCoordinates().get_x(),
                                tmpBullet.getCoordinates().get_y(), null);
                        canvas.drawRect(tmpBullet.getCollisonRectangle(), paint);
                    }
                }
                canvas.drawBitmap(spaceShip.getImage(), spaceShip.getCoordinates().get_x(),
                        spaceShip.getCoordinates().get_y(), null);
                canvas.drawRect(spaceShip.getCollisonRectangle(), paint);
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
                    cleanNotAlive();                    // Must be in this sequence...
                    rebornNewRock();
                    gameProcessing();
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
