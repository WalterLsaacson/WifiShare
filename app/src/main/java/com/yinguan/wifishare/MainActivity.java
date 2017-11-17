package com.yinguan.wifishare;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.yinguan.wifishare.model.WifiInfo;
import com.yinguan.wifishare.model.WifiInfoLab;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    boolean connected;
    static String sPwd;
    WifiInfo mWifiInfo;
    WifiInfoLab mWifiInfoLab;
    AlertDialog mDialog;
    boolean modifyInfo;

    ImageView qr_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qr_code = findViewById(R.id.test_qr_code);
        qr_code.setImageBitmap(generateBitmap("content",500,500));
        /*mWifiInfoLab = WifiInfoLab.getsInstance(MainActivity.this);
        mTextView = findViewById(R.id.wifi_status);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connected) {
                    chooseFun();
                } else {
                    enterPassword();
                }
            }
        });*/
    }

    private void chooseFun() {
        final LinearLayout linearLayout = (LinearLayout) LayoutInflater
                .from(MainActivity.this)
                .inflate(R.layout.choose_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(linearLayout)
                .setTitle("Choose function")
                .setNegativeButton("cancel", null);
        final Button modify = linearLayout.findViewById(R.id.modify);
        Button share = linearLayout.findViewById(R.id.share);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyInfo = true;
                mDialog.dismiss();
                enterPassword();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                showQRCode();
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    private void enterPassword() {
        final LinearLayout linearLayout = (LinearLayout) LayoutInflater
                .from(MainActivity.this)
                .inflate(R.layout.dialog_view, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter password")
                .setView(linearLayout)
                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = linearLayout.findViewById(R.id.pass_word);
                        sPwd = editText.getText().toString();
                        startActivity(new Intent(MainActivity.this, MainActivity
                                .class));
                    }
                })
                .setNegativeButton("cancel", null);
        mDialog = builder.create();
        mDialog.show();
    }

    @Override
    protected void onResume() {
        if (sPwd != null && sPwd.equals("lavalava")) {
            connected = true;
            mTextView.setText("connected");
            //add to database
            mWifiInfo = new WifiInfo("starOS", sPwd);
            if (modifyInfo) {
                mWifiInfoLab.updateWifi(mWifiInfo);
            } else {
                mWifiInfoLab.addWifi(mWifiInfo);
            }
        }
        super.onResume();
    }

    private void showQRCode() {
        mWifiInfo = mWifiInfoLab.getWifiInfo(mWifiInfo.getSsid());
        LinearLayout linearLayout = (LinearLayout) LayoutInflater
                .from(MainActivity.this).inflate(R.layout.dialog_qr, null);
        TextView textView = linearLayout.findViewById(R.id.qr_info);
        ImageView imageView = linearLayout.findViewById(R.id.qr_code);
        textView.setText(mWifiInfo.toString());
        imageView.setImageBitmap(generateBitmap(mWifiInfo.toString(), 500, 500));
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("WifiInfo")
                .setView(linearLayout);
        mDialog = builder.create();
        mDialog.show();
    }

    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height,
                    hints);
            Log.e("MainActivity gengen",encode.toString());
            encode = deleteWhite(encode);
            width = encode.getWidth();
            height = encode.getHeight();
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        Log.e("MainActivity", Arrays.toString(rec));
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }
}
