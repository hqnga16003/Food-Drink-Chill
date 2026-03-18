package com.example.fooddrinkchill.screen.welcome

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fooddrinkchill.R
import com.example.fooddrinkchill.ui.theme.OrangePrimary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.component.getScopeId
import org.koin.core.component.getScopeName
import kotlin.concurrent.thread

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = koinViewModel(), onFinished: () -> Unit
) {
    val coroutine = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })
    LaunchedEffect(Unit) {
        viewModel.shareFlow.collect {
            when (it) {
                is WelcomeSideEffect.Next -> {
                    coroutine.launch {
                        pagerState.animateScrollToPage(it.index)
                    }
                }
                WelcomeSideEffect.CompleteWelcome -> onFinished()
            }
        }
    }
    HorizontalPager(
        state = pagerState, modifier = Modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> WelcomeFirst {
                viewModel.onEvent(WelcomeEvent.ScrollToPage(1))
            }

            1 -> WelcomeSecond {
                viewModel.onEvent(WelcomeEvent.ScrollToPage(2))
            }

            2 -> WelcomeThird {
                viewModel.onEvent(WelcomeEvent.NavigateToAuth)
            }
        }
    }
}

@Composable
private fun WelcomeFirst(onNext: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.welcome_1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.welcome_title_1),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.welcome_subtitle_1),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))
            ButtonWelcome(
                titleButton = stringResource(R.string.welcome_button_start), onClick = onNext
            )
        }
    }
}

@Composable
private fun WelcomeSecond(onNext: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.welcome_2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    text = stringResource(R.string.welcome_title_2),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.welcome_subtitle_2),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))
                ButtonWelcome(
                    titleButton = stringResource(R.string.welcome_button_next), onClick = onNext
                )
            }
        }
    }
}

@Composable
private fun WelcomeThird(onFinished: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.welcome_3),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.welcome_title_3),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.welcome_subtitle_3),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))
            ButtonWelcome(
                titleButton = stringResource(R.string.welcome_button_finish), onClick = onFinished
            )
        }
    }
}

@Composable
private fun ButtonWelcome(titleButton: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = titleButton,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
