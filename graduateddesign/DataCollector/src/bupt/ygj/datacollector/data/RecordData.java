package bupt.ygj.datacollector.data;




public class RecordData extends AttachmentVO {
	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getTotalTs() {
		return totalTs;
	}

	public void setTotalTs(String totalTs) {
		this.totalTs = totalTs;
	}

	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	private static final long serialVersionUID = 1L;
	private String recordType;
	private String totalTs;
	private String recordTime;

}
