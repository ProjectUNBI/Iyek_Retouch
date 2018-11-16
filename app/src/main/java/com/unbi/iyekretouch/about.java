package com.unbi.iyekretouch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        final Button whatsapp = (Button) findViewById(R.id.whatsapp);
        final Button youtube = (Button) findViewById(R.id.youtube);
        whatsapp.setOnClickListener(onClickListener);
        youtube.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.whatsapp:
                    parse("https://chat.whatsapp.com/LGuYpVHKGPMDU5tLqdDKbm");
                    //DO something
                    // Toast.makeText(help.this, "whatsapp", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.youtube:
                    parse("https://youtu.be/czPKKqgTcJg");
                    //DO something
                    //Toast.makeText(help.this, "youtube", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void parse(String arg) {
        Uri uri = Uri.parse(arg); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
