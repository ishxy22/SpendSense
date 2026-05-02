package com.example.spendsense

import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendsense.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendSenseTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = BackgroundColor
                ) { innerPadding ->
                    LandingScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LandingScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    // --- INFINITE ALTERNATING SCREEN GLOW ---
    val infiniteTransition = rememberInfiniteTransition(label = "screen_glow")
    
    // Blue fades in and out (Seconds 0 to 4)
    val blueAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 8000
                0f at 0 // Start invisible
                0.5f at 2000 // Blue peaks at 50% opacity (Visible but not overpowering)
                0f at 4000 // Fades out completely by 4 seconds
                0f at 8000 // Stays invisible while pink plays
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "blue_alpha"
    )

    // Pink fades in and out (Seconds 4 to 8)
    val pinkAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 8000
                0f at 0 // Stays invisible while blue plays
                0f at 4000 // Starts fading in at 4 seconds
                0.5f at 6000 // Pink peaks at 50% opacity
                0f at 8000 // Fades out completely by 8 seconds
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "pink_alpha"
    )

    // We use a Box here like a RelativeLayout so we can layer the glows BEHIND the scrollable content
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // --- AMBIENT FULL-SCREEN GLOWS ---
        
        // Full screen blue glow (Radial from center so it looks like a soft ambient light)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AccentBlue.copy(alpha = blueAlpha),
                            Color.Transparent
                        )
                    )
                )
        )
        
        // Full screen pink glow (Radial from center)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AccentPink.copy(alpha = pinkAlpha),
                            Color.Transparent
                        )
                    )
                )
        )

        // --- MAIN SCROLLABLE CONTENT ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- NAVBAR ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavBackground, RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF404040), RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SpendSense",
                    fontSize = 18.sp,
                    color = Color(0xFFE6E6E6),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                
                Button(
                    onClick = { /* TODO: Sign In */ },
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonAccountGray),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        text = "Sign In", 
                        color = Color(0xFF1A1A1A), 
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            // --- HERO SECTION ---
            Text(
                text = "Smart Payments,\nPowered by AI.",
                fontSize = 42.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Serif,
                color = TextColor,
                lineHeight = 48.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- HERO BUTTONS ---
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Gradient Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(AccentPink, AccentBlue)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Button(
                        onClick = { /* TODO: Get Started */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Text("Get Started", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = { /* TODO: Demo */ },
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonAccountGray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text("View Demo", color = Color(0xFF1A1A1A), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(80.dp))

            // --- SCREENSHOTS PLACEHOLDER ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF262626), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF333333), RoundedCornerShape(12.dp))
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(), 
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.weight(1f).height(200.dp).background(CardBackground, RoundedCornerShape(8.dp)))
                    Box(modifier = Modifier.weight(1f).height(200.dp).background(CardBackground, RoundedCornerShape(8.dp)))
                    Box(modifier = Modifier.weight(1f).height(200.dp).background(CardBackground, RoundedCornerShape(8.dp)))
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF030303)
@Composable
fun LandingScreenPreview() {
    SpendSenseTheme {
        LandingScreen()
    }
}