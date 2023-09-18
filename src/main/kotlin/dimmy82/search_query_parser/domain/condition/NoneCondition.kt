package dimmy82.search_query_parser.domain.condition

import dimmy82.search_query_parser.domain.ICondition

class NoneCondition : ICondition {
    override fun hashCode() = javaClass.hashCode()

    override fun equals(other: Any?) = other?.let { return it is NoneCondition } ?: false

    override fun toString() = toString(0)

    override fun toString(intent: Int) = "${"  ".repeat(intent)}none"
}
