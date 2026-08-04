package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ReelEntity
import com.example.ui.components.rememberDrawableRes
import com.example.ui.theme.*

@Composable
fun ReelsScreen(
    reels: List<ReelEntity>,
    onLikeReel: (ReelEntity) -> Unit,
    onFollowReel: (ReelEntity) -> Unit,
    onOpenComments: (Long) -> Unit
) {
    if (reels.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ZyraBackground),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = ZyraSecondary)
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { reels.size })

    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) { page ->
        val reel = reels[page]
        ReelPageItem(
            reel = reel,
            onLike = { onLikeReel(reel) },
            onFollow = { onFollowReel(reel) },
            onOpenComments = { onOpenComments(reel.id) }
        )
    }
}

@Composable
fun ReelPageItem(
    reel: ReelEntity,
    onLike: () -> Unit,
    onFollow: () -> Unit,
    onOpenComments: () -> Unit
) {
    val coverRes = rememberDrawableRes(reel.coverImageDrawable)
    val avatarRes = rememberDrawableRes(reel.userAvatarDrawable)
    var isPlaying by remember { mutableStateOf(true) }

    // Spinning Vinyl Music Disc Animation
    val infiniteTransition = rememberInfiniteTransition(label = "musicDisc")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { isPlaying = !isPlaying }
    ) {
        // Video Background Image
        Image(
            painter = painterResource(id = coverRes),
            contentDescription = "Reel Video",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient Shadow Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(0.3f),
                            Color.Transparent,
                            Color.Black.copy(0.85f)
                        )
                    )
                )
        )

        // Pause Indicator Overlay
        if (!isPlaying) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(0.5f))
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Top Header Title "Reels"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reels",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Icon(
                imageVector = Icons.Outlined.PhotoCamera,
                contentDescription = "Camera",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Right Side Action Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Like Action
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onLike) {
                    Icon(
                        imageVector = if (reel.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like Reel",
                        tint = if (reel.isLiked) ZyraSecondary else Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = "${reel.likesCount}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Comment Action
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onOpenComments) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(
                    text = "${reel.commentsCount}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Share Action
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "${reel.sharesCount}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Bookmark Action
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = "Save Reel",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Rotating Vinyl Disc
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .rotate(if (isPlaying) rotation else 0f)
                    .border(2.dp, Color.White, CircleShape)
                    .background(ZyraSurface),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = "Music Album",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }
        }

        // Bottom Creator & Caption Details
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 90.dp, start = 16.dp, end = 80.dp)
        ) {
            // User Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = reel.username,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = "@${reel.username}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Follow Button
                Button(
                    onClick = onFollow,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (reel.isFollowing) Color.White.copy(0.2f) else ZyraPrimary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        text = if (reel.isFollowing) "Following" else "Follow",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Video Title & Caption
            Text(
                text = reel.videoTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = reel.caption,
                fontSize = 12.sp,
                color = Color.White.copy(0.9f),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Music Ticker Track
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = reel.musicTrack,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(0.85f)
                )
            }
        }
    }
}
