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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VideoGridController implements Initializable, RefreshScene {
	
	@FXML
	private AnchorPane aPane;
	
	@FXML
    private Button btnClose;

    @FXML
    private Button btnMergeVideos;
    
    @FXML
    private Button btnOutput;
    
    @FXML
    private Button btnClear;
    
    @FXML
    private Button btnVideo;
    
    @FXML
    private Button btnSave;
    
    @FXML
    private Button btnHistory;
    
    @FXML
    private Button btnStop;

    @FXML
    private TextFlow taStatus;
    
    @FXML
    private Label lblVersion;
    
    @FXML
    private Label lblInfo;
    
    @FXML
    private ScrollPane sPane;
    
    @FXML
    private MenuItem menuDelete;
    
    @FXML
    private TableView<VideoInfo> tblVideos;

    @FXML
    private TableColumn<VideoInfo, Integer> tcNum;

    @FXML
    private TableColumn<VideoInfo, String> tcStart;
    
    @FXML
    private TableColumn<VideoInfo, String> tcEnd;

    @FXML
    private TableColumn<VideoInfo, String> tcVideo;

    @FXML
    private TextField txtEnd;

    @FXML
    private TextField txtStart;
    
    @FXML
    private TextField txtOutput;
    
    @FXML
    private TextField txtVideo;
    
    @FXML
    private ComboBox<String> cbResolutions;
    
    private DiscoverChanges discoverChanges = null;
    private DvGlobal dg = DvGlobal.getInstance();
    private Text outText = null;
    private ObservableList<String> resolutions = null;
    private ObservableList<VideoInfo> videos = null;
    private MergeTask mt = null;
    private List<File> lastSelect = new ArrayList<File>();
    
    @FXML
    void doMenuDelete(ActionEvent event) {
    	int idx = tblVideos.getSelectionModel().getSelectedIndex();
    	if (idx >= 0) {
    		tblVideos.getItems().remove(idx);
    		txtVideo.setText("");
    		txtStart.setText("00:00:00");
    		txtEnd.setText("00:00:00");
    	}
    }
    
    @FXML
    void doStart(ActionEvent event) {
    	String v = txtStart.getText();
    	String[] a = v.split(":");
    	String[] a2 = a[2].split("\\.");
    	int h = 0;
    	int m = 0;
    	int sec = 0;
    	int ms = 0;
    	
    	try {
    		h = Integer.parseInt(a[0]);
    		m = Integer.parseInt(a[1]);
    		if (a2.length == 2) {
    			sec = Integer.parseInt(a2[0]);
    			ms = Integer.parseInt(a2[1]);
    		} else {
    			sec = Integer.parseInt(a[2]);
    		}
    		h = Integer.parseInt(a[0]);
    	} catch (NumberFormatException nfe) {
    		return;
    	}
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/TimeSet.fxml"));
    	Parent root;
		try {
			root = (Parent) loader.load();
			TimeSetController controller = (TimeSetController) loader.getController();
			controller.setTime(h, m, sec, ms);
	    	Scene scene = new Scene(root);
	    	Stage stage = new Stage();
	    	stage.setScene(scene);
	    	stage.setTitle("Set Start Time.");
	    	stage.initModality(Modality.APPLICATION_MODAL);
//	    	stage.initStyle(StageStyle.UNDECORATED);
	    	stage.showAndWait();
	    	String s = controller.getTime();
	    	if (s != null)
	    		txtStart.setText(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void doEnd(ActionEvent event) {
    	String v = txtEnd.getText();
    	String[] a = v.split(":");
    	String[] a2 = a[2].split("\\.");
    	int h = 0;
    	int m = 0;
    	int sec = 0;
    	int ms = 0;
    	
    	try {
    		h = Integer.parseInt(a[0]);
    		m = Integer.parseInt(a[1]);
    		if (a2.length == 2) {
    			sec = Integer.parseInt(a2[0]);
    			ms = Integer.parseInt(a2[1]);
    		} else {
    			sec = Integer.parseInt(a[2]);
    		}
    		h = Integer.parseInt(a[0]);
    	} catch (NumberFormatException nfe) {
    		return;
    	}
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/TimeSet.fxml"));
    	Parent root;
		try {
			root = (Parent) loader.load();
			TimeSetController controller = (TimeSetController) loader.getController();
			controller.setTime(h, m, sec, ms);
	    	Scene scene = new Scene(root);
	    	Stage stage = new Stage();
	    	stage.setScene(scene);
	    	stage.setTitle("Set End Time.");
	    	stage.initModality(Modality.APPLICATION_MODAL);
//	    	stage.initStyle(StageStyle.UNDECORATED);
	    	stage.showAndWait();
	    	String s = controller.getTime();
	    	if (s != null)
	    		txtEnd.setText(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void doClose(ActionEvent event) {
    	if (mt != null)
    		mt.endTask();
    	
    	Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
    }
    
    @FXML
    void doStop(ActionEvent event) {
    	if (mt != null)
    		mt.endTask();
    }
    
    @FXML
    void doClear(ActionEvent event) {
    	txtVideo.setText("");
		txtStart.setText("00:00:00");
		txtEnd.setText("00:00:00");
		videos.clear();
		tblVideos.setItems(videos);
    }
    
    @FXML
    void doAdd(ActionEvent event) {
    	String start = txtStart.getText();
    	String end = txtEnd.getText();
    	
    	int num = videos.size();
    	num++;
    	
    	for (File f : lastSelect) {
    		videos.add(new VideoInfo(num++, f.getAbsolutePath(), start, end));
    	}
    	txtVideo.setText("");
    	txtStart.setText("00:00:00");
    	txtEnd.setText("00:00:00");
    }
    
    @FXML
    void doDelete(ActionEvent event) {
    	int idx = tblVideos.getSelectionModel().getSelectedIndex();
    	if (idx >= 0) {
    		tblVideos.getItems().remove(idx);
    		txtVideo.setText("");
    		txtStart.setText("00:00:00");
    		txtEnd.setText("00:00:00");
    	}
    }
    
    @FXML
    void doSave(ActionEvent event) {
    	
    	Object[] secs = dg.history.getSectionNames();
    	
    	int lastNum = 0;
    	for (Object sec : secs) {
    		String s = (String)sec;
    		if (s.startsWith("History") == true) {
    			int n = Integer.parseInt(s.substring(7));
    			if (n > lastNum)
    				lastNum = n;
    		}
    	}
    	
    	lastNum++;
    	
    	if (videos.size() < 2) {
    		lblInfo.setText("Select 2, 4, 9 or 16 videos to build grid.");
    		new java.util.Timer().schedule(new java.util.TimerTask() {
    			@Override
    			public void run() {
    				Platform.runLater(new Runnable() {
    					@Override
    					public void run() {
    						lblInfo.setText("");
    					}
    				});
    			}
    		}, 5000);
    		return;
    	}
    	
    	if (txtOutput.getText().isBlank() == true) {
    		lblInfo.setText("NOT saved, no output filename.");
    		new java.util.Timer().schedule(new java.util.TimerTask() {
    			@Override
    			public void run() {
    				Platform.runLater(new Runnable() {
    					@Override
    					public void run() {
    						lblInfo.setText("");
    					}
    				});
    			}
    		}, 5000);
    		return;
    	}
    	
    	String res = cbResolutions.getValue();
    	if (res == null || res.isBlank() == true) {
    		lblInfo.setText("NOT saved, no output resolution given.");
    		new java.util.Timer().schedule(new java.util.TimerTask() {
    			@Override
    			public void run() {
    				Platform.runLater(new Runnable() {
    					@Override
    					public void run() {
    						lblInfo.setText("");
    					}
    				});
    			}
    		}, 5000);
    		return;
    	}
    	
    	String secName = "History" + lastNum;
    	dg.history.addSection(secName);
    	
    	int cnt = 1;
    	for (VideoInfo vi : videos) {
    		String v = vi.getVideo();
    		String s = vi.getStart();
    		String e = vi.getEnd();
    		
    		dg.history.addValuePair(secName, "video" + cnt, v + "," + s + "," + e);
    		cnt++;
    	}
    	
    	dg.history.addValuePair(secName, "output", txtOutput.getText());
    	dg.history.addValuePair(secName, "resolution", cbResolutions.getValue());
    	
    	dg.history.writeFile(true);
    	lblInfo.setText("Configuration saved.");
    	
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblInfo.setText("");
					}
				});
			}
		}, 5000);
    }
    
    @FXML
    void doHistory(ActionEvent event) {
    	SceneNav.loadScene(SceneNav.HISTORY);
    }

    @FXML
    void doMergeVideos(ActionEvent event) {
    	List<String> v = new ArrayList<String>();
    	List<String> s = new ArrayList<String>();
    	List<String> t = new ArrayList<String>();
    	
    	for (VideoInfo vi : videos) {
    		v.add(vi.getVideo());
    		s.add(vi.getStart());
    		t.add(vi.getEnd());
    	}
    	
    	String res = cbResolutions.getValue();
    	if (res.isEmpty() == true) {
    		lblInfo.setText("No output resolution given.");
    		new java.util.Timer().schedule(new java.util.TimerTask() {
    			@Override
    			public void run() {
    				Platform.runLater(new Runnable() {
    					@Override
    					public void run() {
    						lblInfo.setText("");
    					}
    				});
    			}
    		}, 5000);
    		return;
    	}
    	
    	String op = txtOutput.getText();
    	if (op.isEmpty() == true) {
    		lblInfo.setText("No output file name given.");
    		new java.util.Timer().schedule(new java.util.TimerTask() {
    			@Override
    			public void run() {
    				Platform.runLater(new Runnable() {
    					@Override
    					public void run() {
    						lblInfo.setText("");
    					}
    				});
    			}
    		}, 5000);
    		return;
    	}
    	
    	File f = new File(op);
    	if (f.getParentFile().exists() == false) {
    		lblInfo.setText("The output directory does not exist.");
    		new java.util.Timer().schedule(new java.util.TimerTask() {
    			@Override
    			public void run() {
    				Platform.runLater(new Runnable() {
    					@Override
    					public void run() {
    						lblInfo.setText("");
    					}
    				});
    			}
    		}, 5000);
    		return;
    	}
    	
    	if (v.size() > 1 && v.size() < 17) {
	    	String[] r = res.split("\\s+");
	    	
	    	taStatus.getChildren().clear();
	    	
	    	btnStop.setDisable(false);
	    	
	    	mt = new MergeTask(discoverChanges, v, s, t, op, r[0]);
	    	mt.start();
    	} else {
    		lblInfo.setText("The number of vidoes must be 2 to 16.");
    		new java.util.Timer().schedule(new java.util.TimerTask() {
    			@Override
    			public void run() {
    				Platform.runLater(new Runnable() {
    					@Override
    					public void run() {
    						lblInfo.setText("");
    					}
    				});
    			}
    		}, 5000);
    	}
    }

    @FXML
    void doVideo(ActionEvent event) {
    	Stage stage = (Stage) btnVideo.getScene().getWindow();
    	
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Select Videos");
    	fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Video Files", "*.mp4"),
				new FileChooser.ExtensionFilter("All Files", "*.*")
		);

        List<File> sf = fileChooser.showOpenMultipleDialog(stage);
        if (sf != null) {
        	String files = null;
        	lastSelect.clear();
        	for (File f : sf) {
        		lastSelect.add(f);
        		if (files == null)
        			files = f.getName();
        		else
        			files += " " + f.getName();
        	}
        	
        	txtVideo.setText(files);
        }
    }
    
    @FXML
    void doOutput(ActionEvent event) {
    	Stage stage = (Stage) btnVideo.getScene().getWindow();
    	
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Select Ouptut Video");
    	fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Video Files", "*.mp4"),
				new FileChooser.ExtensionFilter("All Files", "*.*")
		);

        File sf = fileChooser.showSaveDialog(stage);
        if (sf != null)
        	txtOutput.setText(sf.getAbsolutePath());
    }
    
    @FXML
    void tblClicked(MouseEvent event) {
    	VideoInfo vi = tblVideos.getSelectionModel().getSelectedItem();
    	
    	if (vi != null) {
    		txtVideo.setText(vi.getVideo());
    		txtStart.setText(vi.getStart());
    		txtEnd.setText(vi.getEnd());
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		lblVersion.setText("v" + dg.dvVersion);
		
		videos = FXCollections.observableArrayList();
		
		tcNum.setStyle("-fx-alignment: CENTER;");
		tcNum.setCellValueFactory(new PropertyValueFactory<VideoInfo, Integer>("num"));
		tcVideo.setCellValueFactory(new PropertyValueFactory<VideoInfo, String>("video"));
		tcStart.setCellValueFactory(new PropertyValueFactory<VideoInfo, String>("start"));
		tcEnd.setCellValueFactory(new PropertyValueFactory<VideoInfo, String>("end"));
		
		tcVideo.setCellFactory(TextFieldTableCell.forTableColumn());
		tcVideo.setOnEditCommit((TableColumn.CellEditEvent<VideoInfo, String> t) -> {
	        ((VideoInfo) t.getTableView().getItems().get(t.getTablePosition().getRow())).setVideo(t.getNewValue());
	    });
		
		tcStart.setCellFactory(TextFieldTableCell.forTableColumn());
		tcStart.setOnEditCommit((TableColumn.CellEditEvent<VideoInfo, String> t) -> {
			String s = t.getNewValue();
			String[] a = s.split(":");
			int h = 0;
			int m = 0;
			int sec = 0;
			int milli = 0;
			if (a.length == 3) {
				try {
					h = Integer.parseInt(a[0]);
					if (h > 99)
						h = 99;
					if (h < 0)
						h = 0;
					m = Integer.parseInt(a[1]);
					if (m > 59)
						m = 59;
					if (m < 0)
						m = 0;
					String[] a2 = a[2].split("\\.");
					if (a2.length == 2) {
						sec = Integer.parseInt(a2[0]);
						if (sec > 59)
							sec = 59;
						if (sec < 0)
							sec = 0;
						milli = Integer.parseInt(a2[1]);
						if (milli > 999)
							milli = 999;
						if (milli < 0)
							milli = 0;
//						System.out.println(a2[0] + ", " + a2[1]);
					} else {
						sec = Integer.parseInt(a[2]);
					}
					String nt = null;
					if (milli > 0)
						nt = String.format("%02d:%02d:%02d.%03d", h, m, sec, milli);
					else
						nt = String.format("%02d:%02d:%02d", h, m, sec);
					
					((VideoInfo) t.getTableView().getItems().get(t.getTablePosition().getRow())).setStart(nt);
				} catch (NumberFormatException nfe) {

				}
			}
	    });
		
		tcEnd.setCellFactory(TextFieldTableCell.forTableColumn());
		tcEnd.setOnEditCommit((TableColumn.CellEditEvent<VideoInfo, String> t) -> {
			String s = t.getNewValue();
			String[] a = s.split(":");
			int h = 0;
			int m = 0;
			int sec = 0;
			int milli = 0;
			if (a.length == 3) {
				try {
					h = Integer.parseInt(a[0]);
					if (h > 99)
						h = 99;
					if (h < 0)
						h = 0;
					m = Integer.parseInt(a[1]);
					if (m > 59)
						m = 59;
					if (m < 0)
						m = 0;
					String[] a2 = a[2].split("\\.");
					if (a2.length == 2) {
						sec = Integer.parseInt(a2[0]);
						if (sec > 59)
							sec = 59;
						if (sec < 0)
							sec = 0;
						milli = Integer.parseInt(a2[1]);
						if (milli > 999)
							milli = 999;
						if (milli < 0)
							milli = 0;
//						System.out.println(a2[0] + ", " + a2[1]);
					} else {
						sec = Integer.parseInt(a[2]);
					}
					String nt = null;
					if (milli > 0)
						nt = String.format("%02d:%02d%02d.%03d", h, m, sec, milli);
					else
						nt = String.format("%02d:%02d%02d", h, m, sec);
					((VideoInfo) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEnd(nt);
				} catch (NumberFormatException nfe) {

				}
			}
	    });
		
		tblVideos.setItems(videos);
		
		resolutions = FXCollections.observableArrayList();
		
		resolutions.add("640x360 (360p, nHD)");
		resolutions.add("720x480 (480p, SD)");
		resolutions.add("960x540 (qHD)");
		resolutions.add("1280x720 (720p, HD)");
		resolutions.add("1366x768 (WXGA)");
		resolutions.add("1600x900 (HD+)");
		resolutions.add("1920x1080 (1080p, FHD)");
		resolutions.add("2560x1440 (1440p, QHD)");
		resolutions.add("3200x1800 (QHD+)");
		resolutions.add("3840x2160 (4K UHD)");
		resolutions.add("5120x2880 (5K UHD+)");
		resolutions.add("7680x4320 (8K UHD)");
		resolutions.add("15360x8640 (16K UHD)");
		
		cbResolutions.setItems(resolutions);
		
		discoverChanges = new DiscoverChanges();

		discoverChanges.addChangeListener(new DiscoverChangeListener() {
			
			@Override
			public void changeEventOccurred(DiscoverChangeEvent evt) {
				final String data = evt.data;
				
				if (evt.type == DiscoverChanges.OUTPUT) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							addToFlow(data, "-fx-fill: black;");
							if (data.startsWith("Grid Aborted") == true ||
									data.startsWith("Grid Done") == true) {
								btnStop.setDisable(true);
							}
						}
					});
				}
			}
		});
	}
	
	private void addToFlow(String txt, String style) {
		if (txt.startsWith("[libx264 ") == true || txt.startsWith("ffmpeg -y ") == true) {
			// Do not output this stuff.
		} else if (txt.startsWith("frame= ")) {
			outText.setText(txt + "\n");
			outText.setStyle("-fx-fill: #0000dd; -fx-font-size: 16px;");
		} else {
			outText = new Text(txt + "\n");
			outText.setStyle(style + "-fx-font-size: 16px;");
			taStatus.getChildren().add(outText);
		}
		sPane.setVvalue(1.0d);
	}

	@Override
	public void refreshScene() {
		videos.clear();
		
		if (dg.selectedHistory != null) {
			String[] vids = dg.selectedHistory.getVideos().split("\n");
			String[] starts = dg.selectedHistory.getStarts().split("\n");
			String[] ends = dg.selectedHistory.getEnds().split("\n");
			
			int cnt = 0;
			for (String v : vids) {
				videos.add(new VideoInfo(cnt + 1, v, starts[cnt], ends[cnt]));
				cnt++;
			}
			
			txtVideo.setUserData(dg.selectedHistory.getNum());

			txtOutput.setText(dg.selectedHistory.getOutput());
			cbResolutions.setValue(dg.selectedHistory.getResolution());
		}
		
		tblVideos.setItems(videos);
	}

	@Override
	public void leaveScene() {
		
	}

	@Override
	public void clickIt(String text, WidgetType widgetType) {
		
	}
}
