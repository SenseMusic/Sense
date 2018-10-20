package in.sensemusic.sense.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import in.sensemusic.sense.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int externalPermissionResponseCode = 1;
    private static final int requestSettingResponseCode = 2;
    Boolean sentToSettings = false;
    SharedPreferences permissionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

        if(Build.VERSION.SDK_INT>=23)
            permissionsCheck();
        //if sdk is less than 23
        else {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                finish();
            },1000);
        }
    }

    public void permissionsCheck() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permission Required");
                builder.setMessage(" App Won't Run without storage permission");
                builder.setPositiveButton("Grant", (dialog, which) -> {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(SplashScreenActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},externalPermissionResponseCode);
                });
                builder.setNegativeButton("Deny", (dialog, which) -> {
                    dialog.cancel();
                    finish();
                });
                builder.show();
            } else if(permissionStatus.getBoolean(Manifest.permission.READ_EXTERNAL_STORAGE,false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permission Required");
                builder.setMessage("App won't run without storage permission ");
                builder.setPositiveButton("Grant", (dialog, which) -> {
                    dialog.cancel();
                    sentToSettings = true;
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivityForResult(intent,requestSettingResponseCode);
                    Toast.makeText(SplashScreenActivity.this,"Please Grant Storage Permission",Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("Deny", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},externalPermissionResponseCode);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.READ_EXTERNAL_STORAGE,true);
            editor.apply();
        }
        //if already permission is granted
        else {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                finish();
            },500);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == requestSettingResponseCode)
        {
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this,"Permission NOT Granted",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(sentToSettings)
        {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == externalPermissionResponseCode)
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();

                // if successfully permission is granted 1st time
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                    finish();
                },500);

            }
            else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Permission Required");
                    builder.setMessage(" App Won't Run without storage permission");
                    builder.setPositiveButton("Grant", (dialog, which) -> {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SplashScreenActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},externalPermissionResponseCode);
                    });
                    builder.setNegativeButton("Deny", (dialog, which) -> {
                        dialog.cancel();
                        finish();
                    });
                    builder.show();
                } else {
                    Toast.makeText(SplashScreenActivity.this,"Unable to get Permission",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

}
