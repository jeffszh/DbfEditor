package cn.jeff.app.editor.dbf

import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.collections.ObservableList
import tornadofx.*

class DbfWnd(titles: Array<String>, data: ObservableList<Array<Any>>) : View() {

	override val root = tableview(data) {
		titles.forEachIndexed { i, title ->
			column<Array<Any>, Any>(title) {
				// SimpleObjectProperty(it.value[i])
				// 用上面这句也可以，效果一样。
				// 下面的一句是从TornadoFX文档中学来，
				// 位于： Part 1: TornadoFX Fundamentals / 5. Data Controls 中
				// Declaring Column Values Functionally 一节。
				ReadOnlyObjectWrapper(it.value[i])
			}
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
