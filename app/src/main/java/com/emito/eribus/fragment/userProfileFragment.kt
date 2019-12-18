package com.emito.eribus.fragment


import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.emito.eribus.R
import com.emito.eribus.utils.Utils

/**
 * A simple [Fragment] subclass.
 */
class userProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)

//        initData()
//
//        initUI()
//
//        initActions()
    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            finish()
//        } else {
//            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    //endregion
//
//    //region Init Functions
//    private fun initData() {
//
//    }
//
//    private fun initUI() {
//        initToolbar()
//
//        val id = R.drawable.profile2
//        Utils.setCircleImageToImageView(applicationContext, profileImageView, id, 20, R.color.md_white_1000)
//
//        val coverUserImageView = findViewById<ImageView>(R.id.coverUserImageView)
//        Utils.setImageToImageView(applicationContext, coverUserImageView, id)
//
//    }
//
//    private fun initActions() {
//        emailTextView.setOnClickListener {
//            try {
//                val intent = Intent(Intent.ACTION_VIEW)
//                val data = Uri.parse("mailto:?subject=" + "Hello" + "&body=" + "About Awesome Material App")
//                intent.data = data
//                intent.putExtra(Intent.EXTRA_EMAIL, emailTextView.text.toString())
//                startActivity(intent)
//
//            } catch (e: ActivityNotFoundException) {
//                e.printStackTrace()
//            }
//        }
//
//        phoneTextView.setOnClickListener {
//            try {
//
//                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneTextView.text.toString()))
//                startActivity(intent)
//
//            } catch (e: ActivityNotFoundException) {
//                e.printStackTrace()
//            }
//        }
//
//        websiteTextView.setOnClickListener {
//            try {
//                val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteTextView.text.toString()))
//                startActivity(myIntent)
//            } catch (e: ActivityNotFoundException) {
//                e.printStackTrace()
//            }
//        }
//
//        editFAB.setOnClickListener {
//
//            Toast.makeText(applicationContext, "Click Edit FAB", Toast.LENGTH_SHORT).show()
//
//        }
//    }
//
//    private fun initToolbar() {
//
//        toolbar.setNavigationIcon(R.drawable.baseline_menu_black_24)
//
//        if (toolbar.navigationIcon != null) {
//            toolbar.navigationIcon?.setColorFilter(ContextCompat.getColor(this, R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP)
//        }
//
//        toolbar.title = "User Profile 2"
//
//        try {
//            toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.md_white_1000))
//        } catch (e: Exception) {
//            Log.e("TEAMPS", "Can't set color.")
//        }
//
//        try {
//            setSupportActionBar(toolbar)
//        } catch (e: Exception) {
//            Log.e("TEAMPS", "Error in set support action bar.")
//        }
//
//        try {
//            if (supportActionBar != null) {
//                supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            }
//        } catch (e: Exception) {
//            Log.e("TEAMPS", "Error in set display home as up enabled.")
//        }
//
//    }
//    //endregion

}
