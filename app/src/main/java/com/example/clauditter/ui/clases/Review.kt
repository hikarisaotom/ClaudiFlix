package com.example.clauditter.ui.clases

class Review (
   val authorDetails:AuthorDetails,
   val content:String
) //this is based in the author details field.


class AuthorDetails(
    val name:String,
    val username:String,
    var avatar_path:String,
    val rating:Int?
)
