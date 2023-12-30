package com.sdapps.entres.main.home.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileVM: ViewModel() {

    lateinit var _profileData: MutableLiveData<ProfileBO>
    lateinit var _firebaseRef : DatabaseReference



    fun getUserProfileData(): LiveData<ProfileBO>{
        _profileData = MutableLiveData()
        if(_profileData.value == null){
            loadUserProfileData()
        }
        return _profileData
    }


    fun loadUserProfileData(){
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        currentUser?.let {
            val uid: String = it.uid

            _firebaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("contactDetails")

            _firebaseRef?.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userProfile : ProfileBO = snapshot.getValue(ProfileBO::class.java)!!
                    _profileData.value = userProfile
                }
                override fun onCancelled(error: DatabaseError) {
                   Log.d("ERR", "-> ${error.code} : ${error.message} : ${error.details}")
                }
            })
        }
    }


}