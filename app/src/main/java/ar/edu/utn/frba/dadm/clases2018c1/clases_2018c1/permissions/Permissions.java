package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.permissions;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

public class Permissions {
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    public static boolean hasPermissions(Activity activity, String permissionCode){
        return ContextCompat.checkSelfPermission(activity, permissionCode) != PackageManager.PERMISSION_GRANTED;
    }

    public static void checkForPermissions(Activity activity, String permissionCode, String reason, Callback callback){
        if(hasPermissions(activity, permissionCode)){
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionCode)) {
                showStoragePermissionExplanation(activity, permissionCode, reason);
            } else {
                dispatchStoragePermissionRequest(activity, permissionCode);
            }
        } else {
            callback.onSuccess();
        }
    }

    private static void dispatchStoragePermissionRequest(Activity activity, String permissionCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permissionCode}, REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    private static void showStoragePermissionExplanation(final Activity activity, final String permissionCode, String reason) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Necesitamos tu permiso");
        builder.setCancelable(true);
        builder.setMessage(reason);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dispatchStoragePermissionRequest(activity, permissionCode);
            }
        });
        builder.show();
    }

    public interface Callback {
        void onSuccess();
    }
}
