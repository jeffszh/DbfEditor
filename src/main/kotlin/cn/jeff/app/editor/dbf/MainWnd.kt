package cn.jeff.app.editor.dbf

import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.layout.BorderPane
import tornadofx.*
import kotlin.random.Random

class MainWnd : View("DBF编辑器") {

	override val root: BorderPane
	private val j: MainWndJ

	init {
		primaryStage.isResizable = true

		val loader = FXMLLoader()
		root = loader.load(
			javaClass.getResourceAsStream(
				"MainWnd.fxml"
			)
		)
		j = loader.getController()
		j.k = this

		j.mainTabPane.tabs.clear()
	}

	fun btnClicked(actionEvent: ActionEvent) {
		when (actionEvent.source) {
			j.btnOpenFile -> {
				j.lbStatus.text = "运行中……"
				val dbfWnd = DbfWnd(
					arrayOf("序号", "内容"),
					listOf(
						arrayOf<Any>(1, "one"),
						arrayOf<Any>(2, "two"),
						arrayOf<Any>(3, "three"),
					).observable()
				)
				j.mainTabPane.tab(dbfWnd).also {
					it.text = "d${Random.nextInt(9999)}"
					it.select()
				}
				j.lbStatus.text = "就绪"
			}
			else -> {
				println("不会运行到这里。")
			}
		}
	}

}
