package com.example.firebaseapp

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.firebaseapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions
            .DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        googleSignClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        binding.btnGoogle.setOnClickListener {
            val signInClient = googleSignClient.signInIntent
            launcher.launch(signInClient)
        }
    }
    private val launcher = registerForActivityResult(ActivityResultContracts
        .StartActivityForResult()) {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "DONE", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, SignOutActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Jika akun di disable
                        Toast.makeText(this, "AKUN TELAH DIBANNED!!!", Toast.LENGTH_LONG).show()
                        val gso = GoogleSignInOptions.Builder(
                            GoogleSignInOptions
                                .DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail().build()
                        googleSignClient = GoogleSignIn.getClient(this, gso)
                        googleSignClient.signOut() // SignOut
                    }
                }
            }
        } else {
            Toast.makeText(this, "FAILED", Toast.LENGTH_LONG).show()
        }
    }
}