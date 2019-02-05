# BiometricAuthDialog
Add Biometric Authentication Dialog to any Android app</br>

This library provides an easy way to implement fingerprint authentication without having to deal with all the boilerplate stuff going on inside.


USAGE
---

1. Include the library

```gradle
dependencies {
        implementation 'com.riningan.widget:biometricauthdialog:1.0'
}
```

2. Build dialog

```
new BiometricBuilder(MainActivity.this)
                        .setTitle("Add a title")
                        .setSubtitle("Add a subtitle")
                        .setDescription("Add a description")
                        .setNegativeButtonText("Add a cancel button")
                        .setHelpMessage("Add help message only for pre AndroidP")
                        .build()
                        .authenticate(biometricCallback);
```

3. Implement callback

The ```BiometricCallback``` class has the following callback methods:

```
new BiometricCallback() {
              @Override
              public void onSdkVersionNotSupported() {
                     /*  
                      *  Will be called if the device sdk version does not support Biometric authentication
                      */
               }

               @Override
               public void onBiometricAuthenticationNotSupported() {
                     /*  
                      *  Will be called if the device does not contain any fingerprint sensors 
                      */
               }

               @Override
               public void onBiometricAuthenticationNotAvailable() {
                    /*  
                     *  The device does not have any biometrics registered in the device.
                     */
               }

               @Override
               public void onBiometricAuthenticationPermissionNotGranted() {
                      /*  
                       *  android.permission.USE_BIOMETRIC permission is not granted to the app
                       */
               }

               @Override
               public void onAuthenticationFailed() {
                      /*  
                       * When the fingerprint doesn’t match with any of the fingerprints registered on the device, 
                       * then this callback will be triggered.
                       */
               }

               @Override
               public void onAuthenticationCancelled() {
                       /*  
                        * The authentication is cancelled by the user. 
                        */
               }

               @Override
               public void onAuthenticationSuccessful() {
                        /*  
                         * When the fingerprint is has been successfully matched with one of the fingerprints   
                         * registered on the device, then this callback will be triggered. 
                         */
               }

               @Override
               public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                         /*  
                          * This method is called when a non-fatal error has occurred during the authentication 
                          * process. The callback will be provided with an help code to identify the cause of the 
                          * error, along with a help message.
                          */
                }

                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                         /*  
                          * When an unrecoverable error has been encountered and the authentication process has 
                          * completed without success, then this callback will be triggered. The callback is provided 
                          * with an error code to identify the cause of the error, along with the error message. 
                          */
                 }
              });

```


LICENCE
-----

  	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	   http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
