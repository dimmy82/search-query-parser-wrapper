package dimmy82.search_query_parser.domain.condition

import dimmy82.search_query_parser.domain.ICondition

data class OperatorCondition(val operator: Operator, val values: List<ICondition>) : ICondition

enum class Operator {
    And, Or
}
