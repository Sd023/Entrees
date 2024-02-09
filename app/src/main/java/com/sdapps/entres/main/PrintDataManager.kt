package com.sdapps.entres.main

import android.content.Context
import android.util.Xml
import com.sdapps.entres.main.food.main.vm.CartViewModel
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringReader

class PrintDataManager(var vm : CartViewModel,var context: Context) {

    fun xmlRead(){

    }

    fun createPrintFile(){
        try{
            val xmlTemplate = readXmlTemplateFromAssets(context,"entrees_print.xml")
            val thermalBuilder = parseXmlTemplate(xmlTemplate)
            println(thermalBuilder.toString())
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    private fun readXmlTemplateFromAssets(context: Context,fileName: String): String {
        val inputStream: InputStream = context.assets.open(fileName)
        return try {
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            stringBuilder.toString()
        } catch (e: Exception) {
            "${e.printStackTrace()}"
        } finally {
            inputStream.close()
        }
    }

    private fun parseXmlTemplate(xmlTemplate: String): ThermalPrintStringBuilder {
        val thermalBuilder = ThermalPrintStringBuilder()
        val parser: XmlPullParser = Xml.newPullParser()

        parser.setInput(StringReader(xmlTemplate))

        var eventType = parser.eventType
        var currentTagName = ""

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    val tagName = parser.name
                    val attributeName = parser.getAttributeValue(null, "name")
                    val attributeValue = parser.getAttributeValue(null, "text") ?: ""

                    when (tagName) {
                        "view" -> {
                            handleViewTag(attributeName, attributeValue, thermalBuilder)
                        }
                        "line" -> {
                            thermalBuilder.addLine("")
                        }
                    }

                    currentTagName = tagName
                }
                XmlPullParser.TEXT -> {
                    val text = parser.text.trim()
                    if (text.isNotEmpty()) {
                        thermalBuilder.addLine(text)
                    }
                }
            }

            eventType = parser.next()
        }

        return thermalBuilder
    }

    private fun handleViewTag(attributeName: String?, attributeValue: String?, thermalBuilder: ThermalPrintStringBuilder) {
        try {
            when (attributeName) {
                "print_type" -> {
                    thermalBuilder.appendValue("Original")
                }
                "order_number" -> { thermalBuilder.appendValue("OrderID ${vm.getOrderId()}") }
                "ret_name" -> {
                    thermalBuilder.appendValue("Sudhiksha Hotels!")
                }
                "ret_address1" -> {
                    thermalBuilder.addLine("Sulthanpet")
                }
                "prod_name" -> {
                    thermalBuilder.addLine("Item name\t\t\t\t\tQTY\tPrice\tAmount")
                    thermalBuilder.addLine("--------------------------------------------------")
                    thermalBuilder.addLine("")

                    for((key,value) in vm.getOrderedHashMap()!!){
                        thermalBuilder.addLine("${value["FoodName"]}\t\t\t\t\t${value["qty"]}\t${value["price"]}\t${value["lineTotal"]}")
                    }
                }
                "total_price" -> {
                    thermalBuilder.addLine("--------------------------------------------------")
                    thermalBuilder.addLine("\t\t\t\t\t\t\tPrice \t ${vm.getOrderValue()} ")
                }
                "label" -> thermalBuilder.addLine("-")
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }
    private class ThermalPrintStringBuilder {
        private val content = StringBuilder()

        fun addLine(text: String) {
            content.append("$text\n")
        }

        fun appendValue(text: String){
            content.append(text)
        }

        override fun toString(): String {
            return content.toString()
        }
    }
}