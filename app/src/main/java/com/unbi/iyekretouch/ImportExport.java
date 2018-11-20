package com.unbi.iyekretouch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static com.unbi.iyekretouch.PublicStaticMethods.CUSTOMWORDOBJ;
import static com.unbi.iyekretouch.PublicStaticMethods.USERSAVEPREFERANCE;

public class ImportExport extends FileProvider {

    private CustomWords mycustomword;

    public ImportExport() {
        mycustomword = new CustomWords();
    }

    public File export(Context context) {
        CustomWords custum = new CustomWords();
        SharedPreferences sharedPreferences = context.getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(CUSTOMWORDOBJ)) {
            final Gson gson = new Gson();
            custum = gson.fromJson(sharedPreferences.getString(CUSTOMWORDOBJ, ""), CustomWords.class);
        }
        //Convert to GSON STRING
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(custum);
        //Save the String
        String path = Environment.getExternalStorageDirectory().getPath();
        String fileName = "IyekCustumWord" + String.valueOf(System.currentTimeMillis());
        fileName = fileName + ".iyek.custum";
        File file = new File(path, fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            byte[] data = serializedObject.getBytes();
            out.write(data);
            out.close();
            Log.e("TAG", "File Save : " + file.getPath());
            Toast.makeText(context, "File Saved in : " + file.getPath(), Toast.LENGTH_SHORT).show();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "Please give write permission to this app", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Please give write permission to this app", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }


    protected void whatsappshare(Activity activity) {
        if(!appInstalledOrNot("com.whatsapp",activity)){
            return;
        }

        File file =export(activity);
        if(file==null){
//            Toast.makeText(activity, "File Cannot be write...\n Please give Storage Permission", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri path = FileProvider.getUriForFile(activity, "com.unbi.iyekretouch", file);
        //Log.d("hvjkh",String.valueOf(path));

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, path);
        share.setPackage("com.whatsapp");

        activity.startActivity(share);


    }

    public CustomWords importcus(Context context) {
        //REad from share preferance
        CustomWords custum = new CustomWords();
        SharedPreferences sharedPreferences = context.getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(CUSTOMWORDOBJ)) {
            final Gson gson = new Gson();
            custum = gson.fromJson(sharedPreferences.getString(CUSTOMWORDOBJ, ""), CustomWords.class);
        }
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(this);
        //Readfrom file
        //convert to JSON
        //add all to CustomWord
        return custum;
    }

    private String createImageOnSDCard(int resID,Context con) {
        Bitmap bitmap = BitmapFactory.decodeResource(con.getResources(), resID);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + resID + ".jpg";
        File file = new File(path);
        try {
            OutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getPath();
    }
    private boolean appInstalledOrNot(String uri, Context contexti) {
        PackageManager pm = contexti.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

}
