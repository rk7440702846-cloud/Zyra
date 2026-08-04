package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NotificationEntity
import com.example.ui.components.rememberDrawableRes
import com.example.ui.theme.*

@Composable
fun NotificationsScreen(
    notifications: List<NotificationEntity>,
    onToggleFollowBack: (NotificationEntity) -> Unit
) {
    val todayNotifications = notifications.filter { it.timeCategory == "Today" }
    val yesterdayNotifications = notifications.filter { it.timeCategory == "Yesterday" }
    val olderNotifications = notifications.filter { it.timeCategory != "Today" && it.timeCategory != "Yesterday" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ZyraBackground)
            .padding(horizontal = 16.dp)
    ) {
        item {
            Text(
                text = "Notifications",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ZyraTextPrimary,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        if (todayNotifications.isNotEmpty()) {
            item {
                SectionHeader("Today")
            }
            items(todayNotifications, key = { it.id }) { notif ->
                NotificationItemCard(
                    notification = notif,
                    onToggleFollowBack = { onToggleFollowBack(notif) }
                )
            }
        }

        if (yesterdayNotifications.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                SectionHeader("Yesterday")
            }
            items(yesterdayNotifications, key = { it.id }) { notif ->
                NotificationItemCard(
                    notification = notif,
                    onToggleFollowBack = { onToggleFollowBack(notif) }
                )
            }
        }

        if (olderNotifications.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                SectionHeader("This Week")
            }
            items(olderNotifications, key = { it.id }) { notif ->
                NotificationItemCard(
                    notification = notif,
                    onToggleFollowBack = { onToggleFollowBack(notif) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = ZyraTextPrimary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun NotificationItemCard(
    notification: NotificationEntity,
    onToggleFollowBack: () -> Unit
) {
    val avatarRes = rememberDrawableRes(notification.userAvatarDrawable)
    val thumbnailRes = notification.postThumbnailDrawable?.let { rememberDrawableRes(it) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // User Avatar with Notification Type Icon Overlay
            Box {
                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = notification.username,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                )

                val (badgeColor, badgeIcon) = when (notification.notificationType) {
                    "like" -> Pair(ZyraSecondary, Icons.Default.Favorite)
                    "comment" -> Pair(ZyraPrimary, Icons.Default.ModeComment)
                    else -> Pair(ZyraPrimary, Icons.Default.PersonAdd)
                }

                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(badgeColor)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = badgeIcon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Text Block
            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = ZyraTextPrimary)) {
                    append("@${notification.username} ")
                }
                withStyle(style = SpanStyle(color = ZyraTextPrimary)) {
                    append("${notification.actionText} ")
                }
                withStyle(style = SpanStyle(color = ZyraTextSecondary, fontSize = 11.sp)) {
                    append(notification.timeAgo)
                }
            }

            Text(
                text = annotatedText,
                fontSize = 13.sp,
                lineHeight = 17.sp,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Action: Post Thumbnail OR Follow Back Button
        if (notification.notificationType == "follow") {
            val isFollowing = notification.isFollowingBack
            Button(
                onClick = onToggleFollowBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFollowing) ZyraSurface else ZyraPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(34.dp)
                    .border(
                        width = if (isFollowing) 1.dp else 0.dp,
                        color = ZyraCardBorder,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isFollowing) "Following" else "Follow Back",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        } else if (thumbnailRes != null) {
            Image(
                painter = painterResource(id = thumbnailRes),
                contentDescription = "Post Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}
