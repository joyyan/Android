package bupt.ygj.datacollector.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bupt.ygj.datacollector.R;
import bupt.ygj.datacollector.data.ReferCommonVO;

public class ReferValuesAdapter  extends BaseAdapter {

	
	private List<ReferCommonVO> visitItemList;
	private LayoutInflater inflater;
	private Context context;

	public ReferValuesAdapter(Context context, List<ReferCommonVO> list) {
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
		View view = inflater.inflate(R.layout.item_refervalues, null);
		holder.nameTextView = (TextView)view.findViewById(R.id.refervaluename);
		holder.imageView = (ImageView)view.findViewById(R.id.refervalueimage);
		
		ReferCommonVO refer = visitItemList.get(position);
		
		if(refer.getName() != null)
			holder.nameTextView.setText(refer.getName());
		if(refer.getIsSelected()) {
			holder.imageView.setVisibility(View.VISIBLE);
		} else
			holder.imageView.setVisibility(View.GONE);
		return view;
	}

	class ViewHolder {
		TextView nameTextView;
		ImageView imageView;
	}
}
