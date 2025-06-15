package com.example.concesionariosbaca.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.concesionariosbaca.R

class CarNotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val carBrand = inputData.getString("brand") ?: return Result.failure()
        val carModel = inputData.getString("model") ?: ""
        val carPlate = inputData.getString("plate") ?: ""

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Canal de notificación (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "car_channel",
                "Car Purchases",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "car_channel")
            .setContentTitle("¡Enhorabuena!")
            .setContentText("Has adquirido con exito tu $carBrand $carModel con matrícula $carPlate")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)

        return Result.success()
    }
}
