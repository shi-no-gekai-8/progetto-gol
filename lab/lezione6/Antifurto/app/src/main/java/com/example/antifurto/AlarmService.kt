package com.example.antifurto

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import kotlin.math.abs
import kotlin.math.sqrt

class AlarmService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private var mediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null

    // Primo campione utile per calcolare la differenza di movimento.
    private var hasFirstReading = false
    private var lastMagnitude = 0f

    override fun onCreate() {
        super.onCreate()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopAlarmAndService()
                return START_NOT_STICKY
            }

            ACTION_START, null -> {
                startMonitoring()
                return START_STICKY
            }

            else -> return START_NOT_STICKY
        }
    }

    /**
     * Avvia il monitoraggio del sensore.
     * Il service entra in foreground subito, così continua a vivere
     * anche se chiudiamo l'app.
     */
    private fun startMonitoring() {
        if (isRunning) return

        if (accelerometer == null) {
            stopSelf()
            return
        }

        isRunning = true
        isAlarmPlaying = false
        hasFirstReading = false

        startAsForeground(
            title = "Antifurto attivo",
            text = "Sto controllando i movimenti del telefono."
        )

        sensorManager.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    /**
     * Chiamato ogni volta che arriva un nuovo dato dal sensore.
     */
    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // Modulo del vettore accelerazione.
        // Usiamo il modulo per avere un singolo numero più facile da spiegare.
        val magnitude = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

        if (!hasFirstReading) {
            lastMagnitude = magnitude
            hasFirstReading = true
            return
        }

        // Differenza rispetto alla lettura precedente.
        // Se cambia troppo in poco tempo, consideriamo il telefono "mosso".
        val delta = abs(magnitude - lastMagnitude)
        lastMagnitude = magnitude

        if (delta > MOVEMENT_THRESHOLD && !isAlarmPlaying) {
            triggerAlarm(delta)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Per questa demo non ci serve gestire la precisione.
    }

    /**
     * Fa partire la sirena e aggiorna la notifica.
     */
    private fun triggerAlarm(delta: Float) {
        isAlarmPlaying = true

        // Per la demo portiamo il volume musica al massimo.
        // È una scelta aggressiva, ma rende bene l'effetto "antifurto".
        setMusicVolumeToMax()

        startSiren()

        updateNotification(
            title = "🚨 Allarme attivo",
            text = "Movimento rilevato! Intensità = ${"%.1f".format(delta)}"
        )
    }

    private fun startSiren() {
        if (mediaPlayer?.isPlaying == true) return

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)?.apply {
            isLooping = true
            start()
        }
    }

    private fun setMusicVolumeToMax() {
        val manager = audioManager ?: return
        val maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
    }

    private fun stopSiren() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
        }
        mediaPlayer = null
        isAlarmPlaying = false
    }

    /**
     * Ferma sensore, suono e foreground service.
     */
    private fun stopAlarmAndService() {
        try {
            sensorManager.unregisterListener(this)
        } catch (_: Exception) {
        }

        stopSiren()

        isRunning = false
        isAlarmPlaying = false

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        try {
            sensorManager.unregisterListener(this)
        } catch (_: Exception) {
        }

        stopSiren()

        isRunning = false
        isAlarmPlaying = false

        super.onDestroy()
    }

    private fun startAsForeground(title: String, text: String) {
        val notification = buildNotification(title, text)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun updateNotification(title: String, text: String) {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, buildNotification(title, text))
    }

    private fun buildNotification(title: String, text: String): Notification {
        // Apri l'app quando tocchi la notifica.
        val openAppIntent = Intent(this, MainActivity::class.java)
        val openAppPendingIntent = PendingIntent.getActivity(
            this,
            1,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Azione rapida: spegni tutto direttamente dalla notifica.
        val stopIntent = Intent(this, AlarmService::class.java).apply {
            action = ACTION_STOP
        }

        val stopPendingIntent = PendingIntent.getService(
            this,
            2,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(openAppPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .addAction(
                android.R.drawable.ic_delete,
                "Disattiva",
                stopPendingIntent
            )
            .build()
    }

    /**
     * Da Android 8 in poi le notifiche devono appartenere a un canale.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Antifurto",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canale notifiche della demo antifurto"
            }

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val ACTION_START = "com.example.antifurto.ACTION_START"
        const val ACTION_STOP = "com.example.antifurto.ACTION_STOP"

        private const val CHANNEL_ID = "alarm_channel"
        private const val NOTIFICATION_ID = 1001

        /**
         * Soglia della demo:
         * se l'emulatore è troppo sensibile, alzala a 5f o 6f.
         * se non scatta quasi mai, abbassala a 2.5f o 3f.
         */
        private const val MOVEMENT_THRESHOLD = 0.5f

        @Volatile
        var isRunning = false

        @Volatile
        var isAlarmPlaying = false
    }
}