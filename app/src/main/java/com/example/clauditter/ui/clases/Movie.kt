package com.example.clauditter.ui.clases
import kotlin.collections.List

data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    var genre_ids: List<Int> =listOf<Int>(),
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
){
     var cast: List<Person> =listOf<Person>()
    override fun toString(): String {
        return "id: $id title: $title"
    }
}
