package com.zalexdev.simpleusb;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DB {
    public Context context;
    public DB(Context c){
        context = c;
    }
    public String getDeviceNameByPid(String pid){
        String JSON = "";
        String result = "Sorry, can't find your device in our db...";
        BufferedReader reader = null;
        JSONObject usblist = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("devices.txt")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                JSON = mLine;
            }
            usblist = new JSONObject(JSON);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        try {
            JSONArray list = usblist.getJSONArray("list");
            for (int i = 0;i<list.length();i++){
                JSONObject temp = list.getJSONObject(i);
                if (temp.has(pid)){
                    result = temp.getString(pid);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  result;

    }
}
