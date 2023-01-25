package com.example.sb3jooqtest

import com.example.sb3jooqtest.entity.TestObject
import com.example.sb3jooqtest.service.JooqService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.example.sb3JooqTest.jooq.pgsql.tables.references.TEST_TABLE
import org.jooq.DSLContext
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class Sb3JooqTestApplicationTests(
    val jooq: DSLContext,
    val service: JooqService
) : ShouldSpec() {
    override fun extensions() = listOf(SpringExtension)

    init {
        beforeTest {
            truncate()
        }

        should("insert and persist a record") {
            service.insert()
            getTestObject().message shouldBe "inserted"
        }

        should("not persist a record when an exception is thrown during insertion") {
            shouldThrow<RuntimeException> {
                service.rollbackWhileInsertion()
            }
            jooq.selectFrom(TEST_TABLE).fetch().shouldBeEmpty()
        }

        should("not update a record when an exception is thrown during updating") {
            service.insert()
            shouldThrow<RuntimeException> {
                service.rollbackWhileUpdating()
            }
            with(getTestObject()) {
                message shouldNotBe "updated"
                message shouldBe "inserted"
            }
        }

        should("not delete a record when an exception is thrown during deletion") {
            service.insert()
            shouldThrow<RuntimeException> {
                service.rollbackWhileDeletion()
            }
            jooq.selectFrom(TEST_TABLE).fetch().shouldNotBeEmpty()
            getTestObject().message shouldBe "inserted"
        }
    }

    fun truncate() {
        jooq.truncate(TEST_TABLE).execute()
    }

    fun getTestObject() = jooq.selectFrom(TEST_TABLE).fetchOne()!!.let { TestObject(it.id!!, it.message!!) }
}
