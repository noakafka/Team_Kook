package com.example.teamkook

data class snippet(var publishedAt : String, var channelId : String, var title : String, var description : String, var thumnails: thumnail, var channelTitle : String) {
    data class thumnail(var default :defaults) {
        data class defaults(var url : String, var width : Int, var height : Int) {
        }
    }
}


