package com.gj.diary.utils;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DiaryUtil {

	public static Properties properties;
	
	public static String password;

	public static final String outerConfigPath = System.getProperty( "user.dir" ) + "/config/";
	
	private static final String outerConfigName = "diaryConfig.properties";
	
	
	public static final Integer W = 2080;//缩放后宽
	public static final Integer H = 1168;//缩放后高

	public static void setLocationCenter(Container container){
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
		int screenHeight = screenSize.height / 2; // 获取屏幕的高
		int height = container.getHeight();
		int width = container.getWidth();
		container.setLocation( screenWidth - width / 2, screenHeight - height / 2 );
	}
	
	/**
	 * 得到属性值
	 * 
	 * @param name
	 * @return String
	 * @author jin.guo 2016年5月30日
	 */
	public static String getProperties( String name ) {
		if( properties == null ) {
			properties = new Properties();
			try {
				InputStream inputStream = null;
				File file = new File( outerConfigPath );
				if( file.exists() ) {
					 file = new File( outerConfigPath + outerConfigName);
					if(file.exists()){
						inputStream = new FileInputStream( file );
						properties.load( inputStream );
						inputStream.close();
					}
				} 
			} catch( IOException e1 ) {
				e1.printStackTrace();
			}
		}
		return properties.getProperty( name );
	}

	/**
	 * 修改属性值
	 * 
	 * @param name
	 * @param newValue void
	 * @author jin.guo 2016年5月30日
	 */
	public static boolean modfiyProperties( String name, String newValue ) {
		boolean flag = true;
		String value = getProperties( name );
		if( value != null && !"".equals( value ) ) {
			properties.setProperty( name, newValue );
			flag = modfiyProperties( properties );
		}
		return flag;
	}

	public static boolean modfiyProperties( Properties properties ) {
		try {
			File file = new File( outerConfigPath );
			if(!file.exists() ) {
				if(file.mkdirs()){
					file = new File( outerConfigPath +outerConfigName);
					if(!file.createNewFile()){
						return false;
					}
				}else{
					return false;
				}
				properties.setProperty( "password", MD5Util.getMd532( "00000000"+ImageUtil.salt ) );
				properties.setProperty( "passwordPhoto", MD5Util.getMd532( "00000000"+ImageUtil.salt ) );
				
			}else{
				file = new File( outerConfigPath +outerConfigName);
				if(!file.exists()){
					if(file.createNewFile()){
						return false;
					}
				}
			}
			FileOutputStream fileOutputStream = new FileOutputStream( file );
			properties.store( fileOutputStream, "修改配置文件" );
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch( Exception e ) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 得到等比缩放后图片的宽高
	 * 
	 * @param iconWidth
	 * @param iconHeight
	 * @param labelWidth
	 * @param labelHeight
	 * @return Map<String,Integer>
	 * @author jin.guo 2016年5月30日
	 */
	public static Map<String, Integer> getImageWH( int iconWidth, int iconHeight, int labelWidth, int labelHeight ) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		if( iconWidth >= labelWidth && iconHeight >= labelHeight ) {
			double labelRate = 1.0 * iconWidth / iconHeight;
			double iconRate = 1.0 * labelWidth / labelHeight;
			if( labelRate >= iconRate ) {
				returnMap.put( "width", labelWidth );
				returnMap.put( "height", iconHeight * labelWidth / iconWidth );
			} else {
				returnMap.put( "width", iconWidth * labelHeight / iconHeight );
				returnMap.put( "height", labelHeight );
			}
		} else if( iconWidth <= labelWidth && iconHeight <= labelHeight ) {
			returnMap.put( "width", iconWidth );
			returnMap.put( "height", iconHeight );
		} else if( iconWidth >= labelWidth && iconHeight <= labelHeight ) {
			returnMap.put( "width", labelWidth );
			returnMap.put( "height", iconHeight * labelWidth / iconWidth );
		} else if( iconWidth <= labelWidth && iconHeight >= labelHeight ) {
			returnMap.put( "width", iconWidth * labelHeight / iconHeight );
			returnMap.put( "height", labelHeight );
		}

		return returnMap;
	}

	/**
	 * 日期操作
	 * 
	 * @return String
	 * @author jin.guo 2016年5月30日
	 */
	public static String getDateString() {
		return getDateString( new Date(), "yyyy-MM-dd" );
	}

	public static String getDateString( String format ) {
		return getDateString( new Date(), format );
	}

	public static String getDateString( Date data, String format ) {
		SimpleDateFormat sdf = new SimpleDateFormat( format );
		return sdf.format( data );
	}

	public static Date getDate( String dateString ) {
		return getDate( dateString, "yyyy-MM-dd" );
	}

	public static Date getDate( String dateString, String format ) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat( format );
		try {
			date = sdf.parse( dateString );
		} catch( ParseException e ) {
			e.printStackTrace();
			return date;
		}
		return date;
	}

	public static Date getDateAdd( String dateString, int num ) {
		Date nowDate = getDate( dateString );
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime( nowDate );
		rightNow.add( Calendar.DAY_OF_YEAR, num );// 日期加num天
		return rightNow.getTime();
	}
	
	
	public static String getMd5String(String password){
		return MD5Util.getMd532( password+ImageUtil.salt );
	}
	
	public static boolean checkPassword(String password,String model){
		if(password != "" && password != null){
			String oldPassword = "";
			if("1".equals( model )){
				oldPassword = getProperties( "password" );
			}else{
				oldPassword = getProperties( "passwordPhoto" );
			}
			if(getMd5String( password).equalsIgnoreCase( oldPassword )){
				DiaryUtil.password = password;
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * 得到日记图片内信息
	 * 
	 * @param pathName
	 * @return String
	 * @author jin.guo 2016年5月30日
	 */
	public static String getDiaryMessage( String pathName ) {
		String returnMessage = "";
		RandomAccessFile rf = null;
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
					if( "=======================================================================".equals( line ) ) {
						break;
					}
					if( line != null ) {
						returnMessage = new String( line.getBytes( "ISO-8859-1" ), "GBK" ) + "\r\n" + returnMessage;
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
		} catch( Exception e ) {
			e.printStackTrace();
		}
		finally {
			try {
				if( rf != null ) {
					rf.close();
				}
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		return returnMessage;
	}

}
