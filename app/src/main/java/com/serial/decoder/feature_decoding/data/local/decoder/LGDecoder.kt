package com.serial.decoder.feature_decoding.data.local.decoder

import com.serial.decoder.R
import com.serial.decoder.core.util.UIText
import com.serial.decoder.feature_decoding.data.local.entity.ManufactureEntity
import com.serial.decoder.feature_decoding.data.local.util.ManufactureData

class LGDecoder : Decoder, ManufactureData<String, Int, String, String> {

    override var countryMap: HashMap<String, Int> = HashMap()
    override val monthMap: HashMap<String, String> = HashMap()
    override val yearMap: HashMap<String, String> = HashMap()

    init {
        fillCountries()
        fillMonth()
        fillYear()
    }

    override fun isCorrectSerial(serial: String): Boolean {
        return serial.length == 12
    }

    override fun decodeSerial(serial: String): ManufactureEntity {
        val type = getTypeFromSerial(serial)
        val country = getCountryFromSerial(serial)
        val date = getDateFromSerial(serial)

        return ManufactureEntity(type, country, date)
    }

    override fun fillYear() {
        yearMap["9"] = "2009|2019|2029"
        yearMap["8"] = "2008|2018|2028"
        yearMap["7"] = "2007|2017|2027"
        yearMap["6"] = "2006|2016|2026"
        yearMap["5"] = "2005|2015|2025"
        yearMap["4"] = "2004|2014|2024"
        yearMap["3"] = "2003|2013|2023"
        yearMap["2"] = "2002|2012|2022"
        yearMap["1"] = "2001|2011|2021"
        yearMap["0"] = "2000|2010|2020"
    }

    override fun fillMonth() {
        monthMap["01"] = "01"
        monthMap["02"] = "02"
        monthMap["03"] = "03"
        monthMap["04"] = "04"
        monthMap["05"] = "05"
        monthMap["06"] = "06"
        monthMap["07"] = "07"
        monthMap["08"] = "08"
        monthMap["09"] = "09"
        monthMap["10"] = "10"
        monthMap["11"] = "11"
        monthMap["12"] = "12"
    }

    override fun fillCountries() {
        countryMap["RM"] = R.string.mexico
        countryMap["MX"] = R.string.mexico
        countryMap["MA"] = R.string.poland
        countryMap["WR"] = R.string.poland
        countryMap["IN"] = R.string.indonesia
        countryMap["RN"] = R.string.korea
        countryMap["KC"] = R.string.korea
        countryMap["RA"] = R.string.russia
        countryMap["ND"] = R.string.china
    }

    override fun getTypeFromSerial(serial: String): UIText {
        return UIText.StringResource(resId = R.string.tv) // Couldn't find specific rule, so I'm keeping it simple for now.
    }

    override fun getCountryFromSerial(serial: String): UIText {
        val countryTwoLetters = serial.substring(3, 5)
        if (countryMap.containsKey(countryTwoLetters)) return UIText.StringResource(resId = countryMap[countryTwoLetters]!!)

        return UIText.StringResource(R.string.couldnot_identify_country)
    }

    override fun getDateFromSerial(serial: String): UIText {
        val yearChar = serial.substring(0, 1)
        val monthTwoLetter = serial.substring(1, 3)

        var yearValue = UNDEFINED_YEAR
        var monthValue = UNDEFINED_MONTH
        if (yearMap.containsKey(yearChar)) yearValue = yearMap[yearChar]!!
        if (monthMap.containsKey(monthTwoLetter)) monthValue = monthMap[monthTwoLetter]!!


        return UIText.DynamicString("$monthValue/$yearValue")

    }

}