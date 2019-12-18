package com.emito.eribus.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emito.eribus.R
import com.emito.eribus.adapter.UserProfileAdapter
import com.emito.eribus.model.Users
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.user_dialog_event.*
import kotlinx.android.synthetic.main.user_dialog_event.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class LoginListFragment : Fragment() {
    lateinit var userView: View
    private lateinit var reference: DatabaseReference
    var r1: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var madr: UserProfileAdapter? = null
    private lateinit var usersList: ArrayList<Users>
    private lateinit var btnsaveUser:Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        userView = inflater.inflate(R.layout.fragment_login_list, container, false)

        usersList = ArrayList<Users>()
        r1 = userView.findViewById<RecyclerView>(R.id.userListRecyclerView)
        layoutManager = LinearLayoutManager(userView.context)
        r1?.layoutManager = layoutManager

//        reference=FirebaseDatabase.getInstance().reference.child("Users")
        reference = FirebaseDatabase.getInstance().getReference("Users")
        //reference=FirebaseDatabase.getInstance().getReference().child(user.uid)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                //val users = dataSnapshot.getValue(Users::class.java)
                for (dss in dataSnapshot.children) {
                    //userName:String,val FullName:String,var Email:String,var Picture:String,var UserType
                    //val userName = dss.child("userName").getValue().toString()
                    val fullName = dss.child("fullName").getValue().toString()
                    val email = dss.child("email").getValue().toString()
                    val picture = dss.child("picture").getValue().toString()
                    val userType = dss.child("userType").getValue().toString()
                    val user = Users(fullName, email, picture, userType)
                    usersList.add(user)
                    // Toast.makeText(userView.context, "This is emito" + user.toString(), Toast.LENGTH_SHORT).show()
                }


                madr = UserProfileAdapter(userView.context, usersList)
                r1?.adapter = madr

                //for the total Users
                var totalUsersTextView: TextView =
                    userView.findViewById<TextView>(R.id.totalUsersTextView)
                totalUsersTextView.text = madr!!.itemCount.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }

        reference.addListenerForSingleValueEvent(postListener)

        return userView
    }


}
