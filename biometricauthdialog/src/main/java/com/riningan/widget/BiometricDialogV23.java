package com.riningan.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;


public class BiometricDialogV23 extends BottomSheetDialog implements View.OnClickListener {
    private Button btnCancel;
    private ImageView ivLogo;
    private TextView tvTitle, tvDescription, tvSubtitle, tvStatus;

    private BiometricCallback mBiometricCallback;


    BiometricDialogV23(@NonNull Context context, BiometricCallback biometricCallback) {
        super(context, R.style.BottomSheetDialogTheme);
        mBiometricCallback = biometricCallback;
        setDialogView();
    }


    @Override
    public void onClick(View view) {
        dismiss();
        mBiometricCallback.onAuthenticationCancelled();
    }


    void setTitle(String title) {
        tvTitle.setText(title);
    }

    void updateStatus(String status) {
        tvStatus.setText(status);
        tvStatus.setVisibility(status == null ? View.INVISIBLE : View.VISIBLE);
    }

    void setSubtitle(String subtitle) {
        tvSubtitle.setText(subtitle);
        tvSubtitle.setVisibility(subtitle == null ? View.INVISIBLE : View.VISIBLE);
    }

    void setDescription(String description) {
        tvDescription.setText(description);
        tvDescription.setVisibility(description == null ? View.INVISIBLE : View.VISIBLE);
    }

    void setButtonText(String negativeButtonText) {
        btnCancel.setText(negativeButtonText);
    }


    private void setDialogView() {
        @SuppressLint("InflateParams")
        View bottomSheetView = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
        setContentView(bottomSheetView);

        btnCancel = findViewById(R.id.btnCancel);
        if (btnCancel != null) {
            btnCancel.setOnClickListener(this);
        }

        ivLogo = findViewById(R.id.ivLogo);
        tvTitle = findViewById(R.id.tvTitle);
        tvStatus = findViewById(R.id.tvStatus);
        if (tvStatus != null) {
            tvStatus.setVisibility(View.INVISIBLE);
        }
        tvSubtitle = findViewById(R.id.tvSubtitle);
        if (tvSubtitle != null) {
            tvSubtitle.setVisibility(View.INVISIBLE);
        }
        tvDescription = findViewById(R.id.tvDescription);
        if (tvDescription != null) {
            tvDescription.setVisibility(View.INVISIBLE);
        }

        updateLogo();
    }


    private void updateLogo() {
        try {
            Drawable drawable = getContext().getPackageManager().getApplicationIcon(getContext().getPackageName());
            ivLogo.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
