package com.example.prediai.domain.usecase

import com.example.prediai.domain.repository.ScheduleRepository
import javax.inject.Inject

class GetSchedulesUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {
    operator fun invoke() = repository.getAllSchedules()
}