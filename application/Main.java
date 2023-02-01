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
	
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Main extends Application {
	
	private Pane mainPane = null;
	
	@Override
	public void start(Stage primaryStage) {
		
		Thread.setDefaultUncaughtExceptionHandler(Main::showError);
		
		try {
			primaryStage.setScene(createScene(loadMainPane()));
			primaryStage.getIcons().add(new Image("file:app/VideoGrid.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
     * Loads the main fxml layout.
     * Sets up the vista switching VistaNavigator.
     * Loads the first vista into the fxml layout.
     *
     * @return the loaded pane.
     * @throws IOException if the pane could not be loaded.
     */
    private Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        mainPane = (Pane) loader.load(getClass().getResourceAsStream(SceneNav.MAIN));	// SceneNav

        SceneNavController mainController = loader.getController();

        SceneNav.setMainController(mainController);
        SceneNav.loadScene(SceneNav.VIDEOGRID);

        return mainPane;
    }

    /**
     * Creates the main application scene.
     *
     * @param mainPane the main application layout.
     *
     * @return the created scene.
     */
    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(mainPane);

        scene.getStylesheets().setAll(getClass().getResource("application.css").toExternalForm());

        return scene;
    }
    
	private static void showError(Thread t, Throwable e) {
//		System.err.println("***Default exception handler***");
		if (Platform.isFxApplicationThread()) {
			if (e.getMessage().contains("Too many touch")) {
				e.printStackTrace();
				showErrorDialog(e);
			} else {
				System.out.println("Error: exception: " + e);
				e.printStackTrace();
			}
		} else {
			System.err.println("An unexpected error occurred in " + t);

		}
	}
	
	private static void showErrorDialog(Throwable e) {
        StringWriter errorMsg = new StringWriter();
        e.printStackTrace(new PrintWriter(errorMsg));
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("Error.fxml"));
        try {
            Parent root = loader.load();
            ((ErrorController)loader.getController()).setErrorText(errorMsg.toString());
            dialog.setScene(new Scene(root, 1000, 700));
            dialog.show();
            
            Timer timer = new Timer();
            
            TimerTask task = new TimerTask() {
            	public void run() {
            		System.exit(1);
            	}
            };
            
            timer.schedule(task, 10000);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
