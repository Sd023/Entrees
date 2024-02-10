package com.sdapps.entres.main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Xml
import com.sdapps.entres.PrintPreviewActivity
import com.sdapps.entres.main.food.main.vm.CartViewModel
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringReader

class PrintDataManager(var vm: CartViewModel, var context: Context) {
    fun createPrintFile() {
        try {
            val xmlTemplate = readXmlTemplateFromAssets(context, "entrees_print.xml")
            val thermalBuilder = parseXmlTemplate(xmlTemplate)
            println(thermalBuilder)
            writeToFile(thermalBuilder.toString())

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun readXmlTemplateFromAssets(context: Context, fileName: String): String {
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


    private fun handleViewTag(
        attributeName: String?,
        attributeValue: String?,
        thermalBuilder: ThermalPrintStringBuilder
    ) {
        try {
            val itemNameHeader = "Item name"
            val qtyHeader = "Qty"
            val priceHeader = "Price"
            val amountHeader = "Amount"

            val headerLine =
                "$itemNameHeader                 $qtyHeader    $priceHeader   $amountHeader"
            val headerLength = headerLine.length

            when (attributeName) {
                "print_type" -> {
                    thermalBuilder.addLine(formatCentered("Original", 32))
                }

                "order_number" -> {
                    thermalBuilder.addLine(formatCentered("OrderID ${vm.getOrderId()}", 32))
                }

                "ret_name" -> {
                    thermalBuilder.addLine("Hotel: ${vm.getHotelName()}".leftAlign(32))
                }

                "ret_address1" -> {
                    thermalBuilder.addLine("Address: ${vm.getHotelBranch()}".leftAlign(32))
                }

                "prod_name" -> {
                    thermalBuilder.addLine(headerLine)
                    thermalBuilder.addLine("-".repeat(headerLength))

                    for ((key, value) in vm.getOrderedHashMap()!!) {
                        val itemName = value["FoodName"].toString()
                        val qty = value["qty"].toString()
                        val price = value["price"].toString()
                        val lineTotal = value["lineTotal"].toString()

                        val itemLine = itemName.leftAlign(20) +
                                qty.rightAlign(10) +
                                price.rightAlign(12) +
                                lineTotal.rightAlign(14)
                        thermalBuilder.addLine(itemLine)
                    }
                }

                "total_price" -> {
                    thermalBuilder.addLine("-".repeat(headerLength))
                    thermalBuilder.addLine("Price".padEnd(26) + "${vm.getOrderValue()}".rightAlign(6))
                }

                "label" -> thermalBuilder.addLine("-")
            }


        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun formatCentered(text: String, length: Int): String {
        val paddingLength = (length - text.length) / 2
        val leftPadding = " ".repeat(paddingLength)
        val rightPadding = " ".repeat(length - text.length - paddingLength)
        return "$leftPadding$text$rightPadding"
    }

    fun formatCentered(value: String): String {
        val paddingLength = (32 - value.length) / 2
        return value.padStart(paddingLength + value.length, ' ').padEnd(32, ' ')
    }

    fun formatLeftAligned(text: String, length: Int): String {
        val paddingLength = length - text.length
        val rightPadding = " ".repeat(paddingLength)
        return "$text$rightPadding"
    }

    fun String.leftAlign(width: Int): String {
        return this.padEnd(width)
    }

    fun String.rightAlign(width: Int): String {
        return this.padStart(width - this.length + this.length % 2)
    }

    fun String.centerAlign(width: Int): String {
        val padding = (width - this.length) / 2
        return " ".repeat(padding) + this + " ".repeat(padding + (width - this.length) % 2)
    }

    private fun writeToFile(printFile: String) {
        try {
            val dirName = "PRINTFILE"
            val fileName = "ord_${vm.getOrderId()}.txt"

            val rootDirectory = File(context.filesDir.toString())
            val outputDirectory = File(rootDirectory, dirName)

            if (!outputDirectory.exists()) {
                try {
                    outputDirectory.mkdirs()
                    Log.d("PRINT", "Directory created: ${outputDirectory.absolutePath}")
                } catch (e: SecurityException) {
                    Log.d("PRINT", "Failed to create directory: ${e.message}")
                    return
                }
            }
            val outputFile = File(outputDirectory, fileName)
            try {
                outputFile.createNewFile()
                outputFile.writeText(printFile)
                println("File written: ${outputFile.absolutePath}")

                val intent = Intent(context, PrintPreviewActivity::class.java).apply {
                    putExtra("file_path", outputFile.toString())
                }
                context.startActivity(intent)
            } catch (e: IOException) {
                println("Failed to write file: ${e.message}")
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private class ThermalPrintStringBuilder {
        private val content = StringBuilder()

        fun addLine(text: String) {
            content.append("$text\n")
        }

        fun appendValue(text: String) {
            content.append(text)
        }

        override fun toString(): String {
            return content.toString()
        }
    }
}