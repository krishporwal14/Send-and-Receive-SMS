package com.example.sendreceivesms;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    TextView showMsg;
    EditText edtPhn, edtMsg;
    Button btnSend;
    SmsManager sm;
    BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            Bundle bundle = i.getExtras();
            if(bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                for(Object pdu : pdus) {
                    SmsMessage smsg = SmsMessage.createFromPdu((byte[]) pdu);
                    String phnNumber = smsg.getOriginatingAddress();
                    String msgBody = smsg.getMessageBody();
                    String receivedSMS = "\nFrom: " + phnNumber + "\nMessage: " + msgBody;
                    showMsg.append(receivedSMS);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtPhn = findViewById(R.id.edtPhn);
        edtMsg = findViewById(R.id.edtMsg);
        showMsg = findViewById(R.id.showMsg);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(view -> {
            String phn = edtPhn.getText().toString();
            String msg = edtMsg.getText().toString();
            sm = SmsManager.getDefault();
            sm.sendTextMessage(phn, null, msg, null, null);
            showToast("Message sent successfully!!");
        });
    }
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(smsReceiver);
    }
}