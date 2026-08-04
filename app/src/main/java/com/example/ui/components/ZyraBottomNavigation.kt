package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ZyraTab
import com.example.ui.theme.*

@Composable
fun ZyraBottomNavigation(
    selectedTab: ZyraTab,
    unreadChatsCount: Int,
    unreadNotifCount: Int,
    onTabSelected: (ZyraTab) -> Unit
) {
    NavigationBar(
        containerColor = ZyraBackground,
        contentColor = ZyraTextPrimary,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets.navigationBars,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Home Tab
        NavigationBarItem(
            selected = selectedTab == ZyraTab.Home,
            onClick = { onTabSelected(ZyraTab.Home) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == ZyraTab.Home) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ZyraSecondary,
                indicatorColor = ZyraSurface
            )
        )

        // Search Tab
        NavigationBarItem(
            selected = selectedTab == ZyraTab.Search,
            onClick = { onTabSelected(ZyraTab.Search) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == ZyraTab.Search) Icons.Filled.Search else Icons.Outlined.Search,
                    contentDescription = "Search"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ZyraSecondary,
                indicatorColor = ZyraSurface
            )
        )

        // Prominent Center Upload Button
        NavigationBarItem(
            selected = selectedTab == ZyraTab.Upload,
            onClick = { onTabSelected(ZyraTab.Upload) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(ZyraGradientStart, ZyraGradientEnd)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Upload Post",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )

        // Reels Tab
        NavigationBarItem(
            selected = selectedTab == ZyraTab.Reels,
            onClick = { onTabSelected(ZyraTab.Reels) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == ZyraTab.Reels) Icons.Filled.Movie else Icons.Outlined.Movie,
                    contentDescription = "Reels"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ZyraSecondary,
                indicatorColor = ZyraSurface
            )
        )

        // Chat Tab
        NavigationBarItem(
            selected = selectedTab == ZyraTab.Chat,
            onClick = { onTabSelected(ZyraTab.Chat) },
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadChatsCount > 0) {
                            Badge(containerColor = ZyraSecondary) {
                                Text("$unreadChatsCount", color = Color.White)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (selectedTab == ZyraTab.Chat) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Chat"
                    )
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ZyraSecondary,
                indicatorColor = ZyraSurface
            )
        )

        // Profile Tab
        NavigationBarItem(
            selected = selectedTab == ZyraTab.Profile,
            onClick = { onTabSelected(ZyraTab.Profile) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == ZyraTab.Profile) Icons.Filled.Person else Icons.Outlined.PersonOutline,
                    contentDescription = "Profile"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ZyraSecondary,
                indicatorColor = ZyraSurface
            )
        )
    }
}
