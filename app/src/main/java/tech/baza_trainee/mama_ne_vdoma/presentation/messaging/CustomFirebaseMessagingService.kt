package tech.baza_trainee.mama_ne_vdoma.presentation.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmapOrNull
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.inject
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.MessageType
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.MainActivity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import kotlin.random.Random

class CustomFirebaseMessagingService: FirebaseMessagingService() {

    private val groupsRepository by inject<GroupsRepository>()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val context: Context by lazy { this }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "New token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage.data)
    }

    private fun sendNotification(data: Map<String, String>) {
        scope.networkExecutor<GroupEntity> {
            execute {
                groupsRepository.getGroupById(data[GROUP_ID].orEmpty())
            }
            onSuccess {
                val intent = Intent(context, MainActivity::class.java).apply {
                    addFlags(FLAG_ACTIVITY_CLEAR_TOP)
                }

                val pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, FLAG_IMMUTABLE
                )

                val channelId = context.getString(R.string.default_notification_channel_id)

                val notificationBuilder = NotificationCompat.Builder(context, channelId)
                    .setContentTitle(it.name)
                    .setContentText(getMessage(data[TYPE].orEmpty()))
                    .setSmallIcon(R.drawable.ic_notification_logo)
                    .setColor(getColor(R.color.main_theme))
                    .setLargeIcon(AppCompatResources.getDrawable(context, R.drawable.logo_240_240)?.toBitmapOrNull())
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)

                val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(channelId, CHANNEL_NAME, IMPORTANCE_DEFAULT)
                    manager.createNotificationChannel(channel)
                }

                manager.notify(Random.nextInt(), notificationBuilder.build())
            }
        }
    }

    private fun getMessage(messageType: String) =
        when(messageType) {
            MessageType.JOIN.type -> "Вам надійшов запит на приєднання до групи"
            MessageType.ACCEPT.type -> "Адміністратор схвалив Ваш запит на приєднання до групи"
            MessageType.REJECT.type -> "Адміністратор відхилив Ваш запит на приєднання до групи"
            MessageType.KICK.type -> "Адміністратор виключив Вас з групи"
            MessageType.MAKE_ADMIN.type -> "Адміністратор передав Вам права керування групою"
            else -> throw IllegalArgumentException("Unknown message type: $messageType")
        }

    companion object {
        private const val CHANNEL_NAME = "default_notification_channel"
        private const val GROUP_ID = "groupId"
        private const val TYPE = "type"
    }
}