package com.emito.eribus

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.emito.eribus.database.SQLController
import com.emito.eribus.model.Users
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_create_account.*
import java.util.*
import java.util.regex.Pattern

class createAccount : AppCompatActivity() {
    //lateinit var dbcon: SQLController
    private lateinit var auth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
     var selectedPhotoUri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        initActions()
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
//        mDatabase=FirebaseDatabase.getInstance().getReference("Users")
//        dbcon= SQLController(this)
//        dbcon.open()
        registerButton.setOnClickListener{
            SignUp()
        }
    }

    fun SignUp(){
        if(etEmail.text.toString().isEmpty())
        {
            etEmail.error="Please provide Email Address"
            etEmail.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()){
            etEmail.error="Please provide valid Email"
            etEmail.requestFocus()
            return
        }
        if(etPassword.text.toString().isEmpty())
        {
            etPassword.error="Please provide password"
            etPassword.requestFocus()
            return
        }
        if(etPassword.text.toString().length<6)
        {
            etPassword.error="Minimum length of password should be 6."
            etPassword.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user:FirebaseUser?=auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { it ->
                            if (it.isSuccessful) {
                                Toast.makeText(baseContext, "User Created successfully.",
                                    Toast.LENGTH_SHORT).show()
                                uploadImageToFirebaseStorage()
                                finish()
                            }
                        }

//                    var userProfile=Users(etUserName.text.toString(),etEmail.text.toString(),"")
//                    FirebaseDatabase.getInstance().getReference("Users")
//                        .child(FirebaseAuth.getInstance().currentUser!!. uid)
//                        .setValue(user).addOnCompleteListener(OnCompleteListener {
//                            if (it.isSuccessful){
//                                Toast.makeText(baseContext, "User additional data successfully added.",
//                                    Toast.LENGTH_SHORT).show()
//                            }
//                        })
                    // Sign in success, update UI with the signed-in user's information
                    //val user = auth.currentUser
                    //updateUI(user)

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
    }

    private fun uploadImageToFirebaseStorage() {
        if(selectedPhotoUri==null) return
        val fileName=UUID.randomUUID().toString()
        val ref =FirebaseStorage.getInstance().getReference("/Images/$fileName")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Toast.makeText(baseContext, "Successfully upload image. "+it.metadata?.path,
                    Toast.LENGTH_SHORT).show()
                ref.downloadUrl.addOnSuccessListener {
                    //toast text by  $it
                    saveUserToFilebaseDataBase(it.toString())
                }
            }
            .addOnFailureListener{
                //do some login failure
            }
    }

    private fun saveUserToFilebaseDataBase(profileImageUrl:String) {
        var uid=FirebaseAuth.getInstance().uid?:""
        val ref=FirebaseDatabase.getInstance().getReference("/Users/$uid")
        ref.setValue(Users(etUserName.text.toString(),etEmail.text.toString(),profileImageUrl,"Customer"))
            .addOnSuccessListener {
                //notify successuflly saved user to database
                Toast.makeText(baseContext, "User data inserted.",
                    Toast.LENGTH_SHORT).show()
            }
    }


    private fun initActions() {
        helpButton.setOnClickListener {
            Toast.makeText(applicationContext, "Clicked Need Help?", Toast.LENGTH_SHORT).show()
        }

        loginButton.setOnClickListener {
            Toast.makeText(applicationContext, "Clicked Login", Toast.LENGTH_SHORT).show()
        }
        imageButton.setOnClickListener{
            onImageSelector()
        }
     //   registerButton.setOnClickListener {
            //Toast.makeText(applicationContext, "Clicked Register", Toast.LENGTH_SHORT).show()
//            var user=Users(etUserName.text.toString(),etEmail.text.toString(),etPassword.text.toString())
//            if(dbcon.InsertUser(user)) {
//                Toast.makeText(
//                    getApplicationContext(), "Successfully Inserted: UserName: ${user.userName}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }else
//            {
//                Toast.makeText(
//                    getApplicationContext(), "UnSuccessfully Insertion of data.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }

        //}
    }

    fun onImageSelector() {
        var intent= Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== Activity.RESULT_OK && data!=null){
            selectedPhotoUri=data.data
            imageButton.setImageURI(selectedPhotoUri)
            //val bitmap= MediaStore.Images.Media.getBitmap(contentResolver,uri)
        }
    }
    //endregion
}
