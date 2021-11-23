package cn.jeff.app.editor.dbf

import cn.jeff.app.GlobalVars
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

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
		j.btnOpenFile.shortcut("Ctrl+O")
	}

	fun btnClicked(actionEvent: ActionEvent) {
		when (actionEvent.source) {
			j.btnOpenFile -> {
				j.lbStatus.text = "运行中……"
				val selectedFiles = chooseFile(
					"打开DBF文件", arrayOf(
						FileChooser.ExtensionFilter(
							"DBF文件", "*.dbf"
						)
					)
				) {
					initialDirectory = File(GlobalVars.appConf.defaultDbfFilePath)
				}
				if (selectedFiles.isNotEmpty()) {
					val file = selectedFiles[0]
					GlobalVars.appConf.defaultDbfFilePath = file.parent
					GlobalVars.saveConfig()
					val dbfWnd = DbfWnd(file.path)
					j.mainTabPane.tab(dbfWnd) {
						text = file.name
						select()
						onCloseRequest = EventHandler { e ->
							if (!dbfWnd.askForClose()) {
								e.consume()
							}
						}
					}
				}
				j.lbStatus.text = "就绪"
			}
			else -> {
				println("不会运行到这里。")
			}
		}
	}

}
