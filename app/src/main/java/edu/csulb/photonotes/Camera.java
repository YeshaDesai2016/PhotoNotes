package edu.csulb.photonotes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class Camera extends AppCompatActivity {

    Button button,save;
    ImageView imageView;
    EditText editText;
    private static final int CAMERA_REQUEST = 1;
    int f=0;
    Bitmap finalBitmap;
    String path ="";
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = editText.getText().toString();

                    saveImage();
                    Getter getter = new Getter();
                    getter.path = path;
                    getter.caption = editText.getText().toString();
                    databaseHelper.AddData(getter);

                    onBackPressed();

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            finalBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(finalBitmap);

            f=1;
        }
    }

    private void init() {

        databaseHelper = new DatabaseHelper(getApplicationContext());

        button = (Button)findViewById(R.id.cam);
        save = (Button)findViewById(R.id.button2);
        editText = (EditText)findViewById(R.id.editText);

        imageView = (ImageView)findViewById(R.id.img);

    }

    private void saveImage() {

        String root = Environment.getExternalStorageDirectory().toString();
        File Dir = new File(root + "/PhotoDir");
        Dir.mkdirs();
        Random generator = new Random();
        int n = 100;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (Dir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            path = file.getAbsolutePath();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try{
            super.onSaveInstanceState(outState);
            if(finalBitmap!=null)
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                outState.putByteArray("image",byteArray);
                outState.putInt("flag",f);
            }
        }
        catch (Exception e)
        {
            System.out.print(e.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        byte[] byteArray = savedInstanceState.getByteArray("image");
        f = savedInstanceState.getInt("flag");
        if(byteArray!=null)
        {
            finalBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(finalBitmap);
        }
    }
}

