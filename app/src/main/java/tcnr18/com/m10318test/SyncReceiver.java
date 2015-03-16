package tcnr18.com.m10318test;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class SyncReceiver extends BroadcastReceiver {
//    public SyncReceiver() {
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent sendIntent = new Intent(context, SyncService.class);
        context.startService(sendIntent);
    }
}