package com.electric.ccapy.Providers

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.electric.ccapy.MainActivity
import com.electric.ccapy.Models.Device
import com.electric.ccapy.Models.Token
import com.electric.ccapy.Models.Users
import com.electric.ccapy.UI.MenuActivity
import com.electric.ccapy.UI.RegisterActivity
import com.electric.ccapy.UI.SynchronizeDeviceActivity
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityMainBinding
import com.electric.ccapy.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class AuthProviders {

    var auth : FirebaseAuth = FirebaseAuth.getInstance()
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    fun login(email : String , password : String, activity : MainActivity , binding : ActivityMainBinding){
        binding.progress.visibility = View.VISIBLE
        binding.btnLogin.visibility = View.GONE
        binding.lnRegister.visibility = View.GONE
        binding.edtCode.isEnabled = false
        binding.edtPassword.isEnabled = false
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {response ->
            if(response.isSuccessful){
                //val id : String = FirebaseAuth.getInstance().currentUser!!.uid
                getUsersDetails().get().addOnSuccessListener {
                    if(it.exists()){
                        val users = it.toObject(Users::class.java)
                        if(users!!.type == Constants.CLIENT){
                            val db = TinyDB(activity)
                            db.putObject(Constants.USER,users)
                            db.putObject(Constants.ID_CHIP,users.current_device_id)
                            //createToken(id)
                            activity.startActivity(Intent(activity,MenuActivity::class.java))
                        }else{
                            auth.signOut()
                            binding.progress.visibility = View.GONE
                            binding.btnLogin.visibility = View.VISIBLE
                            binding.lnRegister.visibility = View.VISIBLE
                            binding.edtCode.isEnabled = true
                            binding.edtPassword.isEnabled = true
                            Toast.makeText(activity,"Usuario no permitido!", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        binding.progress.visibility = View.GONE
                        binding.btnLogin.visibility = View.VISIBLE
                        binding.lnRegister.visibility = View.VISIBLE
                        binding.edtCode.isEnabled = true
                        binding.edtPassword.isEnabled = true
                        Toast.makeText(activity,"No existe el usuario!",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                binding.progress.visibility = View.GONE
                binding.btnLogin.visibility = View.VISIBLE
                binding.lnRegister.visibility = View.VISIBLE
                binding.edtCode.isEnabled = true
                binding.edtPassword.isEnabled = true
                Toast.makeText(activity,"Error, revise las credenciales",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun register(data : Users, password: String, activity: RegisterActivity, binding: ActivityRegisterBinding){

        binding.btnRegister.visibility = View.GONE
        binding.lnRegister.visibility = View.VISIBLE
        binding.lnAccess.visibility = View.GONE
        binding.edtDni.isEnabled = false
        binding.edtPhone.isEnabled = false
        binding.edtEmail.isEnabled = false
        binding.edtPassword.isEnabled = false

        db.collection(Constants.USERS).get().addOnSuccessListener {query->
            var ctx = 0
            query.forEach {v->
                val dniUser = v["dni"].toString()
                val emailUser = v["email"].toString()
                if(dniUser == data.dni){
                    ctx++
                }
                if(emailUser == data.email){
                    ctx++
                }
            }
            if(ctx == 0){
                auth.createUserWithEmailAndPassword(data.email,password).addOnCompleteListener {response->
                    if(response.isSuccessful){
                        val dbTiny = TinyDB(activity)
                        val id : String = FirebaseAuth.getInstance().currentUser!!.uid
                        data.id = id
                        db.collection(Constants.USERS).document(id).set(data)
                        dbTiny.putObject(Constants.USER,data)
                        activity.startActivity(Intent(activity,SynchronizeDeviceActivity::class.java))
                        activity.finish()
                    }
                }
            }else{
                Toast.makeText(activity,"Este usuario ya existe!",Toast.LENGTH_SHORT).show()
                binding.btnRegister.visibility = View.VISIBLE
                binding.lnRegister.visibility = View.GONE
                binding.lnAccess.visibility = View.VISIBLE
                binding.edtDni.isEnabled = true
                binding.edtPhone.isEnabled = true
                binding.edtEmail.isEnabled = true
                binding.edtPassword.isEnabled = true
            }
        }

    }

    private fun getUsersDetails() : DocumentReference {
        return db.collection(Constants.USERS).document(getCurrentUserID())
    }

    fun updateProfileDevice(data : Users,data2 : Device){
        db.collection(Constants.USERS).document(getCurrentUserID()).set(data)
        db.collection(Constants.DEVICES).document(getCurrentUserID()).set(data2)
        createToken(getCurrentUserID())
    }

    private fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    private fun createToken(id: String){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }else{
                val token = Token(task.result)
                db.collection("token").document(id).set(token)
            }
        }
    }

    fun logout(activity: MenuActivity){
        auth.signOut()
        val db = TinyDB(activity)
        db.remove(Constants.USER)
        db.remove(Constants.ID_CHIP)
        db.remove(Constants.KEY_CURRENT_DATA)
        db.remove(Constants.CACHE_CURRENT_DATA)
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

}