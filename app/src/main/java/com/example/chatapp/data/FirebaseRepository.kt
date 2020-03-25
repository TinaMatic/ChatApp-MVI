package com.example.chatapp.data

import android.content.Intent
import android.text.BoringLayout
import android.widget.Toast
import com.example.chatapp.models.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.reactivex.Observable
import java.util.function.BiPredicate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor() {

    private val mAuth = FirebaseAuth.getInstance()
    var databaseUsers: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
    var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    var currentUser: FirebaseUser? = mAuth.currentUser

    val GALLERY_ID: Int = 1

    fun loginUser(email:String, password: String): Observable<Boolean>{
        return Observable.create<Boolean> {emitter ->
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if(task.isSuccessful){
                        val username = email.split("@")[0]
                        emitter.onNext(true)
                    }else{
                        emitter.onNext(false)
                    }
                }
        }
    }

    fun createAccount(email: String, password: String, displayName: String): Observable<Boolean>{
        return Observable.create<Boolean>{emitter ->
            //create user account
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                        task: Task<AuthResult> ->

                    if(task.isSuccessful){
//                        val currentUser = mAuth!!.currentUser
                        val userId = currentUser!!.uid

                        val currentDatabaseUser = databaseUsers.child(userId)

                        val userObj = HashMap<String, String>()
                        userObj.put("display_name", displayName)
                        userObj.put("status", "Hello there...")
                        userObj.put("image", "default")
                        userObj.put("thumb_image", "default")
                        userObj.put("color", "#ffffff")

                        //add user to realtime database
                        currentDatabaseUser.setValue(userObj).addOnCompleteListener {
                                task: Task<Void> ->

                            if(task.isSuccessful){
                                emitter.onNext(true)
//                                var dashboardIntent = Intent(this, DashboardActivity::class.java)
//                                dashboardIntent.putExtra("name", displayName)
//                                startActivity(dashboardIntent)
//                                finish()
                            }else{
                                emitter.onNext(false)
//                                Toast.makeText(this, "User Not Created!", Toast.LENGTH_LONG).show()
                            }
                        }


                    }else{
                        emitter.onNext(false)
                    }
                }
        }
    }

    fun logout(): Observable<Boolean>{
        return Observable.create<Boolean> {emitter ->
            mAuth.signOut()
            emitter.onNext(true)
        }
    }

    fun loadCurrentUser(): Observable<Pair<Boolean, Users>>{

        return Observable.create<Pair<Boolean, Users>> { emitter->
            val userId = currentUser?.uid

            val currentDatabaseUser = databaseUsers.child(userId!!)

            currentDatabaseUser.addValueEventListener(object : ValueEventListener{

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //geting the value of the display name in the database
                    val displayName = dataSnapshot.child("display_name").value.toString()
                    val image = dataSnapshot.child("image").value.toString()
                    val thumbImage = dataSnapshot.child("thumb_image").value.toString()
                    val status = dataSnapshot.child("status").value.toString()
                    val color = dataSnapshot.child("color").value.toString()

                    val user = Users(displayName, image, thumbImage, status, color)

                    emitter.onNext(Pair(true, user))
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onNext(Pair(false, Users(null, null, null, null, null)))
                }

            })
        }
    }
}