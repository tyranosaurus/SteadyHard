package com.tyranotyrano.steadyhard.presenter;

import android.os.AsyncTask;

import com.tyranotyrano.steadyhard.contract.JoinContract;
import com.tyranotyrano.steadyhard.model.remote.datasource.JoinDataSource;

import java.util.regex.Pattern;

/**
 * Created by cyj on 2017-11-30.
 */

public class JoinPresenter implements JoinContract.Presenter {
    JoinContract.View mView = null;
    JoinDataSource mRepository = null;

    public void attachView(JoinContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setJoinRepository(JoinDataSource joinDataSource) {
        if ( joinDataSource != null ) {
            this.mRepository = joinDataSource;
        }
    }

    @Override
    public void checkEmailDuplication(String email) {

        String regExp = "\\w+@\\w+\\.\\w+(\\.\\w+)?";
        boolean checkEmail = Pattern.matches(regExp,email);

        if ( email == null || email.length() < 1 ) {
            String message = "이메일을 입력해주세요.";
            mView.showSnackBar(message);

            return;
        }

        if ( !checkEmail ) {
            String message = "이메일 형식이 올바르지 않습니다.";
            mView.showSnackBar(message);

            return;
        }


        new EmailDuplicationTask().execute(email);
    }

    @Override
    public void createNewUser(String email, String passwordFirst, String passwordSecond) {
        if ( !checkJoinInput(email, passwordFirst, passwordSecond) ) {
            return;
        }

        new NewUserTask().execute(email, passwordFirst);
    }

    @Override
    public void selectPictureByGallery() {
        mView.selectPictureByGallery();
    }

    @Override
    public void uploadProfileImage(String profileImagePath) {
        new ProfileImageUploadTask().execute(profileImagePath);
    }

    @Override
    public void saveProfileInfo(String userProfileImagePath, String nickname, String email) {
        if ( nickname == null || nickname.length() < 1 ) {
            String message = "닉네임을 입력해주세요.";
            mView.showSnackBar(message);

            return;
        }

        new ProfileInfoSaveTask().execute(userProfileImagePath, nickname, email);
    }

    public boolean checkJoinInput(String email, String passwordFirst, String passwordSecond) {
        String regExp = "\\w+@\\w+\\.\\w+(\\.\\w+)?";
        boolean checkEmail = Pattern.matches(regExp,email);

        if ( email == null || email.length() < 1 ) {
            String message = "이메일을 입력해주세요.";
            mView.showSnackBar(message);

            return false;
        }

        if ( !checkEmail ) {
            String message = "이메일 형식이 올바르지 않습니다.";
            mView.showSnackBar(message);

            return false;
        }

        if ( passwordFirst == null || passwordFirst.length() < 1 ) {
            String message = "비밀번호를 입력해주세요.";
            mView.showSnackBar(message);

            return false;
        }

        if ( passwordFirst.length() < 8 ) {
            String message = "비밀번호는 최소 8자 이상 입력해주세요.";
            mView.showSnackBar(message);

            return false;
        }

        if ( !passwordFirst.equals(passwordSecond) ) {
            String message = "비밀번호가 일치하지 않습니다.";
            mView.showSnackBar(message);

            return false;
        }

        return true;
    }

    public class EmailDuplicationTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String email = params[0];

            boolean availableEmail = mRepository.isEmailDuplication(email);

            return availableEmail;
        }

        @Override
        protected void onPostExecute(Boolean availableEmail) {
            super.onPostExecute(availableEmail);

            if ( availableEmail ) {
                mView.checkedEmailDuplication();
                mView.showSnackBar("사용할 수 있는 이메일입니다.");
            } else {
                mView.showSnackBar("이미 가입된 회원입니다.");
            }
        }
    }

    public class NewUserTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            boolean isJoinSuccess = mRepository.createNewUser(email, password);

            return isJoinSuccess;
        }

        @Override
        protected void onPostExecute(Boolean isJoinSuccess) {
            super.onPostExecute(isJoinSuccess);

            if ( isJoinSuccess ) {
                mView.joinedNewUser();
                mView.showProfileSettings();

                String message = "정상적으로 회원가입 되었습니다.\n프로필 정보를 입력해 주세요.";
                mView.showSnackBar(message);
            } else {
                String message = "회원가입에 실패하였습니다. ";
                mView.showSnackBar(message);
            }
        }
    }

    public class ProfileImageUploadTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
        }

        @Override
        protected String doInBackground(String... params) {
            String imagePath = params[0];
            String userProfileImagePath = mRepository.uploadProfileImage(imagePath);

            return userProfileImagePath;
        }

        @Override
        protected void onPostExecute(String userProfileImagePath) {
            super.onPostExecute(userProfileImagePath);

            if ( userProfileImagePath != null ) {
                // 이미지 전송에 성공한 경우
                mView.setUserProfileImagePath(userProfileImagePath);
            } else {
                // 이미지 전송에 실패한 경우
                String message = "프로필사진 설정에 실패하였습니다.\n 다른 사진을 선택해주세요.";
                mView.showSnackBar(message);
                // 디폴트 이미지로 전환
                mView.setDefaultProfileImage();
            }
        }
    }

    public class ProfileInfoSaveTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String userProfileImagePath = params[0];
            String nickname = params[1];
            String email = params[2];

            boolean saveResult = mRepository.saveProfileInfo(userProfileImagePath, nickname, email);

            return saveResult;
        }

        @Override
        protected void onPostExecute(Boolean saveResult) {
            super.onPostExecute(saveResult);

            if ( saveResult ) {
                String message = "회원가입을 완료하였습니다.\n로그인 후 이용해주세요.";
                mView.finishJoinActivity(message);
            } else {
                String message = "프로필 설정에 실패했습니다. 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }
}
