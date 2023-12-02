package com.example.firebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebaseapp.databinding.ActivitySignOutBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class SignOutActivity : AppCompatActivity() {
    private lateinit var googleSignClient: GoogleSignInClient
    private lateinit var binding: ActivitySignOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignOutBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btn2.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(
                GoogleSignInOptions
                .DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build()
            googleSignClient = GoogleSignIn.getClient(this, gso)
            googleSignClient.signOut() // SignOut
            val intent = Intent(this@SignOutActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}