package com.example.sb3jooqtest.repository

import com.example.sb3jooqtest.entity.TestObject
import org.example.sb3JooqTest.jooq.pgsql.tables.references.TEST_TABLE
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JooqRepository(
    private val jooq: DSLContext
) {
    fun insert(id: UUID) {
        jooq.insertInto(TEST_TABLE)
            .set(TEST_TABLE.ID, id)
            .set(TEST_TABLE.MESSAGE, "inserted")
            .execute()
        check(jooq.selectFrom(TEST_TABLE).where(TEST_TABLE.ID.eq(id)).fetch().size == 1)
    }

    fun selectOne(): TestObject {
        val result = jooq.selectFrom(TEST_TABLE).fetch()
        check(result.isNotEmpty)
        return result[0].let { TestObject(id = it.id!!, message = it.message!!) }
    }

    fun update(id: UUID) {
        jooq.update(TEST_TABLE)
            .set(TEST_TABLE.MESSAGE, "updated")
            .where(TEST_TABLE.ID.eq(id))
            .execute()
        check(jooq.selectFrom(TEST_TABLE).where(TEST_TABLE.ID.eq(id)).fetchOne()?.message == "updated")
    }

    fun delete(id: UUID) {
        jooq.deleteFrom(TEST_TABLE)
            .where(TEST_TABLE.ID.eq(id))
            .execute()
        check(jooq.selectFrom(TEST_TABLE).where(TEST_TABLE.ID.eq(id)).fetchOne() == null)
    }
}