package com.gj.diary.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@SuppressWarnings("restriction")
public class ImageUtil {
	
	public final static String MESSAGE_LINE= "=======================================================================";
	
	public final static String PHOTO_LINE= "***********************************************************************";
	
	public static final String salt = "diary";
	
	public static void coptFile( String fileName,File destFile ) throws IOException {
		File file = new File( fileName );// 读入文件
		Image image = ImageIO.read( file ); // 构造Image对象
		int width = image.getWidth( null ); // 得到源图宽
		int height = image.getHeight( null ); // 得到源图长
		
		Map<String, Integer> imageWH = DiaryUtil.getImageWH( width, height, DiaryUtil.W, DiaryUtil.H );
		int w = imageWH.get( "width" );
		int h = imageWH.get( "height" );
			BufferedImage bufferedImage = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB );
			bufferedImage.getGraphics().drawImage( image.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH), 0, 0,w ,h , null ); // 绘制缩小后的图
			FileOutputStream out = new FileOutputStream( destFile ); // 输出到文件流
			// 可以正常实现bmp、png、gif转jpg
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( out );
			encoder.encode( bufferedImage ); // JPEG编码
			out.flush();
			out.close();
//		}
		
	}
	
	public static void writeMessage(File file,String text,Integer type) throws Exception{
		text = "2050年的自己：\n"+text;
		String key = MD5Util.getMd532( text );
		BufferedWriter bufferedWriter = null;
		bufferedWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( file, true ),"UTF-8"));
		bufferedWriter.newLine();
		bufferedWriter.write( MESSAGE_LINE );
		bufferedWriter.newLine();
		if(1 == type){
			bufferedWriter.write(key +"TRUE");
		}else if(2 == type){
			bufferedWriter.write(key +"TRUE1");
		}else{
			bufferedWriter.write(key);
		}
		
		bufferedWriter.newLine();
		bufferedWriter.write( MESSAGE_LINE );
		String newkey = salt.toUpperCase()+key;
		for(String msg : text.split( "\n" )){
			bufferedWriter.newLine();
			bufferedWriter.write(DESUtil.encrypt( msg, newkey.substring( 0, 24 ).getBytes("UTF-8"),"base64") );
		}
		bufferedWriter.close();
		file.setReadOnly();
	}
	
	public static Map<String,String> getMessage(String pathName){
		 Map<String,String> returnMap = new HashMap<>();
		String returnMessage = "";
		String key = "";
		RandomAccessFile rf = null;
		int flag = 0;
		List<String> messages = new ArrayList<>();
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
					if( MESSAGE_LINE.equals( line ) ) {
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
				String decrypt = new String(DESUtil.decrypt(msg, key.substring(0, 24).getBytes("UTF-8"),"base64"),"UTF-8");
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
	
	public static void writeSplitMessage(String splitText, File desFile,String text) throws Exception{
		text = "2050年的自己：\n"+text;
		String key = MD5Util.getMd532( text );
		BufferedWriter bufferedWriter = null;
		bufferedWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( desFile,true ) ) );
		bufferedWriter.newLine();
		bufferedWriter.write( PHOTO_LINE );
		bufferedWriter.newLine();
		String newKey = salt.toUpperCase()+key.substring(8+salt.length());
		bufferedWriter.write( DESUtil.encrypt( splitText, newKey.getBytes("UTF-8") ,"base64"));
		bufferedWriter.newLine();
		bufferedWriter.write( PHOTO_LINE );
		bufferedWriter.flush();
		bufferedWriter.close();
	}
	
	public static void writePhotoMessage(File tempFile,File desFile,String text) throws Exception{
		text = "2050年的自己：\n"+text;
		String key = MD5Util.getMd532( text );
		FileInputStream in = new FileInputStream( tempFile);
		BufferedWriter bufferedWriter = null;
		bufferedWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( desFile,true ) ) );
		@SuppressWarnings("unused")
		int c;
		byte buffer[] = new byte[1024*5];
		bufferedWriter.newLine();
		bufferedWriter.write( PHOTO_LINE );
		String newKey = salt.toUpperCase()+key.substring(8+salt.length());
		while( ( c = in.read( buffer ) ) != -1 ) {
			bufferedWriter.newLine();
			bufferedWriter.write( DESUtil.encrypt( buffer, newKey.getBytes("UTF-8"),"base64") );
		}
		bufferedWriter.newLine();
		bufferedWriter.write( PHOTO_LINE );
		in.close();
		bufferedWriter.flush();
		bufferedWriter.close();
	}
	
	public static File getPhotoMessage(String readPath,String key) throws Exception{
		if(key == null || "".equals( key )){
			return null;
		}
		key = salt.toUpperCase()+key.substring(8+salt.length(),32); 
		BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(readPath) ) );
		File file = new File(DiaryUtil.outerConfigPath+"tempPhoto" );
		if( !file.exists() ) {
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream( file );
		String line = null;
		boolean flag = false;
		while( ( line = reader.readLine() ) != null ) {
			if(flag && line.equals( PHOTO_LINE)){
				break;
			}
			if(flag){
				out.write( DESUtil.decrypt( line, key.getBytes("UTF-8"),"base64" ) );
			}
			if(!flag && line.equals( PHOTO_LINE )){
				flag =true;
			}
		}
		reader.close();
		out.flush();
		out.close();
		return file;
		 
	}
	
	public static void main( String[] args ) throws Exception {
//		File destFile = new File("f://tem.jpg");
//		ImageUtil.coptFile( "f://2016-05-10.jpg", destFile );
//		String diaryMessage = DiaryUtil.getDiaryMessage(  "f://2016-05-10.jpg" );
//		ImageUtil.writeMessage(destFile,diaryMessage);

//		String message = ImageUtil.getMessage( "f://tem.jpg" );
//		System.out.println( message );
//		
//		File rootFile = new File( "F:\\日记\\日记\\picture\\2016" );
//		File[] roots = rootFile.listFiles();
//		for(File file : roots){
//			for(File file1 : file.listFiles()){
//				String fileName = "f://temp//"+file1.getName();
//				File destFile = new File(fileName);
//				ImageUtil.coptFile( file1.getPath(), destFile );
//				String diaryMessage = DiaryUtil.getDiaryMessage(file1.getPath());
//				ImageUtil.writeMessage(destFile,diaryMessage);
//			}
//		}
//		String returnMessage="";
//		String msg = "CB7B46347705F063295462350327B3754429584652706EF7593CAB37406CAD6DF9D42179949527F036EA29B62F088F313B223FAB3F9C4A3A42D561F3BACB633D6AAA8A84681C5936082777E1FB05C22EB1CFF5BEFF0AE5BF9ABDF01A1F30B3C03481C1F9A702AB7E7E0D7EF318C99B744A11B9BA9FC28BD1163E1FC4054B6B542C82EE3AFF6C5E708B2528E4C78BEE657F2B4537FF42CB9D791C0AEE348888914FB33FC2DB60A39FF43D03910723A2E58D5B4716F0D191512914D8EFA29FE4B384D0E2D5B6F1C34AA7C9F8618C41C11809F7F614ECF6F49C0407FB6207CA4C64";
//        String key ="13E0787ED11A1E4740F0FCA11AE27B63TRUE";
//        returnMessage = DESUtil.Decrypt( msg, key.substring( 0, 24 ).getBytes("UTF-8") )+"\n"+returnMessage;
//        System.out.println( returnMessage );
		
//		ImageUtil.getPhotoMessage("F:\\日记\\日记\\picture\\2016\\2016-06\\2016-06-05.jpg","6D56BA95C8EC762F45C800904AF852B1TRUE");
		File fromFile = new File("D:/diaryPicture/background.jpg");
		File toFile =  new File("D:/diaryPicture/background1.jpg");
		resizePng(fromFile,toFile,100,100,true);
	}
	
	 public static void resizePng(File fromFile, File toFile, int outputWidth, int outputHeight,  
	            boolean proportion) {  
	        try {  
	            BufferedImage bi2 = ImageIO.read(fromFile);  
	            int newWidth;  
	            int newHeight;  
	            // 判断是否是等比缩放  
	            if (proportion) {  
	                // 为等比缩放计算输出的图片宽度及高度  
	                double rate1 = ((double) bi2.getWidth(null)) / (double) outputWidth + 0.1;  
	                double rate2 = ((double) bi2.getHeight(null)) / (double) outputHeight + 0.1;  
	                // 根据缩放比率大的进行缩放控制  
	                double rate = rate1 < rate2 ? rate1 : rate2;  
	                newWidth = (int) ((bi2.getWidth(null)) / rate);  
	                newHeight = (int) ((bi2.getHeight(null)) / rate);  
	            } else {  
	                newWidth = outputWidth; // 输出的图片宽度  
	                newHeight = outputHeight; // 输出的图片高度  
	            }  
	            BufferedImage to = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);  
	            Graphics2D g2d = to.createGraphics();  
	            to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight,  
	                    Transparency.TRANSLUCENT);  
	            g2d.dispose();  
	            g2d = to.createGraphics();  
	            @SuppressWarnings("static-access")  
	            Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);  
	            g2d.drawImage(from, 0, 0, null);  
	            g2d.dispose();  
	            ImageIO.write(to, "png", toFile);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    } 
	
}
