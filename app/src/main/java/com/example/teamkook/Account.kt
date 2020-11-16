package com.example.teamkook

data class Account(
    var id:String, val pw:String, var age:Int, var gender:String,
    var vegan:Boolean, var allergy: ArrayList<String>?
) {
    constructor():this("noinfo","noinfo",0, "noinfo",false,null)
}