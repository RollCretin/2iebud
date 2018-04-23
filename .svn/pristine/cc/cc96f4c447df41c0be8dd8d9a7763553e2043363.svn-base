package com.hxjf.dubei.zxing.android;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.hxjf.dubei.R;
import com.hxjf.dubei.ui.activity.uploadActivity;
import com.hxjf.dubei.util.BitmapUtils;
import com.hxjf.dubei.util.StatusBarUtils;
import com.hxjf.dubei.zxing.camera.CameraManager;
import com.hxjf.dubei.zxing.view.ViewfinderView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/**
 * 这个activity打开相机，在后台线程做常规的扫描；它绘制了一个结果view来帮助正确地显示条形码，在扫描的时候显示反馈信息，
 * 然后在扫描成功的时候覆盖扫描结果
 */
public final class CaptureActivity extends Activity implements
        SurfaceHolder.Callback {

    private static final String TAG = "10086";
    private static final int REQUEST_CODE = 10086;

    // 相机控制
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private IntentSource source;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    // 电量控制
    private InactivityTimer inactivityTimer;
    // 声音、震动控制
    private BeepManager beepManager;

    private TextView tv_back;
    private ImageButton select_pic;
    private String photo_path;
    private Bitmap scanBitmap;
    private ProgressDialog mProgress;

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * OnCreate中初始化一些辅助类，如InactivityTimer（休眠）、Beep（声音）以及AmbientLight（闪光灯）
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // 保持Activity处于唤醒状态
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.capture);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.note_bg);

        hasSurface = false;

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        tv_back = (TextView) findViewById(R.id.capture_textview_back);
        tv_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        select_pic = (ImageButton) findViewById(R.id.capture_imageview_select_pic);
//        select_pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SelectPic();
//            }
//        });
    }

    private void SelectPic() {
        //选择相册中的条形码

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager必须在这里初始化，而不是在onCreate()中。
        // 这是必须的，因为当我们第一次进入时需要显示帮助页，我们并不想打开Camera,测量屏幕大小
        // 当扫描框的尺寸不正确时会出现bug
        cameraManager = new CameraManager(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);

        handler = null;

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // activity在paused时但不会stopped,因此surface仍旧存在；
            // surfaceCreated()不会调用，因此在这里初始化camera
            initCamera(surfaceHolder);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }

        beepManager.updatePrefs();
        inactivityTimer.onResume();

        source = IntentSource.NONE;
        decodeFormats = null;
        characterSet = null;
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    /**
     * 扫描成功，处理反馈信息
     *
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();

        boolean fromLiveScan = barcode != null;
        //这里处理解码完成后的结果，此处将参数回传到Activity处理
        if (fromLiveScan) {
            beepManager.playBeepSoundAndVibrate();

            String content = rawResult.getText();
            //匹配ISBN 书籍的ISBN码是以978或者979开头
            if (content.startsWith("978") || content.startsWith("979")) {
                Toast.makeText(this, "扫描成功", Toast.LENGTH_SHORT).show();
//                Intent intent = getIntent();
//                intent.putExtra("codedBitmap", barcode);
//                setResult(RESULT_OK, intent);
                Intent uploadIntent = new Intent(this, uploadActivity.class);
                uploadIntent.putExtra("codedContent", rawResult.getText());
                startActivity(uploadIntent);
                finish();
            } else {
                Toast.makeText(this, "ISBN条形码格式错误", Toast.LENGTH_SHORT).show();

            }
        }

    }

    /**
     * 初始化Camera
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats,
                        decodeHints, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 显示底层错误信息并退出应用
     */
    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    /**
                     *解决问题1,路径的判断,拿到真确的路径
                     */
                    String filePath = "$$";
                    int sdkInt = Build.VERSION.SDK_INT; //兼容4.4
                    Uri contentUri = data.getData();
                    if (sdkInt == 19 && DocumentsContract.isDocumentUri(CaptureActivity.this, contentUri)) {
                        String wholeID = DocumentsContract
                                .getDocumentId(contentUri);
                        String id = wholeID.split(":")[1];
                        String[] column = {MediaStore.Images.Media.DATA};
                        String sel = MediaStore.Images.Media._ID + "=?";
                        Cursor cursor = CaptureActivity.this.getContentResolver().query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                column, sel, new String[]{id}, null);
                        int columnIndex = cursor.getColumnIndex(column[0]);
                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(columnIndex);
                        }
                        cursor.close();
                    } else if (!TextUtils.isEmpty(contentUri.getAuthority())) {
                        Cursor cursor = getContentResolver().query(contentUri,
                                new String[]{MediaStore.Images.Media.DATA},
                                null, null, null);
                        if (null == cursor) {
                            Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
                        }
                        cursor.moveToFirst();
                        filePath = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close();
                    } else {
                        filePath = data.getData().getPath();
                    }
                    /**到此为止路径判断完成,问题一解决*/
                    try {
                        Log.d(TAG, "1onActivityResult: " + data.getData().getPath());
                        String path = BitmapUtils.getPath(CaptureActivity.this, data.getData());
                        Log.d(TAG, "onActivityResult: " + path);
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        long bitmapsize = getBitmapsize(bitmap);
                        if (bitmapsize > 4000000) {  //大图
                            //解析并且请求数据
                            /**这里就解决了问题二*/
                            analysisAndRequestResultCode(path, false);
                        } else {  //小图  true表示要不用压缩
                            analysisAndRequestResultCode(path, true);
                        }
                    } catch (NotFoundException e) {
                        Toast.makeText(this, "图片解析失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, "图片解析失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    break;
                default:
                    Log.d(TAG, "onActivityResult:error,default. ");
                    break;
            }
        }
    }

    private void analysisAndRequestResultCode(String filePath, boolean isAvailableSize) throws FileNotFoundException, NotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 200);

        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(filePath, options);
        Log.d(TAG, "analysisAndRequestResultCode: " + scanBitmap.toString());
        int width = scanBitmap.getWidth();
        int height = scanBitmap.getHeight();
        int[] ints = new int[width * height];

        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(width, height, ints);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));//把可视图片转为二进制图片

        Hashtable<DecodeHintType, Object> decodeHints = new Hashtable<DecodeHintType, Object>();
        decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        Reader reader = new MultiFormatReader();
        Result result = null;
        try {
            result = reader.decode(binaryBitmap, decodeHints);
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取bitmap的size
     *
     * @param bitmap
     * @return
     */
    public long getBitmapsize(Bitmap bitmap) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();

    }


}
