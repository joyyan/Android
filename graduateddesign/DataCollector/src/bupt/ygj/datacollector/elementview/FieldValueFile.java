package bupt.ygj.datacollector.elementview;

import java.util.ArrayList;
import java.util.List;

import wa.android.common.network.WAParValueList;
import wa.android.common.network.WAParValueVO;
import bupt.ygj.datacollector.data.AbsFieldValue;
import bupt.ygj.datacollector.data.AttachmentVO;

public class FieldValueFile extends AbsFieldValue {
	List<AttachmentVO> attachmentList = new ArrayList<AttachmentVO>();

	String value = null;		//这个提交属性其实在接口文档中没有，口头约定
	public FieldValueFile(String key, List<AttachmentVO> attachmentList,String realvalue) {
		super(key);
		this.attachmentList = attachmentList;
		this.value = realvalue;
	}

	@Override
	public WAParValueVO toWAParameter() {

		WAParValueVO attachListVO = new WAParValueVO();
		
		WAParValueList attachList = new WAParValueList();

		if (null != attachmentList) {
			for (AttachmentVO attvo : attachmentList) {
				if (attvo != null) {
					WAParValueVO attachmentVO = new WAParValueVO();
					attachmentVO.addField("filename", attvo.getFilename());
					attachmentVO.addField("file", attvo.getFilecontent());
					attachList.addItem(attachmentVO);
				}
			}
		}

		attachListVO.addField("filelist", attachList);
		attachListVO.addField("itemkey", key);
		attachListVO.addField("realvalue", value);
		return attachListVO;
	}

	public boolean getRealValueEmpty() {
		int number = 0;
		if (null != attachmentList) {
			for (AttachmentVO attvo : attachmentList) {
				if (attvo != null) {
					if (attvo.getFilename() != null
							&& !attvo.getFilename().equals("")
							&& attvo.getFilecontent() != null
							&& !attvo.getFilecontent().equals("")) {
						number++;
					}
				}
			}
			if (number > 0 && number == attachmentList.size()) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}

	}
}
