package syntax.global;

// 2023-02-22(Wed) SoheeJung
// header's define line
public class Define {
	private String type;
	private String name;
	private String parameter;
	private String value;
	private String rawdata;
	
	public Define(String type, String name, String parameter, String value) {
		this.type = type;
		this.name = name;
		this.parameter = parameter;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getRawdata() {
		String result = "";
		if (type.isEmpty() && name.isEmpty() && parameter.isEmpty() && value.isEmpty() == false){
			result += value;
			return result;
		}		
		if (type != null){
			result += type;
			result += " ";
		}
		if (name != null){
			result += name;
			result += " ";
		}
		if (parameter != null) {
			result += "(";
			result += parameter;
			result += ")";
			result += " ";
		}
		if (value != null){
			result += value;
		}
		return result ;
	}

	public void setRawdata(String rawdata) {
		this.rawdata = rawdata;
	}
}
