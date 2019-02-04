package com.riningan.widget;

import android.content.Context;

import androidx.annotation.NonNull;


public class BiometricBuilder {
    Context mContext;
    String mTitle;
    String mSubtitle;
    String mDescription;
    String mNegativeButtonText;


    public BiometricBuilder(Context context) {
        mContext = context;
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


    public BiometricManagerBase build() {
        if (BiometricUtils.isBiometricPromptEnabled()) {
            return new BiometricManagerV28(this);
        } else {
            return new BiometricManagerV23(this);
        }
    }
}