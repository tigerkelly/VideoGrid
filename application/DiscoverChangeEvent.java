/*
 * Created on Mar 19, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * Copyright Kelly Wiles 2005, 2006, 2007, 2008
 */
package application;

import java.util.EventObject;

/**
 * Part of the custom event Changes class.
 * @author Kelly Wiles
 *
 */
public class DiscoverChangeEvent extends EventObject {
	public int type = 0;
	public String data = null;

	public DiscoverChangeEvent(Object arg0, String data) {
		super(arg0);
		type = (Integer)arg0;
		this.data = data;
	}

	private static final long serialVersionUID = 1L;

}
