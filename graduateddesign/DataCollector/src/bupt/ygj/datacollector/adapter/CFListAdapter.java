package bupt.ygj.datacollector.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.Item;
import bupt.ygj.datacollector.data.WorkSubmitVO;

public class CFListAdapter  extends BaseAdapter {

	
	private List<WorkSubmitVO> visitItemList;
	private LayoutInflater inflater;
	private Context context;

	public CFListAdapter(Context context, List<WorkSubmitVO> list) {
		this.visitItemList = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return visitItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return visitItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		holder = new ViewHolder();
		View view = inflater.inflate(R.layout.item_cflist, null);
		holder.nameTextView1 = (TextView)view.findViewById(R.id.typevalue1);
		holder.nameTextView2 = (TextView)view.findViewById(R.id.typevalue2);
		holder.nameTextView3 = (TextView)view.findViewById(R.id.typevalue3);
		holder.nameTextView4 = (TextView)view.findViewById(R.id.typevalue4);
		holder.image = (ImageView)view.findViewById(R.id.image);
		holder.line = (TextView)view.findViewById(R.id.bottom_line);
		
		List<String> values = new ArrayList<String>();
		
		List<Item> listItem = visitItemList.get(position).getItemlist();
		if(null != listItem) {
			for(Item item : listItem) {
				String value = item.getDisplayvalue();
				if(null != value) {
					values.add(value);
				}
			}
		}
		
		if(getCount() == 1) {
			view.setBackgroundResource(R.drawable.list_item_selector);
		} else {
			if(position == 0) {
				view.setBackgroundResource(R.drawable.list_item_selector);
			} else if(position == getCount() -1) {
				view.setBackgroundResource(R.drawable.list_item_selector);
				holder.line.setVisibility(View.INVISIBLE);
			} else {
				view.setBackgroundResource(R.drawable.list_item_selector);
			}
		}
		
		addValueToView(values.size(),values,holder);
		return view;
	}
	
	private void addValueToView(int size,List<String> values,ViewHolder holder) {
		switch (size) {
		case 1:
			holder.nameTextView1.setText(values.get(0));
			break;
		case 2:
			holder.nameTextView1.setText(values.get(0));
			holder.nameTextView2.setText(values.get(1));
			break;
		case 3:
			holder.nameTextView1.setText(values.get(0));
			holder.nameTextView2.setText(values.get(1));
			holder.nameTextView3.setText(values.get(2));
			break;
		case 4:	
			holder.nameTextView1.setText(values.get(0));
			holder.nameTextView2.setText(values.get(1));
			holder.nameTextView3.setText(values.get(2));
			holder.nameTextView4.setText(values.get(3));
		}
	}

	class ViewHolder {
		TextView nameTextView1;
		TextView nameTextView2;
		TextView nameTextView3;
		TextView nameTextView4;
		ImageView image;
		TextView line ;
	}
}
