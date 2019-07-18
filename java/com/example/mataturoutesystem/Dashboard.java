package com.example.mataturoutesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;



public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;

    TextView userPhoneTV;
    Button search;
    EditText searchEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        search = findViewById(R.id.searchButton);
        searchEdit = findViewById(R.id.searchEditText);



        Log.d("phonenumber", StaticStrings.userphonenumber);




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.menuHome);


        View headerLayout = navigationView.getHeaderView(0);
        userPhoneTV = headerLayout.findViewById(R.id.userPhoneTV);
        userPhoneTV.setText(StaticStrings.userphonenumber);


//        findViewById(R.id.paymentbutton).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//
//
//            }
//        });
//        findViewById(R.id.moneybutton).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//
//
//            }
//        });
//        findViewById(R.id.accountbutton).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//
//
//            }
//        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchItem = searchEdit.getText().toString();
                StaticStrings.staticSearchItem = searchItem;
                startActivity(new Intent(Dashboard.this, SearchResults.class));
                Log.d("seearch", StaticStrings.staticSearchItem);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dasboard, menu);
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
//        int id = item.getItemId();
//
//        if (id == R.id.menuAccount) {
//
//        } else if (id == R.id.menuLogout){
//            FirebaseAuth.getInstance().signOut();
//            finish();
//            startActivity(new Intent(this, MainActivity.class));
//
//        } else if (id == R.id.menuPayment) {
//
//        } else if (id == R.id.menuProfile) {
//
//        } else if (id == R.id.menuTripDetails) {
//
//        }
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        displaySelectedScreen(item.getItemId());
        return true;
    }
    public void displaySelectedScreen(int itemId){
// Creating fragment object
        Fragment fragment = null;

        //initializing the fragment object that is selected
        switch (itemId){
//            case  R.id.menuAccount:
//                Toast.makeText(this, "Account Fragment Created", Toast.LENGTH_SHORT).show();
//
//                break;

            case R.id.menuPayment:
                fragment = new FragPayment();

                break;

            case R.id.menuTripDetails:
                fragment = new FragTripDetails();

                break;

            case R.id.menuHome:
                fragment = new Maps();

                break;

            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

                default:
                    Toast.makeText(this, "This is a default case", Toast.LENGTH_SHORT).show();




        }
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }
       DrawerLayout drawer = findViewById(R.id.drawer_layout);
       drawer.closeDrawer(GravityCompat.START);
    }
}
