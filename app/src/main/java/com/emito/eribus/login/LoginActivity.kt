package com.emito.eribus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.emito.eribus.activity.AdministratorActivity
import com.emito.eribus.activity.CustomerActivity
import com.emito.eribus.activity.DriverActivity
import com.emito.eribus.model.Users
import com.emito.eribus.utils.ValueListenerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_login.loginButton
import kotlinx.android.synthetic.main.activity_create_account.etUserName as etUserName1

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private  var user:FirebaseUser?=null
    private  lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private var userType:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initActions()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()


    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private  fun updateUI(currentUser:FirebaseUser?){
        if(currentUser!=null) {
            if(currentUser.isEmailVerified){
                provideRightActivity()

                when(userType){
                    "Administrator"->{
                        var intent=Intent(this,AdministratorActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "Driver"->{
                        var intent=Intent(this,DriverActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "Customer"->{
                        var intent=Intent(this,CustomerActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

            }else{
                Toast.makeText(baseContext, "Please verify your Email Address .",
                    Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun provideRightActivity() {
        user= auth.currentUser
        if(user!=null){
            var uid=user?.uid
            reference=FirebaseDatabase.getInstance().getReference("/Users/$uid")
            //reference=FirebaseDatabase.getInstance().getReference().child(user.uid)
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    //val users = dataSnapshot.getValue(Users::class.java)
                    // ...
                    userType=dataSnapshot.child("userType").getValue().toString()
//                    Toast.makeText(baseContext, "user type is $userType",
//                        Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                    // ...
                }
            }

            reference.addListenerForSingleValueEvent(postListener)

        }
    }
    //endregion


    private fun initActions() {
        needHelpTextView.setOnClickListener {
            Toast.makeText(applicationContext, "Clicked Need Help?", Toast.LENGTH_SHORT).show()
        }

        createAccountButton.setOnClickListener {
            var intent=Intent(this,createAccount::class.java)
            startActivity(intent)
            //Toast.makeText(applicationContext, "Clicked Create Account", Toast.LENGTH_SHORT).show()
        }

        forgotButton.setOnClickListener {
            var intent=Intent(this,ForgotPassword::class.java)
            startActivity(intent)
            //Toast.makeText(applicationContext, "Clicked Forgot Password?", Toast.LENGTH_SHORT).show()
        }

        loginButton.setOnClickListener {
            //Toast.makeText(applicationContext, "Clicked Login", Toast.LENGTH_SHORT).show()
           doLogin()

        }
    }

    fun doLogin(){
        if(etUserName.text.toString().isEmpty())
        {
            etUserName.error="Please provide Email Address"
            etUserName.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(etUserName.text.toString()).matches()){
            etUserName.error="Please provide valid Email"
            etUserName.requestFocus()
            return
        }
        if(etPassword.text.toString().isEmpty())
        {
            etPassword.error="Please provide password"
            etPassword.requestFocus()
            return
        }

        progressBar.visibility= View.VISIBLE
        auth.signInWithEmailAndPassword(etUserName.text.toString(), etPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                progressBar.visibility= View.GONE
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)

                } else {
                    // If sign in fails, display a message to the user.
                    updateUI(null)
                }

            }

    }
}
