package com.overshoot.moneygoal.component.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.overshoot.moneygoal.AppStateHolder
import com.overshoot.moneygoal.component.account.stateholder.AccountViewModel
import com.overshoot.moneygoal.navigation.authentication.AuthenticationNavigationRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountScreen(appStateHolder: AppStateHolder) {

    val accountViewModel = koinViewModel<AccountViewModel>()

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            Text(text = "Account")
            Button(
                onClick = {
                    accountViewModel.logout()
                    appStateHolder.clearBackStackAndNavigateTo(AuthenticationNavigationRoute.Login)
                }
            ) {
                Text(text = "Sign out")
            }
        }
    }
}