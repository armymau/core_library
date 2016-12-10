package core.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import armymau.it.core_library.R;
import core.utils.CoreConstants;

public abstract class RuntimePermissionsActivity extends AppCompatActivity {

    private String[] requestedPermissions;
    private boolean shouldShowRequestPermissionRationale;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onRequestAppPermissions(final String[] requestedPermissions, final int stringId, final int requestCode) {
        this.requestedPermissions = requestedPermissions;

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {

                Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_INDEFINITE).setAction(this.getResources().getString(R.string.core_application_request), new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(RuntimePermissionsActivity.this, requestedPermissions, requestCode);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            Snackbar.make(findViewById(android.R.id.content), this.getResources().getString(R.string.core_runtime_permissions_settings_txt), Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.core_application_settings, onSnackbarPermissionsResult)
                    .show();
        }
    }

    private View.OnClickListener onSnackbarPermissionsResult = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CoreConstants.CHECK_PERMISSIONS_REQUEST_CODE);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CoreConstants.CHECK_PERMISSIONS_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_CANCELED) {
                onRequestAppPermissions(requestedPermissions, R.string.core_runtime_permissions_txt, 0);
            }
        }
    }

    public abstract void onPermissionsGranted(int requestCode);
}
