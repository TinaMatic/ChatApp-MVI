package com.example.chatapp.data

import android.content.Intent
import android.text.BoringLayout
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import java.util.function.BiPredicate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor() {

    private val mAuth = FirebaseAuth.getInstance()
    var mDatabase: DatabaseReference? = null

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
                        val currentUser = mAuth!!.currentUser
                        val userId = currentUser!!.uid

                        mDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                        val userObj = HashMap<String, String>()
                        userObj.put("display_name", displayName)
                        userObj.put("status", "Hello there...")
                        userObj.put("image", "default")
                        userObj.put("thumb_image", "default")
                        userObj.put("color", "#ffffff")

                        //add user to realtime database
                        mDatabase!!.setValue(userObj).addOnCompleteListener {
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
}