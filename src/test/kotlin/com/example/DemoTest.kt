package com.example
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import io.kotest.core.spec.style.StringSpec
import io.micronaut.core.annotation.Introspected
import java.util.*

@MicronautTest
class DemoTest(
    private val application: EmbeddedApplication<*>,
    private val objectMapper: ObjectMapper
): StringSpec({

    "test the server is running" {
        assert(application.isRunning)
    }

    "test deserialization without introspect" {
        val output1 = objectMapper.readValue("{\"field1\": \"value1\", \"field2\": \"value2\"}", TestNoIntrospection::class.java)
        val output2 = objectMapper.readValue("{\"field1\": null, \"field2\": \"value2\"}", TestNoIntrospection::class.java)
        val output3 = objectMapper.readValue("{}", TestNoIntrospection::class.java)

        assert(output1.field1?.get() == "value1")
        assert(output1.field2?.get() == "value2")

        assert(output2.field1!!.isEmpty)
        assert(output2.field2?.get() == "value2")

        assert(output3.field1 == null)
        assert(output3.field2 == null)
    }

    "test deserialization with introspect" {
        val output1 = objectMapper.readValue("{\"field1\": \"value1\", \"field2\": \"value2\"}", TestWithIntrospection::class.java)
        val output2 = objectMapper.readValue("{\"field1\": null, \"field2\": \"value2\"}", TestWithIntrospection::class.java)
        val output3 = objectMapper.readValue("{}", TestWithIntrospection::class.java)

        assert(output1.field1?.get() == "value1")
        assert(output1.field2?.get() == "value2")

        assert(output2.field1!!.isEmpty)
        assert(output2.field2?.get() == "value2")

        assert(output3.field1 == null)
        assert(output3.field2 == null)
    }
}) {
    data class TestNoIntrospection(
        val field1: Optional<String>? = null,
        val field2: Optional<String>? = null
    )

    @Introspected
    data class TestWithIntrospection(
        val field1: Optional<String>? = null,
        val field2: Optional<String>? = null
    )
}
