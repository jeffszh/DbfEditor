package cn.jeff.app.editor.dbf

import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.layout.BorderPane
import javafx.util.Duration
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
				val lst = listOf(
					arrayOf<Any>(1, "one"),
					arrayOf<Any>(2, "two"),
					arrayOf<Any>(3, "three"),
				)
				val dbfWnd = DbfWnd(
					arrayOf("序号", "内容"),
					lst.observable()
				)
				j.mainTabPane.tab(dbfWnd).also {
					it.text = "d${Random.nextInt(9999)}"
					it.select()
				}
				j.lbStatus.text = "就绪"
				runLater(Duration.seconds(15.0)) {
					lst.forEach {
						print("${it[0]} : ${it[0].javaClass}    ")
						print("${it[1]} : ${it[1].javaClass}\n")
					}
				}
				runLater(Duration.seconds(20.0)) {
					lst.forEach {
						print("${it[0]} : ${it[0].javaClass}    ")
						print("${it[1]} : ${it[1].javaClass}\n")
					}
				}
				runLater(Duration.seconds(25.0)) {
					lst.forEach {
						print("${it[0]} : ${it[0].javaClass}    ")
						print("${it[1]} : ${it[1].javaClass}\n")
					}
				}
			}
			else -> {
				println("不会运行到这里。")
			}
		}
	}

}
