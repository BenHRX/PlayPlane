package ben.home.cn.playplane;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import ben.home.cn.share.BackGround;

/**
 * Created by benhuang on 17-9-20.
 */

public class PlaneFightView extends SurfaceView implements SurfaceHolder.Callback{

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    private static MediaPlayer music;    // For background music
//    private static SoundPool.Builder soundEffect;   // See SoundPool comment
    private static SoundPool soundEffectPool;
    private int timeTick = 0;
    private final SurfaceHolder _holder;
    private BackGround _background;
    private SceneControl director = SceneControl.START;
    // AstroidRock astroidRock;
    ArrayList<AstroidRock> astroidRocks = new ArrayList<>();
    SpaceShip spaceShip;
    LinkedList<Bullet> bullets = new LinkedList<>();
    // Decide to make a Boom class to show the boom status for the corresponding object(eg: Astroid)
    LinkedList<Boom> boomList = new LinkedList<>();
    HashMap<String, Integer> soundPool = new HashMap<>();
    private Bitmap bulletBitmap;

    GameMainThread gameThread;

    String TAG = "Main View (Test) >";

    public PlaneFightView(Context context) {
        super(context);
        music = MediaPlayer.create(context, R.raw.song18);
        music.setLooping(true);
        soundEffectPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);  // Should use soundpool.builder to make the sound pool now, but I am now hope the older machine also can use.
//        soundEffect = new SoundPool.Builder();
        soundPool.put("explode",soundEffectPool.load(context, R.raw.explode, 1));   // soundpool load return int
        /// / get the Surface Holder
        _holder = getHolder();
        _holder.addCallback(this);      // 注册回调函数
        director = SceneControl.START;
        gameInit();
    }

    private void gameInit() {
        _background = new BackGround(BitmapFactory.decodeResource(getResources(), R.drawable.bluespace));
        if(!music.isPlaying()) {
            music.start();
        }
        /*astroidRock = new AstroidRock(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1));*/
        /*astroidRocks.add(new AstroidRock(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1)));          // rock 01
        astroidRocks.add(new AstroidRock(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1)));          // rock 02
        astroidRocks.add(new AstroidRock(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1)));          // rock 03*/
        ArrayList<Bitmap> spaceShipBitmaps = new ArrayList<>();
        spaceShipBitmaps.add(0, BitmapFactory.decodeResource(getResources(),R.drawable.spaceship));
        spaceShipBitmaps.add(1, BitmapFactory.decodeResource(getResources(), R.drawable.ship_thrust));
        spaceShip = new SpaceShip(spaceShipBitmaps, 20);
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
                collisonCheck();
                boomUpdate();
                break;
            case END:
                break;
            default:
                break;
        }
    }

    private void boomUpdate() {
        for (Boom tmpB : boomList) {
            tmpB.boomStatusUpdate();
        }
    }

    private void collisonCheck() {
        // To check the Astroid vs Spaceship and the Bullet vs Astroid method
        // 1. Spaceship vs Astroid
        // 2. Bullet vs Astroid
        for (int i = astroidRocks.size()-1; i >= 0; i--) {
            if(astroidRocks.get(i).is_alive() && spaceShip.is_alive() &&
                    spaceShip.checkCollison(astroidRocks.get(i).getCollisonRectangle()))
            {
                spaceShip.set_alive(false);
                boomList.add(new Boom(BitmapFactory.decodeResource(getResources(),
                        R.drawable.explosion2), 4, 2, spaceShip.getCoordinates().get_x(),
                        spaceShip.getCoordinates().get_y()));

                return;
            }
            for (Bullet tmpBullet:bullets) {
                if(tmpBullet.checkCollison(astroidRocks.get(i).getCollisonRectangle())){
                    tmpBullet.set_alive(false);
                    astroidRocks.get(i).set_alive(false);
                    boomList.add(new Boom(BitmapFactory.decodeResource(getResources(),
                            R.drawable.explosion), 4, 4, astroidRocks.get(i).getCoordinates().get_x(),
                            astroidRocks.get(i).getCoordinates().get_y()));
                    soundEffectPool.play(soundPool.get("explode"), 1.0f, 1.0f, 0, 1, 1.0f);
                }
            }
        }
    }


    private void cleanNotAlive() {              // Whether really need to use this method to clear the objects??
        for (int i = astroidRocks.size()-1; i >= 0; i--) {
            if(!astroidRocks.get(i).is_alive()){
                astroidRocks.remove(i);
            }
        }
        Iterator<Bullet> bulletIter = bullets.listIterator();
        while(bulletIter.hasNext()){
            Bullet tmpBulletStatus = bulletIter.next();
            if(!tmpBulletStatus.is_alive()){
                bulletIter.remove();
            }
        }
        Iterator<Boom> boomIter = boomList.listIterator();
        while(boomIter.hasNext()){
            Boom tmpBoom = boomIter.next();
            if(!tmpBoom.is_alive()){
                boomIter.remove();
            }
        }
        if(!spaceShip.is_alive()){

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
        if(timeTick > 10 && spaceShip.is_alive()){
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
//        Log.v(TAG, "The bullets size is " + bullets.size());
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameThread = new GameMainThread();
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() != MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_MOVE){
            return false;
        }
//        return super.onTouchEvent(event);
        synchronized (_holder){
            float targetX = event.getX();
            float targetY = event.getY();
//            Log.v(TAG, "The spaceship now go to (" + targetX + "," + targetY+")");
            spaceShip.setTarget(targetX, targetY);
            return true;
        }
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
                for(Boom tmpBoom : boomList){
                    if(tmpBoom.is_alive()){
                        canvas.drawBitmap(tmpBoom.getImage(), tmpBoom.getCoordinates().get_x(),
                                tmpBoom.getCoordinates().get_y(), null);
                    }
                }
                if(spaceShip.is_alive()) {
                    canvas.drawBitmap(spaceShip.getImage(), spaceShip.getCoordinates().get_x(),
                            spaceShip.getCoordinates().get_y(), null);
                    canvas.drawRect(spaceShip.getCollisonRectangle(), paint);
                }
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
            music.release();
            soundEffectPool.release();
            try {
                join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
