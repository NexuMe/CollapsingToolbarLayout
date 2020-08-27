package com.example.collapsingtoolbarlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SmsActivity extends AppCompatActivity {

    private static final String TAG = SmsActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 7878;

    ImageButton smsButton;
    Button retryButton;
    EditText editTextPhone, editTextSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        smsButton = findViewById(R.id.message_icon);
        retryButton = findViewById(R.id.button_retry);
        editTextPhone = findViewById(R.id.editText_main);
        editTextSms = findViewById(R.id.sms_message);

        // Проверете дали приложението има разрешение за SMS.
        checkForSmsPermission();
    }

    /**
     * Дефинира стринг (destinationAddress) за телефонния номер
     * и получава текста за въвеждане на SMS съобщението.
     * Използва SmsManager.sendTextMessage за изпращане на съобщението.
     * Преди да изпратите, проверете дали е дадено разрешение.
     *
     * @param view View (message_icon), който е кликнат.
     */
    public void smsSendMessage(View view) {
        // Вземете телефонния номер на получателя в string от editTextPhone.
        String destinationAddress = editTextPhone.getText().toString().trim();
        if (TextUtils.isEmpty(destinationAddress)) {
            editTextPhone.setError(getString(R.string.enter_phone));
            return;
        }
        // Вземете текста на SMS съобщението.
        String smsMessage = editTextSms.getText().toString().trim();
        if (TextUtils.isEmpty(smsMessage)) {
            editTextSms.setError(getString(R.string.enter_message_here));
            return;
        }
        // Задайте адреса на SMS центъра, ако е необходимо, в противен случай null.
        String scAddress = null;
        //Задаване на отложен интент (PendingIntent), когато съобщението е
        // изпратено / доставено или зададено като null.
        PendingIntent sentIntent = null, deliveryIntent = null;
        // Първо проверете за разрешение.
        checkForSmsPermission();
        // Използване на класа SmsManager.
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destinationAddress, scAddress, smsMessage,
                sentIntent, deliveryIntent);
    }

    /**
     * Методът проверява дали приложението има разрешение за SMS.
     */
    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            // Все още не е издадено разрешение. Използвайте requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS е
            // дефинирана в приложение константа int. Методът за обратно извикване получава
            // резултат от заявката.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // Разрешението вече е предоставено. Активиране на бутона за съобщение.
            enableSmsButton();
        }
    }

    /**
     * Обработва кодове за заявка за разрешение.
     *
     * @param requestCode  Кодът на заявката, предаден в requestPermissions()
     * @param permissions  Исканите разрешения. Никога не е null.
     * @param grantResults Резултатите за предоставяне на съответните разрешения,
     *                     което е или PERMISSION_GRANTED или PERMISSION_DENIED. Никога не е null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, getString(R.string.permission_granted));
                    // Разрешението е дадено.
                } else {
                    // Разрешението е отказано.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(SmsActivity.this,
                            getString(R.string.failure_permission),
                            Toast.LENGTH_LONG).show();
                    // Деактивирайте бутона за съобщение.
                    disableSmsButton();
                }
            }
        }
    }

    /**
     * Прави бутона за SMS (иконата на съобщението) невидим, така че да не може да се използва,
     * * и прави бутона Retry (повторен опит) видим.
     */
    private void disableSmsButton() {
        Toast.makeText(this, R.string.sms_disabled, Toast.LENGTH_LONG).show();

        smsButton.setVisibility(View.INVISIBLE);
        retryButton.setVisibility(View.VISIBLE);
    }

    /**
     * Прави бутона видим за SMS (иконата на съобщението), за да може да се използва.
     */
    private void enableSmsButton() {
        smsButton.setVisibility(View.VISIBLE);
    }

    /**
     * Изпраща intent, който да стартира activity.
     *
     * @param view View - Бутонът за повторение, който е кликнат.
     */
    public void retryApp(View view) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        startActivity(intent);
    }
}