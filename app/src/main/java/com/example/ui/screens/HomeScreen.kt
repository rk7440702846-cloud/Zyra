package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PostEntity
import com.example.data.StoryEntity
import com.example.ui.ZyraTab
import com.example.ui.components.rememberDrawableRes
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    posts: List<PostEntity>,
    stories: List<StoryEntity>,
    onLikePost: (PostEntity) -> Unit,
    onBookmarkPost: (PostEntity) -> Unit,
    onOpenComments: (Long) -> Unit,
    onOpenStory: (Long) -> Unit,
    onNavigateTab: (ZyraTab) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ZyraBackground)
    ) {
        // Top App Bar
        item {
            HomeTopBar(
                onOpenNotifications = { onNavigateTab(ZyraTab.Notifications) },
                onOpenMessages = { onNavigateTab(ZyraTab.Chat) }
            )
        }

        // Horizontal Stories Bar
        item {
            StoriesBar(
                stories = stories,
                onStoryClick = onOpenStory
            )
            HorizontalDivider(
                modifier = Modifier.padding(top = 12.dp),
                color = ZyraSurfaceVariant,
                thickness = 0.8.dp
            )
        }

        // Feed Posts
        items(posts, key = { it.id }) { post ->
            PostCard(
                post = post,
                onLike = { onLikePost(post) },
                onBookmark = { onBookmarkPost(post) },
                onOpenComments = { onOpenComments(post.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun HomeTopBar(
    onOpenNotifications: () -> Unit,
    onOpenMessages: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ZYRA Gradient Title Logo
        Text(
            text = "ZYRA",
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            style = LocalTextStyle.current.copy(
                brush = Brush.horizontalGradient(
                    colors = listOf(ZyraGradientStart, ZyraGradientEnd)
                )
            )
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Notifications Icon with Red Badge
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ZyraSurface)
                    .clickable { onOpenNotifications() }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = ZyraTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
                // Unread Badge
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ZyraSecondary)
                        .align(Alignment.TopEnd)
                )
            }

            // Direct Chat Icon
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ZyraSurface)
                    .clickable { onOpenMessages() }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Messages",
                    tint = ZyraTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun StoriesBar(
    stories: List<StoryEntity>,
    onStoryClick: (Long) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(stories, key = { it.id }) { story ->
            StoryAvatarItem(story = story, onClick = { onStoryClick(story.id) })
        }
    }
}

@Composable
fun StoryAvatarItem(
    story: StoryEntity,
    onClick: () -> Unit
) {
    val avatarRes = rememberDrawableRes(story.avatarDrawable)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (story.isUserStory) {
                // User's own story ring
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, ZyraCardBorder, CircleShape)
                        .padding(3.dp)
                ) {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = "Your Story Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
                // Plus Badge
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(ZyraPrimary)
                        .align(Alignment.BottomEnd)
                        .border(2.dp, ZyraBackground, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Story",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            } else {
                // Friends story ring with purple/pink gradient if unseen
                val borderBrush = if (story.hasUnseen) {
                    Brush.linearGradient(listOf(ZyraGradientStart, ZyraGradientEnd))
                } else {
                    Brush.linearGradient(listOf(ZyraCardBorder, ZyraCardBorder))
                }

                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(borderBrush)
                        .padding(2.5.dp)
                        .background(ZyraBackground)
                        .padding(2.5.dp)
                ) {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = "Story Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = story.username,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (story.hasUnseen) ZyraTextPrimary else ZyraTextSecondary,
            maxLines = 1
        )
    }
}

@Composable
fun PostCard(
    post: PostEntity,
    onLike: () -> Unit,
    onBookmark: () -> Unit,
    onOpenComments: () -> Unit
) {
    val avatarRes = rememberDrawableRes(post.userAvatarDrawable)
    val mediaRes = rememberDrawableRes(post.mediaDrawable)
    var showDoubleTapHeart by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(ZyraBackground)
    ) {
        // User Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Gradient Ring Avatar
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(ZyraGradientStart, ZyraGradientEnd)))
                        .padding(2.dp)
                ) {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = post.username,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.username,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ZyraTextPrimary
                        )
                        if (post.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Verified",
                                tint = ZyraPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                    if (post.location.isNotEmpty()) {
                        Text(
                            text = post.location,
                            fontSize = 11.sp,
                            color = ZyraTextSecondary
                        )
                    }
                }
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = ZyraTextSecondary
                )
            }
        }

        // Post Media with Double-Tap Heart Gesture Animation
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .background(ZyraSurface)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            if (!post.isLiked) {
                                onLike()
                            }
                            scope.launch {
                                showDoubleTapHeart = true
                                delay(900)
                                showDoubleTapHeart = false
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = mediaRes),
                contentDescription = "Post Media",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Heart Overlay on Double Tap
            if (showDoubleTapHeart) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = ZyraSecondary,
                    modifier = Modifier.size(96.dp)
                )
            }
        }

        // Interactive Action Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Like Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onLike() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) ZyraSecondary else ZyraTextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${post.likesCount}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ZyraTextPrimary
                    )
                }

                // Comment Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onOpenComments() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = ZyraTextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${post.commentsCount}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ZyraTextPrimary
                    )
                }

                // Share Button
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Share",
                        tint = ZyraTextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Bookmark / Save Button
            IconButton(onClick = onBookmark) {
                Icon(
                    imageVector = if (post.isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Save",
                    tint = if (post.isBookmarked) ZyraPrimary else ZyraTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Caption Section with Hashtag Formatting
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            val annotatedCaption = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = ZyraTextPrimary)) {
                    append("${post.userHandle} ")
                }

                val words = post.caption.split(" ")
                for (word in words) {
                    if (word.startsWith("#")) {
                        withStyle(style = SpanStyle(color = ZyraPrimary, fontWeight = FontWeight.SemiBold)) {
                            append("$word ")
                        }
                    } else {
                        withStyle(style = SpanStyle(color = ZyraTextPrimary)) {
                            append("$word ")
                        }
                    }
                }
            }

            Text(
                text = annotatedCaption,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            if (post.commentsCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "View all ${post.commentsCount} comments",
                    fontSize = 12.sp,
                    color = ZyraTextSecondary,
                    modifier = Modifier.clickable { onOpenComments() }
                )
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = post.timestamp,
                fontSize = 10.sp,
                color = ZyraTextSecondary.copy(alpha = 0.7f)
            )
        }
    }
}
