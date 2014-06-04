package bupt.ygj.datacollector.elementview;

import wa.android.common.network.WAParValueVO;
import bupt.ygj.datacollector.data.AbsFieldValue;

public class FieldValueAddress extends AbsFieldValue{

	private String countryid = null;
	private String provinceid = null;
	private String cityid = null;
	private String districtid = null;
	private String streetstr = null;
	private String postcode = null;
	public FieldValueAddress(String key, String countryid,String provinceid,
			String cityid,String districtid,String streetstr,String postcode) {
		super(key);
		this.countryid = countryid;
		this.provinceid = provinceid;
		this.cityid = cityid;
		this.districtid = districtid;
		this.streetstr = streetstr;
		this.postcode = postcode;
	}

	@Override
	public WAParValueVO toWAParameter() {
		WAParValueVO valueVO = new WAParValueVO();
		valueVO.addField("itemkey", key);
		valueVO.addField("countryid", countryid);
		valueVO.addField("provinceid", provinceid);
		valueVO.addField("cityid", cityid);
		valueVO.addField("districtid", districtid);
		valueVO.addField("streetstr", streetstr);
		valueVO.addField("postcode", postcode);
		return valueVO;
	}
	
	public boolean getRealValueEmpty() {
//		if((countryid != null && !countryid.equals("")) || (provinceid != null && !provinceid.equals("")) || (cityid != null && !cityid.equals("")) ||
//				(districtid != null && !districtid.equals(districtid)) || (streetstr != null && !streetstr.equals("")) || 
//				(postcode != null && !postcode.equals("")))
//			return false;
//		else
//			return true;
		return false;
	}

}
