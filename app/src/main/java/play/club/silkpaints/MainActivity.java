package play.club.silkpaints;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import play.club.skecher.SkecherFragment;
import play.club.skecher.colorpicker.Picker;
import play.club.skecher.colorpicker.PickerDialog;
import play.club.skecher.style.StylesFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private SkecherFragment mSkecherFragment;
    private FloatingActionButton mFab;

    private boolean isEarsh = false;

    private int pre_brush_style = StylesFactory.RIBBON;
    private int cur_brush_style = StylesFactory.RIBBON;

    private Paint curBgColor = new Paint();
    private Paint curPaintColor = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSkecherFragment = new SkecherFragment();
        replaceFrament(mSkecherFragment);
        curBgColor.setColor(Color.BLACK);
        curPaintColor.setColor(Color.RED);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            mSkecherFragment.getSurface().clearBitmap();
            return true;
        } else if (id == R.id.action_bgcolors) {
            showBgColor();
            return true;
        } else if (id == R.id.action_colors) {
            showColors();
            return true;
        }
//        else if (id == R.id.action_radius) {
//            return true;
//        }
        else if (id == R.id.action_save) {
            savePic();
            return true;
//        } else if (id == R.id.action_width) {
//            return true;
//
        } else if (id == R.id.action_earsh) {
            if (isEarsh) {
                pre_brush_style = cur_brush_style;
                mSkecherFragment.getSurface().setStyle(StylesFactory.getStyle(pre_brush_style));
                mSkecherFragment.getSurface().setPaintColor(curPaintColor);
                item.setIcon(R.mipmap.earsh);
            } else {
                mSkecherFragment.getSurface().setStyle(StylesFactory.getStyle(StylesFactory.ERASER));
                mSkecherFragment.getSurface().setPaintColor(curBgColor);
                item.setIcon(R.mipmap.earsh_use);
            }
            isEarsh = !isEarsh;
            return true;
        } else if (id == R.id.action_undo) {
            mSkecherFragment.getSurface().undo();
            return true;
        } else if (id == R.id.action_redo) {
            mSkecherFragment.getSurface().undo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void savePic() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            mSkecherFragment.save();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mSkecherFragment.save();
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void showBgColor() {
        new PickerDialog(this, new Picker.OnColorChangedListener() {
            @Override
            public void colorChanged(Paint color) {
                curBgColor.setColor(color.getColor());
                mSkecherFragment.getSurface().setBgColor(color.getColor());
            }
        }, curBgColor, R.style.DialogNoBackground).show();
    }

    private void showColors() {

        new PickerDialog(this, new Picker.OnColorChangedListener() {
            @Override
            public void colorChanged(Paint color) {
                curPaintColor.setColor(color.getColor());
                mSkecherFragment.getSurface().setPaintColor(color);
            }
        }, mSkecherFragment.getSurface().getPaintColor(), R.style.DialogNoBackground).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setChecked(true);
        if (id == R.id.nav_lines) {
            // Handle the camera action
            cur_brush_style = StylesFactory.SIMPLE;
            mSkecherFragment.getSurface().setStyle(StylesFactory.getStyle(cur_brush_style));
        } else if (id == R.id.nav_fur) {
            cur_brush_style = StylesFactory.LONGFUR;
            mSkecherFragment.getSurface().setStyle(StylesFactory.getStyle(cur_brush_style));
        } else if (id == R.id.nav_silk) {
            cur_brush_style = StylesFactory.HACKPEN;
            mSkecherFragment.getSurface().setStyle(StylesFactory.getStyle(cur_brush_style));
        } else if (id == R.id.nav_ribbon) {
            cur_brush_style = StylesFactory.RIBBON;
            mSkecherFragment.getSurface().setStyle(StylesFactory.getStyle(cur_brush_style));
        } else if (id == R.id.nav_earsh) {
            cur_brush_style = StylesFactory.CHROME;
            mSkecherFragment.getSurface().setStyle(StylesFactory.getStyle(cur_brush_style));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFrament(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, fragment);
        transaction.commit();
        mFab.setVisibility(View.GONE);
    }
}
