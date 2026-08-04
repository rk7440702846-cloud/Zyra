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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.rememberDrawableRes
import com.example.ui.theme.*

data class SuggestedUser(
    val id: Long,
    val name: String,
    val handle: String,
    val bio: String,
    val avatarDrawable: String,
    val isVerified: Boolean = true,
    var isFollowing: Boolean = false
)

data class TrendingTag(
    val tag: String,
    val postsCount: String
)

@Composable
fun SearchScreen(
    searchQuery: String,
    selectedSearchTab: String,
    onQueryChange: (String) -> Unit,
    onTabChange: (String) -> Unit
) {
    val tabs = listOf("Top", "Users", "Tags", "Places")

    val trendingTags = listOf(
        TrendingTag("#Photography", "142.5K posts"),
        TrendingTag("#TravelVibes", "98.2K posts"),
        TrendingTag("#ZyraGlow", "76.4K posts"),
        TrendingTag("#TechTrends", "54.1K posts"),
        TrendingTag("#MumbaiDiaries", "41.8K posts"),
        TrendingTag("#CyberAesthetic", "28.9K posts")
    )

    var suggestedUsers by remember {
        mutableStateOf(
            listOf(
                SuggestedUser(1, "Rohan Verma", "rohan_vibe", "Travel photographer 📷 Chasing sunsets", "img_post_nature_1785803064357", isVerified = false, isFollowing = false),
                SuggestedUser(2, "Priya Kapoor", "priya_art", "Fashion designer & streetwear enthusiast 🔥", "img_reel_fashion_1785803079235", isVerified = true, isFollowing = false),
                SuggestedUser(3, "Kabir Mehta", "kabir_clickz", "Architectural & night city shots 🏙️", "img_post_cityscape_1785803032776", isVerified = true, isFollowing = true)
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ZyraBackground)
            .padding(horizontal = 16.dp)
    ) {
        // Search Input Bar
        item {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                placeholder = { Text("Search users, tags, places...", color = ZyraTextSecondary) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = ZyraPrimary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Search",
                                tint = ZyraTextSecondary
                            )
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ZyraSurface,
                    unfocusedContainerColor = ZyraSurface,
                    focusedBorderColor = ZyraPrimary,
                    unfocusedBorderColor = ZyraCardBorder,
                    focusedTextColor = ZyraTextPrimary,
                    unfocusedTextColor = ZyraTextPrimary
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Horizontal Category Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEach { tab ->
                    val isSelected = tab == selectedSearchTab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) {
                                    Brush.horizontalGradient(listOf(ZyraGradientStart, ZyraGradientEnd))
                                } else {
                                    Brush.linearGradient(listOf(ZyraSurface, ZyraSurface))
                                }
                            )
                            .clickable { onTabChange(tab) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else ZyraTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: Trending Tags
        if (selectedSearchTab == "Top" || selectedSearchTab == "Tags") {
            item {
                Text(
                    text = "Trending Tags 🔥",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZyraTextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(trendingTags) { tagItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onQueryChange(tagItem.tag) }
                        .padding(vertical = 10.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ZyraSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tag,
                                contentDescription = null,
                                tint = ZyraSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = tagItem.tag,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ZyraTextPrimary
                            )
                            Text(
                                text = tagItem.postsCount,
                                fontSize = 11.sp,
                                color = ZyraTextSecondary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Tag",
                        tint = ZyraTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Section: Suggested Users
        if (selectedSearchTab == "Top" || selectedSearchTab == "Users") {
            item {
                Text(
                    text = "Suggested Users",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZyraTextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(suggestedUsers, key = { it.id }) { user ->
                val avatarRes = rememberDrawableRes(user.avatarDrawable)

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
                        Image(
                            painter = painterResource(id = avatarRes),
                            contentDescription = user.name,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ZyraTextPrimary
                                )
                                if (user.isVerified) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Verified",
                                        tint = ZyraPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            Text(
                                text = "@${user.handle}",
                                fontSize = 12.sp,
                                color = ZyraTextSecondary
                            )
                            Text(
                                text = user.bio,
                                fontSize = 11.sp,
                                color = ZyraTextSecondary.copy(alpha = 0.8f),
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Follow / Following Button Toggle
                    val isFollowing = user.isFollowing
                    Button(
                        onClick = {
                            suggestedUsers = suggestedUsers.map {
                                if (it.id == user.id) it.copy(isFollowing = !it.isFollowing) else it
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFollowing) ZyraSurface else ZyraPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .border(
                                width = if (isFollowing) 1.dp else 0.dp,
                                color = ZyraCardBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(
                            text = if (isFollowing) "Following" else "Follow",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
