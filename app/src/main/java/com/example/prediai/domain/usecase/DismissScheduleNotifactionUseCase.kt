package com.example.prediai.domain.usecase

import com.example.prediai.domain.repository.ScheduleRepository
import javax.inject.Inject

class DismissScheduleNotificationUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(scheduleId: String) {
        repository.dismissScheduleNotification(scheduleId)
    }
}