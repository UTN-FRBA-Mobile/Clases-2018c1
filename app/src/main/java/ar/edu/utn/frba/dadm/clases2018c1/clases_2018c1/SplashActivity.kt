package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import java.util.*

/**
 * Created by guille on 4/16/18.
 */
class SplashActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        FirebaseApp.initializeApp(this)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            auth.currentUser!!.reload().addOnCompleteListener { task ->
                if (auth.currentUser != null) {
                    launchMain()
                } else {
                    startLoginActivity()
                }
            }.addOnFailureListener { e ->
                auth.signOut()
                startLoginActivity()
            }
        } else {
            startLoginActivity()
        }

    }


    private fun startLoginActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
//                        .setLogo(R.drawable.)
                        .setAvailableProviders(
                                Arrays.asList<AuthUI.IdpConfig>(
                                        AuthUI.IdpConfig.GoogleBuilder().build(),
                                        AuthUI.IdpConfig.EmailBuilder().build(),
                                        AuthUI.IdpConfig.PhoneBuilder().build()
                                ))
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                launchMain()
                return
            } else {
                showError("Error")
                startLoginActivity()
            }
        }
    }

    fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    fun launchMain() {
        startActivity(Intent(this, MainActivityJava::class.java))
        //startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }
}