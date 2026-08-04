package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        PostEntity::class,
        StoryEntity::class,
        ReelEntity::class,
        ChatEntity::class,
        MessageEntity::class,
        NotificationEntity::class,
        CommentEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ZyraDatabase : RoomDatabase() {
    abstract fun zyraDao(): ZyraDao

    companion object {
        @Volatile
        private var INSTANCE: ZyraDatabase? = null

        fun getDatabase(context: Context): ZyraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ZyraDatabase::class.java,
                    "zyra_database"
                )
                    .addCallback(DatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.zyraDao())
                    }
                }
            }
        }

        private suspend fun populateInitialData(dao: ZyraDao) {
            // Seed Profile
            val avatarDrawable = "img_user_avatar_1785803090179"
            val cityscapeDrawable = "img_post_cityscape_1785803032776"
            val natureDrawable = "img_post_nature_1785803064357"
            val reelFashionDrawable = "img_reel_fashion_1785803079235"

            dao.insertUserProfile(
                UserProfileEntity(
                    id = 1,
                    name = "Aria Sharma",
                    username = "aria.zyra",
                    bio = "Digital Creator ✨ | Live, love, laugh...\n📍 Mumbai, India\n🔗 youtube.com/@aria_zyra",
                    location = "Mumbai, India",
                    website = "youtube.com/@aria_zyra",
                    postsCount = 42,
                    followersCount = 12840,
                    followingCount = 380,
                    isVerified = true,
                    avatarDrawable = avatarDrawable
                )
            )

            // Seed Stories
            dao.insertStories(
                listOf(
                    StoryEntity(id = 1, username = "Your Story", avatarDrawable = avatarDrawable, storyImageDrawable = cityscapeDrawable, hasUnseen = false, isUserStory = true),
                    StoryEntity(id = 2, username = "rohan_vibe", avatarDrawable = natureDrawable, storyImageDrawable = natureDrawable, hasUnseen = true),
                    StoryEntity(id = 3, username = "priya_art", avatarDrawable = reelFashionDrawable, storyImageDrawable = reelFashionDrawable, hasUnseen = true),
                    StoryEntity(id = 4, username = "tech_insider", avatarDrawable = cityscapeDrawable, storyImageDrawable = cityscapeDrawable, hasUnseen = true),
                    StoryEntity(id = 5, username = "wanderlust.in", avatarDrawable = natureDrawable, storyImageDrawable = natureDrawable, hasUnseen = false)
                )
            )

            // Seed Posts
            dao.insertPosts(
                listOf(
                    PostEntity(
                        id = 1,
                        username = "Aria Sharma",
                        userHandle = "aria.zyra",
                        userAvatarDrawable = avatarDrawable,
                        location = "Cyber Hub, Mumbai",
                        isVerified = true,
                        mediaDrawable = cityscapeDrawable,
                        caption = "Midnight vibes in the cyber city ✨ Loving the futuristic glow! #CityLights #MumbaiDiaries #ZyraGlow #CyberAesthetic",
                        likesCount = 1420,
                        commentsCount = 89,
                        sharesCount = 24,
                        isLiked = true,
                        isBookmarked = false,
                        timestamp = "2 hours ago"
                    ),
                    PostEntity(
                        id = 2,
                        username = "Rohan Verma",
                        userHandle = "rohan_vibe",
                        userAvatarDrawable = natureDrawable,
                        location = "Goa Beaches",
                        isVerified = false,
                        mediaDrawable = natureDrawable,
                        caption = "Chasing sunsets and peaceful horizons 🌅 Golden hour hits different when you are surrounded by palm trees. #Wanderlust #GoldenHour #NatureVibes",
                        likesCount = 2890,
                        commentsCount = 154,
                        sharesCount = 67,
                        isLiked = false,
                        isBookmarked = true,
                        timestamp = "5 hours ago"
                    ),
                    PostEntity(
                        id = 3,
                        username = "Priya Kapoor",
                        userHandle = "priya_art",
                        userAvatarDrawable = reelFashionDrawable,
                        location = "Studio Neon, Delhi",
                        isVerified = true,
                        mediaDrawable = reelFashionDrawable,
                        caption = "Streetwear mood for the week 🔥 Which color pair fits better - Neon Magenta or Royal Purple? Let me know below! 👇 #FashionTrends #Streetwear #ZyraStyle",
                        likesCount = 3520,
                        commentsCount = 210,
                        sharesCount = 98,
                        isLiked = true,
                        isBookmarked = true,
                        timestamp = "1 day ago"
                    )
                )
            )

            // Seed Comments for Post 1
            dao.insertComments(
                listOf(
                    CommentEntity(id = 1, postId = 1, username = "rohan_vibe", userAvatarDrawable = natureDrawable, commentText = "Incredible framing! That neon lighting looks super sick ✨", timeAgo = "1h ago", likesCount = 14),
                    CommentEntity(id = 2, postId = 1, username = "priya_art", userAvatarDrawable = reelFashionDrawable, commentText = "Loved the aesthetic vibes 🔥 Keep creating!", timeAgo = "45m ago", likesCount = 8),
                    CommentEntity(id = 3, postId = 1, username = "kabir_clickz", userAvatarDrawable = cityscapeDrawable, commentText = "Which camera lens did you use for this night shot?", timeAgo = "20m ago", likesCount = 3)
                )
            )

            // Seed Reels
            dao.insertReels(
                listOf(
                    ReelEntity(
                        id = 1,
                        username = "priya_art",
                        userAvatarDrawable = reelFashionDrawable,
                        videoTitle = "Neon Streetwear Lookbook 2026",
                        caption = "Cyberpunk style inspo! Save this for your next outfit shoot 💫 #ReelsIndia #Fashion #NeonVibes",
                        musicTrack = "Zyra Beats - Midnight Synthwave (Original)",
                        coverImageDrawable = reelFashionDrawable,
                        likesCount = 12400,
                        commentsCount = 840,
                        sharesCount = 3200,
                        isLiked = true,
                        isBookmarked = true,
                        isFollowing = false
                    ),
                    ReelEntity(
                        id = 2,
                        username = "aria.zyra",
                        userAvatarDrawable = avatarDrawable,
                        videoTitle = "Mumbai Night Skyline In 8K",
                        caption = "Late night drive through Marine Drive & Bandra Worli Sea Link 🚗💫 #MumbaiRains #CityLife #ZyraMoments",
                        musicTrack = "LoFi Chill - City Lights At 2 AM",
                        coverImageDrawable = cityscapeDrawable,
                        likesCount = 8900,
                        commentsCount = 420,
                        sharesCount = 1100,
                        isLiked = false,
                        isBookmarked = false,
                        isFollowing = true
                    ),
                    ReelEntity(
                        id = 3,
                        username = "rohan_vibe",
                        userAvatarDrawable = natureDrawable,
                        videoTitle = "Peaceful Coastal Escape",
                        caption = "Listen to the ocean waves and disconnect from the noise 🌊🧘‍♂️ #Mindfulness #SunsetVibes",
                        musicTrack = "Ambient Sounds - Ocean Waves & Piano",
                        coverImageDrawable = natureDrawable,
                        likesCount = 15800,
                        commentsCount = 610,
                        sharesCount = 4500,
                        isLiked = true,
                        isBookmarked = false,
                        isFollowing = false
                    )
                )
            )

            // Seed Chats
            dao.insertChats(
                listOf(
                    ChatEntity(id = 1, contactName = "Rohan Verma", contactHandle = "rohan_vibe", contactAvatarDrawable = natureDrawable, lastMessage = "Hey! Loved your latest post on Zyra! 🔥", lastMessageTime = "10:42 AM", unreadCount = 2, isOnline = true, category = "Direct"),
                    ChatEntity(id = 2, contactName = "Priya Kapoor", contactHandle = "priya_art", contactAvatarDrawable = reelFashionDrawable, lastMessage = "Are we still meeting for the photo shoot today?", lastMessageTime = "Yesterday", unreadCount = 0, isOnline = true, category = "Direct"),
                    ChatEntity(id = 3, contactName = "Creators Club 🚀", contactHandle = "group_creators", contactAvatarDrawable = cityscapeDrawable, lastMessage = "Kabir: Check out the new trending audio tag!", lastMessageTime = "Sun", unreadCount = 5, isOnline = false, category = "Group"),
                    ChatEntity(id = 4, contactName = "Ananya Roy", contactHandle = "ananya_photos", contactAvatarDrawable = natureDrawable, lastMessage = "Would love to collaborate on a reel!", lastMessageTime = "3 days ago", unreadCount = 1, isOnline = false, category = "Requests")
                )
            )

            // Seed Messages for Chat 1
            dao.insertMessages(
                listOf(
                    MessageEntity(id = 1, chatId = 1, senderName = "Rohan Verma", messageText = "Hey Aria! Hope you are doing great!", timestamp = "10:30 AM", isFromMe = false),
                    MessageEntity(id = 2, chatId = 1, senderName = "Aria Sharma", messageText = "Hey Rohan! Thanks a lot! How are you?", timestamp = "10:35 AM", isFromMe = true),
                    MessageEntity(id = 3, chatId = 1, senderName = "Rohan Verma", messageText = "Hey! Loved your latest post on Zyra! 🔥 That cityscape framing is top notch!", timestamp = "10:42 AM", isFromMe = false)
                )
            )

            // Seed Notifications
            dao.insertNotifications(
                listOf(
                    NotificationEntity(id = 1, username = "rohan_vibe", userAvatarDrawable = natureDrawable, actionText = "liked your post.", timeAgo = "10m ago", timeCategory = "Today", postThumbnailDrawable = cityscapeDrawable, notificationType = "like"),
                    NotificationEntity(id = 2, username = "priya_art", userAvatarDrawable = reelFashionDrawable, actionText = "commented: \"Loved the aesthetic vibes 🔥 Keep creating!\"", timeAgo = "45m ago", timeCategory = "Today", postThumbnailDrawable = cityscapeDrawable, notificationType = "comment"),
                    NotificationEntity(id = 3, username = "kabir_clickz", userAvatarDrawable = cityscapeDrawable, actionText = "started following you.", timeAgo = "2h ago", timeCategory = "Today", notificationType = "follow", isFollowingBack = false),
                    NotificationEntity(id = 4, username = "wanderlust.in", userAvatarDrawable = natureDrawable, actionText = "liked your comment on their reel.", timeAgo = "Yesterday", timeCategory = "Yesterday", postThumbnailDrawable = natureDrawable, notificationType = "like"),
                    NotificationEntity(id = 5, username = "tech_insider", userAvatarDrawable = cityscapeDrawable, actionText = "started following you.", timeAgo = "Yesterday", timeCategory = "Yesterday", notificationType = "follow", isFollowingBack = true)
                )
            )
        }
    }
}
