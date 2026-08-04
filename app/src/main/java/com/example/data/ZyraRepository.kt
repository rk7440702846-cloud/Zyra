package com.example.data

import kotlinx.coroutines.flow.Flow

class ZyraRepository(private val dao: ZyraDao) {
    val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()
    val allStories: Flow<List<StoryEntity>> = dao.getAllStories()
    val allReels: Flow<List<ReelEntity>> = dao.getAllReels()
    val allChats: Flow<List<ChatEntity>> = dao.getAllChats()
    val allNotifications: Flow<List<NotificationEntity>> = dao.getAllNotifications()
    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()

    fun getMessagesForChat(chatId: Long): Flow<List<MessageEntity>> = dao.getMessagesForChat(chatId)
    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>> = dao.getCommentsForPost(postId)

    suspend fun insertPost(post: PostEntity): Long = dao.insertPost(post)
    suspend fun updatePost(post: PostEntity) = dao.updatePost(post)

    suspend fun updateReel(reel: ReelEntity) = dao.updateReel(reel)

    suspend fun sendMessage(message: MessageEntity) = dao.insertMessage(message)
    suspend fun updateChat(chat: ChatEntity) = dao.updateChat(chat)

    suspend fun insertComment(comment: CommentEntity) = dao.insertComment(comment)

    suspend fun updateNotification(notification: NotificationEntity) = dao.updateNotification(notification)
    suspend fun updateUserProfile(profile: UserProfileEntity) = dao.insertUserProfile(profile)
}
