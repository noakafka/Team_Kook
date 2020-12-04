package com.example.teamkook

class ReviewInfo (var ID : String, var link : String,  var content : String, var rating : Float, var spicy : Float, var title : String) {
    constructor() : this("", "", "nocontent", 0.toFloat(), 0.toFloat(), "")
}