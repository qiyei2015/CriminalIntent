package com.qiyei.criminalintent.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qiyei.criminalintent.R;
import com.qiyei.criminalintent.utils.Utils;

import java.util.UUID;

public class PhotoViewActivity extends AppCompatActivity {
    private ImageView mPhotoViewDetail;
    private String mImagePath;
    private UUID mUUID;
    private static final String EXTRA_CRIME_ID = "com.qiyei.activity.crime_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        mPhotoViewDetail = (ImageView) findViewById(R.id.crime_photo_view_detail);
        mImagePath = getIntent().getStringExtra("photoView");
        mUUID = (UUID) getIntent().getSerializableExtra("uuid");

        updatePhotoView(mImagePath);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(this,CrimePagerActivity.class);
            intent.putExtra(EXTRA_CRIME_ID,mUUID);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /***
     * 更新PhotoView图片显示
     * @param path
     */
    private void updatePhotoView(String path){
        if (path != null){
            ViewGroup.LayoutParams params = mPhotoViewDetail.getLayoutParams();
            Log.d("布局大小:","width:" + params.width + ", height:" + params.height);

            Bitmap bitmap = Utils.getScaledBitmap(path,this);
            mPhotoViewDetail.setImageBitmap(bitmap);
        }
    }


}
