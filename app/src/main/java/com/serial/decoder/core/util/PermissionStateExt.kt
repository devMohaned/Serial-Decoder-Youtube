package com.serial.decoder.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isDeniedPermanently() : Boolean{
    return !this.hasPermission && !this.shouldShowRationale
}

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isGranted() : Boolean{
    return this.hasPermission
}
@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isDeniedOnce() : Boolean{
    return this.shouldShowRationale
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionState.HandlePermissionState(
    modifier: Modifier = Modifier,
    GrantedPermissionContent: @Composable (modifier: Modifier) -> Unit,
    DeniedOncePermissionContent: @Composable (modifier: Modifier) -> Unit,
    DeniedPermanentlyContent: @Composable (modifier: Modifier) -> Unit,
)
{
    if(isGranted()) GrantedPermissionContent.invoke(modifier)
    else if(isDeniedOnce()) DeniedOncePermissionContent.invoke(modifier)
    else if(isDeniedPermanently()) DeniedPermanentlyContent.invoke(modifier)
}