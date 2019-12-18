package com.emito.eribus.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.emito.eribus.LoginActivity
import com.emito.eribus.R
import com.emito.eribus.fragment.AboutFragment
import com.emito.eribus.fragment.LoginListFragment
import com.emito.eribus.fragment.userProfileFragment
import com.emito.eribus.model.Users
import com.emito.eribus.utils.Utils
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.feature_menu_general_menu_4_activity.*

class DriverActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    internal lateinit var toolbar: Toolbar
    internal lateinit var drawerLinearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)

        initUI()
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

        if (id == R.id.nav_driver_home) {
            Toast.makeText(this, "Clicked Home.", Toast.LENGTH_SHORT).show()
        } else if (id == R.id.nav_share_location) {
            Toast.makeText(this, "Location Shared.", Toast.LENGTH_SHORT).show()
        } else if (id == R.id.nav_Drivers) {
            Toast.makeText(this, "Clicked Drivers.", Toast.LENGTH_SHORT).show()
        } else if (id == R.id.nav_Bus) {
            Toast.makeText(this, "Clicked Buses.", Toast.LENGTH_SHORT).show()
        } else if (id == R.id.nav_route) {
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
