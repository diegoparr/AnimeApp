package com.example.animeapp2.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animeapp2.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrimsonListTopBar(drawerState: DrawerState){

    CenterAlignedTopAppBar(

        navigationIcon = {
            NavigationDrawerButton(drawerState)
        },

        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Image(
                    painter = painterResource(R.drawable.ic_app_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically) // Asegura centrado vertical
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Crimson List",
                    style = MaterialTheme.typography.displayLarge.copy(),
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}






