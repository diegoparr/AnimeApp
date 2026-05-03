package com.example.animeapp2.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.animeapp2.R
import com.example.animeapp2.ui.navigation.Screen
import com.example.animeapp2.viewmodel.AuthUsersViewModel

@Composable
fun DrawerMenu(
    navController: NavController,
    authviewModel: AuthUsersViewModel,
    onCloseDrawer: () -> Unit // Función para cerrar el drawer tras hacer clic
) {
    // Observamos la ruta actual para saber qué ítem resaltar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentUser = authviewModel.currentUser
    val drawerGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0D0D0D), 
            Color(0xFF240202)
        )
    )

    ModalDrawerSheet(
        drawerContainerColor = Color.Transparent,
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
        drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
        modifier = Modifier
            .fillMaxHeight()
            .width(310.dp)
            .background(drawerGradient)
    ) {
        // --- HEADER PREMIUM ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp, bottom = 32.dp, start = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                // Branding
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.ic_app_2),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "CRIMSON LIST",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            fontSize = 32.sp,
                            lineHeight = 32.sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Sección de Usuario
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar Circular con "Glow"
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(MaterialTheme.colorScheme.primary, Color(0xFF8B0000))
                                ),
                                shape = CircleShape
                            )
                            .padding(2.dp)
                            .background(Color(0xFF1A1A1A), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser?.nombre_usuario?.firstOrNull()?.uppercase() ?: "U",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "BIENVENIDO DE NUEVO",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.4f),
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = currentUser?.nombre_usuario ?: "Explorador",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = Color.White.copy(alpha = 0.08f)
        )

        Spacer(Modifier.height(24.dp))

        // --- MENU ITEMS DINÁMICOS ---
        val menuItems = listOf(
            MenuData("Menú", Icons.Default.Home, Screen.Home.route),
            MenuData("Mi Lista", Icons.AutoMirrored.Filled.List, Screen.MyList.route),
            MenuData("Descubrir", Icons.Default.Search, Screen.Search.route),
            MenuData("Cerrar Sesión",Icons.AutoMirrored.Filled.ExitToApp, Screen.Login.route )
        )

        Column(modifier = Modifier.padding(horizontal = 14.dp)) {
            menuItems.forEach { item ->
                val isSelected = currentRoute == item.route

                NavigationDrawerItem(
                    label = { Text(text = item.label, fontSize = 16.sp) },
                    selected = isSelected,
                    onClick = {
                        if (item.route == Screen.Login.route) {
                            authviewModel.logout()
                            navController.navigate(item.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        else {
                            // NAVEGACIÓN ESTABLE Y SIN ERRORES DE ESTADO
                            navController.navigate(item.route) {
                                // 1. Volvemos siempre a la Home borrando lo que haya en medio
                                // Pero SIN guardar estados problemáticos
                                popUpTo(Screen.Home.route) {
                                    inclusive = (item.route == Screen.Home.route)
                                }
                                // 2. Si ya estamos en la pantalla, no creamos otra
                                launchSingleTop = true
                            }
                        }
                        onCloseDrawer()
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent, // Hacemos transparente para que mande nuestro gradient
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.White.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .then(
                            if (isSelected) {
                                Modifier.background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.01f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            } else Modifier
                        )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

data class MenuData(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val route: String)
