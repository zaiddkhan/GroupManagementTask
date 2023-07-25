package com.webview.groupmanagementtask

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.webview.groupmanagementtask.MainDestinations.GROUP_CHAT_ROUTE
import com.webview.groupmanagementtask.MainDestinations.GROUP_DETAIL_SCREEN
import com.webview.groupmanagementtask.feature.presentation.components.ImagePicker
import com.webview.groupmanagementtask.feature.presentation.components.rememberBitmapFromBytes
import com.webview.groupmanagementtask.feature.presentation.screens.chat_screen.ChatScreen
import com.webview.groupmanagementtask.feature.presentation.screens.chat_screen.ChatScreenViewModel
import com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen.GroupDetailScreen
import com.webview.groupmanagementtask.feature.presentation.screens.group_detail_screen.GroupDetailViewModel
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.GroupListScreen
import com.webview.groupmanagementtask.feature.presentation.screens.group_list_screen.GroupListViewModel
import com.webview.groupmanagementtask.feature.presentation.screens.register_user_screen.RegisterUserScreen
import com.webview.groupmanagementtask.feature.presentation.screens.register_user_screen.RegisterUserViewModel
import com.webview.groupmanagementtask.ui.theme.GroupManagementTaskTheme

@Composable
fun App(
    imagePicker: ImagePicker,
    finishActivity: () -> Unit,
    context : Context
) {
    GroupManagementTaskTheme {

        var hasNotificationPermission by remember {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                mutableStateOf(
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                )
            } else mutableStateOf(false)
        }

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                hasNotificationPermission = true
            }
        )

        SideEffect {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

            NavGraph(
                imagePicker = imagePicker,
                finishActivity = finishActivity
            )

    }
}



@Composable
fun NavGraph(
    imagePicker : ImagePicker,
    finishActivity : () -> Unit,

){
    val navController = rememberNavController()
    val viewModel : GroupListViewModel = hiltViewModel()

    val username by viewModel.registeredUserName.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = if(username.isNotEmpty()) MainDestinations.CHATROOM_ROUTE else MainDestinations.REGISTER_ROUTE){


        composable(
            route = MainDestinations.REGISTER_ROUTE
        ){
            BackHandler {
                finishActivity()
            }
            val registerUserViewModel : RegisterUserViewModel = hiltViewModel()
            val registerUserState by registerUserViewModel.registerUserUiState.collectAsStateWithLifecycle()
            val bitmap = rememberBitmapFromBytes(bytes = registerUserState.selectedPhotoUri)
            RegisterUserScreen(
                bitmap = bitmap,
                imagePicker = imagePicker,
                onPhotoPicked = registerUserViewModel::onImageSelected,
                userName = registerUserState.userName,
                email = registerUserState.email,
                onEmailChanged = registerUserViewModel::emailChanged,
                onUserNameChange = registerUserViewModel::onNameChanged,
                onRegisterClicked =  {
                    registerUserViewModel.onRegister()
                    navController.navigate(MainDestinations.CHATROOM_ROUTE)
                },
                isDetailValid = !registerUserState.isEmailInvalid,
                isLoading = registerUserState.isLoading,
                phoneNumber = registerUserState.phoneNumber,
                onPhoneNumberChanged = registerUserViewModel::onPhoneNumberChanged
            )
        }
        composable(
            route = MainDestinations.CHATROOM_ROUTE
        ){
                viewModel.loadGroups()
                val groupsListUiState by viewModel.groupListUiState.collectAsStateWithLifecycle()

                GroupListScreen(
                    groupListScreenUiState = groupsListUiState,
                    groups = viewModel.groups,
                    onEvent = viewModel::onEvent,
                    imagePicker = ImagePicker(LocalContext.current as ComponentActivity),
                    snackMessage = viewModel.snackMessage,
                    navigateToChat = { groupName ->
                        navController.navigate(
                            "group_chat/$groupName/$username"
                        )
                    }

                )

        }
        composable(
            route = GROUP_CHAT_ROUTE,
            arguments = listOf(navArgument("groupName"){
                type = NavType.StringType
            },
                navArgument("userName"){
                    type = NavType.StringType
                }
            )
        ){

            val groupName = it.arguments?.getString("groupName")
            val userName = it.arguments?.getString("userName")
            val chatViewModel : ChatScreenViewModel = hiltViewModel()
            val uiState by chatViewModel.chatScreenUiState.collectAsStateWithLifecycle()

            ChatScreen(
                username = userName ?: "",
                onEvent = chatViewModel::onEvent,
                groupName = groupName ?: "",
                uiState = uiState,
                loadDetails = chatViewModel::loadDetails,
                navigateToDetailScreen = {
                    navController.navigate("group_detail/$groupName")
                }
            )

        }
        composable(
            GROUP_DETAIL_SCREEN,
            arguments = listOf(navArgument("groupName"){
                type = NavType.StringType
            })
        ){
            val groupName = it.arguments?.getString("groupName")
            val detailsViewModel : GroupDetailViewModel = hiltViewModel()
            val uiState by detailsViewModel.groupDetailUiState.collectAsStateWithLifecycle()


            GroupDetailScreen(
                groupName = groupName ?: "",
                onEvent = detailsViewModel::onEvent,
                uiState = uiState,
                members = detailsViewModel.membersList,
                loadDetails = detailsViewModel::loadMembers
            )
        }
    }


}

object MainDestinations {
    const val GROUP_DETAIL_SCREEN = "group_detail/{groupName}"
    const val REGISTER_ROUTE = "register"
    const val CHATROOM_ROUTE = "chatroom"
    const val GROUP_CHAT_ROUTE = "group_chat/{groupName}/{userName}"
}