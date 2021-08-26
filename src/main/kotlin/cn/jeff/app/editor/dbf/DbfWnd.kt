package cn.jeff.app.editor.dbf

import cn.jeff.app.GlobalVars
import com.linuxense.javadbf.DBFDataType
import com.linuxense.javadbf.DBFField
import com.linuxense.javadbf.DBFReader
import com.linuxense.javadbf.DBFWriter
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.TableView
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
			}
		}
		center {
			theTable = tableview(records) {
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

	private fun loadDbf(): Pair<List<DBFField>, ObservableList<Array<Any?>>> {
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
