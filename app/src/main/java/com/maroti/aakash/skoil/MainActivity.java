package com.maroti.aakash.skoil;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.vistrav.ask.Ask;

public class MainActivity extends AppCompatActivity
{
    Button b;
    AutoCompleteTextView tv;
    DatePicker dp;
    String vehicle[]= {"WB41F0242","WB73C2017","WB73B9942","WB73C0101",};
    String phoneNumber="9224992249";
    protected Vibrator vibration;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b=(Button) findViewById(R.id.button);
        dp=(DatePicker) findViewById(R.id.datePicker);
        tv= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tv.showDropDown();
            }
        });
        tv.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                tv.showDropDown();
            }
        });
        tv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,long id)
            {
                hideKeyboard();
            }
        });
        Ask.on(this).forPermissions(Manifest.permission.SEND_SMS).go();
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,vehicle);
        tv.setThreshold(1);//will start working from first character
        tv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendSMS();
            }
        });
    }

    public void sendSMS()
    {
        String v = tv.getText().toString();
        if(v.length()<9)
        {
            Toast.makeText(this,"Enter Vehicle Number",Toast.LENGTH_LONG).show();
            return;
        }
        String str="",d="",m="",y="";
        int day = dp.getDayOfMonth();
        int month = dp.getMonth() + 1;
        int year = dp.getYear();
        d=day+"";
        m=month+"";
        y=year+"";
        if(d.length()<2)
            d="0"+d;
        if(m.length()<2)
            m="0"+m;
        y=y.substring(2);

        str = "IOCL IND "+d+m+y+" SK 12 VH "+v;
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
        sendSmsByManager(str,phoneNumber);

    }

    public void sendSmsByManager(String s,String n)
    {
        s=s.trim();

        try
        {
            // Get the default instance of the SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(n,null,s,null,null);
            Toast.makeText(getApplicationContext(), "Your sms has been successfully sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),"Your sms has failed...",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        finally
        {
            finish();
        }
    }

    public void hideKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
