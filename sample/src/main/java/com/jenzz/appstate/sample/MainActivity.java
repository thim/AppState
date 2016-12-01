package com.jenzz.appstate.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.jenzz.appstate.AppStateMonitor;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.content.Intent.ACTION_GET_CONTENT;

public class MainActivity extends AppCompatActivity {

  private static final int RESULT_CODE_PICK_IMAGE = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    View pickImageButton = findViewById(R.id.pick_image);
    //noinspection ConstantConditions
    pickImageButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getAppStateMonitor().stop();
        pickImage();
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    getAppStateMonitor().start();
    if (resultCode == RESULT_OK && requestCode == RESULT_CODE_PICK_IMAGE) {
      showImage(data.getData());
    }
  }

  private void pickImage() {
    Intent intent = new Intent(ACTION_GET_CONTENT);
    intent.setType("image/*");
    startActivityForResult(intent, RESULT_CODE_PICK_IMAGE);
  }

  private void showImage(Uri selectedImage) {
    try {
      InputStream imageStream = getContentResolver().openInputStream(selectedImage);
      Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
      ImageView imageView = (ImageView) findViewById(R.id.image_result);
      //noinspection ConstantConditions
      imageView.setImageBitmap(bitmap);
    } catch (FileNotFoundException e) {
      // Ouch!
    }
  }

  private AppStateMonitor getAppStateMonitor() {return ((SampleApplication) getApplication()).getAppStateMonitor();}
}