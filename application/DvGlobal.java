/*
 * Copyright (c) 2023 Richard Kelly Wiles (rkwiles@twc.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *  Created on: Jan 30, 2023
 *      Author: Kelly Wiles
 */

package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rkw.IniFile;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class DvGlobal {

	private static DvGlobal singleton = null;
	
	// methods and attributes for Singleton pattern
	private DvGlobal() {
		initGlobals();
	}
	
	// constructor for global variables
	private void initGlobals() {
		dvVersion = "1.0.0";
		
		File f1 = new File(System.getProperty("user.home") + File.separator + "VideoGrid");
		if (f1.exists() == false) {
			f1.mkdir();
		}
		
		File f2 = new File(System.getProperty("user.home") + File.separator + "VideoGrid" + File.separator + "history.ini");
		
		if (f2.exists() == false) {
			try {
				f2.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		history = new IniFile(f2.getAbsolutePath());
	}
	
	Alert alert = null;
	
	public String dvVersion = null;
	
	public List<DiskInfo> disks = new ArrayList<DiskInfo>();
	public IniFile history = null;
	
	public HistoryInfo selectedHistory = null;
	
	public int startHours = 0;
	public int startMins = 0;
	public int startSecs = 0;
	public int startMilliSecs = 0;
	
	public int endHours = 0;
	public int endMins = 0;
	public int endSecs = 0;
	public int endMilliSecs = 0;
	
	public static DvGlobal getInstance() {
		// return SingletonHolder.singleton;
		if (singleton == null) {
			synchronized (DvGlobal.class) {
				singleton = new DvGlobal();
			}
		}
		return singleton;
	}
	
	public String scenePeek() {
		if (SceneNav.sceneQue == null || SceneNav.sceneQue.isEmpty())
			return SceneNav.VIDEOGRID;
		else
			return SceneNav.sceneQue.peek();
	}
	
	public void loadSceneNav(String fxml) {
		if (SceneNav.loadScene(fxml) == true) {
			showAlert("GUI Error", "A GUI error occurred.\r\nError loading " + fxml + "\r\n\r\nRestarting GUI.", AlertType.ERROR, false);
			System.exit(1);
		}
	}
	
	public void closeAlert() {
		if (alert != null) {
			alert.close();
			alert = null;
		}
	}
	
	public ButtonType showAlert(String title, String msg, AlertType alertType, boolean yesNo) {
		alert = new Alert(alertType);
		alert.getDialogPane().setPrefWidth(725.0);
		for ( ButtonType bt : alert.getDialogPane().getButtonTypes() ) {
		    Button button = ( Button ) alert.getDialogPane().lookupButton( bt );
		    if (yesNo == true) {
			    if (button.getText().equals("Cancel"))
			    	button.setText("No");
			    else if (button.getText().equals("OK"))
			    	button.setText("Yes");
		    }
		    button.setStyle("-fx-font-size: 28px;");
		    button.setPrefWidth(150.0);
		}
		alert.setTitle(title);
		alert.setHeaderText(null);
		
		alert.setContentText(msg);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(
		   getClass().getResource("myDialogs.css").toExternalForm());
		dialogPane.getStyleClass().add("myDialog");
		
		ButtonType bt = alert.showAndWait().get();
		
		alert = null;
		
		return bt;
	}
}
