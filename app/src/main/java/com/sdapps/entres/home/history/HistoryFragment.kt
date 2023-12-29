package com.sdapps.entres.home.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdapps.entres.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {


    private lateinit var binding: FragmentHistoryBinding

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[VM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init()

        setDataToAWS()

    }


    fun setDataToAWS(){

        val name = binding.name.text.trim()
        val email = binding.email.text.trim()

        binding.aws.setOnClickListener {

        }

    }

    fun updateUI(role: String){
        //binding.role.text = role
    }

    fun init(){



        viewModel.data.observe(viewLifecycleOwner){
            dataFromAliens ->
            //binding.role.text = dataFromAliens
        }

        viewModel.uid.observe(viewLifecycleOwner){
            observeFirebaseChanges(it)
        }
    }

    fun observeFirebaseChanges(uid: String){
        try{
            val dbRef = FirebaseDatabase.getInstance().getReference("users")
            dbRef.child(uid).addListenerForSingleValueEvent(object  : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val role = snapshot.child("role").getValue(String::class.java)
                        updateUI(role!!)
                    }else{
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("FIREBASE",error.details)
                }
            })
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }


}