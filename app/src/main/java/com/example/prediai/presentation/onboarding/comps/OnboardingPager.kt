package com.example.prediai.presentation.onboarding.comps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import androidx.compose.ui.text.style.TextAlign

data class OnboardingPage(
    val image: Int,
    val title: String,
    val description: String
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingPager(
    pagerState: PagerState,
    pages: List<OnboardingPage>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = pages[page].image),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ✅ Title
                Text(
                    text = pages[page].title,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ✅ Description
                Text(
                    text = pages[page].description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4B5563),
                    modifier = Modifier.padding(horizontal = 32.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        // ✅ Dots Indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                val color =
                    if (pagerState.currentPage == index) Color(0xFF00B4A3) else Color(0xFFE2E8F0)
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}
