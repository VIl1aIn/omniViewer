package eventViewer.model;

public class ConvertStringField {
	public String convertString(String str) {
		if (str != null && str.length()>0 && str.charAt(str.length()-1) == '\0') {
			return str.substring(0, str.length()-1);
		}
		return str;
	}
}
