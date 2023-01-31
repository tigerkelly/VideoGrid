module dvCheck {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires java.desktop;
	requires IniFile;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base, javafx.controls, java.desktop;
}
