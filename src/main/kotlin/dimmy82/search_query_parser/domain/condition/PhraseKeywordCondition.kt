package dimmy82.search_query_parser.domain.condition

import dimmy82.search_query_parser.domain.ICondition

data class PhraseKeywordCondition(val value: String) : ICondition {
    override fun toString() = toString("", 0)

    override fun toString(prefix: String, intent: Int): String =
        "${prefix}${"  ".repeat(intent)}phraseKeyword: \"$value\""
}
