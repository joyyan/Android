package bupt.ygj.datacollector.data;

import java.util.ArrayList;
import java.util.List;

import wa.android.common.network.WAParValueList;
import wa.android.common.network.WAParValueVO;

public class CFSaveBodyDataVO {
	
	private String childDocId = null;
	
	//表体信息
	//List<CFSaveItemValueVO>	多个CFSaveItemValueVO对象表示表体中的一行
	//List<List<CFSaveItemValueVO>>		多行表示整个表体
	private List<List<AbsFieldValue>> bodyList = null;
	
	public CFSaveBodyDataVO(){
		bodyList = new ArrayList<List<AbsFieldValue>>();
	}
	
	/**
	 * 设置一个ChildDocId
	 * @param childDocId
	 */
	public void setChildDocId(String childDocId){
		this.childDocId = childDocId;
	}
	
	/**
	 * 将一个表体行数据添加到VO中
	 * @param bodyRow
	 */
	public void addBodyRow(List<AbsFieldValue> bodyRow,String childid) {
		this.childDocId = childid;
		bodyList.add(bodyRow);
	}
	/**
	 * 将CFSaveBodyDataVO的实例转换为WAParValueVO
	 * @return
	 */
	public WAParValueVO toWAParameter(){
		WAParValueVO pVO = new WAParValueVO();
//		pVO.addField("childdocid", childDocId);
		WAParValueList bodyPar = new WAParValueList();
		pVO.addField(childDocId, bodyPar);
		
		for (List<AbsFieldValue> row : bodyList) {
			WAParValueVO rowValueVO = new WAParValueVO();
			bodyPar.addItem(rowValueVO);
			
//			WAParValueVO itemValueList = new WAParValueVO();
//			rowValueVO.addField("bitemvaluelist", itemValueList);//hsaveitemlist
			
			
			if(null != row) {
				for (AbsFieldValue field : row) {
					if(field != null) {	//field我们在代码中控制了，可以为null
						WAParValueVO fieldPar = field.toWAParameter();
						rowValueVO.addField(field.key, fieldPar);
					}
				}
			}
		}
		return pVO;
	}
}
