package com.example.firebase

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity: AppCompatActivity() {
    private var auth : FirebaseAuth? = null
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        binding.btnComplete.setOnClickListener {
            Log.e("@@@@@@@",binding.editId.toString())
            createAccount(binding.editId.text.toString(), binding.editPw.text.toString())
        }
    }

    private fun createAccount(email: String, passwd: String) {
        if (email.isNotEmpty() && passwd.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, passwd)
                ?.addOnCompleteListener(this) { task ->
                    Log.e("@@@@@@@", task.isSuccessful.toString())
                    Log.e("email", email)
                    Log.e("passwd", passwd)
                    if (task.isSuccessful) {
                        Toast.makeText(this, "계정 생성 완료", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "계정 생성 실패", Toast.LENGTH_SHORT).show()
                    }


                }
        } else {
            Toast.makeText(this, "빈칸을 전부 채워주세요", Toast.LENGTH_SHORT).show()
        }
    }
}