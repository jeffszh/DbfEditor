package cn.jeff.app.editor.dbf

import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.layout.BorderPane
import tornadofx.*

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
				j.lbStatus.text = "很好！"
			}
			else -> {
				println("不会运行到这里。")
			}
		}
	}

}
