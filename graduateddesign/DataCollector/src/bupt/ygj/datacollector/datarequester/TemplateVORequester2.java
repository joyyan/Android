package bupt.ygj.datacollector.datarequester;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import wa.android.common.activity.BaseActivity;
import wa.android.common.network.WAReqActionVO;
import wa.android.common.network.WARequestVO;
import wa.android.common.network.WAResActionVO;
import wa.android.common.network.WAResStructVO;
import wa.android.common.utils.PreferencesUtil;
import wa.android.constants.CommonServers;
import wa.android.constants.CommonWAPreferences;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import bupt.ygj.datacollector.constants.ActionType;
import bupt.ygj.datacollector.data.BinfoInitValueVO;
import bupt.ygj.datacollector.data.ElementDataVO;
import bupt.ygj.datacollector.data.ElementGroupVO4Body;
import bupt.ygj.datacollector.data.ElementGroupVO4Header;
import bupt.ygj.datacollector.data.FormInitDataVO;
import bupt.ygj.datacollector.data.ItemInitValueVO;
import bupt.ygj.datacollector.data.TemplateVO;

public class TemplateVORequester2 extends WAVORequester {

	private String cachekey = ""; // 缓存模板的key，用于存储，实际上cachekey和cachekeyforget必须一样
	private String cachekeyforget = ""; // 缓存模板的key,用于取数据的时候
	private String requestcachetskey; // 缓存请求参数中模板ts的key
	private String requestcachepkkey; // 缓存请求参数中模板pk的key

	private int times = 0;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public TemplateVORequester2(BaseActivity context, Handler handler, int msgId) {
		super(context, handler, msgId);
	}

	public void request(String functioncode, String pkdoc, String winid,
			String templatepk, String ts, String timezone) {

		WAReqActionVO actionVO = WAReqActionVO
				.createCommonActionVO("getCFTemplateAttribute");

		actionVO.addPar("functioncode", functioncode);
		actionVO.addPar("pkdoc", pkdoc);
		actionVO.addPar("winid", winid);
		actionVO.addPar("templatepk", templatepk);
		actionVO.addPar("ts", ts);
		actionVO.addPar("timezone", timezone);
		cachekey = functioncode + pkdoc + winid + timezone;
		cachekeyforget = functioncode + pkdoc + winid + timezone + templatepk
				+ ts;
		requestcachepkkey = functioncode + pkdoc + winid + timezone + "pk"
				+ getKey();
		requestcachetskey = functioncode + pkdoc + winid + timezone + "ts"
				+ getKey();

		if (times == 1) {
			PreferencesUtil.writePreference(context, requestcachepkkey, "");
			PreferencesUtil.writePreference(context, requestcachetskey, "");
		}

		WAReqActionVO initValueActionVO = WAReqActionVO
				.createCommonActionVO("getCFInitData");
		initValueActionVO.addPar("functioncode", functioncode);
		initValueActionVO.addPar("pkdoc", pkdoc);
		initValueActionVO.addPar("winid", winid);
		initValueActionVO.addPar("templatepk", templatepk);
		initValueActionVO.addPar("ts", ts);
		initValueActionVO.addPar("timezone", timezone);

		WARequestVO requestVO = new WARequestVO(this);
		requestVO.addWAActionVO(ActionType.CID_QDBS, actionVO);
		requestVO.addWAActionVO(ActionType.CID_QDBS, initValueActionVO);

		request(CommonServers.getServerAddress(context)
				+ CommonServers.SERVER_SERVLET_WA, requestVO);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onRequested(WARequestVO vo) {
		TemplateVO templateVO = null;
		List<WAReqActionVO> listAction = vo.getReqComponentVO("WA00038").actionList;
		FormInitDataVO formInit = null;
		for (WAReqActionVO action : listAction) {
			WAResActionVO resVO = action.resActionVO;
			String actionType = action.getActiontype();
			if (resVO.flag == 0 || resVO.flag == 3) {
				try {
					WAResStructVO structVO = resVO.responseList.get(0);

					if (actionType.equals("getCFTemplateAttribute")) {

						if (resVO.flag == 3) {
							// 说明有新的模板需要缓存
							Map<String, Map> data = (Map<String, Map>) structVO.returnValue
									.get(0);
							templateVO = TemplateVO.loadVO(data);
							if (templateVO != null && templateVO.isVaild()) {
								saveTemplateVO(templateVO);
							} else {
								if (times != 1) {
									Message msg = makeMessage(9, resVO.desc);
									handler.sendMessage(msg);
									return;
								}
							}
						} else if (resVO.flag == 0) {
							templateVO = getTemplateVO();
							if (templateVO == null) {
								if (times != 1) {
									Message msg = makeMessage(9, resVO.desc);
									handler.sendMessage(msg);
									return;
								}
							}
						}
					} else if (actionType.equals("getCFInitData")) {
						Map<String, Map> data = (Map<String, Map>) structVO.returnValue
								.get(0);
						formInit = new FormInitDataVO();
						formInit.setAttributes(data.get("forminitdata"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (resVO.flag == 1 || resVO.flag == 2 || resVO.flag == 4) {
				Message msg = makeMessage(90, resVO.desc);
				handler.sendMessage(msg);
				return;
			}
		}

		// 把getCFInitData获取的初始值赋给模板vo
		if (null != formInit && null != templateVO) {
			// 给表头赋初始值
			List<ItemInitValueVO> hItemList = formInit.getHinitvaluelist();
			if (null != hItemList) {
				for (ItemInitValueVO item : hItemList) {
					String initItemKey = item.getItemkey();
					setTemplateVOInitValue(initItemKey, templateVO,
							item.getRealvalue());
				}
			}

			// 给表体赋初始值
			List<BinfoInitValueVO> bItemList = formInit.getBinfoinitvaluelist();
			if (null != bItemList) {
				for (BinfoInitValueVO body : bItemList) {
					String childid = body.getChilddocid();
					setTemplateVOInitBValue(childid, body.getBinitvaluelist(),
							templateVO);
				}
			}
		}

		Message msg = makeMessage(msgId, templateVO);
		handler.sendMessage(msg);
	}

	/**
	 * 缓存模板
	 * 
	 * @param templateVO
	 */
	private void saveTemplateVO(TemplateVO templateVO) {
		try {
			cachekey = cachekey + templateVO.getTemplatePK()
					+ templateVO.getTs();
			SharedPreferences mSharedPreferences = context
					.getSharedPreferences(getKey(), context.MODE_PRIVATE);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(templateVO);
			String personBase64 = new String(Base64.encodeBase64(baos
					.toByteArray()));
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.putString(cachekey, personBase64);
			editor.commit();
			if (null != templateVO) {
				PreferencesUtil.writePreference(context, requestcachepkkey,
						templateVO.getTemplatePK());
				PreferencesUtil.writePreference(context, requestcachetskey,
						templateVO.getTs());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取出缓存中模板
	 * 
	 * @return
	 */
	private TemplateVO getTemplateVO() {
		TemplateVO templatevo = null;
		try { // 读取已经保存的
			SharedPreferences mSharedPreferences = context
					.getSharedPreferences(getKey(), Context.MODE_PRIVATE);
			String personBase64 = mSharedPreferences.getString(cachekeyforget,
					""); // 读取物料参照
			if (null != personBase64 && !"".equals(personBase64)) {
				byte[] base64Bytes = Base64.decodeBase64(personBase64
						.getBytes());
				ByteArrayInputStream bais = new ByteArrayInputStream(
						base64Bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				templatevo = (TemplateVO) ois.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return templatevo;
	}

	private void setTemplateVOInitBValue(String childid,
			List<ItemInitValueVO> binitvaluelist, TemplateVO templateVO) {
		if (childid != null && binitvaluelist != null && templateVO != null) {
			List<ElementGroupVO4Body> bList = templateVO.getBodyList();
			if (null != bList) {
				for (ElementGroupVO4Body body : bList) {
					if (body.getChilddocid() != null
							&& body.getChilddocid().equals(childid)) {
						List<ElementDataVO> eles = body.getElements();
						for (ItemInitValueVO item : binitvaluelist) {
							String initItemKey = item.getItemkey();
							setElementInitValue(eles, initItemKey,
									item.getRealvalue());
						}
					}
				}
			}
		}
	}

	/**
	 * 给TemplateVO类型vo赋初始值(表头)，通过改变TemplateVO 类型vo的状态
	 * 
	 * @param initItemKey
	 *            为初始值中表头某一项的itemkey
	 * @param templateVO
	 *            模板vo
	 * @param initValue
	 *            初始值接口当中的某一个itemkey的初始值
	 */
	private void setTemplateVOInitValue(String initItemKey,
			TemplateVO templateVO, String initValue) {
		if (initItemKey != null && initValue != null) {
			List<ElementGroupVO4Header> headerList = templateVO.getHeaderList();
			if (null != headerList) {
				for (ElementGroupVO4Header header : headerList) { // 这里遍历表头的group,多个group
					List<ElementDataVO> elementList = header.getElements();
					setElementInitValue(elementList, initItemKey, initValue);
				}
			}
		}
	}

	/**
	 * 
	 * @param elementList
	 *            模板中的ele list
	 * @param initItemKey
	 *            初始值接口中的itemkey
	 * @param initValue
	 *            初始值当中的初始值
	 */
	private void setElementInitValue(List<ElementDataVO> elementList,
			String initItemKey, String initValue) {
		if (null != elementList) {
			boolean templateishave = false; // 用于标示初始值当中的该itemkey是否存在于表单中，false
											// 标示没有包含，true表示包含了
			for (ElementDataVO ele : elementList) {
				String tempItemkey = ele.getItemKey();
				if (null != tempItemkey && tempItemkey.equals(initItemKey)) {
					// 参照不支持getCFInitData类型的默认值，为了避免混乱，如果发现是参照那么continue
					templateishave = true;
					if (ele.getItemType() != null) {
						if (ele.getItemType().equals("refertype"))
							continue;
						else if (ele.getItemType().equals("combo")) {
							ele.setDefaultValue(initValue);
						} else {
							ele.setDefaultValue(initValue);
						}
					}
				}
			}
			if (!templateishave) {
				ElementDataVO ele = new ElementDataVO();
				ele.setAllowEmpty(true);
				ele.setEditable(true);
				ele.setEnable(true);
				ele.setItemKey(initItemKey);
				ele.setItemType("initnulltype");
				ele.setVisible(false);
				ele.setDefaultValue(initValue);
				elementList.add(ele);
			}
		}
	}

	private String getKey() {
		String key = PreferencesUtil.readPreference(context,
				CommonWAPreferences.SERVER_IP)
				+ PreferencesUtil.readPreference(context,
						CommonWAPreferences.SERVER_PORT)
				+ PreferencesUtil.readPreference(context,
						CommonWAPreferences.USER_NAME) + "templatevo";
		return key;
	}
}
