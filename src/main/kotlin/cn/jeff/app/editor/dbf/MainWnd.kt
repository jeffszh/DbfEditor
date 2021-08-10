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
	}

	fun btnClicked(actionEvent: ActionEvent) {
		when (actionEvent.source) {
			j.btnOpenFile -> {
				j.lbStatus.text = "运行中……"
				val demoData = listOf(1 to "one", 2 to "two", 3 to "three")
				val dbfWnd = DbfWnd(demoData)
				j.mainTabPane.tab(dbfWnd).text = "d${Random.nextInt(9999)}"
				j.lbStatus.text = "就绪"
			}
			else -> {
				println("不会运行到这里。")
			}
		}
	}

}
