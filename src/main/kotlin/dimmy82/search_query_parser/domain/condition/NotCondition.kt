package dimmy82.search_query_parser.domain.condition

import dimmy82.search_query_parser.domain.ICondition

data class NotCondition(val value: ICondition) : ICondition