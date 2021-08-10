package cn.jeff.app.editor.dbf;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

public class MainWndJ {

	public MainWnd k;
	public TabPane mainTabPane;
	public Button btnOpenFile;
	public Label lbStatus;

	public void btnClicked(ActionEvent actionEvent) {
		k.btnClicked(actionEvent);
	}

}
