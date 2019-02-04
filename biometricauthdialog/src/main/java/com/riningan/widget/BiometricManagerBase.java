package com.riningan.widget;

import android.content.Context;

import androidx.annotation.NonNull;


abstract public class BiometricManagerBase {
    protected Context mContext;
    protected String mTitle;
    protected String mSubtitle;
    protected String mDescription;
    protected String mNegativeButtonText;


    BiometricManagerBase(final BiometricBuilder biometricBuilder) {
        mContext = biometricBuilder.mContext;
        mTitle = biometricBuilder.mTitle;
        mSubtitle = biometricBuilder.mSubtitle;
        mDescription = biometricBuilder.mDescription;
        mNegativeButtonText = biometricBuilder.mNegativeButtonText;
    }


    public void authenticate(@NonNull final BiometricCallback biometricCallback) {
        if (!BiometricUtils.isSdkVersionSupported()) {
            biometricCallback.onSdkVersionNotSupported();
            return;
        }
        if (!BiometricUtils.isPermissionGranted(mContext)) {
            biometricCallback.onBiometricAuthenticationPermissionNotGranted();
            return;
        }
        if (!BiometricUtils.isHardwareSupported(mContext)) {
            biometricCallback.onBiometricAuthenticationNotSupported();
            return;
        }
        if (!BiometricUtils.isFingerprintAvailable(mContext)) {
            biometricCallback.onBiometricAuthenticationNotAvailable();
            return;
        }
        displayBiometricDialog(biometricCallback);
    }



    protected abstract void displayBiometricDialog(@NonNull final BiometricCallback biometricCallback);
}
