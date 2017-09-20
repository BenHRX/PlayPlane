package ben.home.cn.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by benhuang on 17-9-18.
 */

public class ScoreNumber extends View {
    // private int numDigits;
    // private ArrayList<Integer> score = new ArrayList<>();
    // private ArrayList<Bitmap> scoreBitmap = new ArrayList<>();
    private HashMap<Integer, Bitmap> scoreNum = new HashMap<>();
    private ArrayList<Integer> digits;
    private long prv_score;
    Bitmap combinedBitmap = null;
    // private int numDigits;

    public ScoreNumber(Context context, HashMap<Integer, Bitmap> tmpScore){
        super(context);
        scoreNum.putAll(tmpScore);
        // scoreNumber = 0;
    }

    public Bitmap getScoreImage(long score){
        long tmpScore = score;
        if(tmpScore == prv_score && combinedBitmap != null){
            return combinedBitmap;
        }
        // numDigits = 0;
        digits = null;
        digits = new ArrayList<>();
        if(score < 0){
            digits.add(0, Integer.valueOf(0));
            // numDigits = 1;
        }
        if(score == 0){
            digits.add(0, Integer.valueOf(0));
            // numDigits = 1;
        }
        while(tmpScore > 0){
            int tmpInt = (int)tmpScore%10;
            digits.add(0, Integer.valueOf(tmpInt));
            tmpScore = tmpScore / 10;
            // numDigits++;
        }

        Bitmap tmpBitmap = scoreNum.get(Integer.valueOf(0));
        int width = tmpBitmap.getWidth();
        int height = tmpBitmap.getHeight();
        combinedBitmap = Bitmap.createBitmap(width * digits.size(), height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);
        for (int i = 0; i < digits.size(); i++) {
            canvas.drawBitmap(this.scoreNum.get(digits.get(i)), width*i, 0, null);
        }
        prv_score = tmpScore;
        return combinedBitmap;
    }
}
