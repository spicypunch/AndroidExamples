package com.example.viewbindingexam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)

        button1.setOnClickListener{
            Toast.makeText(this, "버튼1", Toast.LENGTH_SHORT).show()
        }

        button2.setOnClickListener{
            Toast.makeText(this, "버튼2", Toast.LENGTH_SHORT).show()
        }

        button3.setOnClickListener{
            Toast.makeText(this, "버튼3", Toast.LENGTH_SHORT).show()
        }

    }
}