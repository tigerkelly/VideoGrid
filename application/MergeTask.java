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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeTask extends Thread {
	
	private DiscoverChanges dChanges = null;
	List<String> videos = new ArrayList<String>();
	List<String> starts = new ArrayList<String>();
	List<String> ends = new ArrayList<String>();
	private String fileName = null;
	private String resolution = null;
	private Resolutions cmds = null;
	private boolean stopTask = false;
	
	
	public MergeTask(DiscoverChanges dChanges, List<String> videos, List<String> starts, List<String> ends, String fileName, String resolution) {
		this.dChanges = dChanges;
		this.videos = videos;
		this.starts = starts;
		this.ends = ends;
		this.fileName = fileName;
		this.resolution = resolution;
		
//		cmds = new Resolutions();
		
		/* 640x360 (nHD)
		   720×480 (480p, SD)
		   960×540 (qHD)
		   1280×720 (720p, HD)
		   1366×768 (WXGA)
		   1600×900 (HD+)
		   1920×1080 (1080p, FHD)
		   2560×1440 (1440p, QHD)
		   3200×1800 (QHD+)
		   3840×2160 (4K UHD)
		   5120×2880 (5K UHD+)
		   7680×4320 (8K UHD)
		   15360×8640 (16K UHD)
		   */
	}
	
	@Override
	public void run() {
		
		List<String> p = new ArrayList<String>();
		
		p.add("ffmpeg");
		p.add("-y");			// over write output file.
		
		for (int i = 0; i < videos.size(); i++) {
			if (starts.get(i).startsWith("00:00:00") == false) {
				p.add("-ss");
				p.add(starts.get(i));
			}
			if (ends.get(i).startsWith("00:00:00") == false) {
				p.add("-to");
				p.add(ends.get(i));
			}
			p.add("-i");
			if (videos.get(i).contains(" ") == true)
				p.add("\"" + videos.get(i) + "\"");
			else
				p.add(videos.get(i));
		}
		
		int numVideos = videos.size();
		
//		System.out.println(numVideos);
		
		String cmd = null;
//		if (numVideos == 2) {
//			cmd = cmds.commands.get("v1x2_" + resolution);		// 1 row, 2 columns
//		} else if (numVideos == 4) {
//			cmd = cmds.commands.get("v2x2_" + resolution);		// 2 rows 2 columns
//		} else if (numVideos ==9 ) {
//			cmd = cmds.commands.get("v3x3_" + resolution);		// 3 rows 3 columns
//		} else if (numVideos == 16) {
//			cmd = cmds.commands.get("v4x4_" + resolution);		// 4 rows 4 column
//		} else {
//			dChanges.fireChange(new DiscoverChangeEvent(DiscoverChanges.OUTPUT, "Error: Must supply 2, 4, 9 or 16 videos..."));
//			return;
//		}
		if (numVideos < 2) {
			return;
		}
		
		int width = 0;
		int height = 0;
		int row = 1;
		int col = 1;
		int div = 1;
		
		if (numVideos == 2) {
			cmd = cmds.commands.get("v1x2_" + resolution);		// 1 row, 2 columns
		} else {
			if (numVideos <= 4) {
				div = 2;
			} else if (numVideos <= 9) {
				div = 3;
			} else if (numVideos <= 16) {
				div = 4;
			}
			cmd = "-filter_complex ";
			
			cmd += "\"nullsrc=size=" + resolution + " [base]; ";
			
			String[] a = resolution.split("x");
			try {
				width = Integer.parseInt(a[0]) / div;
				height = Integer.parseInt(a[1]) / div;
			} catch (NumberFormatException nfe) {
				System.out.println("NFE error.");
				return;
			}
			
			for (int i = 0; i < numVideos; i++) {
				cmd += String.format("[%d:v] setpts=PTS-STARTPTS, scale=%dx%d [r%dc%d]; ", i, width, height, row, col);
				col++;
				if (col > div) {
					col = 1;
					row++;
				}
			}
			
			row = 1;
			col = 2;
			cmd += "[base][r1c1] overlay=shortest=1 [tmp1]; ";
			int w = width;
			int h = 0;
			int n = 1;
			
//			System.out.println("numVides " + numVideos);
			
			for (int i = 0; i < (numVideos - 1); i++) {
				cmd += String.format("[tmp%d][r%dc%d] overlay=shortest=1:x=%d:y=%d", n, row, col, w, h);
				if ((i + 2) < numVideos)
					cmd += String.format(" [tmp%d]; ", (n + 1));
				else
					cmd += "\" ";
				n++;
				col++;
				w += width;
				if (col > div) {
					col = 1;
					row++;
					h += height;
					w = 0;
				}
			}
			
			cmd += "-c:v libx264 ";
		}
		
//		System.out.println(cmd);
		
		String[] arr = cmd.split("\\s+");
		for (String s : arr)
			p.add(s);

		if (fileName.contains(" ") == true)
			p.add("\"" + fileName + "\"");
		else
			p.add(fileName);
		
//		for (String s : p)
//			System.out.print(s + " ");
//		System.out.println("");
//			return;
		
//		for (String s : p)
//			System.out.println(s);
		
		
		dChanges.fireChange(new DiscoverChangeEvent(DiscoverChanges.OUTPUT, "Starting Grid..."));
		
		ProcessBuilder pb = new ProcessBuilder();
		pb.redirectErrorStream(true);
		pb.command(p);
		boolean aborted = false;
		
		try {
			Process process = pb.start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
//				System.out.println(line);
				if (stopTask == true) {
					aborted = true;
					break;
				}
				dChanges.fireChange(new DiscoverChangeEvent(DiscoverChanges.OUTPUT, line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (aborted == true)
			dChanges.fireChange(new DiscoverChangeEvent(DiscoverChanges.OUTPUT, "Grid Aborted..."));
		else
			dChanges.fireChange(new DiscoverChangeEvent(DiscoverChanges.OUTPUT, "Grid Done..."));
	}
	
	public void endTask() {
		stopTask = true;
	}
}
