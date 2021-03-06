package br.com.govote.android.infrastructure.push

import br.com.govote.android.libs.logger.LogUtility
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseDefaultMessagingService : FirebaseMessagingService() {
  override fun onMessageReceived(message: RemoteMessage?) {
    with(LogUtility) {
      d(message?.messageId!!)
      d(message.from!!)
      d(message.to!!)
      d(message.collapseKey!!)
      d(message.messageType!!)
      d(message.sentTime.toString())
    }
  }

  override fun onNewToken(token: String?) {
    super.onNewToken(token)

    if (token == null || token.isEmpty()) {
      LogUtility.d("onNewToken() with empty or null token")
      return
    }

    sendRegistrationToServer(token)
  }

  private fun sendRegistrationToServer(refreshedToken: String) {
    LogUtility.d("( FIREBASE -> onTokenRefresh ) ", refreshedToken)
  }
}
