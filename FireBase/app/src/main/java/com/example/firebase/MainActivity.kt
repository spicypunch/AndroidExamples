package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            signIn(binding.editId.text.toString(), binding.editPw.text.toString())
        }

    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    private fun signIn(email: String, passwd:String) {
        auth?.signInWithEmailAndPassword(email, passwd)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "로그인에 성공 하였습니다.", Toast.LENGTH_SHORT).show()
                    moveMainPage(auth?.currentUser)
                } else {
                    Toast.makeText(baseContext, "로그인에 실패 하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}