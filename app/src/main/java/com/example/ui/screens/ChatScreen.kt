package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatEntity
import com.example.ui.components.rememberDrawableRes
import com.example.ui.theme.*

@Composable
fun ChatScreen(
    chats: List<ChatEntity>,
    onOpenChat: (Long) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Direct") }
    var showNewChatDialog by remember { mutableStateOf(false) }

    val categories = listOf("Direct", "Group", "Requests")

    val filteredChats = chats.filter {
        it.category == selectedCategory &&
                (searchQuery.isBlank() || it.contactName.contains(searchQuery, ignoreCase = true) || it.lastMessage.contains(searchQuery, ignoreCase = true))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ZyraBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Messages",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZyraTextPrimary
                )

                IconButton(onClick = { showNewChatDialog = true }) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "New Message",
                        tint = ZyraPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search chats or friends...", color = ZyraTextSecondary) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = ZyraPrimary
                    )
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
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Online Contacts Horizontal Row
            Text(
                text = "Online Now 🟢",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ZyraTextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(chats.filter { it.isOnline }) { chat ->
                    val avatarRes = rememberDrawableRes(chat.contactAvatarDrawable)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onOpenChat(chat.id) }
                    ) {
                        Box {
                            Image(
                                painter = painterResource(id = avatarRes),
                                contentDescription = chat.contactName,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                            )
                            // Green Online Status Indicator Dot
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(ZyraOnlineGreen)
                                    .border(2.dp, ZyraBackground, CircleShape)
                                    .align(Alignment.BottomEnd)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = chat.contactName.split(" ").firstOrNull() ?: "",
                            fontSize = 11.sp,
                            color = ZyraTextSecondary,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Category Filter Tabs (Direct, Group, Requests)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = cat == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ZyraPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = ZyraSurface,
                            labelColor = ZyraTextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = ZyraCardBorder,
                            selectedBorderColor = ZyraPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Chat Previews List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredChats, key = { it.id }) { chat ->
                    ChatItemCard(chat = chat, onClick = { onOpenChat(chat.id) })
                }

                item {
                    Spacer(modifier = Modifier.height(90.dp))
                }
            }
        }

        // Prominent Purple Floating Action Button (+) for starting new chat
        FloatingActionButton(
            onClick = { showNewChatDialog = true },
            containerColor = ZyraPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New Chat",
                modifier = Modifier.size(28.dp)
            )
        }

        // New Chat Picker Dialog
        if (showNewChatDialog) {
            AlertDialog(
                onDismissRequest = { showNewChatDialog = false },
                containerColor = ZyraSurface,
                title = {
                    Text(
                        text = "Start New Chat",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ZyraTextPrimary
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Select a contact to start messaging:",
                            fontSize = 13.sp,
                            color = ZyraTextSecondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        chats.forEach { chat ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showNewChatDialog = false
                                        onOpenChat(chat.id)
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val avatarRes = rememberDrawableRes(chat.contactAvatarDrawable)
                                Image(
                                    painter = painterResource(id = avatarRes),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = chat.contactName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ZyraTextPrimary
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showNewChatDialog = false }) {
                        Text("Close", color = ZyraSecondary)
                    }
                }
            )
        }
    }
}

@Composable
fun ChatItemCard(
    chat: ChatEntity,
    onClick: () -> Unit
) {
    val avatarRes = rememberDrawableRes(chat.contactAvatarDrawable)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box {
                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = chat.contactName,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                if (chat.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(ZyraOnlineGreen)
                            .border(2.dp, ZyraBackground, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = chat.contactName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZyraTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = chat.lastMessage,
                    fontSize = 12.sp,
                    color = if (chat.unreadCount > 0) ZyraTextPrimary else ZyraTextSecondary,
                    fontWeight = if (chat.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = chat.lastMessageTime,
                fontSize = 10.sp,
                color = if (chat.unreadCount > 0) ZyraSecondary else ZyraTextSecondary
            )

            if (chat.unreadCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                // Clear Purple Circle Badge (#8A2BE2)
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(ZyraPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${chat.unreadCount}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
