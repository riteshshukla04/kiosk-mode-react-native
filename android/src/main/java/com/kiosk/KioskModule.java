package com.kiosk;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import android.content.Context;
import android.app.Activity;
import android.os.UserManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = KioskModule.NAME)
public class KioskModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Kiosk";
  public Activity activity;
  public Context context;
  ComponentName adminComponent;
  DevicePolicyManager adminDevicePolicyManager;
  boolean isAdmin;
  String packageName;
  

  public KioskModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.context=reactContext.getApplicationContext();
    this.activity=activity;
    this.adminComponent = RNKioskDevice.getComponentName(this.context);
    this.adminDevicePolicyManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
    this.packageName=this.context.getPackageName();
    this.isAdmin = this.adminDevicePolicyManager.isDeviceOwnerApp(this.packageName);
  }

  private void addUserRestrictions(String restrictionName){
    this.adminDevicePolicyManager.addUserRestriction(this.adminComponent,restrictionName);
  }
  private void clearUserRestrictions(String restrictionName){
    this.adminDevicePolicyManager.clearUserRestriction(this.adminComponent,restrictionName);
  }

  public void setUserRestrictions(boolean enable,String restrictionName){
    if(enable){
      addUserRestrictions(restrictionName);
      return;
    }
    clearUserRestrictions(restrictionName);
    
  }

  private void setRestrictions(boolean enable) {
    setUserRestrictions(enable,UserManager.DISALLOW_SAFE_BOOT);
    setUserRestrictions(enable,UserManager.DISALLOW_FACTORY_RESET);
    setUserRestrictions(enable,UserManager.DISALLOW_ADD_USER);
    setUserRestrictions(enable,UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA);
    setUserRestrictions(enable,UserManager.DISALLOW_ADJUST_VOLUME);
    adminDevicePolicyManager.setStatusBarDisabled(adminComponent, enable);
  }

  public void setLockTask(boolean enable){
    if(isAdmin){
      adminDevicePolicyManager.setLockTaskPackages(
              adminComponent, enable ? new String[]{packageName} : new String[]{}
          );
    }
    activity = getCurrentActivity();
    if (enable) {
        
        activity.startLockTask();
        return;
    } 
    activity.stopLockTask();
  }


  @ReactMethod
  public void enableKioskMode(Promise promise) {
    try{
      if(isAdmin){
        setRestrictions(true);
      }
      setLockTask(true);
      promise.resolve(true);
    }
    catch(Error err){
      promise.reject(err.toString());
    }
  }

  @ReactMethod
  public void disableKioskMode(Promise promise) {
   try{
      if(isAdmin){
        setRestrictions(false);
      }
      setLockTask(false);
      promise.resolve(true);
    }
    catch(Error err){
      promise.reject(err.toString());
    }
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

}
