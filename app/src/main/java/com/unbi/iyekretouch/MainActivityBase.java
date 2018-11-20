package com.unbi.iyekretouch;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.unbi.iyekretouch.PublicStaticMethods.CUSTOMWORDOBJ;
import static com.unbi.iyekretouch.PublicStaticMethods.USERSAVEPREFERANCE;

public class MainActivityBase extends myExtraActivity {
    protected Intent myintent;
    protected BroadcastReceiver broadcastReceiver;
    protected boolean buttonIyekClick = false;
    private CustomWords custumword;

    private String readUri(Uri uri) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int result;
                String content = "";
                while ((result = inputStream.read(buffer)) != -1) {
                    content = content.concat(new String(buffer, 0, result));
                }
                return content;
            }
        } catch (IOException e) {
            Log.e("receiver", "IOException when reading uri", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("receiver", "IOException when closing stream", e);
                }
            }
        }
        return null;
    }

    protected void viewhelper(Intent intent) {
        Uri a = intent.getData();
        if (!a.toString().startsWith("content:")) {
            doimportfile(a);
            return;
        }
        String content = readUri(a);
//        //Log.d("Hello my sadcsacsd", content);
        if(content.length()<1){
            return;
        }
        doimportcustumword(content);
    }

    private void doimportcustumword(String content) {
        custumword=new CustomWords();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(CUSTOMWORDOBJ)) {
            final Gson gson = new Gson();
            custumword = gson.fromJson(sharedPreferences.getString(CUSTOMWORDOBJ, ""), CustomWords.class);
        }
        CustomWords addable=null;
        final Gson gson = new Gson();
        try{
        addable = gson.fromJson(content, CustomWords.class);}catch (Exception e){
            Toast.makeText(this, "Fail to Import..Illegal Format...", Toast.LENGTH_SHORT).show();
        }

        if(addable!=null){
            custumword.addCustomWordMap(addable.getCustomWordMap(),this);
            Toast.makeText(this, "Custom Words Imported", Toast.LENGTH_SHORT).show();
        }

    }

    protected void handleSendText(Intent intent) {
        Uri myUri;
        myUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (myUri != null) {
//                if(String.valueOf(myUri).contains("iyek"));
            doimportfile(myUri);
        }
    }

    private void doimportfile(Uri myUri) {
        //Log.d("URI", myUri.toString());
//        if (!myUri.toString().contains("iyek")) {
//            Toast.makeText(this, "Unsupported File type", Toast.LENGTH_SHORT).show();
//            return;
//        }
        File f = new File(myUri.getPath());
        if (!f.exists()) {
            Toast.makeText(this, "File doesn't exist..", Toast.LENGTH_SHORT).show();
            return;
        }
        long size = f.length();
        if (size > 1000000) {
            Toast.makeText(this, "File Too Large...", Toast.LENGTH_SHORT).show();
            //Log.d("File too large", "File Should Importing");
            return;
        }
        //Log.d("File should Import", myUri.toString());
        doimportcustumword(readfile(f));
    }

    private String readfile(File myExternalFile) {



        String myData = "";
        if(!permissionFile()){
            Toast.makeText(this, "Read Permission not granted to this App", Toast.LENGTH_SHORT).show();
            return myData;
        }
        try {
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine + "\n";
            }
            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myData;
    }

    private boolean permissionFile() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            return true;
        }
        return false;
    }

    protected void handleSendTextMultipe(Intent intent) {
        Uri myUri = null;
        ArrayList<Uri> myUriarray = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (myUriarray != null) {
            // Update UI to reflect multiple images being shared
//                //Log.d("File should Import","File Should Import ARRAy");
            doimportfile(myUriarray.get(0));
        }
    }



//    protected void showoptimiseui() {
//        Intent intent2 = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
//        startActivity(intent2);
//    }
//
//    protected boolean batteryoptimize() {
//
//            String permission = "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS";
//            int res = this.checkCallingOrSelfPermission(permission);
//            return (res == PackageManager.PERMISSION_GRANTED);
//
//    }


}
