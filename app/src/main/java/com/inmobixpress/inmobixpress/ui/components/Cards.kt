package com.inmobixpress.inmobixpress.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bathtub
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.SquareFoot
import androidx.compose.material.icons.outlined.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.utils.bathroomFormat
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty
import com.inmobixpress.inmobixpress.ui.utils.priceFormat

@Composable
fun ItemCard(property: PropertyItem, onItemClick: (property: PropertyItem) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onItemClick(property) },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Box {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    painter = painterResource(id = property.images[0]),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                OutlinedIconButton(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .align(alignment = Alignment.TopEnd),
                    onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "",
                    )
                }
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(text = "S/${property.price.toString().priceFormat()}", fontWeight = FontWeight.Medium)
                Text(
                    text = "S/${property.maintenance} Mantenimiento",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Text(
                    modifier = Modifier.padding(vertical = 6.dp),
                    text = "${property.address}, ${property.district.name}, ${property.district.province.name}",
                    fontSize = 11.sp
                )
                Row {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                            .align(alignment = Alignment.CenterVertically)
                            .size(20.dp),
                        imageVector = property.type.icon,
                        contentDescription = "",
                    )
                    VerticalDivider(
                        Modifier
                            .height(15.dp)
                            .align(alignment = Alignment.CenterVertically)
                    )
                    Icon(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .align(alignment = Alignment.CenterVertically)
                            .size(20.dp),
                        imageVector = Icons.Outlined.SquareFoot,
                        contentDescription = "",
                    )
                    Text(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically),
                        text = "${property.area.toInt()} m²",
                        fontSize = 10.sp
                    )
                    AnimatedVisibility(visible = property.bedrooms != 0) {
                        Row {
                            Icon(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp, vertical = 4.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .size(20.dp),
                                imageVector = Icons.Outlined.Bed,
                                contentDescription = "",
                            )
                            Text(
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterVertically),
                                text = "${property.bedrooms} dorm.",
                                fontSize = 10.sp
                            )
                        }
                    }
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                            .align(alignment = Alignment.CenterVertically)
                            .size(20.dp),
                        imageVector = Icons.Outlined.Bathtub,
                        contentDescription = "",
                    )
                    Text(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically),
                        text = "${property.bathrooms.toString().bathroomFormat()} baños",
                        fontSize = 10.sp
                    )
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                            .align(alignment = Alignment.CenterVertically)
                            .size(20.dp),
                        imageVector = Icons.Outlined.DirectionsCar,
                        contentDescription = "",
                    )
                    Text(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically),
                        text = "${property.garages} estac.",
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = property.description,
                    fontSize = 11.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    OutlinedIconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = "",
                        )
                    }
                    Button(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .weight(1.0.toFloat()),
                        onClick = {

                        }) {
                        Icon(
                            imageVector = Icons.Outlined.Whatsapp,
                            contentDescription = "",
                        )
                        Text(modifier = Modifier.padding(start = 4.dp), text = "WhatsApp")
                    }
                    OutlinedButton(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1.0.toFloat()),
                        onClick = {

                        }) {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "",
                        )
                        Text(modifier = Modifier.padding(start = 4.dp), text = "Contactar")
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun ItemCardPreview() {
    ItemCard(property = previewListProperty()[0], onItemClick = { })
}