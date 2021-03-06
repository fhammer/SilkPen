package play.club.skecher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * 项目名称：HackArt
 * 类描述：
 * 创建人：fuzh2
 * 创建时间：2016/6/30 16:29
 * 修改人：fuzh2
 * 修改时间：2016/6/30 16:29
 * 修改备注：
 */
public class FileHelper {
    private static final String FILENAME_PATTERN = "image_%d.png";

    private final Context context;

    private Surface surface;

    boolean isSaved = false;

    /* package */
    public FileHelper(Context context, Surface surface) {
        this.context = context;
        this.surface = surface;
    }

    private File getSDDir() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "SilkPaint");
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    Bitmap getSavedBitmap() {
        if (!isStorageAvailable()) {
            return null;
        }

        File lastFile = getLastFile(getSDDir());
        if (lastFile == null) {
            return null;
        }

        Bitmap savedBitmap = null;
        try {
            FileInputStream fis = new FileInputStream(lastFile);
            savedBitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return savedBitmap;
    }

    File getLastFile(File dir) {
        int suffix = 1;

        File newFile = null;
        File file = null;
        do {
            file = newFile;
            newFile = new File(dir, String.format(FILENAME_PATTERN, suffix));
            suffix++;
        } while (newFile.exists());

        return file;
    }

    private File getUniqueFilePath(File dir) {
        int suffix = 1;

        while (new File(dir, String.format(FILENAME_PATTERN, suffix)).exists()) {
            suffix++;
        }

        return new File(dir, String.format(FILENAME_PATTERN, suffix));
    }

    private void saveBitmap(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Bitmap bitmap = getSurface().getBitmap();
            if (bitmap == null) {
                return;
            }
            bitmap.compress(CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isStorageAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        if (!externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, R.string.sd_card_is_not_available,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    void share() {
        if (!isStorageAvailable()) {
            return;
        }

        new SaveTask() {
            protected void onPostExecute(File file) {
                isSaved = true;

                Uri uri = Uri.fromFile(file);

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/png");
                i.putExtra(Intent.EXTRA_STREAM, uri);
                context.startActivity(Intent.createChooser(i,
                        context.getString(R.string.send_image_to)));

                super.onPostExecute(file);
            }
        }.execute();
    }

    void saveToSD() {
        if (!isStorageAvailable()) {
            return;
        }

        new SaveTask().execute();
    }

    File saveBitmap() {
        File newFile = getUniqueFilePath(getSDDir());
        saveBitmap(newFile);
        notifyMediaScanner(newFile);
        return newFile;
    }

    private void notifyMediaScanner(File file) {
        Uri uri = Uri.fromFile(file);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                uri));
    }

    private class SaveTask extends AsyncTask<Void, Void, File> {
        private ProgressDialog dialog = ProgressDialog.show(context, "",
                context.getString(R.string.saving_to_sd_please_wait), true);

        protected File doInBackground(Void... none) {
            getSurface().getDrawThread().pauseDrawing();
            return saveBitmap();
        }

        protected void onPostExecute(File file) {
            dialog.dismiss();

            String absolutePath = file.getAbsolutePath();
            String sdPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
            String beautifiedPath = absolutePath.replace(sdPath, "SD:/");

            Toast.makeText(
                    context,
                    context.getString(R.string.successfully_saved_to,
                            beautifiedPath), Toast.LENGTH_LONG).show();

            getSurface().getDrawThread().resumeDrawing();
        }
    }

    private Surface getSurface() {
        return surface;
    }

}
