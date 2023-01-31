package application;

public class DiskInfo {

	private String name;
	private String size;
	private String type;
	private String label;
	
	public DiskInfo(String name, String size, String type, String label) {
		super();
		this.name = name;
		this.size = size;
		this.type = type;
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "DiskInfo [name=" + name + ", size=" + size + ", type=" + type + ", label=" + label + "]";
	}
	
	
}
