package application;

public class VideoInfo {

	private int num;
	private String video;
	private String start;
	private String end;
	
	public VideoInfo(int num, String video, String start, String end) {
		super();
		this.num = num;
		this.video = video;
		this.start = start;
		this.end = end;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
}
