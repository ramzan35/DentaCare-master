package iit.dentacare;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserProfile extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;

    private ImageView userPic;
    private TextView username;
    private Button changeImageButton;
    private TextView userEmail;
    private TextView userDeviceID;
    private TextView userAddress;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Initialization of the FirebaseAuth Object
        firebaseAuth = FirebaseAuth.getInstance().getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("users");

        //Checking whether a user as already Logged In
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            //Starting the User Login Activity if the user is not Logged in
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        userPic = (ImageView) findViewById(R.id.userPic);
        username = (TextView) findViewById(R.id.username);
        changeImageButton = (Button) findViewById(R.id.changeImageButton);
        userDeviceID = (TextView) findViewById(R.id.userDeviceID);
        userAddress = (TextView) findViewById(R.id.userAddress);



        //Retrieving EditText field values from the XML and storing them in java Variables
        userEmail = (TextView) findViewById(R.id.userEmail);

        //Setting the userEmail field text to show the logged in user's email ID
        userEmail.setText(user.getEmail());

        final String email = user.getEmail();

        /*Toast.makeText(UserProfile.this, users.child(email)., Toast.LENGTH_SHORT).show();*/

        /*username.setText(user.)*/

        logoutButton = (Button) findViewById(R.id.logoutButton);

        //Adding the listener function to the logout Button
        logoutButton.setOnClickListener(this);

        changeImageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //when logoutButton is clicked logoutButton method is invoked
        if(view == logoutButton){
            //Signs out the current logged in user
            firebaseAuth.signOut();
            finish();
            //Switches to login Activity
            startActivity(new Intent(this, LoginActivity.class));
        }else if(view == changeImageButton){
            // file picker
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(i, "Select Picture"), 100);

        }
    }


    private void changeImage(){

        FirebaseStorage imageStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = imageStorage.getReferenceFromUrl("gs://dentacare-47bea.appspot.com");


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
            Intent intent = new Intent(UserProfile.this, UserProfile.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_alarm) {

        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(UserProfile.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_devicelist) {
            Intent intent = new Intent(UserProfile.this, DeviceList.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}