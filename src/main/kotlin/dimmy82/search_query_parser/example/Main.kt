package dimmy82.search_query_parser.example

import dimmy82.search_query_parser.lib.SearchQueryParserWrapper.Companion.SEARCH_QUERY_PARSER_WRAPPER

fun main(args: Array<String>) {
    var start = System.currentTimeMillis()
    println("===> result: ${SEARCH_QUERY_PARSER_WRAPPER.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
    start = System.currentTimeMillis()
    println("===> result: ${SEARCH_QUERY_PARSER_WRAPPER.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
    start = System.currentTimeMillis()
    println("===> result: ${SEARCH_QUERY_PARSER_WRAPPER.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
    start = System.currentTimeMillis()
    println("===> result: ${SEARCH_QUERY_PARSER_WRAPPER.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
    start = System.currentTimeMillis()
    println("===> result: ${SEARCH_QUERY_PARSER_WRAPPER.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
    start = System.currentTimeMillis()
    println("===> result: ${SEARCH_QUERY_PARSER_WRAPPER.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
    start = System.currentTimeMillis()
    println("===> result: ${SEARCH_QUERY_PARSER_WRAPPER.parse("A and B")}, parse time: ${System.currentTimeMillis() - start}")
}
