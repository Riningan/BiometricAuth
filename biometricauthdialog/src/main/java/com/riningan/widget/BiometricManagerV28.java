package com.riningan.widget;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.annotation.NonNull;


public class BiometricManagerV28 extends BiometricManagerBase {
    BiometricManagerV28(BiometricBuilder biometricBuilder) {
        super(biometricBuilder);
    }


    @Override
    @TargetApi(Build.VERSION_CODES.P)
    protected void displayBiometricDialog(@NonNull final BiometricCallback biometricCallback) {
        new BiometricPrompt.Builder(mContext)
                .setTitle(mTitle)
                .setSubtitle(mSubtitle)
                .setDescription(mDescription)
                .setNegativeButton(mNegativeButtonText
                        , mContext.getMainExecutor()
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                biometricCallback.onAuthenticationCancelled();
                            }
                        })
                .build()
                .authenticate(new CancellationSignal()
                        , mContext.getMainExecutor()
                        , new BiometricPrompt.AuthenticationCallback() {
                            @Override
                            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);
                                biometricCallback.onAuthenticationSuccessful();
                            }

                            @Override
                            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                                super.onAuthenticationHelp(helpCode, helpString);
                                biometricCallback.onAuthenticationHelp(helpCode, helpString);
                            }

                            @Override
                            public void onAuthenticationError(int errorCode, CharSequence errString) {
                                super.onAuthenticationError(errorCode, errString);
                                biometricCallback.onAuthenticationError(errorCode, errString);
                            }

                            @Override
                            public void onAuthenticationFailed() {
                                super.onAuthenticationFailed();
                                biometricCallback.onAuthenticationFailed();
                            }
                        });
    }
}