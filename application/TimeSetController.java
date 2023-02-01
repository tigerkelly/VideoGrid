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

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TimeSetController implements Initializable, RefreshScene {

	@FXML
    private AnchorPane aPane;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private Spinner<Integer> spinnerHours;

    @FXML
    private Spinner<Integer> spinnerMilliSecs;

    @FXML
    private Spinner<Integer> spinnerMins;

    @FXML
    private Spinner<Integer> spinnerSecs;
    
//    private DvGlobal dg = DvGlobal.getInstance();
    private String timeStr = null;
    
    @FXML
    void doCancel(ActionEvent event) {
    	timeStr = null;
    	Stage stage = (Stage) btnCancel.getScene().getWindow();
		stage.close();
    }

    @FXML
    void doSave(ActionEvent event) {
    	int h = spinnerHours.getValue();
    	int m = spinnerMins.getValue();
    	int s = spinnerSecs.getValue();
    	int ms = spinnerMilliSecs.getValue();
    	
    	if (ms > 0)
    		timeStr = String.format("%02d:%02d:%02d.%d", h, m, s, ms);
    	else
    		timeStr = String.format("%02d:%02d:%02d", h, m, s);
    	Stage stage = (Stage) btnSave.getScene().getWindow();
		stage.close();
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	spinnerHours.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0));
    	spinnerMins.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    	spinnerSecs.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    	spinnerMilliSecs.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 0));
	}
    
    public String getTime() {
    	
    	return timeStr;
    }
    
    public void setTime(int hour, int min, int sec, int msec) {
    	spinnerHours.getValueFactory().setValue(hour);
    	spinnerMins.getValueFactory().setValue(min);
    	spinnerSecs.getValueFactory().setValue(sec);
    	spinnerMilliSecs.getValueFactory().setValue(msec);
    }

	@Override
	public void refreshScene() {
		
	}

	@Override
	public void leaveScene() {
		
	}

	@Override
	public void clickIt(String text, WidgetType widgetType) {
		
	}
}

