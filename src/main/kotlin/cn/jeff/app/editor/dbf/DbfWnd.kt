package cn.jeff.app.editor.dbf

import tornadofx.*
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

/*
class DbfWnd<T>(data: List<T>, colList: List<Pair<String, KFunction<T>>>) : View() {

	override val root = tableview(data.observable()) {
		colList.forEach { (title, getter) ->
			column(title, getter)
		}
		refresh()
	}

//	override val root = datagrid(data) {
//	}

	override fun onDelete() {
		super.onDelete()
		println("删除窗口。")
	}

	override fun onUndock() {
		super.onUndock()
		print("离开！")
	}

}
*/

class DbfWnd(data: List<Pair<Int, String>>) : View() {

	override val root = tableview(data.observable()) {
		readonlyColumn("序号", Pair<Int, String>::first)
		readonlyColumn("内容", Pair<Int, String>::second)
	}

//	override val root = datagrid(data) {
//	}

	override fun onDelete() {
		super.onDelete()
		println("删除窗口。")
	}

	override fun onUndock() {
		super.onUndock()
		print("离开！")
	}

}
