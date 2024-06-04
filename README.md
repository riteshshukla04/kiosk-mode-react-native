# kiosk-mode-react-native

A Library for setting up kiosk mode on Android with react native

## Installation

```sh
npm install kiosk-react-native
```

## Usage

```js
import { enableKioskMode, disableKioskMode } from 'kiosk-react-native';

// Enable Kiosk Mode
enableKioskMode();

// Disable Kiosk Mode
disableKioskMode();

```
## For Higher Order Access
We need device owner permission for the app for higher order access like disable volume buttons,prevent uninstallation etc.

Add below configuration to AndroidManifest.xml(android/app/src/main/AndroidManifest.xml) 

Add android:testOnly . This will allow to developers to uninstall and remove admin for the app.
```xml
    <application
      android:name=".MainApplication"
      android:label="@string/app_name"
      android:supportsRtl="true"
      android:testOnly="true" 
      ...Rest of the App
```
Now create a new "device_owner.xml" file (android/app/src/main/xml/device_owner.xml) and add the following code.
```xml
    <?xml version="1.0" encoding="utf-8"?>
    <device-admin>
        <uses-policies>
            <limit-password/>
            <watch-login/>
            <reset-password/>
            <force-lock/>
            <wipe-data/>
            <expire-password/>
            <encrypted-storage/>
            <disable-camera/>
        </uses-policies>
    </device-admin>
```


Add a new reciever to your AndroidManifest.xml(android/app/src/main/AndroidManifest.xml).
```xml
    <receiver
        android:name="com.kiosk.RNKioskDevice"
        android:label="@string/app_name"
        android:exported="true"
        android:permission="android.permission.BIND_DEVICE_ADMIN">
        <meta-data
            android:name="android.app.device_admin"
            android:resource="@xml/device_owner" />
        <intent-filter>
            <action android:name="android.app.action.DEVICE_ADMIN_ENABLED"/>
        </intent-filter>
    </receiver>
```
Finally run the following script using adb
```shell
adb shell dpm set-device-owner <YourPackageName>/com.kiosk.RNKioskDevice
```
Rebuild the app. The app should run in admin mode now.



To Remove the device from owner
```shell
adb shell dpm remove-active-admin <YourPackageName>/com.kiosk.RNKioskDevice
```

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
