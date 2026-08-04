package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val userHandle: String,
    val userAvatarDrawable: String, // name of drawable resource or URI
    val location: String = "",
    val isVerified: Boolean = true,
    val mediaDrawable: String, // name of drawable resource
    val caption: String,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val timestamp: String = "2h ago"
)

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val avatarDrawable: String,
    val storyImageDrawable: String,
    val hasUnseen: Boolean = true,
    val isUserStory: Boolean = false,
    val timestamp: String = "4h ago"
)

@Entity(tableName = "reels")
data class ReelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val userAvatarDrawable: String,
    val videoTitle: String,
    val caption: String,
    val musicTrack: String,
    val coverImageDrawable: String,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val isFollowing: Boolean = false
)

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contactName: String,
    val contactHandle: String,
    val contactAvatarDrawable: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = true,
    val category: String = "Direct" // "Direct", "Group", "Requests"
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chatId: Long,
    val senderName: String,
    val messageText: String,
    val timestamp: String,
    val isFromMe: Boolean = false
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val userAvatarDrawable: String,
    val actionText: String,
    val timeAgo: String,
    val timeCategory: String = "Today", // "Today", "Yesterday", "This Week"
    val postThumbnailDrawable: String? = null,
    val notificationType: String = "like", // "like", "comment", "follow"
    val isFollowingBack: Boolean = false
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val postId: Long,
    val username: String,
    val userAvatarDrawable: String,
    val commentText: String,
    val timeAgo: String = "10m ago",
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Long = 1,
    val name: String = "Zyra Creator",
    val username: String = "zyra_official",
    val bio: String = "Digital Creator ✨ | Live, love, laugh... \n📍 Mumbai, India \n🔗 youtube.com/@zyra_official",
    val location: String = "Mumbai, India",
    val website: String = "youtube.com/@zyra_official",
    val postsCount: Int = 42,
    val followersCount: Int = 12840,
    val followingCount: Int = 380,
    val isVerified: Boolean = true,
    val avatarDrawable: String = "img_user_avatar_1785803090179"
)
