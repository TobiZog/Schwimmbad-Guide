package de.zoghaib.schwimmbadguide.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import de.zoghaib.schwimmbadguide.MainActivity
import de.zoghaib.schwimmbadguide.R
import de.zoghaib.schwimmbadguide.data.OpenEnum
import de.zoghaib.schwimmbadguide.database.DatabaseHandler
import de.zoghaib.schwimmbadguide.objects.SwimmingPool
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

/**
 * Service to send notifications on events
 *
 * @author  Tobias Zoghaib
 * @since   2021-06-27
 */
class NotificationService : Service() {

	/* -------------------- Member Variables -------------------- */

	/** Holding the favorite pools in it */
	private val favoritePools = ArrayList<SwimmingPool>()

	/** False to kill the routine */
	var run = true

	/** Shows notifications on first run, after that, only one time, 60 min before the event */
	var firstRun = true

	/** Shared preference object */
	private lateinit var sharedPreferences: SharedPreferences


	/* -------------------- Lifecycle -------------------- */

	/**
	 * todo
	 */
	override fun onBind(intent: Intent?): IBinder? {
		return null
	}


	/**
	 * todo
	 */
	override fun onCreate() {
		super.onCreate()

		// Shared preferences
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)


		// Filling the favorites array list
		val dbHandler = DatabaseHandler(this)
		val favorites = dbHandler.readTableToArrayList("FAVORITES")

		if(favorites != null) {
			for(fav in favorites) {
				favoritePools.add(SwimmingPool(this, fav.getAsInteger("POOL")))
			}
		}


		thread {
			// Running until the service will killed
			// Checks every minute, if a pool opens or close and create a notification
			while(run) {

				for(i in favoritePools) {

					if (i.getOpenState() == OpenEnum.OPENSOON && sharedPreferences.getStringSet("notificationActions", setOf(""))!!.contains("OPENING")) {

						if (firstRun) {
							createNotification(
								title = "Ein Bad öffnet bald!",
								text = "${i.poolInformations.name} öffnet um " +
										"${i.getOpenTimesToday(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).substringBefore('-')} Uhr",
								id = i.poolInformations.dbId,
								channelName = "Open-Event"
							)
						} else {
							val currentTime = Time(System.currentTimeMillis()).hours * 60 + Time(System.currentTimeMillis()).minutes
							val open1 = i.getOpenTimesToday(1).substringBefore(":").toInt() * 60 +
									i.getOpenTimesToday(1).substringAfter(":").substringBefore("-").toInt()
							if (currentTime == (open1 - 60)) {
								createNotification(
									title = "Ein Bad öffnet bald!",
									text = "${i.poolInformations.name} öffnet um " +
											"${i.getOpenTimesToday(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).substringBefore('-')} Uhr",
									id = i.poolInformations.dbId,
									channelName = "Open-Event"
								)
							}
						}

					} else if (i.getOpenState() == OpenEnum.WILLBECLOSING && sharedPreferences.getStringSet("notificationActions", setOf(""))!!.contains("CLOSING")) {
						if (firstRun) {
							createNotification(
								title = "Ein Bad schließt bald!",
								text = "${i.poolInformations.name} schließt um " +
										"${i.getOpenTimesToday(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).substringAfter('-')} Uhr",
								id = i.poolInformations.dbId,
								channelName = "Close-Event"
							)

						} else {
							val currentTime = Time(System.currentTimeMillis()).hours * 60 + Time(System.currentTimeMillis()).minutes
							val close1 = i.getOpenTimesToday(1).substringAfter("-").substringBefore(":").toInt() * 60 +
									i.getOpenTimesToday(1).substringAfterLast(":").toInt()

							if (currentTime == (close1 - 60)) {
								createNotification(
									title = "Ein Bad schließt bald!",
									text = "${i.poolInformations.name} schließt um " +
											"${i.getOpenTimesToday(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).substringAfter('-')} Uhr",
									id = i.poolInformations.dbId,
									channelName = "Close-Event"
								)
							}
						}
					}
				}


				firstRun = false

				Log.d("AAA", "Alive!")

				Thread.sleep(5000)
			}
		}
	}


	/**
	 * todo
	 */
	override fun onDestroy() {
		super.onDestroy()
		run = false
	}


	/**
	 * todo
	 */
	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		return START_STICKY
	}


	override fun onTaskRemoved(rootIntent: Intent?) {
		val restartServiceIntent = Intent(applicationContext, this.javaClass)
		restartServiceIntent.setPackage(packageName)
		startService(restartServiceIntent)
		super.onTaskRemoved(rootIntent)
	}


	/**
	 * todo
	 */
	private fun createNotification(title : String, text : String, id : Int, channelName : String) {
		val pendingIntent = PendingIntent.getActivity(this, 0,
			Intent(this, MainActivity::class.java),
			PendingIntent.FLAG_UPDATE_CURRENT)

		val builder = NotificationCompat.Builder(this, id.toString())
			.setContentTitle(title)
			.setContentText(text)
			.setSmallIcon(R.drawable.ic_pool)
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setContentIntent(pendingIntent)
			.setAutoCancel(true)

		val manager = NotificationManagerCompat.from(this)
		val channel = NotificationChannel(id.toString(), channelName, NotificationManager.IMPORTANCE_DEFAULT)


		manager.createNotificationChannel(channel)
		//startForeground(0x1234, builder.build())
		manager.notify(id, builder.build())
	}

}