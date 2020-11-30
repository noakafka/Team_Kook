package com.example.teamkook

data class Food(val name:String,val comment:String,
           val peanut:Int,val milk:Int,val egg:Int,val crustacean:Int,val flour:Int,val peach:Int,val vegan:Int,
val breakfast:Int,val lunch:Int,val dinner:Int,val rain:Int, val snow:Int, val hot:Int, val cold:Int, val spicy:Int) {
    constructor():this("noname","nocomment",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
}