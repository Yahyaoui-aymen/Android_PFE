package com.example.android.data

import android.content.Context
import com.example.android.R
import com.example.android.data.responses.Category

object DataSource {
    //val PHOTO_URL = "http://172.16.16.230:8080/photos/"
    val PHOTO_URL = "http://10.25.12.169:8080/photos/"

    val categories: List<Category> = listOf(
        Category(
            1,
            "Médecine",
            R.drawable.medecine
        ),
        Category(
            2,
            "Plomberie",
            R.drawable.plomberie
        ),
        Category(
            3,
            "Mecanique",
            R.drawable.mecanique
        ),
        Category(
            4,
            "High Tech",
            R.drawable.high_tech
        ),
        Category(
            5,
            "Coiffure",
            R.drawable.coiffure
        ),
        Category(
            6,
            "Opticien",
            R.drawable.optique
        )

    )

    val time = mapOf(
        "EIGHT" to "8H",
        "NINE" to "9H",
        "TEN" to "10h",
        "ELEVEN" to "11H",
        "TWELVE" to "12H",
        "THIRTEEN" to "13H",
        "FOURTEEN" to "14H",
        "FIFTEEN" to "15H",
        "SIXTEEN" to "16",
        "SEVENTEEN" to "17",
        "EIGHTEEN" to "18",
        "NINETEEN" to "19",
    )

    val city = mapOf(
        "Sousse" to R.array.sousse_city,
        "Tunis" to R.array.tunis_city,
        "Kasserine" to R.array.kasserine_city,
        "Nabeul" to R.array.nabeul_city,
        "Sfax" to R.array.sfax_city,
        "نابل" to R.array.nabeul_city,
        "تونس" to R.array.tunis_city,
        "سوسه" to  R.array.sousse_city,
        "صفاقس" to R.array.sfax_city,
        "القصرين" to R.array.kasserine_city,


    )

    fun getTime(time : String , context: Context): String {
        val convertedTime = when (time) {
            "EIGHT" -> context.getString(R.string.eight)
            "NINE" -> context.getString(R.string.nine)
            "TEN" -> context.getString(R.string.ten)
            "ELEVEN" -> context.getString(R.string.eleven)
            "TWELVE" -> context.getString(R.string.twelve)
            "THIRTEEN" -> context.getString(R.string.thirteen)
            "FOURTEEN" -> context.getString(R.string.fourteen)
            "FIFTEEN" -> context.getString(R.string.fifteen)
            "SIXTEEN" -> context.getString(R.string.sixteen)
            "SEVENTEEN" -> context.getString(R.string.seventeen)
            "EIGHTEEN" -> context.getString(R.string.eighteen)
            "NINETEEN" -> context.getString(R.string.nineteen)

            else -> {"erreur"}
        }
        return convertedTime
    }


    val governmentConverter = mapOf(
        "Nabeul" to "Nabeul",
        "Tunis" to "Tunis",
        "Sousse" to "Sousse",
        "Sfax" to "Sfax",
        "Kasserine" to "Kasserine",
        "نابل" to "Nabeul",
        "تونس" to "Tunis",
        "سوسه" to "Sousse",
        "صفاقس" to "Sfax",
        "القصرين" to "Kasserine",
    )

    val cityConverter = mapOf(
        "قربة" to "Korba",
        "قليبية" to "Kélibia",
        "فريانة" to "Feriana",
        "سبيطلة" to "Sbitla",
        "أكودة" to "Akouda",
        "سهلول" to "Sahloul",
        "ساقية الزيت" to "Sakiet ezzit",
        "محرس" to "Mahres",
        "دندان" to "Danden",
        "المروج" to "Mourouj",

        "Korba" to "Korba",
        "Kélibia" to "Kélibia",
        "Feriana" to "Feriana",
        "Sbitla" to "Sbitla",
        "Akouda" to "Akouda",
        "Sahloul" to "Sahloul",
        "Sakiet ezzit" to "Sakiet ezzit",
        "Mahres" to "Mahres",
        "Danden" to "Danden",
        "Mourouj" to "Mourouj",
    )

    val dayConverter = mapOf(
        "MONDAY" to R.string.monday,
        "TUESDAY" to R.string.tuesday,
        "WEDNESDAY" to R.string.wednesday,
        "THURSDAY" to R.string.thursday,
        "FRIDAY" to R.string.friday,
        "SATURDAY" to R.string.saturday,
        "SUNDAY" to R.string.sunday
    )

    val timeConverter = mapOf(
        "NINE" to 9,
        "TEN" to 10,
        "ELEVEN" to 11,
        "TWELVE" to 12,
        "THIRTEEN" to 13,
        "FOURTEEN" to 14,
        "FIFTEEN" to  15,
        "SIXTEEN" to 16,
        "SEVENTEEN" to 17,
        "EIGHTEEN" to 18,
        "NINETEEN" to 19,

        )
}