package com.riningan.widget;

import android.hardware.biometrics.BiometricPrompt;
import androidx.annotation.RequiresApi;
import android.os.Build;


@RequiresApi(api = Build.VERSION_CODES.P)
public class BiometricCallbackV28 extends BiometricPrompt.AuthenticationCallback {
    private BiometricCallback mBiometricCallback;


    public BiometricCallbackV28(BiometricCallback biometricCallback) {
        mBiometricCallback = biometricCallback;
    }


    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        mBiometricCallback.onAuthenticationSuccessful();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);
        mBiometricCallback.onAuthenticationHelp(helpCode, helpString);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        mBiometricCallback.onAuthenticationError(errorCode, errString);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        mBiometricCallback.onAuthenticationFailed();
    }
}