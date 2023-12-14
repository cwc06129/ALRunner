package syntax.global;

//2023-02-22(Wed) SoheeJung
//header's include line
public class Include {
	private String type;
	private String value;
	private String rawdata;
	private String rawStringData;
	
	public Include(String type, String value) {
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRawdata() {
		return type + " " + value ;
	}

	public void setRawdata(String rawdata) {
		this.rawdata = rawdata;
	}
}
