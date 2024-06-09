package com.overshoot.moneygoal

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import com.overshoot.data.datasource.remote.network.InternetConnectivity
import com.overshoot.moneygoal.navigation.NavigationHost
import com.overshoot.moneygoal.theme.MoneyGoalTheme
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeGoalDetailViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeTransactionViewModel
import com.overshoot.moneygoal.component.notification.NotificationViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val internetConnectivity by inject<InternetConnectivity>()

    private val notificationViewModel by viewModel<NotificationViewModel>()

    private val homeGoalDetailViewModel by viewModel<HomeGoalDetailViewModel>()
    private val homeTransactionViewModel by viewModel<HomeTransactionViewModel>()

//    private val appStateHolder = AppStateHolder.getInstance()
    
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

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class)
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
        setContent {
            MoneyGoalTheme {
                //A surface container using the 'background' color from the theme
                NavigationHost(
                    homeGoalDetailViewModel = homeGoalDetailViewModel,
                    homeTransactionViewModel = homeTransactionViewModel
                )

                val scope = rememberCoroutineScope()
                val sheetState = rememberModalBottomSheetState()
                val appStateHolder = rememberAppState(
                    internetState = internetConnectivity.state
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

                if (appStateHolder.isShowNoInternetDrawer.value == true) {
                    ModalBottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = {
                            scope.launch {
                                sheetState.hide()
                                appStateHolder.setIsShowNoInternetDrawer(false)
                            }
                        }
                    ) {
                        Text(text = "No Internet")
                        Spacer(modifier = Modifier.navigationBarsPadding())
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
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