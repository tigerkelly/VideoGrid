package application;

public class HistoryInfo {

	private int num;
	private String videos;
	private String starts;
	private String ends;
	private String resolution;
	private String output;
	
	public HistoryInfo(int num, String videos, String starts, String ends, String resolution, String output) {
		super();
		this.num = num;
		this.videos = videos;
		this.starts = starts;
		this.ends = ends;
		this.resolution = resolution;
		this.output = output;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getVideos() {
		return videos;
	}

	public void setVideos(String videos) {
		this.videos = videos;
	}
	
	public String getStarts() {
		return starts;
	}

	public void setStarts(String starts) {
		this.starts = starts;
	}
	
	public String getEnds() {
		return ends;
	}

	public void setEnds(String ends) {
		this.ends = ends;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@Override
	public String toString() {
		return "HistoryInfo [num=" + num + ", videos=" + videos + ", start=" + starts + ", end=" + ends + ", resolution=" + resolution + ", output=" + output
				+ "]";
	}
	
}
