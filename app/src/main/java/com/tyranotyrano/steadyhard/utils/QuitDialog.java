package com.tyranotyrano.steadyhard.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.widget.TextView;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.view.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cyj on 2017-12-13.
 */

public class QuitDialog extends Dialog {

    MainActivity activity = null;

    @BindView(R.id.textViewCancel) TextView textViewCancel;
    @BindView(R.id.textViewQuit) TextView textViewQuit;

    public QuitDialog(@NonNull Context context) {
        super(context);

        this.activity = (MainActivity)context;
    }

    public QuitDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_quit);
        // ButterKnife 세팅
        ButterKnife.bind(this);
    }

    @OnClick(R.id.textViewCancel)
    public void onDialogCancelClick() {
        dismiss();
    }

    @OnClick(R.id.textViewQuit)
    public void onDialogQuitClick() {
        // 다이얼로그 먼저 dismiss() 해야함!
        dismiss();
        // 액티비티 종료
        activity.finish();
    }
}
