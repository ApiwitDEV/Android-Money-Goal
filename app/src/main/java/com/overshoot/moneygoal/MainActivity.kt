package com.overshoot.moneygoal

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.messaging
import com.overshoot.moneygoal.navigation.NavigationHost
import com.overshoot.moneygoal.theme.MoneyGoalTheme
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.GoalViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.TransactionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val notificationViewModel by viewModel<NotificationViewModel>()

    private val goalViewModel by viewModel<GoalViewModel>()
    private val transactionViewModel by viewModel<TransactionViewModel>()
    
    private fun askNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                baseContext,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                createNotificationChannel()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.POST_NOTIFICATIONS) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                    //notificationViewModel.showDialog()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                registerForActivityResult(
                    ActivityResultContracts.RequestPermission(),
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        // FCM SDK (and your app) can post notifications.
                        createNotificationChannel()
                    } else {
                        // TODO: Inform user that that your app will not show notifications.
                        Toast.makeText(baseContext, "xxx", Toast.LENGTH_LONG).show()
                    }
                }.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        Firebase.messaging.subscribeToTopic("A")
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Firebase Log", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                val msg = "notification token : $token"
                Log.d("Firebase Log", msg)
            }
        )
        goalViewModel.error.observe(this) {
            Toast.makeText(baseContext, goalViewModel.error.value, Toast.LENGTH_SHORT).show()
        }
        setContent {
            MoneyGoalTheme {
                //A surface container using the 'background' color from the theme
                NavigationHost(
                    goalViewModel = goalViewModel,
                    transactionViewModel = transactionViewModel
                )
                if (notificationViewModel.showDialog.value == true) {
                    Dialog(onDismissRequest = {
                        notificationViewModel.hideDialog()
                    }) {
                        Card {
                            Column {
                                Text(text = "werf")
                                Text(text = "sfdv")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val NOTIFICATION_CHANNEL_ID = "1"
            // Create the NotificationChannel.
            val name = "channel_name"
            val descriptionText = " description "
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(mChannel)
        }
    }
}