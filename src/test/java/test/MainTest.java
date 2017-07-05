package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gj.diary.utils.DESUtil;
import com.gj.diary.utils.ImageUtil;
import com.gj.diary.utils.MD5Util;

public class MainTest {
	private static String salt = "diary";

	public static void main( String[] args ) throws Exception {
		File file = new File("E:\\日记\\picture\\2017\\2017-07\\");
		getFile(file);
		
	}
	
	public static void getFile(File file){
		File[] listFiles = file.listFiles();
		for(File temp : listFiles){
			if(temp.isDirectory()){
				getFile(temp);
			}else{
				if(temp.getName().endsWith("jpg")){
					try {
						makePicture(temp);
						System.out.println(temp.getName()+"处理完成");
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(temp.getName()+"处理失败");
					}
				}
			}
		}
	}
	
	private static void makePicture(File file) throws Exception{
		Map<String, String> map = ImageUtil.getMessage(file.getPath());
		String message = map.get("message");
		String key = map.get("key");
		int type = 0;
		if(key.endsWith("TRUE")){
			type = 1;
		}else if(key.endsWith("TRUE1")){
			type = 2;
		}
		File newFile = new File(file.getParentFile(),file.getName()+".tem.jpg");
		newFile.createNewFile();
		FileInputStream reader =  new FileInputStream(file.getPath());
		String line = null;
		FileOutputStream out = new FileOutputStream( newFile );
		
		byte[] b=new byte[1];
		while(reader.read(b)!=-1){
		    String string = new String(b);
		    if(type == 0 && string.equals("=")){
		    	byte[] c=new byte[ImageUtil.PHOTO_LINE.length()-10];
		    	reader.read(c);
		    	if(new String(c).contains("===============================")){
		    		break;
		    	}else{
		    		out.write(b);
		    		out.write(c);
		    		continue;
		    	}
		    }
		    if(type != 0 && string.equals("*") ){
		    	byte[] c=new byte[ImageUtil.PHOTO_LINE.length()-10];
		    	reader.read(c);
		    	if(new String(c).contains("****************************")){
		    		break;
		    	}else{
		    		out.write(b);
		    		out.write(c);
		    		continue;
		    	}
		    }
		    out.write(b);
	    }
		reader.close();
		out.flush();
		out.close();
		if(type == 1){
			File photo = ImageUtil.getPhotoMessage(file.getPath(), key);
			writePhotoMessage(photo,newFile,message);
		}
		if(type == 2){
			String spiltMessage = getSpiltMessage(file.getPath(), key);
			writeSplitMessage(spiltMessage,newFile,message);
		}
		writeMessage(newFile,message,type);
		
		
		FileInputStream in = new FileInputStream( newFile);
		file.setWritable(true);
		
		FileWriter fileWriter =new FileWriter(file);
        fileWriter.write("");
        fileWriter.flush();
        fileWriter.close();
		
		FileOutputStream out1 =new FileOutputStream( file,false );
		byte[] bb=new byte[1];
		while(in.read(bb)!=-1){
			out1.write(bb);
	    }
		file.setLastModified(file.lastModified());
		out1.flush();
		out1.close();
		in.close();
		newFile.delete();
	}
	
	public static String getSpiltMessage(String readPath, String key) throws IOException{
		if(key == null || "".equals( key )){
			return null;
		}
		key = key.substring( 8,32 );
		BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(readPath) ) );
		String returnString="";
		String line = null;
		boolean flag = false;
		while( ( line = reader.readLine() ) != null ) {
			if(flag && line.equals( ImageUtil.PHOTO_LINE)){
				break;
			}
			if(flag){
				returnString +=  DESUtil.decrypt( line, key.getBytes("UTF-8"),null);
			}
			if(!flag && line.equals( ImageUtil.PHOTO_LINE )){
				flag =true;
			}
		}
		reader.close();
		return returnString;
	}
	
	
	public static void writeMessage(File file,String text,Integer type) throws Exception{
		String key = MD5Util.getMd532( text );
		BufferedWriter bufferedWriter = null;
		bufferedWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( file, true ),"UTF-8"));
		bufferedWriter.newLine();
		bufferedWriter.write( ImageUtil.MESSAGE_LINE );
		bufferedWriter.newLine();
		if(1 == type){
			bufferedWriter.write(key +"TRUE");
		}else if(2 == type){
			bufferedWriter.write(key +"TRUE1");
		}else{
			bufferedWriter.write(key);
		}
		bufferedWriter.newLine();
		bufferedWriter.write( ImageUtil.MESSAGE_LINE );
		String newkey = salt.toUpperCase()+key;
		for(String msg : text.split( "\n" )){
			bufferedWriter.newLine();
			bufferedWriter.write(DESUtil.encrypt( msg, newkey.substring( 0, 24 ).getBytes("UTF-8"),"base64"));
		}
		bufferedWriter.flush();
		bufferedWriter.close();
		file.setReadOnly();
	}
	
	public static void writePhotoMessage(File tempFile,File desFile,String text) throws Exception{
		String key = MD5Util.getMd532( text );
		FileInputStream in = new FileInputStream( tempFile);
		BufferedWriter bufferedWriter = null;
		bufferedWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( desFile,true ) ) );
		@SuppressWarnings("unused")
		int c;
		byte buffer[] = new byte[1024*5];
		bufferedWriter.newLine();
		bufferedWriter.write( ImageUtil.PHOTO_LINE );
		String newKey = salt.toUpperCase()+key.substring(8+salt.length());
		while( ( c = in.read( buffer ) ) != -1 ) {
			bufferedWriter.newLine();
			bufferedWriter.write( DESUtil.encrypt( buffer, newKey.getBytes("UTF-8"),"base64") );
		}
		bufferedWriter.newLine();
		bufferedWriter.write( ImageUtil.PHOTO_LINE );
		in.close();
		bufferedWriter.flush();
		bufferedWriter.close();
	}
	
	
	public static void writeSplitMessage(String splitText, File desFile,String text) throws Exception{
		String key = MD5Util.getMd532( text );
		BufferedWriter bufferedWriter = null;
		bufferedWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( desFile,true ) ) );
		bufferedWriter.newLine();
		bufferedWriter.write( ImageUtil.PHOTO_LINE );
		bufferedWriter.newLine();
		String newKey = salt.toUpperCase()+key.substring(8+salt.length());
		bufferedWriter.write( DESUtil.encrypt( splitText, newKey.getBytes("UTF-8"),"base64") );
		bufferedWriter.newLine();
		bufferedWriter.write( ImageUtil.PHOTO_LINE );
		bufferedWriter.flush();
		bufferedWriter.close();
	}
	
	private static Map<String,String> getMessage(String pathName){
		 Map<String,String> returnMap = new HashMap<String,String>();
		String returnMessage = "";
		String key = "";
		RandomAccessFile rf = null;
		int flag = 0;
		List<String> messages = new ArrayList<String>();
		try {
			rf = new RandomAccessFile( pathName, "r" );
			long fileLength = rf.length();
			long start = rf.getFilePointer();// 返回此文件中的当前偏移量
			long readIndex = start + fileLength - 1;
			String line;
			rf.seek( readIndex );// 设置偏移量为文件末尾
			int c = -1;
			while( readIndex > start ) {
				c = rf.read();
				if( c == '\n' || c == '\r' ) {
					line = rf.readLine();
					if( ImageUtil.MESSAGE_LINE.equals( line ) ) {
						flag ++;
					}
					if( line != null ) {
					   if(flag == 1){
						   flag++;
					   }else if(flag == 2){
						   key = line;
						   returnMap.put( "key", key );
						   break;
					   }else{
						   messages.add( line );
					   }
					} else {
						System.out.println( line );
					}
					readIndex--;
				}
				readIndex--;
				rf.seek( readIndex );
				if( readIndex == 0 ) {// 当文件指针退至文件开始处，输出第一行
					System.out.println( rf.readLine() );
				}
			}
			key = key.replaceAll( "\n", "" );
			key = salt.toUpperCase()+key;
			for(String msg :messages){
				msg =msg.replaceAll( "\n", "" );
				String decrypt = new String(DESUtil.decrypt(msg, key.substring(0, 24).getBytes("UTF-8"),"base64"));
				if(decrypt.startsWith(" ")){
					decrypt = "    "+decrypt.trim();
				}
				returnMessage = decrypt+"\n"+returnMessage;
			}
			returnMap.put( "message", returnMessage );
		} catch( Exception e ) {
			e.printStackTrace();
		}finally {
			try {
				if( rf != null ) {
					rf.close();
				}
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		return returnMap;
	}
	
	public static File getPhotoMessage(String readPath,String key) throws Exception{
		if(key == null || "".equals( key )){
			return null;
		}
		key = salt.toUpperCase()+key.substring(8+salt.length(),32); 
		BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(readPath) ) );
		File file = new File("E:\\test\\"+"tempPhoto.jpg" );
		if( !file.exists() ) {
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream( file );
		String line = null;
		boolean flag = false;
		while( ( line = reader.readLine() ) != null ) {
			if(flag && line.equals( ImageUtil.PHOTO_LINE)){
				break;
			}
			if(flag){
				out.write( DESUtil.decrypt( line, key.getBytes("UTF-8"),"base64") );
			}
			if(!flag && line.equals( ImageUtil.PHOTO_LINE )){
				flag =true;
			}
		}
		reader.close();
		out.flush();
		out.close();
		return file;
		 
	}


}
