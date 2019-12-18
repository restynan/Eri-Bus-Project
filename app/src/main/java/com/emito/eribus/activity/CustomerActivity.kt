package com.emito.eribus.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.emito.eribus.LoginActivity
import com.emito.eribus.R
import com.emito.eribus.fragment.*
import com.emito.eribus.model.Users
import com.emito.eribus.utils.Utils
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.feature_menu_general_menu_4_activity.*
import java.util.jar.Manifest

class CustomerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback{
    private lateinit var mMap: GoogleMap
    private lateinit var locationProvider: FusedLocationProviderApi
    private lateinit var locationManager:LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var  latLng:LatLng
    private  val Min_Time:Long=1000
    private  val Min_Dist:Float=5f


    internal lateinit var toolbar: Toolbar
    internal lateinit var drawerLinearLayout: LinearLayout
    lateinit var fmanager: FragmentManager
    lateinit var tx: FragmentTransaction


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        initUI()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))



        locationListener= object: LocationListener{
            override fun onLocationChanged(location: Location?) {
                // Add a marker to over new location****************************************
                // editTextlatitude.setText(location?.latitude.toString())
                //   editTextlongitude.setText(location?.longitude.toString())

                try {
                    latLng = LatLng(location!!.latitude, location!!.longitude)
                    mMap.clear() // clear old location markerin google map
                    mMap.addMarker(MarkerOptions().position(latLng).title("user Location "))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                }
                catch (e:SecurityException){
                    e.printStackTrace()
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onProviderDisabled(provider: String?) {

            }
        }

        ///asking for map pemissions/user persmissions

        askPermission()


    }
    fun askPermission(){
        val withListener = Dexter.withActivity(this).withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION).withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                try{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Min_Time, Min_Dist, locationListener)
                    var lastLocation =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    latLng = LatLng( lastLocation!!.latitude, lastLocation!!.longitude)
                    mMap.clear() // clear old location markerin google map
                    mMap.addMarker(MarkerOptions().position(latLng).title("user Location "))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                }
                catch (e:SecurityException){
                    e.printStackTrace()
                }

            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest?,
                token: PermissionToken?
            ) {token?.continuePermissionRequest()

            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {

            }

        }).check()
    }


    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_subject, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_menu_2) {

            if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
                drawer_layout.closeDrawer(GravityCompat.END)
            } else {
                drawer_layout.openDrawer(GravityCompat.END)
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_customer_home) {
            Toast.makeText(this, "Clicked Home.", Toast.LENGTH_SHORT).show()
        } else if (id == R.id.nav_customer_shareLocation) {
            Toast.makeText(this, "Location Shared.", Toast.LENGTH_SHORT).show()
        } else if (id == R.id.nav_customer_booking) {
            Toast.makeText(this, "Clicked Drivers.", Toast.LENGTH_SHORT).show()
            try{
                this.supportFragmentManager.beginTransaction().replace(R.id.content_frame,bookFragment())
                    .commitAllowingStateLoss()
            }catch (e:Exception){
                Log.d("error:", "Error! Can't Replace")
            }


        } else if (id == R.id.nav_customer_booking_history) {
            Toast.makeText(this, "Clicked Buses.", Toast.LENGTH_SHORT).show()
        }
        else if (id == R.id.nav_route) {
            Toast.makeText(this, "Clicked Routes.", Toast.LENGTH_SHORT).show()
        } else if (id == R.id.nav_map) {
            Toast.makeText(this, "Clicked Routes.", Toast.LENGTH_SHORT).show()
        }else if (id == R.id.nav_search) {
            Toast.makeText(this, "Clicked Search.", Toast.LENGTH_SHORT).show()
        } else if (id == R.id.nav_profile) {
            try{
                this.supportFragmentManager.beginTransaction().replace(R.id.content_frame,userProfileFragment())
                    .commitAllowingStateLoss()
            }catch (e:Exception){
                Log.d("error:", "Error! Can't Replace")
            }
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut()
            finish()
            startActivity(Intent(this,LoginActivity::class.java))
            //Toast.makeText(this, "Clicked Logout.", Toast.LENGTH_SHORT).show()
        } else if (id == R.id.nav_about_us) {
            try{
                this.supportFragmentManager.beginTransaction().replace(R.id.content_frame,AboutFragment())
                    .commitAllowingStateLoss()
            }catch (e:Exception){
                Log.d("error:", "Error! Can't Replace")
            }
            //Toast.makeText(this, "Clicked About Us.", Toast.LENGTH_SHORT).show()
        } else if (id == R.id.nav_contact_us) {
            Toast.makeText(this, "Clicked Contact Us.", Toast.LENGTH_SHORT).show()
        } else if (id == R.id.nav_setting) {
            Toast.makeText(this, "Clicked Setting.", Toast.LENGTH_SHORT).show()
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true

    }

    fun onMenuClicked(view: View) {
        resetMenuColor()

        selectMenu(view)

        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun selectMenu(view: View) {
        try {
            val imageView = view as ImageView
            if (imageView.id == R.id.homeImageView) {
                Toast.makeText(this, "Clicked Home.", Toast.LENGTH_SHORT).show()
            } else if (imageView.id == R.id.announceImageView) {
                Toast.makeText(this, "Clicked Announce.", Toast.LENGTH_SHORT).show()
            } else if (imageView.id == R.id.profileImageView) {
                Toast.makeText(this, "Clicked Graph.", Toast.LENGTH_SHORT).show()
            } else if (imageView.id == R.id.clockImageView) {
                Toast.makeText(this, "Clicked History.", Toast.LENGTH_SHORT).show()
            } else if (imageView.id == R.id.profileImageView) {
                Toast.makeText(this, "Clicked Profile.", Toast.LENGTH_SHORT).show()
            } else if (imageView.id == R.id.infoImageView) {
                Toast.makeText(this, "Clicked Info.", Toast.LENGTH_SHORT).show()
            } else if (imageView.id == R.id.settingImageView) {
                Toast.makeText(this, "Clicked Setting.", Toast.LENGTH_SHORT).show()
            } else if (imageView.id == R.id.powerImageView) {
                Toast.makeText(this, "Clicked Power.", Toast.LENGTH_SHORT).show()
            }
            imageView.setColorFilter(ContextCompat.getColor(this, R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP)
            imageView.setBackgroundColor(ContextCompat.getColor(this,R.color.md_grey_700))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun resetMenuColor() {

        try {
            val color = ContextCompat.getColor(this, R.color.md_grey_600)

            for (index in 0 until drawerLinearLayout.childCount) {
                val nextChild = drawerLinearLayout.getChildAt(index)

                if (nextChild is ImageView) {
                    nextChild.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                    nextChild.setBackground(null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initUI() {
        initToolbar()

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        if (Utils.isRTL) {
            navigationView.textDirection = View.TEXT_DIRECTION_RTL
        } else {
            navigationView.textDirection = View.TEXT_DIRECTION_LTR
        }

        val headerLayout = navigationView.getHeaderView(0)
        val userImageView = headerLayout.findViewById<ImageView>(R.id.userImageView)
        Utils.setCircleImageToImageView(this, userImageView, R.drawable.amanuel, 0, 0)

        val rightNavigationView = findViewById<NavigationView>(R.id.nav_view_right)
        rightNavigationView.setNavigationItemSelectedListener(this)
        if (Utils.isRTL) {
            rightNavigationView.textDirection = View.TEXT_DIRECTION_RTL
        } else {
            rightNavigationView.textDirection = View.TEXT_DIRECTION_LTR
        }

        val rightHeaderLayout = rightNavigationView.getHeaderView(0)

        drawerLinearLayout = rightHeaderLayout.findViewById(R.id.drawerLinearLayout)


    }

    private fun initToolbar() {

        toolbar = findViewById(R.id.toolbar)

        toolbar.setNavigationIcon(R.drawable.baseline_menu_black_24)

//        if (toolbar.navigationIcon != null) {
//            toolbar.navigationIcon?.setColorFilter(
//            ContextCompat.getColor(this, R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP)         }

        toolbar.title = "EriBus"

        try {
            toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.md_white_1000))
        } catch (e: Exception) {
            Log.e("TEAMPS", "Can't set color.")
        }

        try {
            setSupportActionBar(toolbar)
        } catch (e: Exception) {
            Log.e("TEAMPS", "Error in set support action bar.")
        }

        try {
            if (supportActionBar != null) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        } catch (e: Exception) {
            Log.e("TEAMPS", "Error in set display home as up enabled.")
        }

    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.user_dialog_event)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//        val spn_from_date =
//            dialog.findViewById<View>(R.id.spn_from_date) as Button
//        val spn_from_time =
//            dialog.findViewById<View>(R.id.spn_from_time) as Button
//        val spn_to_date =
//            dialog.findViewById<View>(R.id.spn_to_date) as Button
//        val spn_to_time =
//            dialog.findViewById<View>(R.id.spn_to_time) as Button
//        val tv_email = dialog.findViewById<View>(R.id.tv_email) as TextView
        val et_FullName = dialog.findViewById<View>(R.id.etUserFullName) as EditText
        val et_Email = dialog.findViewById<View>(R.id.etUserEmail) as EditText
        val et_Password = dialog.findViewById<View>(R.id.etUserPassword) as EditText
//        val et_location =
//            dialog.findViewById<View>(R.id.et_location) as EditText
//        val cb_allday: AppCompatCheckBox =
//            dialog.findViewById<View>(R.id.cb_allday) as AppCompatCheckBox
//        val spn_timezone: AppCompatSpinner =
//            dialog.findViewById<View>(R.id.spn_timezone) as AppCompatSpinner
//        val timezones =
//            resources.getStringArray(R.array.timezone)
//        val array: ArrayAdapter<String?> =
//            ArrayAdapter<Any?>(this, R.layout.simple_spinner_item, timezones)
//        array.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
//        spn_timezone.setAdapter(array)
//        spn_timezone.setSelection(0)
        (dialog.findViewById<View>(R.id.btnUserCreateCancel) as Button).setOnClickListener { dialog.dismiss() }
        (dialog.findViewById<View>(R.id.btnUserCreateSubmit) as Button).setOnClickListener {

            lateinit var auth: FirebaseAuth
            auth=FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(et_Email.text.toString(), et_Password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                    } else {
                        // If sign in fails, display a message to the user.
                        if(task.exception is FirebaseAuthUserCollisionException){
                            Toast.makeText(baseContext, "Email already registered.",
                                Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(baseContext, task.exception?.message,
                                Toast.LENGTH_SHORT).show()
                        }

                        //updateUI(null)
                    }

                    // ...
                }
            var uid=auth.uid
            val ref= FirebaseDatabase.getInstance().getReference("/Users/$uid")
            ref.setValue(Users(et_FullName.text.toString(),et_Email.text.toString(),"","Customer"))
                .addOnSuccessListener {
                    //notify successuflly saved user to database
                    Toast.makeText(baseContext, "User data inserted.",
                        Toast.LENGTH_SHORT).show()
                }

            dialog.dismiss()
            //Toast.makeText(this, "Data Successfully saved.", Toast.LENGTH_LONG).show()
        }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    fun onCreateNewAccount(view: View) {
        showCustomDialog()
    }


}
