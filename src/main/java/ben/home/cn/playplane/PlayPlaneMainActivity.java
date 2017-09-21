package ben.home.cn.playplane;

//import android.app.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class PlayPlaneMainActivity extends AppCompatActivity {
//public class PlayPlaneMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);  // Full screen mode, remove title
        getSupportActionBar().hide();       /* AppCompatActivity use this to become full screen */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Full screen, no status bar
//        setContentView(R.layout.activity_play_plane_main);
        setContentView(new PlaneFightView(this));
    }
}
