package com.gj.diary.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.gj.diary.DiaryBrowse;

public class BufferedImageUtil {
	
	private static HashMap<String,HashMap<String,BufferedImage>> bufferImageMap = new HashMap<>();
	
	public static BufferedImage getBufferedImage(String date) throws IOException{
		if(date.endsWith("jpg")){
			date = date.substring(0,10);
		}
		String year = date.substring(0,4);
		String month = date.substring(0,7);
		File file = new File(DiaryBrowse.FILE_PATH+"\\"+year+"\\"+month+".temp");
		if(bufferImageMap.containsKey(month)){//如果含有该月份
			HashMap<String, BufferedImage> hashMap = bufferImageMap.get(month);
			if(!hashMap.containsKey(date)){
				File  photo= new File(DiaryBrowse.FILE_PATH+"\\"+year+"\\"+month+"\\"+date+".jpg");
				createImage(month,file,photo);
			}
		}else{//如果不含有该月份
			bufferImageMap.put(month, new HashMap<String,BufferedImage>());
			File path = new File( DiaryBrowse.FILE_PATH+"\\"+year+"\\"+month);
			if( !file.exists() ) { //如果不存在新建
				file.createNewFile();
				File[] listFiles = path.listFiles();
				for(File photo:listFiles){
					if(photo.isFile() && photo.getName().endsWith("jpg")){
						createImage(month, file, photo);
					}
				}
			}else{//如果存在读取文件
				BufferedReader in = new BufferedReader(new FileReader(file));
				String tempString = "";
				
				while ((tempString = in.readLine()) != null) {
					String imgDate =tempString.substring(0, 10);
					String imgString = tempString.substring(10);
					ByteArrayInputStream stram = new ByteArrayInputStream(Base64.getDecoder().decode(imgString));    
					bufferImageMap.get(month).put(imgDate, ImageIO.read(stram));
		        }
				in.close();
			}
		}
		BufferedImage imageIcon = bufferImageMap.get(month).get(date);
		if(imageIcon == null){
			getBufferedImage(date);
			imageIcon = bufferImageMap.get(month).get(date);
		}
		return imageIcon;
	}

	/**
	 * 生成小图标
	 * @param month  月份
	 * @param file   临时文件
	 * @param photo  照片图片
	 * @throws IOException
	 */
	private static void createImage(String month, File file, File photo) throws IOException {
		String photoName = photo.getName().substring(0,10);
		BufferedImage photoImg = ImageIO.read(photo);
		
		BufferedImage bufferedImage = new BufferedImage( 120,67, BufferedImage.TYPE_INT_RGB );
		bufferedImage.getGraphics().drawImage(photoImg.getScaledInstance(120,67, Image.SCALE_SMOOTH), 0, 0, 120,67, null ); // 绘制缩小后的图
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", byteOut);
		byte[] photoArray = byteOut.toByteArray();
		BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
		String encode =new String(Base64.getEncoder().encode(photoArray));
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(encode);
		encode = m.replaceAll("");
		out.write(photoName+encode);
		out.newLine();
		out.flush();
		out.close();
		bufferImageMap.get(month).put(photoName, photoImg);
	}
	
}
