package com.example.imageload

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.example.imageload.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val requestMultiplePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            result.forEach {
                Log.e("test", result.toString())
                if (!it.value) {
                    Toast.makeText(applicationContext, "${it.key}권한 허용 필요", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
        }

    private val readImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        binding.imgLoad.load(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestMultiplePermission.launch(permissionList)

        binding.btnFileImg.setOnClickListener {
            readImage.launch("image/*")
        }

    }
}