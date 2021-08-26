package cn.jeff.app

import javafx.geometry.Pos
import tornadofx.*

fun UIComponent.textInputDialog(
	title: String = "请输入", initialText: String = "",
	op: (String) -> Unit
) {
	dialog(title) {
		style += "-fx-font-family: 'Courier New'; -fx-font-size: 16;"
		val tv = textfield(initialText)
		hbox {
			alignment = Pos.CENTER
			paddingAll = 10.0
			spacing = 10.0
			button("确定") {
				action {
					close()
					op(tv.text)
				}
			}.isDefaultButton = true
			button("取消") {
				action {
					close()
				}
			}.isCancelButton = true
		}
	}
}
