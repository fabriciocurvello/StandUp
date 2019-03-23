package com.example.standup

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.ToggleButton

class MainActivity : AppCompatActivity() {

    private var mNotificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val alarmToggle = findViewById<ToggleButton>(R.id.alarmToggle)

        val notifyIntent = Intent(this, AlarmReceiver::class.java)

        val alarmUp = PendingIntent.getBroadcast(this, NOTIFICATION_ID,
                notifyIntent, PendingIntent.FLAG_NO_CREATE) != null
        alarmToggle.isChecked = alarmUp


        val notifyPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmToggle.setOnCheckedChangeListener { buttonView, isChecked ->
            val toastMessage: String
            if (isChecked) {

                //deliverNotification(MainActivity.this);
                val repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES
                val triggerTime = SystemClock.elapsedRealtime() + repeatInterval

                alarmManager?.setInexactRepeating(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        triggerTime, repeatInterval, notifyPendingIntent)
                toastMessage = getString(R.string.Toast_Alarm_On)

            } else {
                mNotificationManager!!.cancelAll()

                alarmManager?.cancel(notifyPendingIntent)

                toastMessage = getString(R.string.Toast_Alarm_Off)
            }

            Toast.makeText(this@MainActivity, toastMessage, Toast.LENGTH_LONG).show()
        }

        createNotificationChannel()

    }

    //Canal de notificações é necessário para Android 8 e superiores
    fun createNotificationChannel() {
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Verificar a versão do Android, pois canal de notificação é válido apenas para versões 8 ou superiores
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID, "Stand Up notification", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.descricao_notificacao)
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
    }

    companion object {

        private val NOTIFICATION_ID = 0
        private val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }


}
