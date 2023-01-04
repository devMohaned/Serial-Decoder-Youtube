package com.serial.decoder.feature_decoding.data.local.decoder

import androidx.compose.ui.res.stringResource
import com.serial.decoder.R
import com.serial.decoder.core.util.UIText
import com.serial.decoder.feature_decoding.data.local.entity.ManufactureEntity
import com.serial.decoder.feature_decoding.data.local.util.ManufactureData

const val TV_TYPE = '3'

class SamsungDecoder : Decoder, ManufactureData<Char, Int, Char, String> {

    override val countryMap: HashMap<Char, Int> = HashMap()
    override val monthMap: HashMap<Char, String> = HashMap()
    override val yearMap: HashMap<Char, String> = HashMap()

    init {
        fillCountries()
        fillMonth()
        fillYear()
    }

    override fun isCorrectSerial(serial: String): Boolean {
        return serial.length == 14 || serial.length == 15
    }

    override fun decodeSerial(serial: String): ManufactureEntity {
        val type = getTypeFromSerial(serial)
        val country = getCountryFromSerial(serial)
        val date = getDateFromSerial(serial)

        return ManufactureEntity(type, country, date)
    }

    override fun fillYear() {
        yearMap['R'] = "2001"
        yearMap['T'] = "2002"
        yearMap['W'] = "2003"
        yearMap['X'] = "2004"
        yearMap['Y'] = "2005"
        yearMap['A'] = "2006|2021"
        yearMap['L'] = "2006"
        yearMap['P'] = "2007"
        yearMap['Q'] = "2008"
        yearMap['S'] = "2009"
        yearMap['Z'] = "2010"
        yearMap['B'] = "2011|2022"
        yearMap['C'] = "2012|2023"
        yearMap['D'] = "2013"
        yearMap['E'] = "2014"
        yearMap['G'] = "2015"
        yearMap['H'] = "2016"
        yearMap['J'] = "2017"
        yearMap['K'] = "2018"
        yearMap['M'] = "2019"
        yearMap['N'] = "2020"
    }

    override fun fillMonth() {
        monthMap['1'] = "01"
        monthMap['2'] = "02"
        monthMap['3'] = "03"
        monthMap['4'] = "04"
        monthMap['5'] = "05"
        monthMap['6'] = "06"
        monthMap['7'] = "07"
        monthMap['8'] = "08"
        monthMap['9'] = "09"
        monthMap['A'] = "10"
        monthMap['B'] = "11"
        monthMap['C'] = "12"
    }

    override fun fillCountries() {
        countryMap['1'] = R.string.korea
        countryMap['3'] = R.string.korea
        countryMap['4'] = R.string.romania
        countryMap['8'] = R.string.india
        countryMap['C'] = R.string.mexico
        countryMap['H'] = R.string.hungary
        countryMap['L'] = R.string.russia
        countryMap['M'] = R.string.malaysia
        countryMap['N'] = R.string.india
        countryMap['S'] = R.string.slovenia
        countryMap['W'] = R.string.china
    }

    override fun getTypeFromSerial(serial: String): UIText {
        val typeChar = serial[4]
        return if (typeChar == TV_TYPE) UIText.StringResource(R.string.tv)
        else UIText.StringResource(R.string.couldnot_identify_type)
    }

    override fun getCountryFromSerial(serial: String): UIText {
        val countryChar = serial[5]
        if (countryMap.containsKey(countryChar)) return UIText.StringResource(resId = countryMap[countryChar]!!)


        return UIText.StringResource(R.string.couldnot_identify_country)
    }

    override fun getDateFromSerial(serial: String): UIText {
        val yearChar = serial[7]
        val monthChar = serial[8]

        var yearValue = UNDEFINED_YEAR
        var monthValue = UNDEFINED_MONTH
        if (yearMap.containsKey(yearChar)) yearValue = yearMap[yearChar]!!
        if (monthMap.containsKey(monthChar)) monthValue = monthMap[monthChar]!!

        return UIText.DynamicString("$monthValue/$yearValue")
    }

}