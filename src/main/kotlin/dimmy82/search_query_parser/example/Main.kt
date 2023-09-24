package dimmy82.search_query_parser.example

import dimmy82.search_query_parser.lib.SearchQueryParserWrapper.Companion.SEARCH_QUERY_PARSER

/**
 * main
 */
fun main(args: Array<String>) {
    SEARCH_QUERY_PARSER.parseQueryToCondition("A and B")
    SEARCH_QUERY_PARSER.parseQueryToCondition("\"A a\" or B")
    SEARCH_QUERY_PARSER.parseQueryToCondition("A and -B")
}
