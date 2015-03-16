package tcnr18.com.m10318test;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class M1440test extends Activity implements AdapterView.OnItemSelectedListener {
    // final String LOGIN_URL =
    // "http://opensource-forever.com/final/login.php?"; //Mark

    public final static String LOG_TAG = "markchen987";
    public static String activeGrp = "2";
    Cursor mGrpMemberCursor;

    private Spinner spinner;
    private TextView txt1;
    private TextView txt2;
    private TextView txt3;
    private TextView txt4;
    private TextView txt5;
    private TextView txt6;
    private TextView txt7;
    private TextView txt8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m10318test);

        spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);
        txt5 = (TextView) findViewById(R.id.txt5);
        txt6 = (TextView) findViewById(R.id.txt6);
        txt7 = (TextView) findViewById(R.id.txt7);
        txt8 = (TextView) findViewById(R.id.txt8);

        txt1.setText("1");
        txt2.setText("2");
        txt3.setText("3");
        txt4.setText("4");
        txt5.setText("5");
        txt6.setText("6");
        txt7.setText("7");
        txt8.setText("8");

//		if (Envir.eIsLogged) {
//			updateSpinner();
//		}
        if (Envir.eIsLogged) {
            updateSpinnerWithLiveData();
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        Log.d(LOG_TAG, "onStart");

        super.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        Log.d(LOG_TAG, "onResume");
        super.onResume();

    }


    //
    // private void loginFailed(){
    // logged.setText("<none>");
    // grpId.setText("0");
    // spinner.setAdapter(null);
    // }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.m10318test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu1) {
            Intent intent = new Intent(this, Maccount.class);
            // EditText editText = (EditText) findViewById(R.id.edit_message);
            // String message = editText.getText().toString();
            // intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);

            return true;
        }
        if (id == R.id.menu2) {
            Envir.eIsLogged = false;
            spinner.setAdapter(null);
            return true;
        }
        if (id == R.id.menu3) {

            Toast.makeText(getApplicationContext(), "...menu3 ...",
                    Toast.LENGTH_SHORT).show();

            // UI Spinner with SQLite data
            updateSpinner();
            return true;
        }
        if (id == R.id.menu4) {
            String url = "http://tcnr18mark.er-webs.com/index.php";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            startActivity(browserIntent);
            return true;
        }
        if (id == R.id.menu5) {

            Toast.makeText(getApplicationContext(), "...debug ...",
                    Toast.LENGTH_SHORT).show();

            // start auto sync, MySQL -> SQLite
            // startAlarmRepeating();
            startAlarmOneTime();

            // UI Spinner with SQLite data
            updateSpinner();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //
    private void updateSpinnerWithLiveData() {
        startAlarmOneTime();
        updateSpinner();
    }

    private void updateSpinner() {

        //String intGrpId = Envir.eActiveGroup;

        // try {
        // intGrpId = Integer.parseInt(grpId.getText().toString());
        // } catch (Exception ex) {
        // Log.d(LOG_TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!11NOT INTEGER");
        // return;
        // }

        mGrpMemberCursor = getGrpMembers(Envir.eActiveGroup);
        startManagingCursor(mGrpMemberCursor);
        SpinnerAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, mGrpMemberCursor, new String[]{
                MembersProvider.COLUMN_USERNAME, MembersProvider.COLUMN_EMAIL
        }, new int[]{
                android.R.id.text1, android.R.id.text2});

        spinner.setAdapter(adapter);

    }

    /**
     * @param grpId
     * @return
     */
    private Cursor getGrpMembers(String grpId) {
        Uri uri = MembersProvider.CONTENT_URL;
        String[] projection = new String[]{MembersProvider.COLUMN_ID,
                MembersProvider.COLUMN_EMAIL, MembersProvider.COLUMN_USERNAME};
        //
        String selection = MembersProvider.COLUMN_GROUP + " = '" + grpId
                + "'";

        String[] selectionArgs = null;
        String sortOrder = MembersProvider.COLUMN_EMAIL;

        return managedQuery(uri, projection, selection, selectionArgs,
                sortOrder);
    }

    // Using AlarmManager to repeat SyncService
    private void startAlarmRepeating() {
        Intent intent = new Intent(this, SyncReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, 0 // run first
                // time
                // immediately
                , 10000 // repeat every 10 seconds
                , pendingIntent);
    }

    // Using AlarmManager to repeat SyncService
    private void startAlarmOneTime() {
        Intent intent = new Intent(this, SyncReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, 0 // run first
                // time
                // immediately
                , 1000000 // repeat every 1000 seconds
                , pendingIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tv=null;
        try {
            tv = (TextView) view.findViewById(android.R.id.text2);
        }catch (Exception e){
            Log.d(LOG_TAG,"onItemSelected --- IS NULL  ");
        }

        String spinnerEmail = tv.getText().toString();
        Log.d(LOG_TAG, "onItemSelected --- " + spinnerEmail);


        final ContentResolver resolver = getContentResolver();
        Uri uri = MembersProvider.CONTENT_URL;
        String[] projection = {
                MembersProvider.COLUMN_MEMBER_ID,//0
                MembersProvider.COLUMN_USERNAME, //1
                MembersProvider.COLUMN_EMAIL,
                MembersProvider.COLUMN_AUTHCODE,
                MembersProvider.COLUMN_STATE,    //4
                MembersProvider.COLUMN_GROUP,
                MembersProvider.COLUMN_ADDRESS1,
                MembersProvider.COLUMN_ADDRESS2
        };
        String selection = MembersProvider.COLUMN_EMAIL + "='" + spinnerEmail + "'";
        String[] selectionArgs = null;
        String sortOrder = MembersProvider.COLUMN_USERNAME;
        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor.moveToFirst()) {
            String username = cursor.getString(1);
            Log.d(LOG_TAG, "username is ... " + username);

            txt1.setText(cursor.getString(0));
            txt2.setText(cursor.getString(1));
            txt3.setText(cursor.getString(2));
            txt4.setText(cursor.getString(3));
            txt5.setText(cursor.getString(4));
            txt6.setText(cursor.getString(5));
            txt7.setText(cursor.getString(6));
            txt8.setText(cursor.getString(7));


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
