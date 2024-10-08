package lk.ads.app.greenelec.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Please Connect Your Device Charger",Toast.LENGTH_LONG).show();
    }
}
