package com.example.teamkook

data class search_json(var kind : String, var etag : String, var nextPageToken : String, var regionCode : String, var PageInfo : pageinfo, var items : ArrayList<result_items>) {

}