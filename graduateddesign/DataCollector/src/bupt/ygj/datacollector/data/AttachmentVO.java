package bupt.ygj.datacollector.data;

import java.io.Serializable;
import java.util.Map;

public class AttachmentVO extends ValueObject implements Serializable {
	private static final long serialVersionUID = 1L;
	private String fileid;
	private String filesize;
	private String filename;
	private String filetype;
	private String downflag;
	private String path;

	private String filecontent;

	public void setAttributes(Map<String, String> mapvalue) {
		if (null != mapvalue) {
			this.setFileid(this.getMapStringValue(mapvalue, "fileid"));
			this.setFilesize(this.getMapStringValue(mapvalue, "filesize"));
			this.setFilename(this.getMapStringValue(mapvalue, "filename"));
			this.setDownflag(this.getMapStringValue(mapvalue, "downflag"));
			this.setFiletype(filename.substring(filename.lastIndexOf('.') + 1));
		}

	}

	public String getFileid() {
		return fileid;
	}

	public String getFilesize() {
		return filesize;
	}

	public String getFilename() {
		return filename;
	}

	public String getFiletype() {
		return filetype;
	}

	public String getDownflag() {
		return downflag;
	}

	public String getPath() {
		return path;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public void setDownflag(String downflag) {
		this.downflag = downflag;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}

}
