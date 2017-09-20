package ben.home.cn.share;

import android.graphics.Bitmap;

/**
 * Created by benhuang on 17-8-24.
 */

public class Spirit {
    // This class is to use as a share class for coming spirits
    // 设置两个内部类，分别是坐标和速度，稍后需要继续扩展
    private Bitmap _bitmap;
    private Coordinates _coordinates;
    private Speed _speed;
    private boolean _alive;

    private int _bitmapSequence;        // 特异的

    public Spirit(Bitmap bitmap) {
        this._bitmap = bitmap;
        _coordinates = new Coordinates();
        _speed = new Speed();
        _alive = true;
    }

    Spirit() {
        this._bitmap = null;
        _coordinates = new Coordinates();
        _speed = new Speed();
        _alive = false;
    }

    public Bitmap getImage(){
        return _bitmap;
    }

    public Speed getSpeed(){
        return _speed;
    }

    public Coordinates getCoordinates(){
        return _coordinates;
    }

    public void statusUpdate(){
        this.getCoordinates().set_x(getCoordinates().get_x() +
                getSpeed().get_velx()*getSpeed().get_velxDirection());
        this.getCoordinates().set_y(getCoordinates().get_y() +
                getSpeed().get_vely()*getSpeed().get_velyDirection());
    }

    // 描述速度的类
    public class Speed{
        public static final int X_DIRECTION_RIGHT = 1;
        public static final int X_DIRECTION_LEFT = -1;
        public static final int Y_DIRECTION_DOWN = 1;
        public static final int Y_DIRECTION_UP = -1;

        private int _velx = 0;
        private int _vely = 0;
        private int _velxDirection = X_DIRECTION_RIGHT;
        private int _velyDirection = Y_DIRECTION_DOWN;

        public int get_velx() {
            return _velx;
        }

        public void set_velx(int _velx) {
            this._velx = _velx;
        }

        public int get_vely() {
            return _vely;
        }

        public void set_vely(int _vely) {
            this._vely = _vely;
        }

        public int get_velxDirection() {
            return _velxDirection;
        }

        public void set_velxDirection(int _velxDirection) {
            this._velxDirection = _velxDirection;
        }

        public int get_velyDirection() {
            return _velyDirection;
        }

        public void set_velyDirection(int _velyDirection) {
            this._velyDirection = _velyDirection;
        }

        public void toggleXDirection(){
            if(_velxDirection == X_DIRECTION_RIGHT){
                _velxDirection = X_DIRECTION_LEFT;
                return;
            }
            if(_velxDirection == X_DIRECTION_LEFT){
                _velxDirection = X_DIRECTION_RIGHT;
                return;
            }
        }

        public void toggleYDirection(){
            if(_velyDirection == Y_DIRECTION_DOWN){
                _velyDirection = Y_DIRECTION_UP;
                return;
            }
            if(_velyDirection == Y_DIRECTION_UP){
                _velyDirection = Y_DIRECTION_DOWN;
                return;
            }
        }

        @Override
        public String toString() {
            return "Speed{" +
                    "_velx=" + _velx +
                    ", _vely=" + _vely +
                    ", _velxDirection=" + _velxDirection +
                    ", _velyDirection=" + _velyDirection +
                    '}';
        }
    }
    // 描述坐标的类
    public class Coordinates{
        private int _x = 0;
        private int _y = 0;

        public int get_x() {
            return _x;
        }

        public void set_x(int _x) {
            this._x = _x;
        }

        public int get_y() {
            return _y;
        }

        public void set_y(int _y) {
            this._y = _y;
        }

        @Override
        public String toString() {
            return "Coordinates{" +
                    "_x=" + _x +
                    ", _y=" + _y +
                    '}';
        }
    }

    public boolean is_alive() {
        return _alive;
    }

    public void set_alive(boolean _alive) {
        this._alive = _alive;
    }

    public boolean is_click(double x, double y) {
        if(x <= getCoordinates().get_x() + _bitmap.getWidth() && x >= getCoordinates().get_x()
                && y<=getCoordinates().get_y()+_bitmap.getHeight() && y>=getCoordinates().get_y()){
            return true;
        }
        else{
            return false;
        }
    }

    public int get_bitmapSequence() {
        return _bitmapSequence;
    }

    public void set_bitmapSequence(int _bitmapSequence) {
        this._bitmapSequence = _bitmapSequence;
    }
}
