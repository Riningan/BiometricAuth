package com.riningan.widget;

import android.app.Activity;

import androidx.annotation.NonNull;


public class BiometricBuilder {
    Activity mActivity;
    String mTitle;
    String mSubtitle;
    String mDescription;
    String mNegativeButtonText;
    String mHelpMessage;


    public BiometricBuilder(Activity activity) {
        mActivity = activity;
    }


    public BiometricBuilder setTitle(@NonNull final String title) {
        mTitle = title;
        return this;
    }

    public BiometricBuilder setSubtitle(@NonNull final String subtitle) {
        mSubtitle = subtitle;
        return this;
    }

    public BiometricBuilder setDescription(@NonNull final String description) {
        mDescription = description;
        return this;
    }

    public BiometricBuilder setNegativeButtonText(@NonNull final String negativeButtonText) {
        mNegativeButtonText = negativeButtonText;
        return this;
    }

    /**
     * only for pre Android-P
     */
    public BiometricBuilder setHelpMessage(@NonNull final String helpMessage) {
        mHelpMessage = helpMessage;
        return this;
    }


    public BiometricManagerBase build() {
        if (BiometricUtils.isBiometricPromptEnabled()) {
            return new BiometricManagerV28(this);
        } else {
            return new BiometricManagerV23(this);
        }
    }
}