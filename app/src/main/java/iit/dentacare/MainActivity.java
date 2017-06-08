package iit.dentacare;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener { //registration ethu da?
    //nee file onnu podu
    //nan senjadu errors varudu

    static int dsf;

    private Button signOut, monitorX;
    //    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    public static String uID;
    private DatabaseReference databaseUser;
    private static String BLUE_MAC_ADDRESS = null;
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothDevice device;
    private BluetoothSocket btSocket;
    private InputStream inputStream;
    static boolean deviceOff;
    static int brushingTime;
    ProgressBar p1;
    ProgressBar p2;
    ProgressBar p3;
    ProgressBar p4;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent newint = getIntent();
        BLUE_MAC_ADDRESS = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);
//        Toast.makeText(this, BLUE_MAC_ADDRESS, Toast.LENGTH_LONG).show();
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        databaseUser = FirebaseDatabase.getInstance().getReference("device");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        p1 = (ProgressBar) findViewById(R.id.progressBar1);
        p2 = (ProgressBar) findViewById(R.id.progressBar2);
        p3 = (ProgressBar) findViewById(R.id.progressBar3);
        p4 = (ProgressBar) findViewById(R.id.progressBar4);


//        Toast.makeText(this,"Fuck",Toast.LENGTH_LONG).show();

        signOut = (Button) findViewById(R.id.sign_out);
        monitorX = (Button) findViewById(R.id.monitor);

//        progressBar = (ProgressBar) findViewById(R.id.progressBar);

//        if (progressBar != null) {
//            progressBar.setVisibility(View.GONE);
//        }

        uID = auth.getCurrentUser().getUid();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        monitorX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Stopwatch timerFlow = new Stopwatch();
                LoadData2 l = new LoadData2(p1, p2, p3, p4);
                l.start();

                try {
                    if (connectBluetooth()) {
                        getData();
                    } else {
                        Toast.makeText(getApplicationContext(), "There is a problem with connecting to your DentaCare device", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "There is a problem..", Toast.LENGTH_LONG).show();
                }

//               if(deviceOff) {
//                   l.stop();
//                   brushingTime = (int) timerFlow.elapsedTime();
//               }

//               LoadData2.saveData();
            }
        });
        /*Timer t = new Timer(p, dsf);
        t.start();*/
    }

    public void signOut() {
        auth.signOut();
    }

    //@Thilo had to remove this,
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void Monitor(View view){
//        LoadData2 l = new LoadData2(p1, p2, p3, p4);
//        l.start();
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        progressBar.setVisibility(View.GONE);
//    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, UserProfile.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_alarm) {
            Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_devicelist) {
            Intent intent = new Intent(MainActivity.this, DeviceList.class);
            startActivity(intent);
            finish();
//        } else if (id == R.id.nav_share) {

//        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean connectBluetooth() {
//        Toast.makeText(this,BLUE_MAC_ADDRESS,Toast.LENGTH_LONG).show();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice iterator : bondedDevices) {
            if (iterator.getAddress().equals(BLUE_MAC_ADDRESS)) {
                device = iterator;
                break;
            }
        }
        boolean connected = true;
//        Toast.makeText(getApplicationContext(), "Came2", Toast.LENGTH_LONG).show();
        try {
            btSocket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            btSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
            connected = false;
        }
        if (connected) {
            try {
                inputStream = btSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return connected;
    }

//    void getData() {
//        final android.os.Handler handler = new android.os.Handler();
////        buffer = new byte[1024];
//        Toast.makeText(getApplicationContext(), "X1", Toast.LENGTH_LONG).show();
//
//        final Thread thread = new Thread(new Runnable() {
//            public void run() {
////                Toast.makeText(getApplicationContext(), "X2", Toast.LENGTH_LONG).show();
//                while (true) {
//                    try {
//                        int byteCount = inputStream.available();
//                        if (byteCount > 0) {
//                            byte[] rawBytes = new byte[byteCount];
//                            int bytes = inputStream.read(rawBytes);
//                            final String string = new String(rawBytes, 0, bytes);
//
//                            try {
////                                Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
//                                final String[] arr = string.split(";");
//                                final String[] data = arr[0].split(",");
//
//                                final int ort = Integer.parseInt(data[0]);
//                                final int nmb = Integer.parseInt(data[1]);
//
//                                handler.post(new Runnable() {
//                                    public void run() {
//                                        while (true) {
////                                        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
//                                            addUsers(ort, nmb);
//                                            //try to slow down the toasting
//                                            break;
//                                        }
//                                    }
//                                });
//                            }catch (Exception e){
//                                Toast.makeText(getApplicationContext(), "Fucked", Toast.LENGTH_LONG).show();
//                            }
//                        }else{
//                            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
//                        }
//                    } catch (Exception ex) {
////                        Thread.currentThread().interrupt();
//                        break;
//                    }
//                    SystemClock.sleep(800);
//                }
//            }
//        });
//
//        thread.start();
//    }

    void getData() {
        final android.os.Handler handler = new android.os.Handler();
//        buffer = new byte[1024];
//        Toast.makeText(getApplicationContext(), "Came3", Toast.LENGTH_LONG).show();
        final Thread thread = new Thread(new Runnable() {
            public void run() {
//                Toast.makeText(getApplicationContext(), "Why", Toast.LENGTH_LONG).show();
                while (true) {
//                    Toast.makeText(getApplicationContext(), "VVVV", Toast.LENGTH_LONG).show();
                    try {
                        int byteCount = inputStream.available();
//                        Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
                        if (byteCount > 0) {
//                            Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
//                            byte[] rawBytes = new byte[byteCount];
//                            int bytes = inputStream.read(rawBytes);
//                            final String string = new String(rawBytes, 0, bytes);
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string = new String(rawBytes, "UTF-8");

                            try {
//                                Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
                                final String[] arr = string.split(";");
                                final String[] data = arr[0].split(",");
//
                                final int ort = Integer.parseInt(data[0]);
                                final int nmb = Integer.parseInt(data[1]);

                                handler.post(new Runnable() {
                                    public void run() {
                                        while (true) {
//                                        Toast.makeText(getApplicationContext(), ort+" "+nmb, Toast.LENGTH_LONG).show();
                                            addUsers(ort, nmb);
                                            //try to slow down the toasting
                                            break;
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                deviceOff = true;
//                                Toast.makeText(getApplicationContext(), "incomplete readings", Toast.LENGTH_LONG).show();
                                Log.d("incomplete readings",e.toString());
                            }
                        }
                    } catch (Exception ex) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    SystemClock.sleep(800);
                }
            }
        });
        if(!thread.isInterrupted())
        thread.start();
    }

    private void addUsers(int ort, int acceleration) {

//        String id = databaseUser.push().getKey();
//        Random rand = new Random();
//        int nmb = rand.nextInt();

        String orientation = null;

        if (ort == 1)
            orientation = "UP_LEFT";
        else if (ort == 2)
            orientation = "UP_RIGHT";
        else if (ort == 3)
            orientation = "DOWN_LEFT";
        else
            orientation = "DOWN_RIGHT";

        String id = uID;

        User user = new User(id, orientation, acceleration);

        databaseUser.child(id).setValue(user);

//        Toast.makeText(this, "User added :"+orientation, Toast.LENGTH_LONG).show();

    }

}