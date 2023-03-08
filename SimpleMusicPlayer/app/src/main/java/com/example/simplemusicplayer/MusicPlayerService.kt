package com.example.simplemusicplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast

class MusicPlayerService: Service() {

    var mMediaPlayer: MediaPlayer? = null //미디어 플레이어 객체를 null로 초기화
    var mBinder: MusicPlayerBinder = MusicPlayerBinder()
    //바인더를 반환해서비스 함수를 쓸 수 있게 함
    inner class MusicPlayerBinder: Binder() {
        fun getService(): MusicPlayerService {
            return this@MusicPlayerService
        }
    }
    //서비스가 생성될 떄 딱 한 번 실행
    override fun onCreate() {
        super.onCreate()
        //포그라운드 서비스 시작
        startForegroundService()
    }
    //바인드
    override fun onBind(p0: Intent?): IBinder? {
        return mBinder
    }
    //시작된 상태 & 백그라운드, startService()를 호출하면 실행되는 콜백 함수
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    //서비스 종료
    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true)
        }
    }

    fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val mChannel = NotificationChannel( //알림 채널 생성
                "CHANNEL_ID",
                "CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(mChannel)
        }
        //알림 생성
        val notification: Notification = Notification.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_baseline_play_arrow_24) //알림 아이콘
            .setContentTitle("뮤직 플레이어 앱") //알림의 제목 설정
            .setContentText("앱이 실행 중입니다.") //알림의 내용 설정
            .build()
        startForeground(1, notification) //인수로 알림 ID와 알림 지정
    }
    //재생 중인지 확인
    fun isPlaying(): Boolean {
        return (mMediaPlayer != null && mMediaPlayer?.isPlaying ?: false)
    }
    //재생
    fun play() {
        if (mMediaPlayer == null) {
            //음악 파일의 리소스를 가져와 미디어 플레이어 객체를 할댕해줌
            mMediaPlayer = MediaPlayer.create(this, R.raw.chocolate)

            mMediaPlayer?.setVolume(1.0f, 1.0f) //볼륨을 지정해줍니다
            mMediaPlayer?.isLooping = true //반복재생 여부를 정해줍니다
            mMediaPlayer?.start() //음악을 재생합니다
        } else { //음악 재생 중인 경우
            if (mMediaPlayer!!.isPlaying) {
                Toast.makeText(this, "이미 음악이 실행 중입니다.", Toast.LENGTH_SHORT).show()
            } else {
                mMediaPlayer?.start() //음악을 재생합니다
            }
        }
    }
    //일시정지
    fun pause() {
        mMediaPlayer?.let {
            if (it.isPlaying) {
                it.pause() //음악을 일시정지합니다.
            }
        }
    }
    //완전 정지
    fun stop() {
        mMediaPlayer?.let {
            if (it.isPlaying) {
                it.stop() //음악을 멈춥니다
                it.release() //미디어 플레이어에 할당된 자원을 해제시켜줍니다
                mMediaPlayer = null
            }
        }

    }
}