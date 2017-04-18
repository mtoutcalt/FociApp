package foci.bu.outcalt.fociapp.habit;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import foci.bu.outcalt.fociapp.BaseActivity;
import foci.bu.outcalt.fociapp.R;
import foci.bu.outcalt.fociapp.calm.BreatheActivity;
import foci.bu.outcalt.fociapp.home.HomeActivity;
import foci.bu.outcalt.fociapp.inspire.QuoteActivity;
import foci.bu.outcalt.fociapp.tab.TabLayoutActivity;
import foci.bu.outcalt.fociapp.timer.TimerActivity;
import foci.bu.outcalt.fociapp.timer.TimerSessionActivity;
import foci.bu.outcalt.fociapp.todo.ToDoActivity;

public class HabitActivity extends AppCompatActivity {

    private static final String TAG = "StateChange";
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final String EXTRA_MESSAGE = "foci.bu.outcalt.fociapp.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHabit);
        setSupportActionBar(toolbar);
        setPermissions();

        List<ContactEntity> contactEntityList = getContacts(this);
        Log.i(TAG, "contacts found: " + contactEntityList.size());
        for (ContactEntity contactEntity : contactEntityList) {
            Log.i(TAG, "id: " + contactEntity.getId());
            Log.i(TAG, "name: " + contactEntity.getName());
            Log.i(TAG, "number: " + contactEntity.getPhoneNumber());
        }

        createSpinner(contactEntityList);
//        EditText editText = (EditText) findViewById(R.id.habitTextMessage);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addListItem();
//                snackView = view;
//            }
//        });
    }

    private void setPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to READ CONTACTS - requesting it");
                String[] permissions = {Manifest.permission.READ_CONTACTS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            } else if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }
    }

    public List<ContactEntity> getContacts(Context ctx) {
        List<ContactEntity> contactEntities = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Log.i(TAG, "do the query");
        Cursor cursor = null;
//        try {
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//        } catch (Exception e) {
//            Log.i(TAG, e.getMessage());
//        }
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String entityId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{entityId}, null);

                    while (cursorInfo.moveToNext()) {
                        ContactEntity entity = new ContactEntity();
                        entity.setId(entityId);
                        entity.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        entity.setPhoneNumber(cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        contactEntities.add(entity);
                    }
                    cursorInfo.close();
                }
            }
        }
        cursor.close();
        return contactEntities;
    }

    public void createTextMessage(View view) {
        final EditText smsEditText = new EditText(this);
        Log.i(TAG, "create Text Message");
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Text Message")
                .setMessage("What do you want to say?")
                .setView(smsEditText)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String smsText = String.valueOf(smsEditText.getText());
                        Log.i(TAG, "SMS TO SEND: " + smsText);
                        sendSMS(smsText);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    private void createSpinner(List<ContactEntity> contactEntities) {
        List<String> contactNames = new ArrayList<>();
        Spinner spinner = (Spinner) findViewById(R.id.habitContactSpinner);
        for (ContactEntity contactEntity : contactEntities) {
            contactNames.add(contactEntity.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contactNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    public void sendSMS(String smsText) {
        Log.i(TAG, "sendMGS");
        SmsManager sms = SmsManager.getDefault();
        try {
            sms.sendTextMessage("5556", null, smsText, null, null);
        } catch (Exception e) {
            Log.i(TAG, "SMS FAIL");
            Log.i(TAG, e.getMessage());
        }
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, BreakChainActivity.class);
        EditText editText = (EditText) findViewById(R.id.habitDurationText);
        String duration = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, duration);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.foci_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_home:
                intent = new Intent(this, HomeActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_todo:
                intent = new Intent(this, ToDoActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_habit:
                intent = new Intent(this, HabitActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_timer:
                intent = new Intent(this, TimerActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_breathe:
                intent = new Intent(this, BreatheActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_info:
                intent = new Intent(this, TabLayoutActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_quote:
                intent = new Intent(this, QuoteActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_session_history:
                intent = new Intent(this, TimerSessionActivity.class);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }



}
