package bupt.ygj.datacollector.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.ReferCommonVO;

public class MaterialReferListAdapter extends BaseAdapter {
	private List<ReferCommonVO> stafflist;
	private LayoutInflater inflater;

	public MaterialReferListAdapter(Context context, List<ReferCommonVO> list) {
		this.stafflist = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return stafflist.size();
	}

	@Override
	public Object getItem(int position) {
		return stafflist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		View view = inflater.inflate(R.layout.item_mrefer_list, null);
		holder.nameTextView = (TextView) view.findViewById(R.id.materialname);
		holder.departmentTextView = (TextView) view.findViewById(R.id.materialcode);
		holder.image = (TextView) view.findViewById(R.id.refer_selectedicon);
		ReferCommonVO refer = stafflist.get(position);
		String name = refer.getName();
		String code = refer.getCode();
		
		if(null != name)
			holder.nameTextView.setText(name);
		if(null != code) 
			holder.departmentTextView.setText(code);
		view.setTag(holder);
		
		return view;
	}

	class ViewHolder {
		TextView nameTextView;
		TextView departmentTextView;
		TextView image;
	}

}
