package com.riningan.widget;

import android.app.Activity;

import androidx.annotation.NonNull;


abstract public class BiometricManagerBase {
    Activity mActivity;
    String mTitle;
    String mSubtitle;
    String mDescription;
    String mNegativeButtonText;


    BiometricManagerBase(final BiometricBuilder biometricBuilder) {
        mActivity = biometricBuilder.mActivity;
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
        if (!BiometricUtils.isPermissionGranted(mActivity)) {
            biometricCallback.onBiometricAuthenticationPermissionNotGranted();
            return;
        }
        if (!BiometricUtils.isHardwareSupported(mActivity)) {
            biometricCallback.onBiometricAuthenticationNotSupported();
            return;
        }
        if (!BiometricUtils.isFingerprintAvailable(mActivity)) {
            biometricCallback.onBiometricAuthenticationNotAvailable();
            return;
        }
        displayBiometricDialog(biometricCallback);
        mActivity = null;
    }


    protected abstract void displayBiometricDialog(@NonNull final BiometricCallback biometricCallback);
}