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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class HistoryController implements Initializable, RefreshScene {

    @FXML
    private AnchorPane aPane;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSelect;
    
    @FXML
    private MenuItem cmDelete;

    @FXML
    private TableView<HistoryInfo> tblHistory;

    @FXML
    private TableColumn<HistoryInfo, Integer> tcNum;

    @FXML
    private TableColumn<HistoryInfo, String> tcOutput;

    @FXML
    private TableColumn<HistoryInfo, String> tcResolution;

    @FXML
    private TableColumn<HistoryInfo, String> tcVideos;
    
    @FXML
    private TableColumn<HistoryInfo, String> tcStarts;
    
    @FXML
    private TableColumn<HistoryInfo, String> tcEnds;
    
    private ObservableList<HistoryInfo> hists = null;
    private DvGlobal dg = DvGlobal.getInstance();

    @FXML
    void doCancel(ActionEvent event) {
    	dg.selectedHistory = null;
    	SceneNav.scenePop();
    }

    @FXML
    void doSelect(ActionEvent event) {
    	dg.selectedHistory = tblHistory.getSelectionModel().getSelectedItem();
    	SceneNav.scenePop();
    }
    
    @FXML
    void doMenuDelete(ActionEvent event) {
    	if (tblHistory.getSelectionModel().getSelectedIndex() < 0)
    		return;
    	
    	HistoryInfo hi = tblHistory.getSelectionModel().getSelectedItem();
    	
    	dg.history.removeSection("History" + hi.getNum());
    	
    	tblHistory.getSelectionModel().clearSelection();
    	
    	dg.history.writeFile(true);
    	
    	loadHistory();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		hists = FXCollections.observableArrayList();
		
		tcNum.setStyle("-fx-alignment: CENTER;");
		tcResolution.setStyle("-fx-alignment: CENTER;");
		tcOutput.setStyle("-fx-alignment: CENTER;");
		tcNum.setCellValueFactory(new PropertyValueFactory<HistoryInfo, Integer>("num"));
		tcVideos.setCellValueFactory(new PropertyValueFactory<HistoryInfo, String>("videos"));
		tcStarts.setCellValueFactory(new PropertyValueFactory<HistoryInfo, String>("starts"));
		tcEnds.setCellValueFactory(new PropertyValueFactory<HistoryInfo, String>("ends"));
		tcResolution.setCellValueFactory(new PropertyValueFactory<HistoryInfo, String>("resolution"));
		tcOutput.setCellValueFactory(new PropertyValueFactory<HistoryInfo, String>("output"));
		
		tblHistory.setRowFactory( tv -> {
		    TableRow<HistoryInfo> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		    	if (event.getClickCount() > 1) {
			    	dg.selectedHistory = tblHistory.getSelectionModel().getSelectedItem();
			    	SceneNav.scenePop();
		    	}
		    });
		    return row ;
		});
		
		loadHistory();
	}
	
	private void loadHistory() {
		
		hists.clear();
		
		Object[] secs = dg.history.getSectionNames();
		
		for (Object sec : secs) {
			String s = (String)sec;
			
			if (s.startsWith("History") == true) {
				int n = Integer.parseInt(s.substring(7));
				Object[] keys = dg.history.getSectionKeys(s);
				
				String videos = null;
				String starts = null;
				String ends = null;
				for (Object key : keys) {
					String k = (String)key;
					if (k.startsWith("video") == true) {
						String vidInfo = dg.history.getString(s, k);
						String[] v = vidInfo.split(",");
//						System.out.println(v.length + ", " + s + ", " + vidInfo);
						if (videos == null)
							videos = v[0];
						else
							videos += "\n" + v[0];
						if (starts == null)
							starts = v[1];
						else
							starts += "\n" + v[1];
						if (ends == null)
							ends = v[2];
						else
							ends += "\n" + v[2];
					}
				}
				
				String resolution = dg.history.getString(s, "resolution");
				String output = dg.history.getString(s, "output");
				hists.add(new HistoryInfo(n, videos, starts, ends, resolution, output));
				
//				try {
//					int n = Integer.parseInt(s.substring(7));
//					String v1 = dg.history.getString(s, "video1");
//					String v2 = dg.history.getString(s, "video2");
//					String v3 = dg.history.getString(s, "video3");
//					String v4 = dg.history.getString(s, "video4");
//					String videos = v1;
//					if (v2 != null && v2.isBlank() == false)
//						videos += "\n" + v2;
//					if (v3 != null && v3.isBlank() == false)
//						videos += "\n" + v3;
//					if (v4 != null && v4.isBlank() == false)
//						videos += "\n" + v4;
//					
//					String t1 = dg.history.getString(s, "offset1");
//					String t2 = dg.history.getString(s, "offset2");
//					String t3 = dg.history.getString(s, "offset3");
//					String t4 = dg.history.getString(s, "offset4");
//					String offsets = t1;
//					if (t2 != null && t2.isBlank() == false)
//						offsets += "\n" + t2;
//					if (v3 != null && t3.isBlank() == false)
//						offsets += "\n" + t3;
//					if (v4 != null && t4.isBlank() == false)
//						offsets += "\n" + t4;
//					
//					String resolution = dg.history.getString(s, "resolution");
//					String output = dg.history.getString(s, "output");
//					hists.add(new HistoryInfo(n, videos, offsets, resolution, output));
//				} catch (NumberFormatException nfe) {
//					
//				}
			}
		}
		
		tblHistory.setItems(hists);
	}
	
	@Override
	public void refreshScene() {
		loadHistory();
	}

	@Override
	public void leaveScene() {
		
	}

	@Override
	public void clickIt(String text, WidgetType widgetType) {
		
	}

}

