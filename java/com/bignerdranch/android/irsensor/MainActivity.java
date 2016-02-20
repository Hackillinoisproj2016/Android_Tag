package com.bignerdranch.android.irsensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
    /*
        need the hex of the reciever we are going to use. This works for this hex, but we need the hex of the reciever we are going to use
        Below are sample patterns and frequencies from samsung, and various other sources.
    */
    private String commande="0000 0070 0000 0032 0080 003F 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 "
            + "0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 "
            + "0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0030 0010 0030 0010 0030 0010 0030 "
            + "0010 0010 0010 0010 0010 0010 0010 0030 0010 0030 0010 0030 0010 0030 0010 0030 0010 0010 0010 0030 0010 0A98";
    //Samsungs frequency and pattern- these is unique to every phone/reciever, need the
    private static final int SAMSUNG_FREQ = 38028;
    private static final int[] SAMSUNG_POWER_TOGGLE_DURATION = {4368,546,1638,546,1638,546,1638,546,546,546,546,546,546,546,546,546,546,546,1638,546,1638,546,1638,546,546,546,546,546,546,546,546,546,546,546,546,546,1638,546,546,546,546,546,546,546,546,546,546,546,546,546,1664,546,546,546,1638,546,1638,546,1638,546,1638,546,1638,546,1638,546,46644,4394,4368,546,546,546,96044};
    private static final int[] SAMSUNG_POWER_TOGGLE_COUNT = {169,168,21,63,21,63,21,63,21,21,21,21,21,21,21,21,21,21,21,63,21,63,21,63,21,21,21,21,21,21,21,21,21,21,21,21,21,63,21,21,21,21,21,21,21,21,21,21,21,21,21,64,21,21,21,63,21,63,21,63,21,63,21,63,21,63,21,1794,169,168,21,21,21,3694};
    private static final int[] pattern = {1901, 4453, 625, 1614, 625, 1588, 625, 1614, 625, 442, 625, 442, 625,
            468, 625, 442, 625, 494, 572, 1614, 625, 1588, 625, 1614, 625, 494, 572, 442, 651,
            442, 625, 442, 625, 442, 625, 1614, 625, 1588, 651, 1588, 625, 442, 625, 494, 598,
            442, 625, 442, 625, 520, 572, 442, 625, 442, 625, 442, 651, 1588, 625, 1614, 625,
            1588, 625, 1614, 625, 1588, 625, 48958};
    //These functions can be used if needed to split up a hex and turn it into a int array
//    String splitHex = "[ ]+";
//    String[] tokens = commande.split(splitHex);
//    int[] ir = hexStringToIntArray(tokens);
    /*
    Switch, TextView, and Ir manager used to turn the IR blaster on and off.
     */
    TextView mTextView;
    Switch mSwitch;
    ConsumerIrManager mConsumerIrManager;
    private ToggleButton mPressButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConsumerIrManager = (ConsumerIrManager)getSystemService(Context.CONSUMER_IR_SERVICE);
        mTextView = (TextView) findViewById(R.id.result_text);
        mTextView.setVisibility(View.INVISIBLE);
        mSwitch = (Switch)findViewById(R.id.second_ir);
        /*
        All of the functions work, we just need to apply it to our reciever.
         */
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if(!mConsumerIrManager.hasIrEmitter())
                    {
                        Toast.makeText(getApplicationContext(), "No IR Emitter Found", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(mConsumerIrManager.getCarrierFrequencies() != null)
                       {
                           mConsumerIrManager.transmit(38028, SAMSUNG_POWER_TOGGLE_DURATION); //We need to use the Hex code for the reciever we are going to use
                           Toast.makeText(getApplicationContext(), "IR Emitter Found", Toast.LENGTH_SHORT).show();
                           mTextView.setText("IR On");
                           mTextView.setVisibility(View.VISIBLE);
                        }
                    }

                }
                else
                {
                    mTextView.setText("IR Off");
                }
            }
        });

    }
    /*
    Converts hex string to an integer array if needed, helper function
     */
    private static int[] hexStringToIntArray(String[] hexString){
        int[] ir = new int[hexString.length];
        for(int i = 0; i < hexString.length;i++) {
           int x = Integer.parseInt(hexString[i], 16);
            ir[i] = x;
        }
        return ir;
    }
    /*
    Another toggle button that can be used if we do not want the switch
     */
//    public void changeState(View view)
//    {
//        boolean isChecked = ((ToggleButton)view).isChecked();
//            if(isChecked)
//            {
//                mTextView.setText("IR Blaster On");
//                mTextView.setVisibility(View.VISIBLE);
//            }
//            else
//            {
//                mTextView.setText("IR Blaster Off");
//            }
//    }
    /*


 * preforms some calculations on the codesets we have in order to make them work with certain models of phone.
 *
 * HTC devices need formula 1
 * Samsungs want formula 2
 *
 * Samsung Pre-4.4.3 want nothing, so just return the input data
 *
 */
    /*
    The next to methods I found online and are the formulas used to get the frequency and pattern from Hex
    we need our recievers hex
     */
private static int[] string2dec(int[] irData, int frequency) {
    int formula = shouldEquationRun();

    //Should we run any computations on the irData?
    if (formula != 0) {
        for (int i = 0; i < irData.length; i++) {
            if (formula == 1) {
                irData[i] = irData[i] * 1000000 / frequency;
            } else if (formula == 2) {
                irData[i] = (int) Math.ceil(irData[i] * 26.27272727272727); //this is the samsung formula as per http://developer.samsung.com/android/technical-docs/Workaround-to-solve-issues-with-the-ConsumerIrManager-in-Android-version-lower-than-4-4-3-KitKat
            }
        }
    }
    return irData;
}

    /*
     * This method figures out if we should be running the equation in string2dec,
     * which is based on the type of device. Some need it to run in order to function, some need it NOT to run
     *
     * HTC needs it on (HTC One M8)
     * Samsung needs occasionally a special formula, depending on the version
     * Android 5.0+ need it on.
     * Other devices DO NOT need anything special.
     */
    private static int shouldEquationRun() {
        //Some notes on what Build.X will return
        //System.out.println(Build.MODEL); //One M8
        //System.out.println(Build.MANUFACTURER); //htc
        //System.out.println(Build.VERSION.SDK_INT); //19

        //Samsung's way of finding out if their OS is too new to work without a formula:
        //int lastIdx = Build.VERSION.RELEASE.lastIndexOf(".");
        //System.out.println(Build.VERSION.RELEASE.substring(lastIdx+1)); //4

        //handle HTC
        if (Build.MANUFACTURER.equalsIgnoreCase("HTC")) {
            return 1;
        }
        //handle Lollipop (Android 5.0.1 == SDK 21) / beyond
        if (Build.VERSION.SDK_INT >= 21) {
            return 1;
        }
        //handle Samsung PRE-Android 5
        if (Build.MANUFACTURER.equalsIgnoreCase("SAMSUNG")) {
            if (Build.VERSION.SDK_INT >= 19) {
                int lastIdx = Build.VERSION.RELEASE.lastIndexOf(".");
                int VERSION_MR = Integer.valueOf(Build.VERSION.RELEASE.substring(lastIdx + 1));
                if (VERSION_MR < 3) {
                    // Before version of Android 4.4.2
                    //Note: NO formula here, not even the other one
                    return 0;
                } else {
                    // Later version of Android 4.4.3
                    //run the special samsung formula here
                    return 2;
                }
            }
        }
        //if something else...
        return 0;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
