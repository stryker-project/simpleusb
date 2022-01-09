package com.zalexdev.simpleusb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public Boolean usbstate = false;
    public DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView info = findViewById(R.id.info);
        LottieAnimationView img = findViewById(R.id.img);
        db = new DB(this);
        checkforusb(img,info);
    }
    public void checkforusb(LottieAnimationView img, TextView info){
        Timer usb = new Timer();
        usb.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                boolean temp = usbstate;
                usbstate = isConnected();
                if (temp != usbstate && usbstate){
                    runOnUiThread(() -> {
                        img.setAnimation(R.raw.wifi_dongle);
                        img.playAnimation();
                        info.setText("["+getpid()+"] "+db.getDeviceNameByPid(getpid()));
                    });
                }else if (temp != usbstate){
                    runOnUiThread(() -> {
                        img.setAnimation(R.raw.wait_usb);
                        img.playAnimation();
                        info.setText(getResources().getString(R.string.wait));
                    });
                }
            }
        },0,1000);
    }
    public boolean isConnected() {
        return getpid() != null;
    }
    public String getpid(){
        String deviceid = null;
        UsbManager manager = (UsbManager) MainActivity.this.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        for (String deviceName : devices.keySet()) {
            UsbDevice device = devices.get(deviceName);
            String string2 = Integer.toHexString(device.getVendorId());
            while (string2.length() < 4) {
                string2 = "0" + string2;
            }
            String string3 = Integer.toHexString(device.getProductId());
            while (string3.length() < 4) {
                string3 = "0" + string3;
            }
            deviceid = string2 + ":" + string3;
        }
        return deviceid;
    }
}