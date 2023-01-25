package com.example.sb3jooqtest.service

import com.example.sb3jooqtest.repository.JooqRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class JooqService(
    private val jooqRepository: JooqRepository
) {
    @Transactional
    fun insert() {
        val id = UUID.randomUUID()
        jooqRepository.insert(id)
    }

    @Transactional
    fun rollbackWhileInsertion() {
        val id = UUID.randomUUID()
        jooqRepository.insert(id)
        throw RuntimeException("exception")
    }

    @Transactional
    fun rollbackWhileUpdating() {
        val testObject = jooqRepository.selectOne()
        jooqRepository.update(testObject.id)
        throw RuntimeException("exception")
    }

    @Transactional
    fun rollbackWhileDeletion() {
        val testObject = jooqRepository.selectOne()
        jooqRepository.delete(testObject.id)
        throw RuntimeException("exception")
    }
}