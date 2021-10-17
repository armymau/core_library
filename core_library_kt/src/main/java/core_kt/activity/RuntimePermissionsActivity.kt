package core_kt.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import armymau.it.core_library.R
import com.google.android.material.snackbar.Snackbar
import core_kt.utils.CHECK_PERMISSIONS_REQUEST_CODE

abstract class RuntimePermissionsActivity : AppCompatActivity() {

    lateinit var requestedPermissions: Array<String>
    private var shouldShowRequestPermissionRationale: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun onRequestAppPermissions(requestedPermissions: Array<String>, stringId: Int, requestCode: Int) {
        this.requestedPermissions = requestedPermissions

        var permissionCheck = PackageManager.PERMISSION_GRANTED
        shouldShowRequestPermissionRationale = false

        for (permission in requestedPermissions) {
            permissionCheck += ContextCompat.checkSelfPermission(this, permission)
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {

                Snackbar.make(findViewById(android.R.id.content),
                        stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction(this.resources.getString(R.string.core_application_request)) {
                            ActivityCompat.requestPermissions(this@RuntimePermissionsActivity, requestedPermissions, requestCode)
                        }
                        .show()
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode)
            }
        } else {
            onPermissionsGranted(requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var permissionCheck = PackageManager.PERMISSION_GRANTED
        for (permission in grantResults) {
            permissionCheck += permission
        }
        if (grantResults.isNotEmpty() && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode)
        } else {
            Snackbar.make(findViewById(android.R.id.content),
                    this.resources.getString(R.string.core_runtime_permissions_settings_txt),
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.core_application_settings) {
                        onSnackbarPermissionsResult
                    }
                    .show()
        }
    }

    private val onSnackbarPermissionsResult = View.OnClickListener {

            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:$packageName")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            startActivityForResult(intent, CHECK_PERMISSIONS_REQUEST_CODE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CHECK_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                onRequestAppPermissions(requestedPermissions, R.string.core_runtime_permissions_txt, 0)
            }
        }
    }

    abstract fun onPermissionsGranted(requestCode: Int)
}