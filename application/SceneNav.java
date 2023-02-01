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
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class SceneNav {

//	private AgGlobal ag = AgGlobal.getInstance();
	
	/**
     * Convenience constants for fxml layouts managed by the navigator.
     */
	public static final String MAIN			= "SceneNav.fxml";
    public static final String VIDEOGRID	= "VideoGrid.fxml";
    public static final String HISTORY		= "History.fxml";
    
    /** The main application layout controller. */
    private static SceneNavController mainController = null;
    
    // sceneQue is stack of screens.
    public static Stack<String> sceneQue = null;
    
    // fxmls is cache for screens.
    private static Map<String, SceneInfo> fxmls = new HashMap<String, SceneInfo>();
    
    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainController the main application layout controller.
     */
    public static void setMainController(SceneNavController mainController) {
    	SceneNav.sceneQue = new Stack<String>();
        SceneNav.mainController = mainController;
    }
    
    public static SceneInfo getSceneInfo(String sceneName) {
    	if (SceneNav.fxmls != null)
    		return fxmls.get(sceneName);
    	else
    		return null;
    }
    
    /**
     * Loads the vista specified by the fxml file into the
     * vistaHolder pane of the main application layout.
     *
     * Previously loaded vista for the same fxml file are not cached.
     * The fxml is loaded anew and a new vista node hierarchy generated
     * every time this method is invoked.
     *
     * A more sophisticated load function could potentially add some
     * enhancements or optimizations, for example:
     *   cache FXMLLoaders
     *   cache loaded vista nodes, so they can be recalled or reused
     *   allow a user to specify vista node reuse or new creation
     *   allow back and forward history like a browser
     *
     * @param fxml the fxml file to be loaded.
     */
    public static boolean loadScene(String fxml) {
    	
//    	SceneNav sn = new SceneNav();
    	
    	if (SceneNav.fxmls != null && SceneNav.fxmls.containsKey(fxml)) {
    		SceneInfo si = fxmls.get(fxml);
    		mainController.setSceneNav(si.node);
    		if (si.controller instanceof RefreshScene) {
	    		RefreshScene c = (RefreshScene)si.controller;
	    		c.refreshScene();
    		}
    	} else {
	        try {
	        	SceneInfo si = new SceneInfo();
	        	FXMLLoader loader = new FXMLLoader(SceneNav.class.getResource(fxml));
	        	if (loader != null) {
		        	si.loader = loader;
		        	si.node = (Node)loader.load();			// must be called before you call getController
		        	si.controller = loader.getController();
		        	
		        	fxmls.put(fxml, si);
		            mainController.setSceneNav(si.node);
	        	}
	        } catch (IOException e) {
	        	System.out.println("Error loading FXML: " + fxml);
	            e.printStackTrace();
	            return true;
	        }
    	}
    	
    	pushStack(fxml);
    	
    	return false;
    }
    
    public static void scenePop() {
		if (SceneNav.sceneQue != null && SceneNav.sceneQue.empty() == false) {
			String s = SceneNav.sceneQue.pop();
			SceneInfo si = SceneNav.getSceneInfo(s);
			SceneNav.fxmls.put(s,  si);
			
//    		mainController.setSceneNav(si.node);
//    		if (si.controller instanceof RefreshScene) {
//	    		RefreshScene c = (RefreshScene)si.controller;
//	    		c.leaveScene();
//    		}
			
			if (SceneNav.sceneQue.empty() == false) {
				SceneNav.loadScene(SceneNav.sceneQue.peek());
			} else {
				SceneNav.loadScene(SceneNav.VIDEOGRID);
			}
		}
	}
    
    public static String getCurrentScene() {
    	return SceneNav.sceneQue.peek();
    }
    
    private static void pushStack(String fxml) {
    	if (SceneNav.sceneQue.size() > 0) {
    		if (SceneNav.sceneQue.peek().equals(fxml) == false)
    			SceneNav.sceneQue.push(fxml);
    	} else {
    		SceneNav.sceneQue.push(fxml);
    	}
    	
//    	System.out.println("STACK: " + ag.sceneQue);
    }
    
    public static void clickButton(String txt) {
    	String fxml = SceneNav.sceneQue.peek();		// get current scene.
    	
    	SceneInfo si = fxmls.get(fxml);
		mainController.setSceneNav(si.node);
		if (si.controller instanceof RefreshScene) {
    		RefreshScene c = (RefreshScene)si.controller;
    		c.clickIt(txt, WidgetType.BUTTON);
		}
    }
    
    public static void clickRegion(String txt) {
    	String fxml = SceneNav.sceneQue.peek();		// get current scene.
    	
    	SceneInfo si = fxmls.get(fxml);
		mainController.setSceneNav(si.node);
		if (si.controller instanceof RefreshScene) {
    		RefreshScene c = (RefreshScene)si.controller;
    		c.clickIt(txt, WidgetType.REGION);
		}
    }
    
    public static void clickCheckbox(String txt) {
    	String fxml = SceneNav.sceneQue.peek();		// get current scene.
    	
    	SceneInfo si = fxmls.get(fxml);
		mainController.setSceneNav(si.node);
		if (si.controller instanceof RefreshScene) {
    		RefreshScene c = (RefreshScene)si.controller;
    		c.clickIt(txt, WidgetType.CHECKBOX);
		}
    }
    
    public static void gotoMainMenu() {
    	if (SceneNav.sceneQue != null && SceneNav.sceneQue.empty() == false) {
    		while(true) {
    			String fxml = SceneNav.sceneQue.peek();
    			if (fxml.equals(SceneNav.VIDEOGRID) == true)
    				break;
    			
    			SceneInfo si = fxmls.get(fxml);
        		mainController.setSceneNav(si.node);
        		if (si.controller instanceof RefreshScene) {
    	    		RefreshScene c = (RefreshScene)si.controller;
    	    		c.leaveScene();
        		}
    		}
    	}
    }
    
}
