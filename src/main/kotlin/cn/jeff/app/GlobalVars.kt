package cn.jeff.app

import com.google.gson.GsonBuilder
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception

object GlobalVars {

	private const val appConfFilename = "DbfEditorConf.json"
	private val gson = GsonBuilder().setPrettyPrinting().create()

	var appConf = AppConf()

	fun loadConfig() {
		try {
			FileReader(appConfFilename).use { reader ->
				appConf = gson.fromJson(reader, AppConf::class.java)
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun saveConfig() {
		FileWriter(appConfFilename).use { writer ->
			gson.toJson(appConf, writer)
		}
	}

}
