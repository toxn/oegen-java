package fr.cdbs.oegen.data.io

class SchemaValidationException(
    message: String,
    val details: List<String> = emptyList(),
    cause: Throwable? = null,
) : RuntimeException(message, cause)
