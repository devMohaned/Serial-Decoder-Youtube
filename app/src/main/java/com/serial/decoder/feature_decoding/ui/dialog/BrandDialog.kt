package com.serial.decoder.feature_decoding.ui.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.serial.decoder.R
import com.serial.decoder.core.ui.DOUBLE_SPACING
import com.serial.decoder.core.ui.NORMAL_SPACING
import com.serial.decoder.core.ui.NormalButton
import com.serial.decoder.feature_decoding.data.local.entity.Brands
import com.serial.decoder.ui.theme.CornerButtonShape


@Composable
fun BrandsAlertDialog(
    modifier: Modifier = Modifier,
    brands: Array<Brands>,
    onBrandClicked: (brand: String) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(modifier = modifier, onDismissRequest = onDismissRequest, title = {
        AlertDialogTitle(title = stringResource(id = R.string.select_a_brand))
    }, text = {
        LazyColumn(modifier = modifier) {
            items(brands.size)
            {
                Brand(brandName = brands[it].name, onBrandClicked = {
                    onBrandClicked(brands[it].name)
                    onDismissRequest()
                })
            }
        }
    }, buttons = {
        OutlinedButton(
            modifier = modifier
                .padding(horizontal = DOUBLE_SPACING, vertical = DOUBLE_SPACING)
                .fillMaxWidth()
                .clip(CornerButtonShape),
            onClick = onDismissRequest
        ) {
            Text(text = stringResource(id = R.string.dismiss))
        }
    })
}


@Composable
fun AlertDialogTitle(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.h3
    )
}

@Composable
fun Brand(modifier: Modifier = Modifier, brandName: String, onBrandClicked: () -> Unit) {
    NormalButton(
        modifier = modifier
            .padding(horizontal = DOUBLE_SPACING)
            .padding(bottom = NORMAL_SPACING)
            .fillMaxWidth(), onClick = onBrandClicked
    ) {
        Text(text = brandName)
    }
}