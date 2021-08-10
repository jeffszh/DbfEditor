package cn.jeff.app.editor.dbf

import javafx.scene.control.TableColumn
import javafx.util.Callback
import tornadofx.*
import kotlin.reflect.KProperty1

class DbfWnd<T>(
	data: List<T>,
	vararg columnDefs: Pair<String, KProperty1<T, Any>>
) : View() {

	override val root = tableview(data.observable()) {
		columnDefs.forEach { (title, prop) ->
			val column = TableColumn<T, Any>(title)
			column.cellValueFactory = Callback { observable(it.value, prop) }
			addColumnInternal(column)
		}
	}

	override fun onDelete() {
		super.onDelete()
		println("删除窗口。")
	}

	override fun onUndock() {
		super.onUndock()
		print("离开！")
	}

}
