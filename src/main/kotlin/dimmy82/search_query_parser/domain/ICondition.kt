package dimmy82.search_query_parser.domain

import com.fasterxml.jackson.annotation.JsonInclude
import dimmy82.search_query_parser.domain.condition.NoneCondition

interface ICondition {
    companion object {
        internal fun parseConditionFromJson(conditionJson: String): ICondition {
            return NoneCondition()
        }
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
private data class ConditionJson(
    val keyword: String?,
    val phraseKeyword: String?,
    val not: ConditionJson?,
    val and: List<ConditionJson>?,
    val or: List<ConditionJson>?,
) {
    private constructor() : this(null, null, null, null, null)

    fun isEmpty() = listOfNotNull(keyword, phraseKeyword, not, and, or).isEmpty()
}
