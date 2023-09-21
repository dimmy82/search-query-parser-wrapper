package dimmy82.search_query_parser.domain.condition

import dimmy82.search_query_parser.domain.ICondition

data class OperatorCondition(val operator: Operator, val values: List<ICondition>) : ICondition {
    override fun toString() = toString("", 0)

    override fun toString(prefix: String, intent: Int): String =
        "${prefix}${"  ".repeat(intent)}${operator.toString().lowercase()}: (\n${
            values.joinToString(separator = ",\n") { it.toString(prefix, intent + 1) }
        }\n${prefix}${"  ".repeat(intent)})"
}

enum class Operator {
    And, Or
}
