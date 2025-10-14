package com.example.prediai.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R

enum class BottomNavItem(
    val route: String,
    val label: String,
    val icon: Int
) {
    BERANDA("beranda", "Beranda", R.drawable.ic_home),
    RIWAYAT("riwayat", "Riwayat", R.drawable.ic_history),
    SCAN("scan", "Scan", R.drawable.ic_camera),
    LABS("labs", "Labs", R.drawable.ic_labs),
    PROFIL("profil", "Profil", R.drawable.ic_profile)
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .shadow(8.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Beranda
                BottomNavItemView(
                    item = BottomNavItem.BERANDA,
                    isSelected = currentRoute == BottomNavItem.BERANDA.route,
                    onClick = { onNavigate(BottomNavItem.BERANDA.route) }
                )

                // Riwayat
                BottomNavItemView(
                    item = BottomNavItem.RIWAYAT,
                    isSelected = currentRoute == BottomNavItem.RIWAYAT.route,
                    onClick = { onNavigate(BottomNavItem.RIWAYAT.route) }
                )

                // Spacer untuk Scan button
                Spacer(modifier = Modifier.width(64.dp))

                // Labs
                BottomNavItemView(
                    item = BottomNavItem.LABS,
                    isSelected = currentRoute == BottomNavItem.LABS.route,
                    onClick = { onNavigate(BottomNavItem.LABS.route) }
                )

                // Profil
                BottomNavItemView(
                    item = BottomNavItem.PROFIL,
                    isSelected = currentRoute == BottomNavItem.PROFIL.route,
                    onClick = { onNavigate(BottomNavItem.PROFIL.route) }
                )
            }
        }

        Box(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-8).dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = { onNavigate(BottomNavItem.SCAN.route) },
                containerColor = Color(0xFF00B4A3),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(64.dp),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_scan_tongue_nail),
                    contentDescription = "Scan",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun BottomNavItemView(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) Color(0xFF00B4A3) else Color(0xFF9CA3AF)

    IconButton(
        onClick = onClick,
        modifier = Modifier.size(56.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = item.label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = color
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FA))
    ) {
        BottomNavigationBar(
            currentRoute = "beranda",
            onNavigate = {},
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}