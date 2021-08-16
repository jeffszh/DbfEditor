package cn.jeff.app.editor.dbf

import javafx.collections.ObservableList
import javafx.util.StringConverter
import tornadofx.*

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
