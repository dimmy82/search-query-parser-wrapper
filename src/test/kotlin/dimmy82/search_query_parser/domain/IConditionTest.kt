package dimmy82.search_query_parser.domain

import dimmy82.search_query_parser.domain.condition.*
import dimmy82.search_query_parser.lib.SearchQueryParserWrapper.Companion.SEARCH_QUERY_PARSER
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class IConditionTest {
    @Test
    fun testEmptyCondition() {
        val conditionJsonString = SEARCH_QUERY_PARSER.parseQueryToConditionJsonString("")
        val condition = ICondition.parseConditionFromJsonString(conditionJsonString)
        assertEquals(NoneCondition(), condition)
        println("===================\n$condition")
    }

    @Test
    fun testKeywordConditionOnly() {
        val conditionJsonString = SEARCH_QUERY_PARSER.parseQueryToConditionJsonString("keyword")
        val condition = ICondition.parseConditionFromJsonString(conditionJsonString)
        assertEquals(KeywordCondition("keyword"), condition)
        println("===================\n$condition")
    }

    @Test
    fun testPhraseKeywordConditionOnly() {
        val conditionJsonString = SEARCH_QUERY_PARSER.parseQueryToConditionJsonString("\"phrase keyword\"")
        val condition = ICondition.parseConditionFromJsonString(conditionJsonString)
        assertEquals(PhraseKeywordCondition("phrase keyword"), condition)
        println("===================\n$condition")
    }

    @Test
    fun testNotConditionOnKeywordOnly() {
        val conditionJsonString = SEARCH_QUERY_PARSER.parseQueryToConditionJsonString("-keyword")
        val condition = ICondition.parseConditionFromJsonString(conditionJsonString)
        assertEquals(NotCondition(KeywordCondition("keyword")), condition)
        println("===================\n$condition")
    }

    @Test
    fun testNotConditionOnPhraseKeywordOnly() {
        val conditionJsonString = SEARCH_QUERY_PARSER.parseQueryToConditionJsonString("-\"phrase keyword\"")
        val condition = ICondition.parseConditionFromJsonString(conditionJsonString)
        assertEquals(NotCondition(PhraseKeywordCondition("phrase keyword")), condition)
        println("===================\n$condition")
    }

    @Test
    fun testSpaceIsAndCondition() {
        val conditionJsonString = SEARCH_QUERY_PARSER.parseQueryToConditionJsonString("keyword \"phrase keyword\"")
        val condition = ICondition.parseConditionFromJsonString(conditionJsonString)
        assertEquals(
            OperatorCondition(
                Operator.And,
                listOf(KeywordCondition("keyword"), PhraseKeywordCondition("phrase keyword"))
            ), condition
        )
        println("===================\n$condition")
    }

    @Test
    fun testAndCondition() {
        val conditionJsonString = SEARCH_QUERY_PARSER.parseQueryToConditionJsonString("\"phrase keyword\" and keyword")
        val condition = ICondition.parseConditionFromJsonString(conditionJsonString)
        assertEquals(
            OperatorCondition(
                Operator.And,
                listOf(PhraseKeywordCondition("phrase keyword"), KeywordCondition("keyword"))
            ), condition
        )
        println("===================\n$condition")
    }

    @Test
    fun testOrCondition() {
        val conditionJsonString = SEARCH_QUERY_PARSER.parseQueryToConditionJsonString("keyword or \"phrase keyword\"")
        val condition = ICondition.parseConditionFromJsonString(conditionJsonString)
        assertEquals(
            OperatorCondition(
                Operator.Or,
                listOf(KeywordCondition("keyword"), PhraseKeywordCondition("phrase keyword"))
            ), condition
        )
        println("===================\n$condition")
    }

    @Test
    fun testAndConditionIsPriorityOverOrCondition() {
        val conditionJsonString =
            SEARCH_QUERY_PARSER.parseQueryToConditionJsonString("keyword or \"phrase keyword\" and keyword2")
        val condition = ICondition.parseConditionFromJsonString(conditionJsonString)
        assertEquals(
            OperatorCondition(
                Operator.Or,
                listOf(
                    KeywordCondition("keyword"),
                    OperatorCondition(
                        Operator.And,
                        listOf(PhraseKeywordCondition("phrase keyword"), KeywordCondition("keyword2"))
                    )
                )
            ), condition
        )
        println("===================\n$condition")
    }

    @Test
    fun testBracketsArePriorityOverOthers() {
        val conditionJsonString =
            SEARCH_QUERY_PARSER.parseQueryToConditionJsonString("(keyword or \"phrase keyword\") and keyword2")
        val condition = ICondition.parseConditionFromJsonString(conditionJsonString)
        assertEquals(
            OperatorCondition(
                Operator.And,
                listOf(
                    OperatorCondition(
                        Operator.Or,
                        listOf(KeywordCondition("keyword"), PhraseKeywordCondition("phrase keyword"))
                    ),
                    KeywordCondition("keyword2"),
                )
            ), condition
        )
        println("===================\n$condition")
    }

    @Test
    fun testNotConditionOnBrackets() {
        val conditionJsonString =
            SEARCH_QUERY_PARSER.parseQueryToConditionJsonString("-(keyword or \"phrase keyword\") and keyword2")
        val condition = ICondition.parseConditionFromJsonString(conditionJsonString)
        assertEquals(
            OperatorCondition(
                Operator.And,
                listOf(
                    NotCondition(
                        OperatorCondition(
                            Operator.Or,
                            listOf(KeywordCondition("keyword"), PhraseKeywordCondition("phrase keyword"))
                        )
                    ),
                    KeywordCondition("keyword2"),
                )
            ), condition
        )
        println("===================\n$condition")
    }

    @Test
    fun testMultiBrackets() {
        val conditionJsonString =
            SEARCH_QUERY_PARSER.parseQueryToConditionJsonString("-(keyword or \"phrase keyword\") and (\"phrase keyword 2\" or keyword2)")
        val condition = ICondition.parseConditionFromJsonString(conditionJsonString)
        assertEquals(
            OperatorCondition(
                Operator.And,
                listOf(
                    NotCondition(
                        OperatorCondition(
                            Operator.Or,
                            listOf(KeywordCondition("keyword"), PhraseKeywordCondition("phrase keyword"))
                        )
                    ),
                    OperatorCondition(
                        Operator.Or,
                        listOf(PhraseKeywordCondition("phrase keyword 2"), KeywordCondition("keyword2"))
                    ),
                )
            ), condition
        )
        println("===================\n$condition")
    }
}