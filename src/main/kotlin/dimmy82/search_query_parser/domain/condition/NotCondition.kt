package dimmy82.search_query_parser.domain.condition

import dimmy82.search_query_parser.domain.ICondition

data class NotCondition(val value: ICondition) : ICondition {
    override fun toString() = toString(0)

    override fun toString(intent: Int) =
        "${"  ".repeat(intent)}not: (\n${value.toString(intent + 1)}\n${"  ".repeat(intent)})"
}
