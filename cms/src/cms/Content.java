package cms;

public class Content {
	String id;
	String text;
	
	public Content(String i, String t) {
		id = i;
		text = t;		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
