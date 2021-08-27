package cn.jeff.app.editor.dbf

import cn.jeff.app.GlobalVars
import cn.jeff.app.textInputDialog as inputText
import com.linuxense.javadbf.DBFDataType
import com.linuxense.javadbf.DBFField
import com.linuxense.javadbf.DBFReader
import com.linuxense.javadbf.DBFWriter
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList as TableData
import javafx.geometry.Pos
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.util.StringConverter
import tornadofx.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset

class DbfWnd(private val dbfFilename: String) : Fragment() {

	companion object {
		private const val NULL_SIGN = "(null)"
	}

	private val isModified = SimpleBooleanProperty(false)
	private val defaultCharset = Charset.forName(GlobalVars.appConf.defaultCharset)
	private var theTable: TableView<Array<Any?>> by singleAssign(SingleAssignThreadSafetyMode.NONE)

	override val root = borderpane {
		val (fields, records) = loadDbf()
		top {
			hbox {
				alignment = Pos.CENTER_LEFT
				spacing = 10.0
				paddingAll = 10.0
				button("保存修改") {
					action {
						saveDbf(fields, records)
						isModified.value = false
					}
				}.enableWhen(isModified)
				label(dbfFilename)
				button("导出Excel") {
					action {
						information("未实现。")
					}
				}
				button("添加新记录") {
					action {
						records.add(arrayOfNulls(fields.count()))
					}
				}

				var searchingText = ""
				var foundIndex = -1
				button("_F搜索") {
					// isMnemonicParsing = false
					//shortcut(KeyCodeCombination(KeyCode.F, KeyCodeCombination.CONTROL_DOWN))
					shortcut("Ctrl-F")
					shortcut("Ctrl+F") {
						println("这样吗？")
					}
					action {
						inputText("请输入搜索内容", searchingText) { inputtedText ->
							searchingText = inputtedText.let {
								if (it.isBlank())
									NULL_SIGN
								else
									it.trim()
							}
							/*
							theTable.selectWhere { rec ->
								rec.any {
									it.toString().contains(text, true)
								}
							}
							*/
							foundIndex = records.indexOfFirst { rec ->
								rec.any {
									it.toString().contains(searchingText, true)
								}
							}
							if (foundIndex < 0) {
								information("找不到包含“$searchingText”的任何内容。")
							} else {
								theTable.selectionModel.select(foundIndex)
								theTable.scrollTo(foundIndex)
							}
						}
					}
				}
				button("_N搜下一个") {
					shortcut(KeyCodeCombination(KeyCode.GREATER, KeyCombination.CONTROL_DOWN))
					action {
						if (foundIndex < 0) {
							information("先前没有搜索到，请重新搜索。")
						} else {
							val subList = records.subList(foundIndex + 1, records.count())
							val nextFound = subList.indexOfFirst { rec ->
								rec.any {
									it.toString().contains(searchingText, true)
								}
							}
							if (nextFound < 0) {
								information("后面没有包含“$searchingText”的内容了。")
							} else {
								foundIndex += nextFound + 1    // 因为subList是从foundIndex+1开始的，
								// 漏了上面这句就会有bug。
								theTable.selectionModel.select(foundIndex)
								theTable.scrollTo(foundIndex)
							}
						}
					}
				}
				button("_P搜上一个") {
					shortcut(KeyCodeCombination(KeyCode.LESS, KeyCombination.CONTROL_DOWN))
					action {
						if (foundIndex < 0) {
							information("先前没有搜索到，请重新搜索。")
						} else {
							val subList = records.subList(0, foundIndex)
							val prevFound = subList.indexOfLast { rec ->
								rec.any {
									it.toString().contains(searchingText, true)
								}
							}
							if (prevFound < 0) {
								information("前面没有包含“$searchingText”的内容了。")
							} else {
								foundIndex = prevFound
								theTable.selectionModel.select(foundIndex)
								theTable.scrollTo(foundIndex)
							}
						}
					}
				}
			}
		}
		center {
			theTable = tableview(records) {
				isTableMenuButtonVisible = true
				fields.forEachIndexed { index, dbfField ->
					column<Array<Any?>, Any>(dbfField.name) {
						val arrAccessor = ArrAccessor(it.value, index)
						arrAccessor.observable(ArrAccessor::value)
					}.makeEditable(object : StringConverter<Any>() {
						override fun toString(obj: Any?) = obj?.toString() ?: NULL_SIGN
						override fun fromString(aStr: String?): Any {
							isModified.value = true
							if (aStr.isNullOrBlank()) {
								// 若为空或只包含空格，换成空信号。
								return NULL_SIGN
							}
							val str = aStr.trim()
							return when (dbfField.type) {
								DBFDataType.CHARACTER, DBFDataType.VARCHAR -> {
									str
								}
								DBFDataType.NUMERIC,
								DBFDataType.FLOATING_POINT,
								DBFDataType.DOUBLE,
								DBFDataType.CURRENCY -> {
									str.toDouble()
								}
								DBFDataType.LONG -> {
									str.toInt()
								}
								DBFDataType.LOGICAL -> {
									str.toBoolean()
								}
								else -> {
									throw IOException("不支持修改的数据类型：${dbfField.type}")
								}
							}
						}
					})
				}
			}
		}
	}

	/**
	 * 询问是否可关闭窗口
	 * @return 若确认可关闭，返回true。
	 */
	fun askForClose(): Boolean {
		if (!isModified.value) {
			return true
		}
		var canClose = false
		confirm("不保存就关闭吗？") {
			canClose = true
		}
		return canClose
	}

	private fun loadDbf(): Pair<List<DBFField>, TableData<Array<Any?>>> {
		DBFReader(FileInputStream(dbfFilename), defaultCharset).use { reader ->
			val fields = (0 until reader.fieldCount).map { i ->
				reader.getField(i)
			}
			val records = (0 until reader.recordCount).map {
				reader.nextRecord() ?: arrayOfNulls(reader.fieldCount)
			}
			return fields to records.observable()
		}
	}

	private fun saveDbf(fields: List<DBFField>, records: List<Array<Any?>>) {
		DBFWriter(FileOutputStream(dbfFilename), defaultCharset).use { writer ->
			writer.setFields(fields.toTypedArray())
			records.forEach { record ->
				if (record.all { it == null }) {
					// writer.addRecord(null)
				} else {
					writer.addRecord(record)
				}
//				writer.addRecord(record)
			}
		}
	}

	class ArrAccessor(private val arr: Array<Any?>, private val ind: Int) {
		var value: Any
			get() = arr[ind] ?: NULL_SIGN
			set(value) {
				arr[ind] = if (value == NULL_SIGN) null else value
			}
	}

}
