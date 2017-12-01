package com.tyranotyrano.steadyhard.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tyranotyrano.steadyhard.R;

public class NewSteadyProjectActivity extends AppCompatActivity {

    // 리퀘스트 코드
    static final int GALLERY_CODE = 100;

    ImageView imageViewNewProjectBack = null;
    ImageView imageViewProjectImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_steady_project);

        /** 툴바 세팅 */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNewProject);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 앱 기본 이름 안보이게 하는 것.

        // Back 화살표 눌렀을 때
        imageViewNewProjectBack = (ImageView) findViewById(R.id.imageViewNewProjectBack);
        imageViewNewProjectBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Project 이미지 설정
        imageViewProjectImage = (ImageView) findViewById(R.id.imageViewProjectImage);
        imageViewProjectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPictureByGallery();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case GALLERY_CODE:
                    getPicture(data); //갤러리에서 가져오기
                    break;
                default:
                    break;
            }
        }
    }

    // 갤러리에서 사진을 가져오는 경우
    private void selectPictureByGallery() {
        //사진가져오기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_CODE);
    }

    private void getPicture(Intent data) {

        Glide.with(this) // 애플리케이션의 Context 넣으면 에러발생 -> this(NewProjectActivity)를 넣어준다.
                .load(data.getData()) // 불러올 이미지 경로
                .into(imageViewProjectImage); // 이미지 넣어줄 ImageView
    }
}
