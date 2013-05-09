package vivelab.provider;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter{
	
	private Context context;
	private List<String> list;
	private static LayoutInflater inflater;
	
	public CustomAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// define row layout
		View rowView = inflater.inflate(R.layout.row, parent, false);
		rowView.setOnLongClickListener(new onListItemLongClickListener(position));
		
		// change text representation
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		textView.setText(getItem(position));
		
		return rowView;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private class onListItemLongClickListener implements OnLongClickListener{       
	    
		private int position;
	    
		onListItemLongClickListener(int position){
	        this.position = position;
	    }

		@Override
		public boolean onLongClick(View v) {
			Toast.makeText(context, "onLongClick: " + getItem(position), Toast.LENGTH_LONG).show();
			return false;
		}    
	}
	
}
