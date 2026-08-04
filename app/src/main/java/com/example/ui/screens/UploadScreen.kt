package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.rememberDrawableRes
import com.example.ui.theme.*

@Composable
fun UploadScreen(
    caption: String,
    location: String,
    music: String,
    selectedMedia: String,
    onCaptionChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onMusicChange: (String) -> Unit,
    onMediaSelect: (String) -> Unit,
    onPublish: () -> Unit,
    onCancel: () -> Unit
) {
    val galleryMediaOptions = listOf(
        "img_post_cityscape_1785803032776",
        "img_post_nature_1785803064357",
        "img_reel_fashion_1785803079235",
        "img_user_avatar_1785803090179"
    )

    val scrollState = rememberScrollState()
    val previewRes = rememberDrawableRes(selectedMedia)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZyraBackground)
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        // Top Action Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = ZyraTextPrimary
                )
            }

            Text(
                text = "New Post",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ZyraTextPrimary
            )

            // Publish Button
            Button(
                onClick = onPublish,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(ZyraGradientStart, ZyraGradientEnd)
                        )
                    ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Share",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Selected Media Preview Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ZyraSurface)
        ) {
            Image(
                painter = painterResource(id = previewRes),
                contentDescription = "Selected Media Preview",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(0.6f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoLibrary,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Gallery Selected",
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Caption Input Area
        OutlinedTextField(
            value = caption,
            onValueChange = onCaptionChange,
            placeholder = { Text("Write a caption...", color = ZyraTextSecondary) },
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ZyraSurface,
                unfocusedContainerColor = ZyraSurface,
                focusedBorderColor = ZyraPrimary,
                unfocusedBorderColor = ZyraCardBorder,
                focusedTextColor = ZyraTextPrimary,
                unfocusedTextColor = ZyraTextPrimary
            ),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Select Gallery Presets Section
        Text(
            text = "Select Photo / Video",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = ZyraTextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            galleryMediaOptions.forEach { mediaName ->
                val mediaResId = rememberDrawableRes(mediaName)
                val isSelected = mediaName == selectedMedia

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = if (isSelected) 3.dp else 0.dp,
                            brush = Brush.horizontalGradient(listOf(ZyraGradientStart, ZyraGradientEnd)),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onMediaSelect(mediaName) }
                ) {
                    Image(
                        painter = painterResource(id = mediaResId),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Options List with Icons
        OptionRowItem(
            icon = Icons.Outlined.LocationOn,
            title = "Add Location",
            value = location,
            placeholder = "e.g. Mumbai, India",
            onValueChange = onLocationChange
        )

        OptionRowItem(
            icon = Icons.Outlined.MusicNote,
            title = "Add Music",
            value = music,
            placeholder = "e.g. Midnight Synthwave",
            onValueChange = onMusicChange
        )

        OptionRowItem(
            icon = Icons.Outlined.Group,
            title = "Tag People",
            value = "",
            placeholder = "Tap to tag friends",
            onValueChange = {}
        )

        OptionRowItem(
            icon = Icons.Outlined.AutoFixHigh,
            title = "Filters & Effects",
            value = "",
            placeholder = "Cyber Magenta Glow Applied",
            onValueChange = {}
        )

        Spacer(modifier = Modifier.height(90.dp))
    }
}

@Composable
fun OptionRowItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ZyraSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = ZyraPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZyraTextPrimary
                )
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = { Text(placeholder, fontSize = 12.sp, color = ZyraTextSecondary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = ZyraPrimary,
                        unfocusedBorderColor = ZyraCardBorder,
                        focusedTextColor = ZyraTextPrimary,
                        unfocusedTextColor = ZyraTextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                )
            }
        }
    }
}
