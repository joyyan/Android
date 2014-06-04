package bupt.ygj.datacollector.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.MessageGroupVO;
import bupt.ygj.datacollector.util.AddressGroupVO;
import bupt.ygj.datacollector.util.SortModelVO;

public class AddressListAdapter extends BaseExpandableListAdapter implements SectionIndexer {

	private List<SortModelVO> list = null;
	private List<AddressGroupVO> groupList = new ArrayList<AddressGroupVO>();
	
	private List<MessageGroupVO> messageGroupList;	//关于分组的List，每个元素代表一个分组
	private LayoutInflater layoutInflater;
	private Context context;
	private int paddingLeft;
	private int groupNameFontSize;
	
	public AddressListAdapter(Context context, List<SortModelVO> list) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.list = list;
		VOAdapter(list);
		paddingLeft = context.getResources().getDimensionPixelSize(R.dimen.commonPadding);
	}
	
	private void VOAdapter(List<SortModelVO> list) {
		String curLetter = "";
		AddressGroupVO group  = null ;
		List<SortModelVO> listVO = null ;
		for(SortModelVO vo : list) {
			String letter = vo.getSortLetters();
			if(letter != null && letter.equals(curLetter)) {	//是否是新字母，因为对参数list已经排序，所以不会出现重复的分组
				listVO.add(vo);
			} else if(letter != null) {	//出现了新的分组
				curLetter = letter;
				group = new AddressGroupVO();
				group.setGroupname(curLetter);
				listVO = new ArrayList<SortModelVO>();
				group.setList(listVO);
				groupList.add(group);		//
			}
		}
		
	}
	
	//获取分组数量
	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	//获取某个分组中的消息数量
	@Override
	public int getChildrenCount(int groupPosition) {
		return list.size();
	}

	//获取某个分组的相关数据
	@Override
	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition).getGroupname();
	}

	//获取某一条信息
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groupList.get(groupPosition).getList().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {//获取分组的id
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	/**
	 * 返回分组样式
	 * @param groupPosition
	 * @param isExpanded
	 * @param convertView
	 * @param parent
	 * @return
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		TextView groupNameTextView = new TextView(context);
		groupNameTextView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, groupNameFontSize));
		groupNameTextView.setPadding(paddingLeft, 0, 0, 0);
		groupNameTextView.setText(groupList.get(groupPosition).getGroupname());
		groupNameTextView.setTextColor(context.getResources().getColor(R.color.groupname));
		return groupNameTextView;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		AddressGroupVO groupData = groupList.get(groupPosition);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.item_addresslist, null);
			viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < list.size(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getSortLetters().charAt(0);
	}
}
