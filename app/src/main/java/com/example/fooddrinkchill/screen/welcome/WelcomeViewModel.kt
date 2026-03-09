package com.example.fooddrinkchill.screen.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddrinkchill.data.repository.PreferenceRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


class WelcomeViewModel(private val preferenceRepository: PreferenceRepository) : ViewModel() {
    private val _shareFlow = MutableSharedFlow<WelcomeSideEffect>()
    val shareFlow: SharedFlow<WelcomeSideEffect> = _shareFlow

    fun onEvent(event: WelcomeEvent) {
        when (event) {
            WelcomeEvent.NavigateToAuth -> completeWelcome()
            is WelcomeEvent.ScrollToPage -> next(event.index)
        }
    }

    private fun completeWelcome() {
        viewModelScope.launch {
            preferenceRepository.setFirstTimeLaunch(false)
            _shareFlow.emit(WelcomeSideEffect.CompleteWelcome)
        }
    }

    private fun next(index: Int) {
        viewModelScope.launch {
            _shareFlow.emit(WelcomeSideEffect.Next(index))
        }
    }
}

sealed class WelcomeSideEffect {

    data class Next(val index: Int) : WelcomeSideEffect()

    data object CompleteWelcome : WelcomeSideEffect()

}

sealed class WelcomeEvent {
    data class ScrollToPage(val index: Int) : WelcomeEvent()
    object NavigateToAuth : WelcomeEvent()
}
