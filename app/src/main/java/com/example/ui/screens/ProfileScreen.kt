package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PersonPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PostEntity
import com.example.data.UserProfileEntity
import com.example.ui.ZyraTab
import com.example.ui.components.rememberDrawableRes
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    profile: UserProfileEntity?,
    posts: List<PostEntity>,
    onOpenPostDetail: (Long) -> Unit,
    onNavigateToChat: () -> Unit,
    onLogout: () -> Unit
) {
    if (profile == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ZyraBackground),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = ZyraPrimary)
        }
        return
    }

    val avatarRes = rememberDrawableRes(profile.avatarDrawable)
    var selectedProfileTab by remember { mutableIntStateOf(0) } // 0 = Grid, 1 = Reels, 2 = Tagged
    var isFollowing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZyraBackground)
    ) {
        // Top Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = profile.username,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZyraTextPrimary
                )
                if (profile.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = ZyraPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Settings / Logout",
                        tint = ZyraTextPrimary
                    )
                }
            }
        }

        // Profile Header Block
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Large Avatar with Story Ring Option
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(ZyraGradientStart, ZyraGradientEnd)))
                        .padding(3.dp)
                ) {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = profile.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                // Stats Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(count = "${profile.postsCount}", label = "Posts")
                    StatItem(count = formatFollowers(profile.followersCount), label = "Followers")
                    StatItem(count = "${profile.followingCount}", label = "Following")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Full Bio Block
            Text(
                text = profile.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ZyraTextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = profile.bio,
                fontSize = 13.sp,
                color = ZyraTextPrimary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Location & Link Chips
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = ZyraSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = profile.location,
                    fontSize = 12.sp,
                    color = ZyraTextSecondary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = ZyraPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = profile.website,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ZyraPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons Row (Follow/Edit, Message, Share)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { isFollowing = !isFollowing },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFollowing) ZyraSurface else ZyraPrimary
                    ),
                    border = if (isFollowing) ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(ZyraCardBorder, ZyraCardBorder))) else null
                ) {
                    Text(
                        text = if (isFollowing) "Following" else "Follow",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = onNavigateToChat,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = ZyraSurface),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(ZyraCardBorder, ZyraCardBorder)))
                ) {
                    Text(
                        text = "Message",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ZyraTextPrimary
                    )
                }

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ZyraSurface)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = "Saved",
                        tint = ZyraTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Content View Tabs (3-Column Grid vs Reels vs Tagged)
        TabRow(
            selectedTabIndex = selectedProfileTab,
            containerColor = ZyraBackground,
            contentColor = ZyraPrimary,
            divider = { HorizontalDivider(color = ZyraSurfaceVariant) }
        ) {
            Tab(
                selected = selectedProfileTab == 0,
                onClick = { selectedProfileTab = 0 },
                icon = {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Grid View",
                        tint = if (selectedProfileTab == 0) ZyraSecondary else ZyraTextSecondary
                    )
                }
            )
            Tab(
                selected = selectedProfileTab == 1,
                onClick = { selectedProfileTab = 1 },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Movie,
                        contentDescription = "Reels View",
                        tint = if (selectedProfileTab == 1) ZyraSecondary else ZyraTextSecondary
                    )
                }
            )
            Tab(
                selected = selectedProfileTab == 2,
                onClick = { selectedProfileTab = 2 },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.PersonPin,
                        contentDescription = "Tagged View",
                        tint = if (selectedProfileTab == 2) ZyraSecondary else ZyraTextSecondary
                    )
                }
            )
        }

        // 3-Column Photo Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(posts, key = { it.id }) { post ->
                val mediaRes = rememberDrawableRes(post.mediaDrawable)
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(ZyraSurface)
                        .clickable { onOpenPostDetail(post.id) }
                ) {
                    Image(
                        painter = painterResource(id = mediaRes),
                        contentDescription = "Profile Post",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = ZyraTextPrimary
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = ZyraTextSecondary
        )
    }
}

private fun formatFollowers(count: Int): String {
    return if (count >= 1000) {
        String.format("%.1fK", count / 1000f)
    } else {
        "$count"
    }
}
