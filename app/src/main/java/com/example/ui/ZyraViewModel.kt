package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen { Splash, Auth, Main }
enum class ZyraTab { Home, Search, Upload, Reels, Chat, Notifications, Profile }

class ZyraViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ZyraRepository

    init {
        val db = ZyraDatabase.getDatabase(application)
        repository = ZyraRepository(db.zyraDao())
    }

    // App Navigation State
    private val _appScreen = MutableStateFlow(AppScreen.Splash)
    val appScreen: StateFlow<AppScreen> = _appScreen.asStateFlow()

    private val _selectedTab = MutableStateFlow(ZyraTab.Home)
    val selectedTab: StateFlow<ZyraTab> = _selectedTab.asStateFlow()

    // Active modals/sub-screens
    private val _activeChatId = MutableStateFlow<Long?>(null)
    val activeChatId: StateFlow<Long?> = _activeChatId.asStateFlow()

    private val _activeStoryId = MutableStateFlow<Long?>(null)
    val activeStoryId: StateFlow<Long?> = _activeStoryId.asStateFlow()

    private val _activePostDetailId = MutableStateFlow<Long?>(null)
    val activePostDetailId: StateFlow<Long?> = _activePostDetailId.asStateFlow()

    // Auth State
    private val _isSignUpMode = MutableStateFlow(false)
    val isSignUpMode: StateFlow<Boolean> = _isSignUpMode.asStateFlow()

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible.asStateFlow()

    private val _authEmail = MutableStateFlow("")
    val authEmail: StateFlow<String> = _authEmail.asStateFlow()

    private val _authPassword = MutableStateFlow("")
    val authPassword: StateFlow<String> = _authPassword.asStateFlow()

    private val _authUsername = MutableStateFlow("")
    val authUsername: StateFlow<String> = _authUsername.asStateFlow()

    // Search State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSearchTab = MutableStateFlow("Top")
    val selectedSearchTab: StateFlow<String> = _selectedSearchTab.asStateFlow()

    // Upload Post State
    private val _uploadCaption = MutableStateFlow("")
    val uploadCaption: StateFlow<String> = _uploadCaption.asStateFlow()

    private val _uploadLocation = MutableStateFlow("")
    val uploadLocation: StateFlow<String> = _uploadLocation.asStateFlow()

    private val _uploadMusic = MutableStateFlow("")
    val uploadMusic: StateFlow<String> = _uploadMusic.asStateFlow()

    private val _selectedGalleryMedia = MutableStateFlow("img_post_cityscape_1785803032776")
    val selectedGalleryMedia: StateFlow<String> = _selectedGalleryMedia.asStateFlow()

    // Reactive DB streams
    val posts: StateFlow<List<PostEntity>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stories: StateFlow<List<StoryEntity>> = repository.allStories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reels: StateFlow<List<ReelEntity>> = repository.allReels
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chats: StateFlow<List<ChatEntity>> = repository.allChats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun getMessagesForChat(chatId: Long): Flow<List<MessageEntity>> =
        repository.getMessagesForChat(chatId)

    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>> =
        repository.getCommentsForPost(postId)

    // Action Handlers
    fun finishSplash() {
        _appScreen.value = AppScreen.Auth
    }

    fun proceedToMain() {
        _appScreen.value = AppScreen.Main
    }

    fun logout() {
        _appScreen.value = AppScreen.Auth
    }

    fun toggleAuthMode() {
        _isSignUpMode.value = !_isSignUpMode.value
    }

    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    fun updateAuthEmail(email: String) { _authEmail.value = email }
    fun updateAuthPassword(password: String) { _authPassword.value = password }
    fun updateAuthUsername(username: String) { _authUsername.value = username }

    fun selectTab(tab: ZyraTab) {
        if (tab == ZyraTab.Upload) {
            _selectedTab.value = tab
        } else {
            _selectedTab.value = tab
        }
    }

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setSearchTab(tab: String) { _selectedSearchTab.value = tab }

    fun setUploadCaption(caption: String) { _uploadCaption.value = caption }
    fun setUploadLocation(location: String) { _uploadLocation.value = location }
    fun setUploadMusic(music: String) { _uploadMusic.value = music }
    fun setSelectedGalleryMedia(media: String) { _selectedGalleryMedia.value = media }

    fun openChat(chatId: Long) {
        _activeChatId.value = chatId
    }

    fun closeChat() {
        _activeChatId.value = null
    }

    fun openStory(storyId: Long) {
        _activeStoryId.value = storyId
    }

    fun closeStory() {
        _activeStoryId.value = null
    }

    fun openPostDetail(postId: Long) {
        _activePostDetailId.value = postId
    }

    fun closePostDetail() {
        _activePostDetailId.value = null
    }

    fun toggleLikePost(post: PostEntity) {
        viewModelScope.launch {
            val updated = post.copy(
                isLiked = !post.isLiked,
                likesCount = if (post.isLiked) post.likesCount - 1 else post.likesCount + 1
            )
            repository.updatePost(updated)
        }
    }

    fun toggleBookmarkPost(post: PostEntity) {
        viewModelScope.launch {
            val updated = post.copy(isBookmarked = !post.isBookmarked)
            repository.updatePost(updated)
        }
    }

    fun toggleLikeReel(reel: ReelEntity) {
        viewModelScope.launch {
            val updated = reel.copy(
                isLiked = !reel.isLiked,
                likesCount = if (reel.isLiked) reel.likesCount - 1 else reel.likesCount + 1
            )
            repository.updateReel(updated)
        }
    }

    fun toggleFollowReel(reel: ReelEntity) {
        viewModelScope.launch {
            val updated = reel.copy(isFollowing = !reel.isFollowing)
            repository.updateReel(updated)
        }
    }

    fun sendChatMessage(chatId: Long, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val newMessage = MessageEntity(
                chatId = chatId,
                senderName = "Aria Sharma",
                messageText = text.trim(),
                timestamp = "Just now",
                isFromMe = true
            )
            repository.sendMessage(newMessage)

            // Update chat last message
            chats.value.find { it.id == chatId }?.let { chat ->
                repository.updateChat(
                    chat.copy(
                        lastMessage = text.trim(),
                        lastMessageTime = "Just now",
                        unreadCount = 0
                    )
                )
            }
        }
    }

    fun addComment(postId: Long, commentText: String) {
        if (commentText.isBlank()) return
        viewModelScope.launch {
            val comment = CommentEntity(
                postId = postId,
                username = "aria.zyra",
                userAvatarDrawable = "img_user_avatar_1785803090179",
                commentText = commentText.trim(),
                timeAgo = "Just now"
            )
            repository.insertComment(comment)

            // Increment post comments count
            posts.value.find { it.id == postId }?.let { post ->
                repository.updatePost(post.copy(commentsCount = post.commentsCount + 1))
            }
        }
    }

    fun publishNewPost() {
        viewModelScope.launch {
            val currentProf = userProfile.value
            val newPost = PostEntity(
                username = currentProf?.name ?: "Aria Sharma",
                userHandle = currentProf?.username ?: "aria.zyra",
                userAvatarDrawable = currentProf?.avatarDrawable ?: "img_user_avatar_1785803090179",
                location = _uploadLocation.value.ifBlank { "Mumbai, India" },
                isVerified = true,
                mediaDrawable = _selectedGalleryMedia.value,
                caption = _uploadCaption.value.ifBlank { "New moment shared on ZYRA ✨ #ZyraMoments" },
                likesCount = 1,
                commentsCount = 0,
                sharesCount = 0,
                isLiked = true,
                isBookmarked = false,
                timestamp = "Just now"
            )
            repository.insertPost(newPost)

            // Increment user profile post count
            currentProf?.let {
                repository.updateUserProfile(it.copy(postsCount = it.postsCount + 1))
            }

            // Reset upload form & return to Home feed
            _uploadCaption.value = ""
            _uploadLocation.value = ""
            _uploadMusic.value = ""
            _selectedTab.value = ZyraTab.Home
        }
    }

    fun toggleFollowBackNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            repository.updateNotification(notification.copy(isFollowingBack = !notification.isFollowingBack))
        }
    }
}
