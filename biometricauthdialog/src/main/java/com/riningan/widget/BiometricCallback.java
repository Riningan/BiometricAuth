package com.riningan.widget;


public interface BiometricCallback {
    /**
     * Will be called if the device sdk version does not support Biometric authentication
     */
    void onSdkVersionNotSupported();

    /**
     * Will be called if the device does not contain any fingerprint sensors
     */
    void onBiometricAuthenticationNotSupported();

    /**
     * The device does not have any biometrics registered in the device.
     */
    void onBiometricAuthenticationNotAvailable();

    /**
     * android.permission.USE_BIOMETRIC permission is not granted to the app
     * or android.permission.USE_FINGERPRINT permission is not granted to the app
     */
    void onBiometricAuthenticationPermissionNotGranted();

    /**
     * When the fingerprint doesn’t match with any of the fingerprints registered on the device,
     * then this callback will be triggered.
     */
    void onAuthenticationFailed();

    /**
     * The authentication is cancelled by the user.
     */
    void onAuthenticationCancelled();

    /**
     * When the fingerprint is has been successfully matched with one of the fingerprints
     * registered on the device, then this callback will be triggered.
     */
    void onAuthenticationSuccessful();

    /**
     * This method is called when a non-fatal error has occurred during the authentication
     * process. The callback will be provided with an help code to identify the cause of the
     * error, along with a help message.
     */
    void onAuthenticationHelp(int helpCode, CharSequence helpString);

    /**
     * When an unrecoverable error has been encountered and the authentication process has
     * completed without success, then this callback will be triggered. The callback is provided
     * with an error code to identify the cause of the error, along with the error message.
     */
    void onAuthenticationError(int errorCode, CharSequence errString);
}