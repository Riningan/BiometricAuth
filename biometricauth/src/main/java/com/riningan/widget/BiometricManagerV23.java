package com.riningan.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

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


@TargetApi(Build.VERSION_CODES.M)
public class BiometricManagerV23 {
    private static final String KEY_NAME = UUID.randomUUID().toString();


    protected Context context;

    protected String title;
    protected String subtitle;
    protected String description;
    protected String negativeButtonText;

    private BiometricDialogV23 biometricDialogV23;


    public void displayBiometricPromptV23(final BiometricCallback biometricCallback) {
        KeyStore keyStore = generateKey();
        Cipher cipher = initCipher(keyStore);
        if (cipher != null) {
            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
            FingerprintManager fingerprintManager = BiometricUtils.getFingerPrintManager(context);
            fingerprintManager.authenticate(cryptoObject, new CancellationSignal(), 0,
                    new FingerprintManager.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationError(int errMsgId, CharSequence errString) {
                            super.onAuthenticationError(errMsgId, errString);
                            updateStatus(String.valueOf(errString));
                            biometricCallback.onAuthenticationError(errMsgId, errString);
                        }

                        @Override
                        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                            super.onAuthenticationHelp(helpMsgId, helpString);
                            updateStatus(String.valueOf(helpString));
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
                            updateStatus(context.getString(R.string.biometric_failed));
                            biometricCallback.onAuthenticationFailed();
                        }
                    }, null);

            displayBiometricDialog(biometricCallback);
        }
    }


    private void displayBiometricDialog(final BiometricCallback biometricCallback) {
        biometricDialogV23 = new BiometricDialogV23(context, biometricCallback);
        biometricDialogV23.setTitle(title);
        biometricDialogV23.setSubtitle(subtitle);
        biometricDialogV23.setDescription(description);
        biometricDialogV23.setButtonText(negativeButtonText);
        biometricDialogV23.show();
    }

    private void dismissDialog() {
        if (biometricDialogV23 != null) {
            biometricDialogV23.dismiss();
        }
    }

    private void updateStatus(String status) {
        if (biometricDialogV23 != null) {
            biometricDialogV23.updateStatus(status);
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