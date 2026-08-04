package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.*
import com.example.ui.components.ZyraBottomNavigation
import com.example.ui.screens.*
import com.example.ui.theme.ZyraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ZyraTheme {
                ZyraMainApp()
            }
        }
    }
}

@Composable
fun ZyraMainApp(viewModel: ZyraViewModel = viewModel()) {
    val appScreen by viewModel.appScreen.collectAsStateWithLifecycle()
    val isSignUpMode by viewModel.isSignUpMode.collectAsStateWithLifecycle()

    when (appScreen) {
        AppScreen.Splash -> {
            SplashScreen(onSplashFinished = { viewModel.finishSplash() })
        }
        AppScreen.Auth -> {
            AuthScreen(
                isSignUpMode = isSignUpMode,
                onToggleMode = { viewModel.toggleAuthMode() },
                onLoginSuccess = { viewModel.proceedToMain() }
            )
        }
        AppScreen.Main -> {
            MainContentContainer(viewModel = viewModel)
        }
    }
}

@Composable
fun MainContentContainer(viewModel: ZyraViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val stories by viewModel.stories.collectAsStateWithLifecycle()
    val reels by viewModel.reels.collectAsStateWithLifecycle()
    val chats by viewModel.chats.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    val activeChatId by viewModel.activeChatId.collectAsStateWithLifecycle()
    val activeStoryId by viewModel.activeStoryId.collectAsStateWithLifecycle()
    val activePostDetailId by viewModel.activePostDetailId.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedSearchTab by viewModel.selectedSearchTab.collectAsStateWithLifecycle()

    val uploadCaption by viewModel.uploadCaption.collectAsStateWithLifecycle()
    val uploadLocation by viewModel.uploadLocation.collectAsStateWithLifecycle()
    val uploadMusic by viewModel.uploadMusic.collectAsStateWithLifecycle()
    val selectedGalleryMedia by viewModel.selectedGalleryMedia.collectAsStateWithLifecycle()

    // Active Chat Sub-Screen
    if (activeChatId != null) {
        val activeChat = chats.find { it.id == activeChatId }
        val activeMessages by viewModel.getMessagesForChat(activeChatId!!).collectAsStateWithLifecycle(initialValue = emptyList())

        if (activeChat != null) {
            ConversationDetailScreen(
                chat = activeChat,
                messages = activeMessages,
                onSendMessage = { text -> viewModel.sendChatMessage(activeChat.id, text) },
                onBack = { viewModel.closeChat() }
            )
            return
        }
    }

    // Active Story Viewer Sub-Screen
    if (activeStoryId != null) {
        val activeStory = stories.find { it.id == activeStoryId }
        if (activeStory != null) {
            StoryViewerModal(
                story = activeStory,
                onClose = { viewModel.closeStory() }
            )
            return
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            ZyraBottomNavigation(
                selectedTab = selectedTab,
                unreadChatsCount = chats.sumOf { it.unreadCount },
                unreadNotifCount = notifications.size,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                ZyraTab.Home -> {
                    HomeScreen(
                        posts = posts,
                        stories = stories,
                        onLikePost = { viewModel.toggleLikePost(it) },
                        onBookmarkPost = { viewModel.toggleBookmarkPost(it) },
                        onOpenComments = { viewModel.openPostDetail(it) },
                        onOpenStory = { viewModel.openStory(it) },
                        onNavigateTab = { viewModel.selectTab(it) }
                    )
                }
                ZyraTab.Search -> {
                    SearchScreen(
                        searchQuery = searchQuery,
                        selectedSearchTab = selectedSearchTab,
                        onQueryChange = { viewModel.setSearchQuery(it) },
                        onTabChange = { viewModel.setSearchTab(it) }
                    )
                }
                ZyraTab.Upload -> {
                    UploadScreen(
                        caption = uploadCaption,
                        location = uploadLocation,
                        music = uploadMusic,
                        selectedMedia = selectedGalleryMedia,
                        onCaptionChange = { viewModel.setUploadCaption(it) },
                        onLocationChange = { viewModel.setUploadLocation(it) },
                        onMusicChange = { viewModel.setUploadMusic(it) },
                        onMediaSelect = { viewModel.setSelectedGalleryMedia(it) },
                        onPublish = { viewModel.publishNewPost() },
                        onCancel = { viewModel.selectTab(ZyraTab.Home) }
                    )
                }
                ZyraTab.Reels -> {
                    ReelsScreen(
                        reels = reels,
                        onLikeReel = { viewModel.toggleLikeReel(it) },
                        onFollowReel = { viewModel.toggleFollowReel(it) },
                        onOpenComments = { viewModel.openPostDetail(it) }
                    )
                }
                ZyraTab.Chat -> {
                    ChatScreen(
                        chats = chats,
                        onOpenChat = { viewModel.openChat(it) }
                    )
                }
                ZyraTab.Notifications -> {
                    NotificationsScreen(
                        notifications = notifications,
                        onToggleFollowBack = { viewModel.toggleFollowBackNotification(it) }
                    )
                }
                ZyraTab.Profile -> {
                    ProfileScreen(
                        profile = userProfile,
                        posts = posts,
                        onOpenPostDetail = { viewModel.openPostDetail(it) },
                        onNavigateToChat = { viewModel.selectTab(ZyraTab.Chat) },
                        onLogout = { viewModel.logout() }
                    )
                }
            }

            // Post Comments Modal Overlay
            if (activePostDetailId != null) {
                val postComments by viewModel.getCommentsForPost(activePostDetailId!!).collectAsStateWithLifecycle(initialValue = emptyList())
                PostCommentsModal(
                    comments = postComments,
                    onAddComment = { commentText -> viewModel.addComment(activePostDetailId!!, commentText) },
                    onClose = { viewModel.closePostDetail() }
                )
            }
        }
    }
}

