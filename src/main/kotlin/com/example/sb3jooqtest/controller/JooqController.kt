package com.example.sb3jooqtest.controller

import com.example.sb3jooqtest.service.JooqService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class JooqController(
    private val jooqService: JooqService
) {
    @PostMapping("/normal-case")
    fun normalCase() {
        jooqService.insert()
    }

    @PostMapping("/insert-rollback-case")
    fun insertRollbackCase() {
        jooqService.rollbackWhileInsertion()
    }

    @PostMapping("/update-rollback-case")
    fun updateRollbackCase() {
        jooqService.rollbackWhileUpdating()
    }

    @PostMapping("/delete-rollback-case")
    fun deleteRollbackCase() {
        jooqService.rollbackWhileDeletion()
    }
}