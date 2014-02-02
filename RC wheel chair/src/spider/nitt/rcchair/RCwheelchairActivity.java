package spider.nitt.rcchair;

import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current remote session.
 */
public class RCwheelchairActivity extends Activity  {
    // Debugging
    private static final String TAG = "RC chair";
    private static final boolean D = true;

    // Message types sent from the BluetoothRemoteService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothRemoteService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
	private static final String ROBOTNAME = "linvor";

    // Layout Views
    private TextView mTitle;
    // Name of the connected device
    private String mConnectedDeviceName = null;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the remote services
    private BluetoothRemoteService mRemoteService = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
        setContentView(R.layout.main);

        // Set up the custom title

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupremote() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the remote session
        } else {
            if (mRemoteService == null) setupbuttons();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mRemoteService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mRemoteService.getState() == BluetoothRemoteService.STATE_NONE) {
              // Start the Bluetooth remote services
              mRemoteService.start();
            }
        }
    }

    private void setupbuttons() {
        Log.d(TAG, "setupbuttons()");
        
        Button b1 = (Button) findViewById(R.id.f);
        Button b2 = (Button) findViewById(R.id.b);
        Button b3 = (Button) findViewById(R.id.a);
        Button b4 = (Button) findViewById(R.id.c);
        Button b5 = (Button) findViewById(R.id.l);
        Button b6 = (Button) findViewById(R.id.r);
        Button b7 = (Button) findViewById(R.id.s);
        
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
            	String x="f";
                 sendMessage(x);
                }});
            b2.setOnClickListener(new OnClickListener() {
                 public void onClick(View v) {
                     // Send a message using content of the edit text widget
                 	String x="b";
                      sendMessage(x);
                     }});
             b3.setOnClickListener(new OnClickListener() {
                      public void onClick(View v) {
                          // Send a message using content of the edit text widget
                      	String x="a";
                           sendMessage(x);
                          }});
             b4.setOnClickListener(new OnClickListener() {
                           public void onClick(View v) {
                               // Send a message using content of the edit text widget
                           	String x="c";
                                sendMessage(x);
                               }});
          b5.setOnClickListener(new OnClickListener() {
                                public void onClick(View v) {
                                    // Send a message using content of the edit text widget
                                	String x="l";
                                     sendMessage(x);
                                    }});
           b6.setOnClickListener(new OnClickListener() {
                                     public void onClick(View v) {
                                         // Send a message using content of the edit text widget
                                     	String x="r";
                                          sendMessage(x);
                                         }});
         b7.setOnClickListener(new OnClickListener() {
                                          public void onClick(View v) {
                                              // Send a message using content of the edit text widget
                                          	String x="s";
                                               sendMessage(x);
                                              }});
                                                             
        

        // Initialize the BluetoothRemoteService to perform bluetooth connections
        mRemoteService = new BluetoothRemoteService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
    	   if (mRemoteService != null) mRemoteService.stop();
    	 finish();    
    	super.onDestroy();
        // Stop the Bluetooth remote services
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mRemoteService.getState() != BluetoothRemoteService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothRemoteService to write
            byte[] send = message.getBytes();
            mRemoteService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }

    // The Handler that gets information back from the BluetoothRemoteService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothRemoteService.STATE_CONNECTED:
                    break;
                case BluetoothRemoteService.STATE_CONNECTING:
                    break;
                case BluetoothRemoteService.STATE_LISTEN:
                	break;
                case BluetoothRemoteService.STATE_NONE:
                    break;
                }
                break;
            case MESSAGE_WRITE:
                break;
            case MESSAGE_READ:
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
       
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a remote session
                setupbuttons();
            } else {
                // User did not enable Bluetooth or an error occured
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.scan:
            // Launch the DeviceListActivity to see devices and do scan
        	try
        	{
        	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        	Iterator<BluetoothDevice> it = pairedDevices.iterator();
        	while (it.hasNext())
        	{BluetoothDevice bd = it.next();
        	if (bd.getName().equalsIgnoreCase(ROBOTNAME)) {
        		mRemoteService.connect(bd);
        		return false;
        		}
        		}
        		}
        		catch (Exception e)
        		{
        		Log.e(TAG,"Failed in findRobot() " + e.getMessage());
        		}
        		return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }

}