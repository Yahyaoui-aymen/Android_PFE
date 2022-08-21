package com.example.android.data

import android.content.Context
import com.example.android.R
import com.example.android.data.responses.Category

data class CategoryData(var context: Context) {
    val categories: List<Category> = listOf(
        Category(
            1,
            //"Médecine",
            context.getString(R.string.medecine),
            R.drawable.medecine
        ),
        Category(
            2,
            context.getString(R.string.plomberie),
            R.drawable.plomberie
        ),
        Category(
            3,
            context.getString(R.string.mecanique),
            R.drawable.mecanique
        ),
        Category(
            4,
            context.getString(R.string.high_tech),
            R.drawable.high_tech
        ),
        Category(
            5,
            context.getString(R.string.coiffure),
            R.drawable.coiffure
        ),
        Category(
            6,
            context.getString(R.string.opticien),
            R.drawable.optique
        )
    )


    val categoryConverter = mapOf(
        "Medecine" to "MEDECINE",
        "mecanique" to "MECANIQUE",
        "Plomberie" to "PLOMBERIE",
        "High Tech" to "HIGH_TECH",
        "Coiffure" to "COIFFURE",
        "Opticien" to "OPTICIEN",
        "الطب" to "MEDECINE",
        "السباكة" to "PLOMBERIE",
        "ميكانيكي" to "MECANIQUE",
        "تقنية" to "HIGH_TECH",
        "حلاقة" to "COIFFURE",
        "النظاراتي" to "OPTICIEN"
    )
}
