package com.example.metrorecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	public BufferedWriter out;
	DateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
	public Logger(String line_number,SpeedRecord tmp,String path){
		String[] name_list = tmp.name_list;
		String[] info = tmp.station_info;
		boolean[] halt = tmp.halt_or_not;
		String[] distance = tmp.distance_info;
    	Date date=new Date();
    	String time=format.format(date);
		try {
			out = new BufferedWriter(new FileWriter(path+"/"+time+".txt",true));
			int length = info.length;
			out.write(line_number+";"+info.length/2+"\r\n");
			for (int i=0;i<length/2;i++){
				out.write(name_list[i]+" "+name_list[i+1]+";");
				out.write(info[i*2]+";"+info[i*2+1]+";"+halt[i]+";");
				out.write(distance[i]);
				out.write("\r\n");
				out.flush();
			}
			out.write("\r\n");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
