package cn.jeff.app

import cn.jeff.app.editor.dbf.MainWnd
import tornadofx.*

class DbfEditor : App(MainWnd::class) {
	init {
		GlobalVars.loadConfig()
	}
}
