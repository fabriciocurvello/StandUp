package com.example.standup

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    private var mNotificationManager: NotificationManager? = null

    override fun onReceive(context: Context, intent: Intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        deliverNotification(context)

    }

    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, MainActivity::class.java)

        val contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(context.getString(R.string.alerta_titulo))
                .setContentText(context.getString(R.string.alerta_texto))
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        mNotificationManager!!.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {

        private val NOTIFICATION_ID = 0
        private val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }
}
