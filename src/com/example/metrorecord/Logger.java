package com.example.metrorecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	public BufferedWriter out;
	DateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
	public Logger(String[] info,boolean[] halt,String path){
    	Date date=new Date();
    	String time=format.format(date);
		try {
			out = new BufferedWriter(new FileWriter(path+time+".txt",true));
			int length = info.length;
			for (int i=0;i<length/2;i++){
				out.write(info[i*2]+";"+info[i*2+1]+";"+halt[i]+";");
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
