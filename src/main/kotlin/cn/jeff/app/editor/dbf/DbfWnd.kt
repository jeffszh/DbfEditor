package cn.jeff.app.editor.dbf

import cn.jeff.app.GlobalVars
import com.linuxense.javadbf.DBFField
import com.linuxense.javadbf.DBFReader
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import tornadofx.*
import java.io.FileInputStream
import java.nio.charset.Charset

class DbfWnd(private val dbfFilename: String) : Fragment() {

	private val isModified = SimpleBooleanProperty(false)
	private val defaultCharset = Charset.forName(GlobalVars.appConf.defaultCharset)

	override val root = borderpane {
		top {
			hbox {
				alignment = Pos.CENTER_LEFT
				spacing = 10.0
				paddingAll = 10.0
				button("保存修改") {
					action {
					}
				}.enableWhen(isModified)
				label(dbfFilename)
			}
		}
		val (fields, records) = loadDbf()
		center {
			tableview(records.observable()) {
				fields.forEachIndexed { index, dbfField ->
					column<Array<Any>, Any>(dbfField.name) {
						val arrAccessor = ArrAccessor(it.value, index)
						arrAccessor.observable(ArrAccessor::value)
					}
				}
			}
		}
	}

	/**
	 * 询问是否可关闭窗口
	 * @return 若确认可关闭，返回true。
	 */
	fun askForClose(): Boolean {
		var canClose = false
		confirm("关闭吗？") {
			canClose = true
		}
		return canClose
	}

	private fun loadDbf(): Pair<List<DBFField>, List<Array<Any>>> {
		DBFReader(FileInputStream(dbfFilename), defaultCharset).use { reader ->
			val fields = (0 until reader.fieldCount).map { i ->
				reader.getField(i)
			}
			val records = (0 until reader.recordCount).map {
				reader.nextRecord()
			}
			return fields to records
		}
	}

	class ArrAccessor(private val arr: Array<Any>, private val ind: Int) {
		var value: Any
			get() = arr[ind]
			set(value) {
				arr[ind] = value
			}
	}

}

/*
class DbfWnd(titles: Array<String>, data: ObservableList<Array<Any>>) : Fragment() {

	override val root = tableview(data) {
		titles.forEachIndexed { i, title ->
			column<Array<Any>, Any>(title) {
				val arrAccessor = ArrAccessor(it.value, i)
//				it.value.observable(
//					getter = arrAccessor::value::get,
//					setter = arrAccessor::value::set
//				)

				arrAccessor.observable("value")
				// arrAccessor.observable(ArrAccessor::value)	// 这句也可以。
			}.makeEditable(object : StringConverter<Any>() {
				override fun toString(obj: Any?): String =
					obj.toString()

				override fun fromString(str: String?): Any {
					return str ?: ""
				}
			})
		}
	}

	class ArrAccessor(private val arr: Array<Any>, private val ind: Int) {
		var value: Any
			get() = arr[ind]
			set(value) {
				arr[ind] = value
//				arr.forEach {
//					println("$it : ${it.javaClass}")
//				}
			}
	}

}
*/
