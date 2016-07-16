package com.example.metrorecord;

import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;  

public class SpeedRecord extends ListFragment{
    public ListView list ;  
    public int current_line=0;
    static public int current_item=0;
    
	private List<Map<String, Object>> mData; 
    private SimpleAdapter adapter;  
    //this size of int is 17,beacuse there is 16 lines in Shanghai,but there is no line 0
    int[] number2resource = new int[17];
    public String[] station_info;
    public boolean[] halt_or_not;
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
    	View view = inflater.inflate(R.layout.fragment_main, container,false); 
    	list = (ListView) view.findViewById(android.R.id.list);
        return view;  
    }
    //this is used to map the position of drawer to the resource of string
    private void init_map(){
    	number2resource[5] = R.string.line_5;
    	number2resource[1] = R.string.line_1;	
    }
    
    private int get_resource(){
    	return number2resource[current_line];
    }
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        Bundle b = savedInstanceState;  
        init_map();
        Log.i("listview", "--------onCreate");
        String line_info = this.getString(get_resource());
        String[] list = line_info.split(";");
        station_info = new String[list.length*2];
        halt_or_not = new boolean[list.length];
        for (int i=0;i<list.length;i++){
        	halt_or_not[i]=false;
        }
        mData = getData(list);
        adapter = new my_adapter(getActivity(), mData, R.layout.item, new String[]{"title"}, new int[]{R.id.title});  
        setListAdapter(adapter);  
    }  
    
    @Override  
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(2);
	}
    
    private List<Map<String, Object>> getData(String[] strs) {  
        List<Map<String ,Object>> list = new ArrayList<Map<String,Object>>();  
        for (int i = 0; i < strs.length; i++) {  
            Map<String, Object> map = new HashMap<String, Object>();  
            map.put("title", strs[i]); 
            list.add(map);  
        }
        return list;  
    }
    
    private class my_adapter extends SimpleAdapter{
    	
    	DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
        public final class ViewHolder { 
        	public TextView name;
            public TextView start_time;  
            public TextView stop_time;  
            public Button Btn_start;
            public Button Btn_stop;
            public Button Btn_halt;
        }
        
		private LayoutInflater mInflater;  
        public my_adapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.mInflater = LayoutInflater.from(context);
		}
        
        @Override  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return mData.size();  
        }  
  
        @Override  
        public Object getItem(int position) {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public long getItemId(int position) {  
            // TODO Auto-generated method stub  
            return 0;  
        }  
        @Override  
        public View getView(final int position, View convertView, ViewGroup parent) {  
            ViewHolder holder = null;  
            if (convertView == null) {  
                holder=new ViewHolder();    
                convertView = mInflater.inflate(R.layout.item, null);
                holder.name = (TextView)convertView.findViewById(R.id.title);
                holder.start_time = (TextView)convertView.findViewById(R.id.start_time);  
                holder.stop_time = (TextView)convertView.findViewById(R.id.stop_time);  
                holder.Btn_start = (Button)convertView.findViewById(R.id.start);
                holder.Btn_stop = (Button)convertView.findViewById(R.id.stop);
                holder.Btn_halt = (Button)convertView.findViewById(R.id.halt);
                convertView.setTag(holder);               
            }else {               
                holder = (ViewHolder)convertView.getTag();  
            }         
            
            holder.name.setText((String)mData.get(position).get("title"));
            final TextView mstart = holder.start_time;
            final TextView mstop = holder.stop_time;
            
            mstart.setText(station_info[position*2]); 
            mstop.setText(station_info[position*2+1]);
        	if (halt_or_not[position]){
        		mstart.setTextColor(Color.RED);
        	}else{
        		mstart.setTextColor(0xffffffff);
        	}
        	
            holder.Btn_start.setTag(position);  
            holder.Btn_stop.setTag(position);  
            holder.Btn_halt.setTag(position);
            //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉  
            holder.Btn_start.setOnClickListener(new View.OnClickListener() {  
                @Override  
                public void onClick(View v) { 
                	Date date=new Date();
                	String time=format.format(date);
                	if (station_info[position*2] == null){
                		mstart.setText(time);
                		station_info[position*2] = time;
                	}else{
                		AlertDialog.Builder builder = new Builder(getActivity());
                		builder.setTitle("Warning You are gong to override the time");  
                		builder.setPositiveButton("确认", new OnClickListener() {   
							@Override
							public void onClick(DialogInterface dialog, int which) {
			                	Date date=new Date();
			                	String time=format.format(date);
		                		mstart.setText(time);
		                		station_info[position*2] = time;
								dialog.dismiss();
							}
                		});  
                		builder.setNegativeButton("取消", new OnClickListener() {   
                			@Override
                			public void onClick(DialogInterface dialog, int which) {
                			    dialog.dismiss();
                			}
                		});  
                		builder.create().show();
                	}
                }  
            });  
             
            holder.Btn_stop.setOnClickListener(new View.OnClickListener() {  
                @Override  
                public void onClick(View v) { 
                	Date date=new Date();
                	String time=format.format(date);
                	if (station_info[position*2+1] == null){
                		mstop.setText(time);
                		station_info[position*2+1] = time;
                	}else{
                		AlertDialog.Builder builder = new Builder(getActivity());
                		builder.setTitle("Warning You are gong to override the time");  
                		builder.setPositiveButton("确认", new OnClickListener() {   
							@Override
							public void onClick(DialogInterface dialog, int which) {
			                	Date date=new Date();
			                	String time=format.format(date);
		                		mstop.setText(time);
		                		station_info[position*2+1] = time;
								dialog.dismiss();
							}
                		});  
                		builder.setNegativeButton("取消", new OnClickListener() {   
                			@Override
                			public void onClick(DialogInterface dialog, int which) {
                			    dialog.dismiss();
                			}
                		});  
                		builder.create().show();
                	}
                }  
            }); 
            
            holder.Btn_halt.setOnClickListener(new View.OnClickListener() {  
                @Override  
                public void onClick(View v) { 
                	if (!halt_or_not[position]){
                		mstart.setTextColor(Color.RED);
                	}else{
                		mstart.setTextColor(0xffffffff);
                	}
                	halt_or_not[position] = !halt_or_not[position];
                }  
            }); 
            return convertView;  
        }  
    }  
}
