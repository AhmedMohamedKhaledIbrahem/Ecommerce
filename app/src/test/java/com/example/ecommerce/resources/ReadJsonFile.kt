package com.example.ecommerce.resources

import java.io.File
private const val absolutePath :String= "D:\\AndroidStudioProjects\\ECommerce\\app\\src\\test\\java\\com\\example\\ecommerce\\resources\\"
fun fixture(jsonName: String): String =
    File(absolutePath+jsonName).readText()