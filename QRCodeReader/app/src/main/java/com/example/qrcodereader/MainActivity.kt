package com.example.qrcodereader


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.qrcodereader.databinding.ActivityMainBinding
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private val PERMISSIONS_REQUEST_CODE = 1 // 태그 기능을 하는 코드
    private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA) // 카메라 권한 설정

    private var isDetected = false // 이미지 분석이 실시간으로 이루어지므로 함수가 여러 번 호출될 수 있음, 이를 막기 위한 변수

    // MainActivity로 돌아가고 다시 QR 인식을 한다면 onResume() 함수로 isDetected를 false로 돌려놓음
    override fun onResume() {
        super.onResume()
        isDetected = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!hasPermission(this)) {
            // 카메라 권한을 요청합니다.
            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
        } else {
            // 이미 권한이 있다면 카메라를 시작합니다.
            startCamera()
        }
    }

    // 권한 유뮤 확인
    fun hasPermission(context: Context) = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    // 권한 요청 콜백 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // 인수로 넣은 PERMISSION_REQUEST_CODE와 맞는지 확인합니다.
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                Toast.makeText(this@MainActivity, "권한 요청이 승인되었습니다.", Toast.LENGTH_SHORT).show()
                startCamera()
            } else {
                Toast.makeText(this@MainActivity, "권한 요청이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    // 미리보기와 이미지 분석 시장
    fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get() // 카메라의 생명 주기를 액티비티나 프레그먼트와 같은 생명 주기에 바인드 해주는 ProcessCameraProviedr 객체를 가져옴

            val preview = getPreview() // 미리보기 객체 가져오기
            val imageAnalysis = getImageAnalysis()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA // 후면 카메라 선택

            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            // 미리보기 기능 선택

        }, ContextCompat.getMainExecutor(this))
    }

    // 미리보기 객체 반환
    fun getPreview(): Preview {
        val preview: Preview = Preview.Builder().build() // Preview 객체 생성
        preview.setSurfaceProvider(binding.barcodePreview.getSurfaceProvider())

        return preview
    }

    // QRCodeAnalyzer 객체를 생성해
    fun getImageAnalysis() : ImageAnalysis {
        val cameraExecutor : ExecutorService = Executors.newSingleThreadExecutor()
        val imageAnalysis = ImageAnalysis.Builder().build()

        imageAnalysis.setAnalyzer(cameraExecutor,
            QRCodeAnalyzer(object : OnDetectListener {
                override fun onDetect(msg: String) {
                    if (!isDetected) { // QR 코드가 인식된 적 없는지 검사
                        isDetected = true // 데이터가 감지외었으므로 true로 바꿈
                        val intent = Intent(this@MainActivity, ResultActivity::class.java)
                        intent.putExtra("msg", msg)
                        startActivity(intent)
                    }
                }
            }))

        return imageAnalysis
    }


}