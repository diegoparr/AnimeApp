package com.example.animeapp2.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerButton(
    drawerState : DrawerState
){

    var scope = rememberCoroutineScope()

    IconButton(
        onClick = {
        scope.launch {
            if(drawerState.isOpen){
                drawerState.close()
            }else{
                drawerState.open()

            }
        }

        },

    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu"
        )
    }

}

@Preview
@Composable
fun NavigationDrawerPreview(){
    NavigationDrawerButton(drawerState = rememberDrawerState(initialValue = DrawerValue.Closed))
}
