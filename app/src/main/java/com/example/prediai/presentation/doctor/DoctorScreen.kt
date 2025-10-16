package com.example.prediai.presentation.doctor

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.prediai.presentation.common.TopBar
import kotlinx.coroutines.delay

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DoctorScreen(
    navController: NavController,
    url: String = "https://www.alodokter.com/cari-dokter/dokter-endokrin"
) {
    var showWebView by remember { mutableStateOf(false) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    LaunchedEffect(Unit) {
        delay(3000)
        showWebView = true
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = "Cari Dokter",
            subtitle = "Search Doctor",
            onBackClick = { navController.popBackStack() }
        )

        Box(modifier = Modifier.fillMaxSize()) {

            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewInstance = this
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                view?.evaluateJavascript(
                                    """
                                    (function() {
                                        var topbar = document.getElementById('top-navbar-view');
                                        if (topbar) topbar.style.display = 'none';
                                        
                                        var footer = document.getElementById('footerMobile');
                                        if (footer) footer.style.display = 'none';
                                        
                                        var banners = document.querySelectorAll('[id*="banner"], .bottom-banner, .side-banner');
                                        banners.forEach(function(b){ b.style.display = 'none'; });
                                        
                                        var detail = document.querySelector('.speciality-detail-container');
                                        if (detail) detail.style.display = 'none';
                                        
                                        var popular = document.querySelector('.popular-search');
                                        if (popular) popular.style.display = 'none';
                                        
                                        document.body.style.paddingTop = '0px';
                                        document.body.style.marginTop = '0px';
                                        
                                        var wrappers = document.querySelectorAll('div, main, section');
                                        wrappers.forEach(function(el) {
                                            el.style.paddingTop = '0px';
                                            el.style.marginTop = '0px';
                                        });
                                        
                                        window.scrollTo(0, 0);
                                    })();
                                    """.trimIndent(),
                                    null
                                )
                            }
                        }

                        loadUrl(url)
                    }
                },
                update = { webView ->
                    webView.loadUrl(url)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (showWebView) Modifier else Modifier.background(Color.White)
                    )
            )

            if (!showWebView) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF00B4A3))
                }
            }
        }
    }
}
