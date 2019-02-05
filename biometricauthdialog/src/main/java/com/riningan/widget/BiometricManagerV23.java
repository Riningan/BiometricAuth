package com.riningan.widget;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.view.View;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import androidx.annotation.NonNull;


@TargetApi(Build.VERSION_CODES.M)
public class BiometricManagerV23 extends BiometricManagerBase {
    private static final String KEY_NAME = UUID.randomUUID().toString();
    private static final int HIDE_DIALOG_DELAY = 2000; // ms


    private String mHelpMessage;
    private BiometricDialogV23 mBiometricDialogV23;

    private Handler mResetHelpMessageHandler = new Handler();


    BiometricManagerV23(BiometricBuilder biometricBuilder) {
        super(biometricBuilder);
        mHelpMessage = biometricBuilder.mHelpMessage;
    }


    @Override
    protected void displayBiometricDialog(@NonNull final BiometricCallback biometricCallback) {
        if (TextUtils.isEmpty(mTitle)) {
            throw new IllegalArgumentException("Title must be set and non-empty");
        }
        if (TextUtils.isEmpty(mNegativeButtonText)) {
            throw new IllegalArgumentException("Negative text must be set and non-empty");
        }
        final String authenticationFailedText = mActivity.getString(R.string.fingerprint_dialog_failed);

        KeyStore keyStore = generateKey();
        Cipher cipher = initCipher(keyStore);
        if (cipher != null) {
            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
            final CancellationSignal cancellationSignal = new CancellationSignal();
            FingerprintManager fingerprintManager = BiometricUtils.getFingerPrintManager(mActivity);
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0
                    , new FingerprintManager.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationError(int errMsgId, CharSequence errString) {
                            super.onAuthenticationError(errMsgId, errString);
                            // ignore: Fingerprint operation canceled.
                            if (errMsgId != 5) {
                                setErrorMessage(String.valueOf(errString));
                                biometricCallback.onAuthenticationError(errMsgId, errString);
                                if (errMsgId == 9) {
                                    mResetHelpMessageHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismissDialog();
                                        }
                                    }, HIDE_DIALOG_DELAY);
                                }
                            }
                        }

                        @Override
                        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                            super.onAuthenticationHelp(helpMsgId, helpString);
                            setHelpMessage(String.valueOf(helpString));
                            biometricCallback.onAuthenticationHelp(helpMsgId, helpString);
                        }

                        @Override
                        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            dismissDialog();
                            biometricCallback.onAuthenticationSuccessful();
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            setErrorMessage(authenticationFailedText);
                            biometricCallback.onAuthenticationFailed();
                        }
                    }, null);

            mBiometricDialogV23 = new BiometricDialogV23(mActivity, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                    cancellationSignal.cancel();
                    biometricCallback.onAuthenticationCancelled();
                }
            });
            mBiometricDialogV23.setTitle(mTitle);
            mBiometricDialogV23.setSubtitle(mSubtitle);
            mBiometricDialogV23.setDescription(mDescription);
            mBiometricDialogV23.setHelpMessage(mHelpMessage);
            mBiometricDialogV23.setButtonText(mNegativeButtonText);
            mBiometricDialogV23.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancellationSignal.cancel();
                    biometricCallback.onAuthenticationCancelled();
                }
            });
            mBiometricDialogV23.show();
        }
    }


    private void dismissDialog() {
        if (mBiometricDialogV23 != null) {
            mBiometricDialogV23.dismiss();
            mBiometricDialogV23 = null;
        }
        mResetHelpMessageHandler.removeCallbacksAndMessages(null);
    }


    private void setErrorMessage(String status) {
        if (mBiometricDialogV23 != null) {
            mBiometricDialogV23.setErrorMessage(status);
            mResetHelpMessageHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setHelpMessage(mHelpMessage);
                }
            }, HIDE_DIALOG_DELAY);
        }
    }

    private void setHelpMessage(String status) {
        if (mBiometricDialogV23 != null) {
            mBiometricDialogV23.setHelpMessage(status);
        }
    }


    private KeyStore generateKey() {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            keyStore = null;
        }
        return keyStore;
    }

    private Cipher initCipher(KeyStore keyStore) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES
                    + "/" + KeyProperties.BLOCK_MODE_CBC
                    + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher;
        } catch (KeyPermanentlyInvalidatedException e) {
            return null;
        } catch (KeyStoreException
                | CertificateException
                | UnrecoverableKeyException
                | IOException
                | NoSuchAlgorithmException
                | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}