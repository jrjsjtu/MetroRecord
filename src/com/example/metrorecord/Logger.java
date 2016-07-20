package com.example.metrorecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class Logger {
	public BufferedWriter out;
	DateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
	String[] name_list;
	String[] info;
	boolean[] halt;
	String[] distance;
	String path;
	String line_number;
	public Logger(String mnumber,SpeedRecord tmp,String mpath){
		name_list = tmp.name_list;
		info = tmp.station_info;
		halt = tmp.halt_or_not;
		distance = tmp.distance_info;	
		path = mpath;
		line_number = mnumber;
		write_data();
	}
	
	private synchronized void write_data(){
    	Date date=new Date();
    	String time=format.format(date);
    	DateFormat mformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			out = new BufferedWriter(new FileWriter(path+"/"+time+".txt",true));
			int length = info.length;
			out.write(line_number+";"+info.length/2+"\r\n");
			for (int i=0;i<length/2;i++){
				out.write(name_list[i]+" "+name_list[i+1]+";");
				out.write(info[i*2]+";"+info[i*2+1]+";"+halt[i]+";");
				out.write(distance[i]+";");
				if (info[i*2]!=null&&info[i*2+1]!=null){
					Date date1 = mformat.parse(info[i*2]);
					Date date2 = mformat.parse(info[i*2+1]);
					Log.d("time", date2.getTime()+"");
					Log.d("time", date1.getTime()+"");
					Double result = 1000000*Double.parseDouble(distance[i])/(date2.getTime()-date1.getTime());
					out.write(result.toString());
				}else{
					out.write("0");
				}
				out.write("\r\n");
				out.flush();
			}
			out.write("\r\n");
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
