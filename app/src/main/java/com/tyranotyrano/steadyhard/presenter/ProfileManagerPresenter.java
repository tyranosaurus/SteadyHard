package com.tyranotyrano.steadyhard.presenter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.ProfileManagerContract;
import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileManagerDataSource;

/**
 * Created by cyj on 2017-12-04.
 */

public class ProfileManagerPresenter implements ProfileManagerContract.Presenter {
    ProfileManagerContract.View mView = null;
    ProfileManagerDataSource mRepository = null;

    public void attachView(ProfileManagerContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setProfileManagerRepository(ProfileManagerDataSource profileManagerDataSource) {
        if ( profileManagerDataSource != null ) {
            this.mRepository = profileManagerDataSource;
        }
    }

    @Override
    public AlertDialog.Builder buildAlertDialog(AlertDialog.Builder builder, final EditText edittextInputPassword) {
        edittextInputPassword.setHint("비밀번호를 입력해주세요.");
        edittextInputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edittextInputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());


        builder.setIcon(R.drawable.logo_black_star);
        builder.setTitle("SteadyHard");
        builder.setMessage("\n그동안 이용해 주셔서 감사합니다.\n확인을 위해 비밀번호를 입력해주세요.");
        builder.setView(edittextInputPassword, 50, 0, 50, 0);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 회원탈퇴
                        String deletePassword = edittextInputPassword.getText().toString().trim();
                        new UserDeleteTask().execute(deletePassword);

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder;
    }

    @Override
    public void selectPictureByGallery() {
        mView.selectPictureByGallery();
    }

    @Override
    public void uploadNewProfileImage(String projectImagePath) {
        new NewProfileImageUploadTask().execute(projectImagePath);
    }

    @Override
    public void deleteNewProfileImage(String deleteFileName) {
        new NewProfileImageDeleteTask().execute(deleteFileName);
    }

    @Override
    public void updateNewProfile(String newProfileImagePath, String newNickname, String originalPassword, String newPasswordFirst, String newPasswordSecond) {
        if ( !checkProfileUpdateInput(newNickname, originalPassword, newPasswordFirst, newPasswordSecond) ) {
            return;
        }

        if ( newNickname != null ) {
            newNickname = newNickname.replace("\'", "");
            newNickname = newNickname.replace("\"", "");
        }

        new NewProfileUpdateTask().execute(newProfileImagePath, newNickname, originalPassword, newPasswordFirst);
    }

    public boolean checkProfileUpdateInput(String newNickname, String originalPassword, String newPasswordFirst, String newPasswordSecond) {
        // 닉네임 예외처리
        if ( newNickname == null || newNickname.length() < 1) {
            String message = "닉네임을 입력해주세요.";
            mView.showSnackBar(message);

            return false;
        }

        // 비밀번호 예외처리
        boolean allEmpty = (originalPassword == null || originalPassword.length() < 1)
                && (newPasswordFirst == null || newPasswordFirst.length() < 1)
                && (newPasswordSecond == null || newPasswordSecond.length() < 1);

        boolean allOverMinLength = originalPassword.length() >= 8
                && newPasswordFirst.length() >= 8
                && newPasswordSecond.length() >= 8;
        if ( !(allEmpty || allOverMinLength) ) {
            String message = "비밀번호 변경은 최소 8자 이상 입력해주세요.";
            mView.showSnackBar(message);

            return false;
        }

        if ( !newPasswordFirst.equals(newPasswordSecond) ) {
            String message = "비밀번호가 일치하지 않습니다.";
            mView.showSnackBar(message);

            return false;
        }

        return true;
    }

    public class UserDeleteTask extends AsyncTask<String, Integer, Boolean> {
        Dialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
            progressDialog = new Dialog(mView.getActivityContext(), R.style.SemoDialog);
            progressDialog.setCancelable(true);

            ProgressBar progressbar = new ProgressBar(mView.getActivityContext());
            progressbar.setIndeterminateDrawable(mView.getActivityContext().getDrawable(R.drawable.progress_dialog));

            progressDialog.addContentView(progressbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String deletePassword = params[0];
            boolean deleteResult = mRepository.deleteUser(deletePassword);

            return deleteResult;
        }

        @Override
        protected void onPostExecute(Boolean deleteResult) {
            super.onPostExecute(deleteResult);
            progressDialog.dismiss();

            if ( deleteResult ) {
                // 회원탈퇴 성공
                mView.clearSharedPreferencesData();
                mView.callLoginActivity();
            } else {
                // 회원탈퇴 실패
                String message = "회원탈퇴에 실패하였습니다. 비밀번호를 확인해 주세요.";
                mView.showSnackBar(message);
            }
        }
    }

    public class NewProfileImageUploadTask extends AsyncTask<String, Integer, String> {
        Dialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
            progressDialog = new Dialog(mView.getActivityContext(), R.style.SemoDialog);
            progressDialog.setCancelable(true);

            ProgressBar progressbar = new ProgressBar(mView.getActivityContext());
            progressbar.setIndeterminateDrawable(mView.getActivityContext().getDrawable(R.drawable.progress_dialog));

            progressDialog.addContentView(progressbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String imagePath = params[0];
            String newProfileImagePath = mRepository.uploadNewProfileImage(imagePath);

            return newProfileImagePath;
        }

        @Override
        protected void onPostExecute(String newProfileImagePath) {
            super.onPostExecute(newProfileImagePath);
            progressDialog.dismiss();

            if ( newProfileImagePath != null ) {
                // 새 프로필 사진 업로드 성공
                mView.setNewProfileImagePath(newProfileImagePath);
            } else {
                // 새 프로필 사진 업로드 실패
                String message = "새 프로필사진 업로드에 실패했습니다. 다른 사진을 선택해주세요.";
                mView.showSnackBar(message);
                // 원래 이미지로 전환
                mView.setOriginalProfileImage();
            }
        }
    }

    public class NewProfileImageDeleteTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String deleteFileName = params[0];
            boolean deleteResult = mRepository.deletedNewProfileImage(deleteFileName);

            return deleteResult;
        }

        @Override
        protected void onPostExecute(Boolean deleteResult) {
            super.onPostExecute(deleteResult);

            // empty
            /*if ( deleteResult ) {
                // 정상적으로 삭제된 경우
            } else {
                // 삭제가 안된 경우
            }*/
        }
    }

    public class NewProfileUpdateTask extends AsyncTask<String, Integer, Boolean> {
        Dialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
            progressDialog = new Dialog(mView.getActivityContext(), R.style.SemoDialog);
            progressDialog.setCancelable(true);

            ProgressBar progressbar = new ProgressBar(mView.getActivityContext());
            progressbar.setIndeterminateDrawable(mView.getActivityContext().getDrawable(R.drawable.progress_dialog));

            progressDialog.addContentView(progressbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String newProfileImagePath = params[0];
            String newNickname = params[1];
            String originalPassword = params[2];
            String newPasswordFirst = params[3];

            boolean updateResult = mRepository.updateNewProfile(newProfileImagePath, newNickname, originalPassword, newPasswordFirst);

            return updateResult;
        }

        @Override
        protected void onPostExecute(Boolean updateResult) {
            super.onPostExecute(updateResult);
            progressDialog.dismiss();

            if ( updateResult ) {
                // 프로필 업데이트 성공 시
                mView.updatedNewProfileInfo();
            } else {
                // 프로필 업데이트 실패 시
                String message = "프로필 업데이트에 실패하였습니다. 잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }
}
