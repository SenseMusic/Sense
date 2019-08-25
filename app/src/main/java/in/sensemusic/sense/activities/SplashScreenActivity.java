package in.sensemusic.sense.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import in.sensemusic.sense.R;
import in.sensemusic.sense.extras.Utils;

@SuppressLint("ClickableViewAccessibility")
public class SplashScreenActivity extends AppCompatActivity {

    private static final int externalPermissionResponseCode = 1;
    private static final int requestSettingResponseCode = 2;
    SharedPreferences permissionStatus;

    private void checkReadStoragePermissions() {
        //if sdk is more than 23 (marshmallow) ask for run time permissions
        if (Utils.isMarshmallow()) {
            // if permission has not been granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                /* NOTES:- shouldShowRequestPermissionRationale() function which returns true if the
                 app has requested this permission previously and the user denied the request. If the
                 user turned down the permission request in the past and chose the Don't ask again option,
                 this method returns false.*/

                // Asking for the permission again if request was previously denied
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Permission request was previously denied
                    // Should we show an explanation?
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    showCustomPermissionRationale();
                }
                // Asking for the permission again if request was previously denied and user have set don't ask again to true
                else if (permissionStatus.getBoolean(Manifest.permission.READ_EXTERNAL_STORAGE, false)) {

                    // Permission request was previously denied and set to don't ask again
                    // Should we show an explanation?
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Permission Required");
                    builder.setMessage("App won't run without storage permission ");
                    builder.setPositiveButton("Grant", (dialog, which) -> {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, requestSettingResponseCode);
                        Toast.makeText((Context) SplashScreenActivity.this, "Please Grant Storage Permission", Toast.LENGTH_SHORT).show();
                    });
                    /*builder.setNegativeButton("Deny", (dialog, which) -> {
                        dialog.cancel();
                        finish();
                    });*/
                    builder.setCancelable(false);
                    try {
                        builder.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Asking for the first time android will create request dialog by itself
                else {
                    // No explanation needed; request the permission
                    showCustomPermissionRationale();

                    //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, externalPermissionResponseCode);
                }
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(Manifest.permission.READ_EXTERNAL_STORAGE, true);
                editor.apply();
            }
            // Permission has already been granted
            else {
                onPermissionGranted();
            }
        }
        //if sdk is less than 23 (marshmallow)
        else {
            onPermissionGranted();
        }
    }

    @TargetApi(23)
    private void showCustomPermissionRationale() {
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        final View view = View.inflate((Context) this, R.layout.dialog_permission, null);
        builder.setView(view);
        if (builder.getWindow() != null) {
            builder.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        final Button positiveButton = view.findViewById(R.id.dlg_permission_btn_ok);
        positiveButton.setOnClickListener((View v) -> {
            builder.dismiss();
            ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, externalPermissionResponseCode);
        });
        builder.setCanceledOnTouchOutside(false);
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //do this if permission is granted
    private void onPermissionGranted() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent((Context) SplashScreenActivity.this, ArtistsActivity.class));
            finish();
        },500);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == externalPermissionResponseCode)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText((Context) this,"Permission Granted",Toast.LENGTH_SHORT).show();

                // if successfully permission is granted 1st time
                onPermissionGranted();
            }
            else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){

                    // Permission request was previously denied
                    // Should we show an explanation?

                    showCustomPermissionRationale();

                } else {
                    // permission not granted yet check for don't ask option set true
                    Toast.makeText((Context) SplashScreenActivity.this,"Unable to get Permission",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
        else{
            Toast.makeText((Context) SplashScreenActivity.this,"Invalid requestCode",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

        //check for permission
        checkReadStoragePermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == requestSettingResponseCode)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText((Context) this, "Permission Granted", Toast.LENGTH_SHORT).show();
                onPermissionGranted();
            }
            else
            {
                Toast.makeText((Context) this,"Permission NOT Granted",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else
        {
            Toast.makeText((Context) this,"Invalid Response Code",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
