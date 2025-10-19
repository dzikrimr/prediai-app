package com.example.prediai.domain.usecase

import com.example.prediai.domain.repository.ScheduleRepository
import javax.inject.Inject

class DeleteScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(scheduleId: String) {
        repository.deleteSchedule(scheduleId)
    }
}