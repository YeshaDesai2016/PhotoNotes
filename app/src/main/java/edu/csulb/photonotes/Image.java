package edu.csulb.photonotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Image extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        DatabaseHelper sqLiteHelper = new DatabaseHelper(getApplicationContext());
        Getter getter = new Getter();

        getter = sqLiteHelper.GetData(id);

        Bitmap myBitmap = BitmapFactory.decodeFile(getter.path);

        imageView.setImageBitmap(myBitmap);

        textView.setText(getter.caption);

        getter = sqLiteHelper.GetData(id);


    }
}
