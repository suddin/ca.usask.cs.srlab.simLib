package ca.usask.cs.srlab.simcad.dataprovider.xml;
public class RawFragment implements Comparable<RawFragment>{
	String file;
	String startLine;
	String endLine;
	String content;
	
	public RawFragment(String file, String startLine, String endLine, String content) {
		super();
		this.file = file;
		this.startLine = startLine;
		this.endLine = endLine;
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFile() {
		return file;
	}

	public String getStartLine() {
		return startLine;
	}

	public String getEndLine() {
		return endLine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endLine == null) ? 0 : endLine.hashCode());
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result
				+ ((startLine == null) ? 0 : startLine.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RawFragment other = (RawFragment) obj;
		if (endLine == null) {
			if (other.endLine != null)
				return false;
		} else if (!endLine.equals(other.endLine))
			return false;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (startLine == null) {
			if (other.startLine != null)
				return false;
		} else if (!startLine.equals(other.startLine))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "File: " + file + "\nFrom: " + startLine + "\nTo: " + endLine
				+ "\n" + content;
	}

	@Override
	public int compareTo(RawFragment other) {
		if(other == null) return -1;
		
		int comp = file.compareTo(other.file);
		
		if(comp != 0) return comp;
		else{
			comp = Integer.valueOf(startLine).compareTo(Integer.valueOf(other.startLine));
			if(comp != 0) return comp;
			else{
				return Integer.valueOf(endLine).compareTo(Integer.valueOf(other.endLine));
			}
		}
	}
}