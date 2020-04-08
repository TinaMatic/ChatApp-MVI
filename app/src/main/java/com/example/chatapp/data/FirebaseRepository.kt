package com.example.chatapp.data

import android.graphics.Bitmap
import android.net.Uri
import com.example.chatapp.models.Messages
import com.example.chatapp.models.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.reactivex.Observable
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor() {

    val mAuth = FirebaseAuth.getInstance()
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    var databaseUsers: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
    var databaseMessages: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Messages")
    var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    var currentUser: FirebaseUser? = null

    var receivingUserId: String? = null

    fun isUserLoggedIn(): Observable<Boolean>{
        currentUser =  mAuth.currentUser

        return Observable.create<Boolean> {emitter ->
            mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth ->

                val user = firebaseAuth.currentUser

                if(user != null){
                    emitter.onNext(true)
                }else{
                    emitter.onNext(false)
                }
            }
        }
    }

    fun loginUser(email:String, password: String): Observable<Boolean>{
        return Observable.create<Boolean> {emitter ->
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if(task.isSuccessful){
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
                        currentUser =  mAuth.currentUser
                        val userId = currentUser!!.uid

                        val currentDatabaseUser = databaseUsers.child(userId)

                        val user = Users(userId, displayName,  "Default image", "Default thumb image", "Default status", "#ffffff")


                        //add user to realtime database
                        currentDatabaseUser.setValue(user).addOnCompleteListener {
                                task: Task<Void> ->

                            if(task.isSuccessful){
                                emitter.onNext(true)
                            }else{
                                emitter.onNext(false)
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

    fun loadUsers(): Observable<Pair<Boolean, List<Users>>>{
        currentUser =  mAuth.currentUser
        val currentUserId = currentUser?.uid

        return Observable.create<Pair<Boolean, List<Users>>> { emitter ->
            val allUsers = arrayListOf<Users>()

            databaseUsers.orderByChild("display_name").addValueEventListener(object : ValueEventListener{

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val orderSnapshot = dataSnapshot.children

                    if(dataSnapshot.value != null){
                        for (users in orderSnapshot){
                            val userId = users.key

                            //do not include currently logged in user
                            if(currentUserId != userId){
                                val displayName = users.child("displayName").value.toString()
                                val image = users.child("image").value.toString()
                                val thumbImage = users.child("thumbImage").value.toString()
                                val status = users.child("status").value.toString()
                                val color = users.child("color").value.toString()

                                val tempUser = Users(userId, displayName, image, thumbImage, status, color)

                                allUsers.add(tempUser)
                            }
                        }
                        emitter.onNext(Pair(true, allUsers))
                    }else{
                        emitter.onNext(Pair(true, emptyList()))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onNext(Pair(false, emptyList()))
                }

            })
        }
    }

    fun loadSingleUser(userId: String): Observable<Pair<Boolean, Users>>{
        return Observable.create<Pair<Boolean, Users>> { emitter->
            val currentDatabaseUser = databaseUsers.child(userId)

            currentDatabaseUser.addValueEventListener(object : ValueEventListener{

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //geting the value of the display name in the database
                    val displayName = dataSnapshot.child("displayName").value.toString()
                    val image = dataSnapshot.child("image").value.toString()
                    val thumbImage = dataSnapshot.child("thumbImage").value.toString()
                    val status = dataSnapshot.child("status").value.toString()
                    val color = dataSnapshot.child("color").value.toString()

                    val user = Users(userId, displayName, image, thumbImage, status, color)

                    emitter.onNext(Pair(true, user))
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onNext(Pair(false, Users(null,null, null, null, null, null)))
                }

            })
        }
    }

    fun loadCurrentUser(): Observable<Pair<Boolean, Users>>{
        currentUser =  mAuth.currentUser

        return Observable.create<Pair<Boolean, Users>> { emitter->
            val userId = currentUser?.uid

            val currentDatabaseUser = databaseUsers.child(userId!!)

            currentDatabaseUser.addValueEventListener(object : ValueEventListener{

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //geting the value of the display name in the database
                    val displayName = dataSnapshot.child("displayName").value.toString()
                    val image = dataSnapshot.child("image").value.toString()
                    val thumbImage = dataSnapshot.child("thumbImage").value.toString()
                    val status = dataSnapshot.child("status").value.toString()
                    val color = dataSnapshot.child("color").value.toString()

                    val user = Users(userId, displayName, image, thumbImage, status, color)

                    emitter.onNext(Pair(true, user))
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onNext(Pair(false, Users(null,null, null, null, null, null)))
                }

            })
        }
    }


    fun changeColor(color: String): Observable<Boolean>{
        currentUser =  mAuth.currentUser
        val userId = currentUser?.uid
        val currentDatabaseUser = databaseUsers.child(userId!!)

        return Observable.create {emitter ->
            currentDatabaseUser.child("color").setValue(color).addOnCompleteListener {
                    task: Task<Void> ->

                if(task.isSuccessful){
                    emitter.onNext(true)
                }else{
                    emitter.onNext(false)
                }
            }
        }
    }

    fun changeStatus(status: String): Observable<Boolean>{
        currentUser =  mAuth.currentUser
        val userId = currentUser?.uid
        val currentDatabaseUser = databaseUsers.child(userId!!)

        return Observable.create<Boolean> {emitter ->
            currentDatabaseUser.child("status")
                .setValue(status).addOnCompleteListener {
                        task: Task<Void> ->
                    if(task.isSuccessful){
                        emitter.onNext(true)
                    }
                    else{
                        emitter.onNext(false)
                    }
                }
        }
    }
    
    fun changeName(name: String): Observable<Boolean>{
        currentUser =  mAuth.currentUser
        val userId = currentUser?.uid
        val currentDatabaseUser = databaseUsers.child(userId!!)

        return Observable.create<Boolean> {emitter ->

            currentDatabaseUser.child("displayName").setValue(name).addOnCompleteListener {
                    task: Task<Void> ->
                if(task.isSuccessful){
                    emitter.onNext(true)
                }else{
                    emitter.onNext(false)
                }
            }
        }
    }

    fun changePicture(resultUri: Uri?): Observable<Boolean>{
        val thumbBitmap: Bitmap? = null
        currentUser =  mAuth.currentUser
        val userId = currentUser?.uid
        val currentDatabaseUser = databaseUsers.child(userId!!)

        return Observable.create<Boolean> {emitter ->
            //upload images to firebase
            val byteArray = ByteArrayOutputStream()
            thumbBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)

            val thumbByteArray: ByteArray = byteArray.toByteArray()

            val filePath = storageRef.child("chat_profile_images")
                .child("$userId.jpg")

            //create another directory for thumb images
            val thumbFilePath = storageRef.child("chat_profile_images")
                .child("thumbs")
                .child("$userId.jpg")

            filePath.putFile(resultUri!!).addOnCompleteListener {task: Task<UploadTask.TaskSnapshot> ->
                if(task.isSuccessful){

                    var downloadUrl: Any? = null
                    filePath.downloadUrl.addOnCompleteListener {task: Task<Uri> ->
                        downloadUrl = task.result.toString()
                    }

                    //Upload task
                    val uploadTask: UploadTask = thumbFilePath.putBytes(thumbByteArray)

                    uploadTask.addOnCompleteListener {task: Task<UploadTask.TaskSnapshot> ->

                        val thumbUrl = task.result?.storage?.downloadUrl.toString()

                        if(task.isSuccessful){

                            val updateObj = HashMap<String, Any>()
                            updateObj["image"] = downloadUrl!!
                            updateObj["thumbImage"] = thumbUrl

                            //save the profile image
                            currentDatabaseUser.updateChildren(updateObj).addOnCompleteListener {task: Task<Void> ->
                                if(task.isSuccessful){
                                    emitter.onNext(true)
                                }else{
                                    emitter.onNext(false)
                                }
                            }

                        }else{
                            emitter.onNext(false)
                        }
                    }

                }else{
                    emitter.onNext(false)
                }
            }
        }
    }

    fun loadMessages(): Observable<Pair<Boolean, List<Messages>>>{
        currentUser =  mAuth.currentUser
        val currentUserId = currentUser?.uid

        return Observable.create<Pair<Boolean, List<Messages>>> {emitter ->

            val tempMessages = arrayListOf<Messages>()

            databaseMessages.addValueEventListener(object : ValueEventListener{

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val orderSnapshot = dataSnapshot.children

                    tempMessages.clear()
                    if(dataSnapshot != null){
                        for (messages in orderSnapshot){
                            val sendingUserId = messages.child("sendingId").value.toString()
                            val receivingUser = messages.child("receivingId").value.toString()

                            if((currentUserId.equals(sendingUserId) and receivingUserId!!.equals(receivingUser)) or
                                (currentUserId.equals(receivingUser) and receivingUserId.equals(sendingUserId))){
                                val messageId = messages.child("messageId").value.toString()
                                val text = messages.child("text").value.toString()
                                val receiveName = messages.child("receiverName").value.toString()
                                val sendName = messages.child("senderName").value.toString()

                                val messageObejct = Messages(messageId, sendingUserId, receivingUser, text, receiveName, sendName)

                                tempMessages.add(messageObejct)
                            }
                        }
                        emitter.onNext(Pair(true, tempMessages))
                    } else{
                        emitter.onNext(Pair(true, emptyList()))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onNext(Pair(false, emptyList()))
                }

            })
        }
    }

    //load two users that are chetting with each other
    fun loadChatUser(): Observable<List<Users>>{
        currentUser =  mAuth.currentUser
        val currentUserId = currentUser?.uid

        return Observable.create<List<Users>> {emitter ->
            val tempUsers = arrayListOf<Users>()

            databaseUsers.addValueEventListener(object : ValueEventListener{

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val orderSnapshot = dataSnapshot.children

                    if(dataSnapshot != null){
                        for (users in orderSnapshot){
                            val userId = users.child("userId").value.toString()

                            if(userId.equals(currentUserId) or userId.equals(receivingUserId)){
                                val displayName = users.child("displayName").value.toString()
                                val image = users.child("image").value.toString()
                                val thumbImage = users.child("thumbImage").value.toString()
                                val status = users.child("status").value.toString()
                                val color = users.child("color").value.toString()

                                val user = Users(userId, displayName, image, thumbImage, status, color)

                                tempUsers.add(user)
                            }
                        }

                        emitter.onNext(tempUsers)
                    }else{
                        emitter.onNext(emptyList())
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    emitter.onNext(emptyList())
                }
            })
        }
    }

    fun loadAllChatUsers(): Observable<List<Users>>{
        currentUser =  mAuth.currentUser
        val currentUserId = currentUser?.uid

        return Observable.create<List<Users>> { emitter ->
            val allUsers = arrayListOf<Users>()

            loadUsers().subscribe {(isSuccessful, users) ->
                if(isSuccessful){
                    databaseMessages.addListenerForSingleValueEvent(object : ValueEventListener{

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            users.forEach {user->

                                val orderSnapshot = dataSnapshot.children

                                for(messages in orderSnapshot){
                                    val sendingUserId = messages.child("sendingId").value.toString()
                                    val receivingUserId = messages.child("receivingId").value.toString()

                                    if((user.userId.equals(sendingUserId) and currentUserId.equals(receivingUserId)) or
                                        (user.userId.equals(receivingUserId) and currentUserId.equals(sendingUserId))){
                                        allUsers.add(user)
                                        break
                                    }
                                }
                            }
                                emitter.onNext(allUsers)
                        }

                        override fun onCancelled(error: DatabaseError) {
                                emitter.onNext(emptyList())
                        }
                    })
                }else{
                    emitter.onNext(emptyList())
                }
            }
        }
    }

    fun sendMessage(messages: Messages): Observable<Boolean>{
        val messageId = databaseMessages.push().key

        val tempMessage = Messages(messageId, messages.sendingId, messages.receivingId, messages.text, messages.receiverName, messages.senderName)
        return Observable.create<Boolean> {emitter ->
            databaseMessages.child(messageId!!).setValue(tempMessage).addOnCompleteListener {task: Task<Void> ->
                if(task.isSuccessful){
                    emitter.onNext(true)
                }else{
                    emitter.onNext(false)
                }
            }

        }
    }

    fun editMessage(message: String, messageId: String): Observable<Boolean>{
        return Observable.create<Boolean> {emitter ->
            databaseMessages.child(messageId)
                .child("text").setValue(message).addOnCompleteListener {
                        task: Task<Void> ->
                    if(task.isSuccessful){
                        emitter.onNext(true)

                    }else{
                        emitter.onNext(false)
                    }
                }
        }
    }

    fun deleteMessage(messageId: String): Observable<Boolean>{
        return Observable.create<Boolean> {emitter ->
            databaseMessages.child(messageId).removeValue()
            emitter.onNext(true)
        }
    }
}