package dimmy82.kotlin

import dimmy82.kotlin.SearchQueryParserWrapper.Companion.SEARCH_QUERY_PARSER_WRAPPER

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
