package com.example.animeapp2.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.animeapp2.R
import com.example.animeapp2.ui.navigation.Screen

@Composable
fun DrawerMenu(
    navController: NavController,
    onCloseDrawer: () -> Unit // Función para cerrar el drawer tras hacer clic
) {
    // Observamos la ruta actual para saber qué ítem resaltar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
        // --- HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 32.dp, start = 24.dp),
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
                        // Navegación inteligente
                        if (currentRoute != item.route) {
                            if (item.route == Screen.Login.route) {
                                // Al cerrar sesión, limpiamos todo el stack para evitar volver atrás
                                navController.navigate(item.route) {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                            } else {
                                navController.navigate(item.route) {
                                    // Evita acumular pantallas iguales en el historial
                                    popUpTo(Screen.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        onCloseDrawer() // Cerramos el menú lateral
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.White.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

        }
    }
}

// Clase de ayuda para los datos del menú
data class MenuData(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val route: String)
