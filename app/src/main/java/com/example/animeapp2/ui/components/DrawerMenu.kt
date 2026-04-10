package com.example.animeapp2.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animeapp2.R

@Composable
fun DrawerMenu() {
    // degradado vertical "Vampírico" para un look Premium
    val drawerGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0D0D0D), // Negro puro arriba
            Color(0xFF240202)  // Rojo carmesí muy profundo abajo
        )
    )

    ModalDrawerSheet(
        drawerContainerColor = Color.Transparent, // Transparente para dejar ver el degradado
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
        drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
        modifier = Modifier
            .fillMaxHeight()
            .width(310.dp)
            .background(drawerGradient)
    ) {
        // --- HEADER (Branding) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_app_icon),
                    contentDescription = null,
                    modifier = Modifier.size(42.dp)
                )
                Spacer(Modifier.width(14.dp))
                Text(
                    text = "CRIMSON LIST",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 2.sp
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = Color.White.copy(alpha = 0.08f)
        )

        Spacer(Modifier.height(24.dp))

        // --- MENU ITEMS ---
        val menuItems = listOf(
            Triple("Menu", Icons.Default.Home, true),
            Triple("Mi Lista", Icons.AutoMirrored.Filled.List, false),
            Triple("Descubrir", Icons.Default.Search, false),
            Triple("Notificaciones", Icons.Default.Notifications, false)
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .fillMaxHeight()
        ) {
            menuItems.forEach { (label, icon, isSelected) ->
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    },
                    selected = isSelected,
                    onClick = {

                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary, // Rojo Sangre
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color(0xFFF1FAEE).copy(alpha = 0.7f) // GhostWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el Logout al final

            // --- SECCIÓN INFERIOR (Logout) ---
            NavigationDrawerItem(
                label = { Text("Cerrar Sesión", fontSize = 16.sp) },
                selected = false,
                onClick = { /* Acción Logout futura */ },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    unselectedTextColor = Color(0xFFF1FAEE).copy(alpha = 0.5f)
                ),
                modifier = Modifier.padding(bottom = 28.dp)
            )
        }
    }
}
