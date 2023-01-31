# VideoGrid
A GUI to create video grids using ffmpeg.

This GUI runs on either Windows or Linux.

![alt text](/images/VideoGrid_scrn.png?raw=true)

# Install under Windows.
Use the Windows install program called **VideGrid-?.?.?.exe** to install VideoGrid.

# Install under Linux.
First create a directory called **Updates** in your home directory and copy the **VideoGrid-?.?.?.run** script to it.
Change directory to the ~/Updates directory.
Next execute the **VideGrid-?.?.?.run**, this will install the VideGrid program.
You will need to install Java 19 (openjdk-19) also install the Java FX 19 (openjfx-19)
Set an environment varable called **PATH_TO_FX** that points the the lib directory of your installed Java FX.

With this GUI you can combine up to 16 videos into a single video.  This program will tile the videos into a grid of 2x1, 2x2, 3x3 or 4x4. The grid used is determined by the number of videos supplied.

The top table contains the videos selected using the fields below the table.  You can edit the column if you need to adjust the vide file or start and end times.

The output field is the filename to write the video to.  The output video format can be any extention supported by your instaled version of ffmpeg. **NOTE:** The windows install will install a private copy of FFMPEG.

You can also select the output video resolution.

	640x360 (nHD)
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

Us the Start and End fields select a time range to use.  The time fields format is hh:mm:ss.ms were hh = hours, mm = minutes, ss = seconds and ms = milliseconds. Examples...

	Start 00:00:30 End 00:00:00 - means to start 30 seconds into video and goto end of video.
	Start 00:01:00 End 00:30:00 - means to start 30 seconds into video and go for 30 minutes.
	Start 00:00:30.500 End 01:00:00 = means to start 30 seconds and 500 milliseconds into video and fo for 1 hour.

The time to create the video is based on the speed of your system, the number of input videos and output resolution size.

The Save button allows you to save the current settings into a history file, which you can use the History button to select later.

Use the Clear button to clear the table and fields, to start a new video grid.

The bottom section will show the progress of the VideoGrid.

Use the Stop button to stop a currently running Grid.

