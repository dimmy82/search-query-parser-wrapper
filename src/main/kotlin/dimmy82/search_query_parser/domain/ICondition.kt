package dimmy82.search_query_parser.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue
import dimmy82.search_query_parser.domain.condition.*

interface ICondition {
    companion object {
        private val JACKSON_MAPPER =
            jacksonMapperBuilder().configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false).build()

        internal fun parseConditionFromJsonString(conditionJsonString: String) =
            JACKSON_MAPPER.readValue<ConditionJson>(conditionJsonString).let(::parseConditionFromJson)

        private fun parseConditionFromJson(conditionJson: ConditionJson): ICondition {
            // KeywordCondition
            conditionJson.keyword?.let { return KeywordCondition(it) }

            // PhraseKeywordCondition
            conditionJson.phraseKeyword?.let { return PhraseKeywordCondition(it) }

            // NotCondition
            conditionJson.not?.let { return NotCondition(parseConditionFromJson(it)) }

            // AndCondition
            conditionJson.and?.let { conditions ->
                return OperatorCondition(
                    Operator.And,
                    conditions.map { parseConditionFromJson(it) })
            }

            // OrCondition
            conditionJson.or?.let { conditions ->
                return OperatorCondition(
                    Operator.Or,
                    conditions.map { parseConditionFromJson(it) })
            }

            return NoneCondition()
        }
    }

    fun toString(intent: Int = 0): String
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
}
