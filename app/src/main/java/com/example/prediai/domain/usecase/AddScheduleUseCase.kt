package com.example.prediai.domain.usecase

import com.example.prediai.domain.model.ScheduleItem
import com.example.prediai.domain.repository.ScheduleRepository
import javax.inject.Inject

class AddScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(scheduleItem: ScheduleItem) {
        repository.addSchedule(scheduleItem)
    }
}