package play.club.silkpaints;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.Timer;
import java.util.TimerTask;

import play.club.svg.PathView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends Activity {

    private final int MSG_OPEN_NEXTPAGE = 1000;

    private int[] colors = {};

    private int[] rawPaths = {R.raw.beard, R.raw.effird, R.raw.girl,
            R.raw.freegirl, R.raw.eron,
            R.raw.kiss, R.raw.songshine};

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_NEXTPAGE:
                    enterMain();
                    break;
            }
        }
    };

    private PathView mPathView;
    private Timer mTimer;

    private void enterMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        mPathView = (PathView) findViewById(R.id.pathView);
        int index = (int) (System.currentTimeMillis() % rawPaths.length);
        mPathView.setSvgResource(rawPaths[index]);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_OPEN_NEXTPAGE);
            }
        }, 4000);

        mPathView.getPathAnimator().
                delay(100).
                duration(1500).
                interpolator(new AccelerateDecelerateInterpolator()).
                start();
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.onDestroy();
    }
}
