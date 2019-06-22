package com.example.alohomora

import android.app.Activity
import android.content.AbstractThreadedSyncAdapter
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var providers: List<AuthUI.IdpConfig>

    val MY_REQUEST_CODE: Int = 7117 // um número qualquer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val item1 = AHBottomNavigationItem("Início", R.drawable.ic_home_black_24dp,android.R.color.white )
        val item2 = AHBottomNavigationItem("Reservas", R.drawable.ic_event_note_black_24dp,android.R.color.white )
        val item3 = AHBottomNavigationItem("Liberar", R.drawable.ic_lock_open_black_24dp,android.R.color.white )
        val item4 = AHBottomNavigationItem("Perfil", R.drawable.ic_face_black_24dp,android.R.color.white )

        //add item
        bottom_nav.addItem(item1)
        bottom_nav.addItem(item2)
        bottom_nav.addItem(item3)
        bottom_nav.addItem(item4)


        bottom_nav.setOnTabSelectedListener { position, wasSelected ->
            Toast.makeText(this@MainActivity,"Start activity: "+position,Toast.LENGTH_SHORT).show()
            true
        }

        //init
        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            //AuthUI.IdpConfig.FacebookBuilder().build(),
            //AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
            )
        ShowSignInOptions()

        // Event
        btn_sign_out.setOnClickListener {
            //Signout
            AuthUI.getInstance().signOut(this@MainActivity)
                .addOnCompleteListener {
                    btn_sign_out.isEnabled=false
                    ShowSignInOptions()
                }
                .addOnFailureListener{
                    e-> Toast.makeText( this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_REQUEST_CODE)
        {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK)
            {
                val user = FirebaseAuth.getInstance().currentUser // get current user
                Toast.makeText(this,""+user!!.email,Toast.LENGTH_SHORT).show()
                btn_sign_out.isEnabled = true
            }
            else
            {
                Toast.makeText(this,""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ShowSignInOptions(){

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.MyTheme)
            .build(),MY_REQUEST_CODE)

    }



}
