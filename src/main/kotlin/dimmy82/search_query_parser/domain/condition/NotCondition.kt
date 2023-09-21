package dimmy82.search_query_parser.domain.condition

import dimmy82.search_query_parser.domain.ICondition

data class NotCondition(val value: ICondition) : ICondition {
    override fun toString() = toString("", 0)

    override fun toString(prefix: String, intent: Int): String =
        "${prefix}${"  ".repeat(intent)}not: (\n${value.toString(prefix, intent + 1)}\n${prefix}${"  ".repeat(intent)})"
}
