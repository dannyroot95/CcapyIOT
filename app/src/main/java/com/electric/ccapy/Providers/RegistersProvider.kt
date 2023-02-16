package com.electric.ccapy.Providers

import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.UI.MenuActivity
import com.electric.ccapy.UI.RegistersActivity
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityMenuBinding
import com.electric.ccapy.databinding.ActivityRegistersBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegistersProvider {

    var db : DatabaseReference = Firebase.database.reference

    fun getData(activity : RegistersActivity, binding : ActivityRegistersBinding){
        val idChip = TinyDB(activity).getString(Constants.ID_CHIP).replace("\"","")
        db.child(Constants.DEVICES).child(idChip).child(Constants.REGISTERS).limitToLast(25).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val reg : ArrayList<DataDevice> = ArrayList()
                    for(postSnapshot in snapshot.children){
                        val data = postSnapshot.getValue(DataDevice::class.java)!!
                        reg.add(data)
                        reg.reverse()
                    }
                    activity.receiveData(reg)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}
