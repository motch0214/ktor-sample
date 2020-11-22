package com.eighthours.sample.app.support

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import org.slf4j.Logger

class AdditionalCallLogging(
    private val log: Logger,
    private val filters: List<(ApplicationCall) -> Boolean>
) {

    class Configuration {

        internal val filters = mutableListOf<(ApplicationCall) -> Boolean>()

        /**
         * Log messages for calls matching a [predicate]
         */
        fun filter(predicate: (ApplicationCall) -> Boolean) {
            filters.add(predicate)
        }
    }

    companion object Feature : ApplicationFeature<Application, Configuration, AdditionalCallLogging> {

        override val key: AttributeKey<AdditionalCallLogging> = AttributeKey("Additional Call Logging")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): AdditionalCallLogging {
            val config = Configuration().apply(configure)

            val loggingPhase = PipelinePhase("AdditionalLogging")
            val feature = AdditionalCallLogging(pipeline.log, config.filters.toList())

            pipeline.insertPhaseBefore(ApplicationCallPipeline.Monitoring, loggingPhase)
            pipeline.intercept(loggingPhase) {
                if (feature.filters.isEmpty() || feature.filters.any { it(call) }) {
                    feature.logCalling(call)
                }
                proceed()
            }

            return feature
        }
    }

    private fun logCalling(call: ApplicationCall) {
        log.info("Calling ${call.request.toLogString()}")
    }
}
