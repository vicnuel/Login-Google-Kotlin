package com.vicnuel.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.vicnuel.login.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initButtonGoogle()

        binding.buttonLogin.setOnClickListener {

            val username = binding.editUsername.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            when {
                username.isEmpty() -> {
                    binding.editUsername.error = "Campo obrigatório"
                }

                password.isEmpty() -> {
                    binding.editPassword.error = "Campo obrigatário"
                }

                username.isNotEmpty() && password.isNotEmpty() -> {
                    login(username, password)
                }

            }


        }
    }

    private fun login(username: String, password: String) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCustomToken:success")
                    val user = auth.currentUser

                    Toast.makeText(
                        baseContext, "Autenticado com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.editUsername.text.clear()
                    binding.editPassword.text.clear()

                    openHome()
                    //                            updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCustomToken:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Erro de autenticação",
                        Toast.LENGTH_SHORT
                    ).show()
                    //                            updateUI(null)
                }
            }
    }

    private fun openHome() {
        startActivity(Intent(this@MainActivity, HomeActivity::class.java))

        finish()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
//        updateUI(currentUser)


        if (currentUser != null) {
            when {
                currentUser.email!!.isNotEmpty() -> {
                    openHome()
                }

            }
        }
    }

    private fun initButtonGoogle() {
        val textButtonGoogle = binding.buttonGoogle.getChildAt(0) as TextView
        textButtonGoogle.text = getString(R.string.entrar_com_google)
    }
}