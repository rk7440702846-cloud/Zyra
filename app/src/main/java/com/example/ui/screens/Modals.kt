package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.rememberDrawableRes
import com.example.ui.theme.*
import kotlinx.coroutines.delay

// 1. STORY VIEWER MODAL
@Composable
fun StoryViewerModal(
    story: StoryEntity,
    onClose: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var replyText by remember { mutableStateOf("") }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "storyProgress"
    )

    val storyImageRes = rememberDrawableRes(story.storyImageDrawable)
    val avatarRes = rememberDrawableRes(story.avatarDrawable)

    LaunchedEffect(Unit) {
        for (i in 1..100) {
            delay(50)
            progress = i / 100f
        }
        delay(300)
        onClose()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = storyImageRes),
            contentDescription = "Story Content",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Story Header Overlay
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            // Linear Progress Bar
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = Color.White,
                trackColor = Color.White.copy(0.3f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = story.username,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = story.username,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = story.timestamp,
                        fontSize = 12.sp,
                        color = Color.White.copy(0.7f)
                    )
                }

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }
        }

        // Reply Input Bar at Bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = replyText,
                onValueChange = { replyText = it },
                placeholder = { Text("Send message...", color = Color.White.copy(0.6f)) },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Black.copy(0.5f),
                    unfocusedContainerColor = Color.Black.copy(0.5f),
                    focusedBorderColor = Color.White.copy(0.8f),
                    unfocusedBorderColor = Color.White.copy(0.4f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            IconButton(
                onClick = {
                    replyText = ""
                    onClose()
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(ZyraPrimary)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Send Reply",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// 2. POST COMMENTS MODAL
@Composable
fun PostCommentsModal(
    comments: List<CommentEntity>,
    onAddComment: (String) -> Unit,
    onClose: () -> Unit
) {
    var newCommentText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.6f))
            .clickable { onClose() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(ZyraSurface)
                .clickable(enabled = false) { /* prevent click through */ }
                .padding(16.dp)
        ) {
            // Drag handle & Header
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(ZyraCardBorder)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Comments",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZyraTextPrimary
                )

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = ZyraTextSecondary
                    )
                }
            }

            HorizontalDivider(color = ZyraSurfaceVariant)

            // Comments List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(comments, key = { it.id }) { comment ->
                    val avatarRes = rememberDrawableRes(comment.userAvatarDrawable)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Image(
                            painter = painterResource(id = avatarRes),
                            contentDescription = comment.username,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "@${comment.username}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ZyraTextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = comment.timeAgo,
                                    fontSize = 10.sp,
                                    color = ZyraTextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = comment.commentText,
                                fontSize = 13.sp,
                                color = ZyraTextPrimary,
                                lineHeight = 18.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = "Like Comment",
                            tint = ZyraTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newCommentText,
                    onValueChange = { newCommentText = it },
                    placeholder = { Text("Add a comment...", color = ZyraTextSecondary) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ZyraBackground,
                        unfocusedContainerColor = ZyraBackground,
                        focusedBorderColor = ZyraPrimary,
                        unfocusedBorderColor = ZyraCardBorder,
                        focusedTextColor = ZyraTextPrimary,
                        unfocusedTextColor = ZyraTextPrimary
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (newCommentText.isNotBlank()) {
                            onAddComment(newCommentText)
                            newCommentText = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ZyraPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Post Comment",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// 3. CONVERSATION DETAIL SCREEN
@Composable
fun ConversationDetailScreen(
    chat: ChatEntity,
    messages: List<MessageEntity>,
    onSendMessage: (String) -> Unit,
    onBack: () -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    val avatarRes = rememberDrawableRes(chat.contactAvatarDrawable)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZyraBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Navigation Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = ZyraTextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Box {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = chat.contactName,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                    )
                    if (chat.isOnline) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(ZyraOnlineGreen)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = chat.contactName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ZyraTextPrimary
                    )
                    Text(
                        text = if (chat.isOnline) "Active now" else "@${chat.contactHandle}",
                        fontSize = 11.sp,
                        color = ZyraTextSecondary
                    )
                }
            }

            Row {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Audio Call",
                        tint = ZyraTextPrimary
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Video Call",
                        tint = ZyraTextPrimary
                    )
                }
            }
        }

        HorizontalDivider(color = ZyraSurfaceVariant)

        // Messages Thread List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                MessageBubble(message = msg)
            }
        }

        // Bottom Message Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text("Type a message...", color = ZyraTextSecondary) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ZyraSurface,
                    unfocusedContainerColor = ZyraSurface,
                    focusedBorderColor = ZyraPrimary,
                    unfocusedBorderColor = ZyraCardBorder,
                    focusedTextColor = ZyraTextPrimary,
                    unfocusedTextColor = ZyraTextPrimary
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (textInput.isNotBlank()) {
                        onSendMessage(textInput)
                        textInput = ""
                    }
                },
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(ZyraPrimary)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: MessageEntity) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (message.isFromMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (message.isFromMe) 16.dp else 4.dp,
                            bottomEnd = if (message.isFromMe) 4.dp else 16.dp
                        )
                    )
                    .background(
                        if (message.isFromMe) ZyraPrimary else ZyraSurface
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.messageText,
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 19.sp
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = message.timestamp,
                fontSize = 10.sp,
                color = ZyraTextSecondary
            )
        }
    }
}
