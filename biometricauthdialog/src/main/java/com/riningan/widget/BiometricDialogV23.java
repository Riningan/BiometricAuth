package com.riningan.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.annotation.NonNull;


public class BiometricDialogV23 extends BottomSheetDialog {
    private static final int STATE_NONE = 0;
    private static final int STATE_FINGERPRINT = 1;
    private static final int STATE_FINGERPRINT_ERROR = 2;
    private static final int STATE_FINGERPRINT_AUTHENTICATED = 3;


    private final int mErrorColor;
    private final int mHelpColor;

    private Button btnCancel;
    private ImageView ivIcon;
    private TextView tvTitle, tvDescription, tvSubtitle, tvError;

    private int mLastState = STATE_NONE;


    BiometricDialogV23(@NonNull Context context, View.OnClickListener onClickListener) {
        super(context, R.style.BottomSheetDialogTheme);
        mErrorColor = context.getResources().getColor(R.color.fingerprint_dialog_error_color, null);
        mHelpColor = context.getResources().getColor(R.color.fingerprint_dialog_text_light_color, null);
        setDialogView();
        btnCancel.setOnClickListener(onClickListener);
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mLastState = STATE_FINGERPRINT;
        /*
         * using static fingerprint icon because sometime animation work wrong
          */
        ivIcon.setImageResource(R.drawable.ic_fingerprint);
    }


    void setTitle(String title) {
        tvTitle.setText(title);
    }

    void setSubtitle(String subtitle) {
        tvSubtitle.setText(subtitle);
        tvSubtitle.setVisibility(subtitle == null ? View.GONE : View.VISIBLE);
    }

    void setDescription(String description) {
        tvDescription.setText(description);
        tvDescription.setVisibility(description == null ? View.GONE : View.VISIBLE);
    }

    void setButtonText(String negativeButtonText) {
        btnCancel.setText(negativeButtonText);
    }

    void setErrorMessage(String error) {
        updateFingerprintIcon(STATE_FINGERPRINT_ERROR);
        tvError.setText(error);
        tvError.setTextColor(mErrorColor);
        tvError.setContentDescription(error);
    }

    void setHelpMessage(String help) {
        updateFingerprintIcon(STATE_FINGERPRINT);
        if (help == null) {
            tvError.setText(R.string.fingerprint_dialog_touch_sensor);
        } else {
            tvError.setText(help);
        }
        tvError.setTextColor(mHelpColor);
    }


    private void setDialogView() {
        @SuppressLint("InflateParams")
        View bottomSheetView = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
        setContentView(bottomSheetView);

        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        tvDescription = findViewById(R.id.tvDescription);
        ivIcon = findViewById(R.id.ivFingerprintIcon);
        tvError = findViewById(R.id.tvError);
        btnCancel = findViewById(R.id.btnCancel);
    }


    private void updateFingerprintIcon(int newState) {
        Drawable icon = getAnimationForTransition(mLastState, newState);
        if (icon == null) {
            return;
        }
        final AnimatedVectorDrawable animation = icon instanceof AnimatedVectorDrawable
                ? (AnimatedVectorDrawable) icon
                : null;
        ivIcon.setImageDrawable(icon);
        if (animation != null && shouldAnimateForTransition(mLastState, newState)) {
            try {
                @SuppressLint("PrivateApi")
                Method method = animation.getClass().getDeclaredMethod("forceAnimationOnUI");
                method.setAccessible(true);
                method.invoke(animation);
            } catch (NoSuchMethodException ignored) {
            } catch (IllegalAccessException ignored) {
            } catch (InvocationTargetException ignored) {
            }
            animation.start();
        }
        mLastState = newState;
    }

    private boolean shouldAnimateForTransition(int oldState, int newState) {
        if (oldState == STATE_NONE && newState == STATE_FINGERPRINT) {
            return false;
        } else if (oldState == STATE_FINGERPRINT && newState == STATE_FINGERPRINT_ERROR) {
            return true;
        } else if (oldState == STATE_FINGERPRINT_ERROR && newState == STATE_FINGERPRINT) {
            return true;
        } else if (oldState == STATE_FINGERPRINT && newState == STATE_FINGERPRINT_AUTHENTICATED) {
            return false;
        } else {
            return false;
        }
    }

    private Drawable getAnimationForTransition(int oldState, int newState) {
        int iconRes;
        if (oldState == STATE_NONE && newState == STATE_FINGERPRINT) {
            iconRes = R.drawable.fingerprint_dialog_fp_to_error;
        } else if (oldState == STATE_FINGERPRINT && newState == STATE_FINGERPRINT_ERROR) {
            iconRes = R.drawable.fingerprint_dialog_fp_to_error;
        } else if (oldState == STATE_FINGERPRINT_ERROR && newState == STATE_FINGERPRINT) {
            iconRes = R.drawable.fingerprint_dialog_error_to_fp;
        } else if (oldState == STATE_FINGERPRINT && newState == STATE_FINGERPRINT_AUTHENTICATED) {
            iconRes = R.drawable.fingerprint_dialog_error_to_fp;
        } else {
            return null;
        }
        return getContext().getDrawable(iconRes);
    }
}