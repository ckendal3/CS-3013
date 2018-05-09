package com.wdibstudios.platerate;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseIntArray;

/**
 * Created by Jerry Kendall on 5/5/2018.
 */

public abstract class AbstractRuntimePermission extends Activity {


    private SparseIntArray mErrorString;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    mErrorString = new SparseIntArray();

    }

    public abstract void onPermissionsGranted(int requestCode);

    public void requestAppPermissions(final String[] requestedPermissions, final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean showRequestPermissions = false;
        for(String permission: requestedPermissions){
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            showRequestPermissions = showRequestPermissions || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }

        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            if(showRequestPermissions) {
                Log.e("Permissions: ", "");
                ActivityCompat.requestPermissions(AbstractRuntimePermission.this, requestedPermissions, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else{
            onPermissionsGranted(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;

        for(int permission: grantResults) {
            permissionCheck = permissionCheck + permission;
        }

        if((grantResults.length > 0) && PackageManager.PERMISSION_GRANTED == permissionCheck){
            onPermissionsGranted(requestCode);
        } else {
            //Display stuffvfor dangerous permissions
        }
    }
}
