package de.davidsparkles.logevents

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

class LogEventWithMessage (
                            @JsonProperty("timestamp") override val timestamp: Long,
                            @JsonProperty("logFile") override val logFile: String,
                            @JsonProperty("server") override val server: String,
                            @JsonProperty("severity") override val severity: String,
                            @JsonProperty("loggingClass") override val loggingClass: String,
                            @JsonProperty("lineNumber") override val lineNumber: Int,
                            @JsonProperty("message") val message: String
                          ) extends AbstractLogEvent {}
