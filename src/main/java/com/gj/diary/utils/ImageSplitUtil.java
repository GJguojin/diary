package com.gj.diary.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageSplitUtil {

	public static void main(String[] args) throws IOException {
		
		String originalImg = "E:\\test\\test.jpg";
		File file = new File(originalImg);
		Map<String, Object> map = splitImage(file,60,50);
		System.out.println(map.get("codeString"));
		BufferedImage imageBuffer = (BufferedImage) map.get("imageBuffer");
		ImageIO.write(imageBuffer, "jpg", new File("E:\\test\\imgSpilt.jpg"));
		
		originalImg = "E:\\test\\imgSpilt.jpg";
		file = new File(originalImg);
		
		String codeString=(String) map.get("codeString");
		String whString = (String) map.get("whString");
		BufferedImage mergeImage = mergeImage(file,codeString,whString);
		ImageIO.write(mergeImage, "jpg", new File("E:\\test\\imgmerge.jpg"));


	}
	public static BufferedImage mergeImage(String readPath, String key) throws IOException{
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
				returnString += new String( DESUtil.Decrypt1( line, key.getBytes("UTF-8")),"UTF-8");
			}
			if(!flag && line.equals( ImageUtil.PHOTO_LINE )){
				flag =true;
			}
		}
		reader.close();
		BufferedImage mergeImage = mergeImage(new File(readPath),returnString.split("&")[1],returnString.split("&")[0]);
		return mergeImage;
	}
	
	public static BufferedImage mergeImage(File file,String codeString,String whString) throws IOException{
		String[][] code = getCode(codeString);
		int rows = code.length;
		int cols = code[0].length;
		
		BufferedImage[][] imgs = splitImage(file,Integer.parseInt(whString.split("#")[0]),Integer.parseInt(whString.split("#")[1]),null,null);
		BufferedImage[][] newImgs = new BufferedImage[rows][cols] ;
		for (int i=0; i<rows; i++) {
			for(int j=0;j<cols;j++) {
				String[] split =  code[i][j].split("_");
				newImgs[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = imgs[i][j];
			}
		}
		BufferedImage imageBuffer = mergeImage(newImgs);
		return imageBuffer;
		
	}
	
	/**
	 * 分割图片并保存
	 * @param destFile 保存的目的地
	 * @param file     要处理的图片
	 * @param chunkWidth 分割的宽
	 * @param chunkHeight 分割的高
	 * @return
	 * @throws IOException 
	 */
	public static String splitImage(File destFile, File file,Integer chunkWidth, Integer chunkHeight) throws IOException{
		 Map<String, Object> map = splitImage( file,chunkWidth,chunkHeight);
		 BufferedImage imageBuffer = (BufferedImage) map.get("imageBuffer");
		 FileOutputStream out = new FileOutputStream( destFile ); // 输出到文件流
		 JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( out );
	     encoder.encode( imageBuffer ); // JPEG编码
	     out.flush();
	     out.close();
		 
		 String codeString=(String) map.get("codeString");
		 String whString = (String) map.get("whString");
		 return whString+"&"+codeString;
	}
	
	public static Map<String,Object> splitImage(File file, Integer chunkWidth, Integer chunkHeight) throws IOException{
		BufferedImage[][] imgs = splitImage(file,chunkWidth,chunkHeight,null,null);
		int rows = imgs.length;
		int cols = imgs[0].length;
		String[][] code = getCode(rows,cols);
		// 输出小图
		BufferedImage[][] newImgs = new BufferedImage[rows][cols] ;
		for (int i=0; i<rows; i++) {
			for(int j=0;j<cols;j++) {
                //ImageIO.write(imgs[i][j], "jpg", new File("E:\\test\\split\\img" + i + j+".jpg"));
				String[] split =  code[i][j].split("_");
				newImgs[i][j] = imgs[Integer.parseInt(split[0])][Integer.parseInt(split[1])];
			}
		}
		BufferedImage mergeImage = mergeImage(newImgs);
		String codeString = getCodeString(code);
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("codeString", codeString);
		returnMap.put("whString", ""+chunkWidth+"#"+chunkHeight);
		returnMap.put("imageBuffer", mergeImage);
		return returnMap;
	}
	
	

	/**
	 * 
	 * @param file 要分割的图片
	 * @param chunkWidth   分割块的宽度
	 * @return chunkHeight 分割快的高度
	 * @throws IOException
	 */
	private static BufferedImage[][] splitImage(File file, Integer chunkWidth, Integer chunkHeight,Integer rows,Integer cols) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		BufferedImage image = ImageIO.read(fis);
		int width = image.getWidth(); // 原图宽
		int height = image.getHeight();// 原图高
		
		Map<String, Integer> imageWH = DiaryUtil.getImageWH( width, height,  DiaryUtil.W, DiaryUtil.H);
		int w = imageWH.get( "width" );
		int h = imageWH.get( "height" );
		BufferedImage bufferedImage = new BufferedImage( w, h, BufferedImage.SCALE_SMOOTH );
		bufferedImage.getGraphics().drawImage( image, 0, 0,w ,h , null ); // 绘制缩小后的图
		
		image = bufferedImage;
		width = image.getWidth(); // 原图宽
		height = image.getHeight();// 原图高
		
		
		if(chunkWidth != null && chunkHeight != null ){
			rows = height%chunkHeight==0?height/chunkHeight:(int) Math.ceil(1.0 * height / chunkHeight);
			cols = width%chunkWidth==0?width/chunkWidth:(int) Math.ceil(1.0 * width / chunkWidth);
		}else if(rows != null && cols!=null){
			chunkWidth = width%cols==0?width/cols:width/(cols-1);
		    chunkHeight =height%rows==0?height/rows:height/(rows-1);
		}else{
			chunkWidth = 50;
			chunkWidth = 50;
			rows = (int) Math.ceil(1.0 * height / chunkHeight);
			cols = (int) Math.ceil(1.0 * width / chunkWidth);
		}
		

		BufferedImage imgs[][] = new BufferedImage[rows][cols];
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				int smallWidth = 0; //分割后图片的宽
				int smallHeight = 0;//分割后图片的高
				if(rows-x != 1 && cols-y != 1){//非边缘
					smallWidth = chunkWidth;
					smallHeight = chunkHeight;
				}else if(rows-x == 1 && cols-y != 1){//最后一行
					smallWidth = chunkWidth;
					smallHeight = height % chunkHeight==0?chunkHeight:height % chunkHeight;
				}else if(rows-x != 1 && cols-y == 1){//最后一列
					smallWidth =  width % chunkWidth==0?chunkWidth:width % chunkWidth;
					smallHeight = chunkHeight;
				}else{
					smallWidth =  width % chunkWidth==0?chunkWidth: width % chunkWidth;
					smallHeight = height % chunkHeight==0?chunkHeight:height % chunkHeight;
				}
				// 设置小图的大小和类型
				imgs[x][y] = new BufferedImage(smallWidth, smallHeight, image.getType());
				// 写入图像内容
				Graphics2D gr = imgs[x][y].createGraphics();
				//gr.drawImage(image, 0, 0, smallWidth, smallHeight, smallWidth * y, smallHeight * x, smallWidth * y + smallWidth, smallHeight * x + smallHeight, null);
				gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + smallWidth, chunkHeight * x + smallHeight, null);
				gr.dispose();
			}
		}
		return imgs;
	}

	
	/**
	 * 合并图片
	 * @param imgs
	 * @return
	 * @throws IOException
	 */
	private static BufferedImage mergeImage(BufferedImage[][] imgs) throws IOException {
		int imageWidth = 0;//原图宽
		int imageHeight = 0;//原图高
		for(int i=0;i<imgs[0].length;i++){
			imageWidth += imgs[0][i].getWidth();
		}
		for (BufferedImage[] img : imgs) {
			imageHeight += img[0].getHeight();
		}
		int chunkWidth = imgs[0][0].getWidth();
		int chunkHeight = imgs[0][0].getHeight();
		int type = imgs[0][0].getType();
		int rows = imgs.length;
		int cols = imgs[0].length;
		// 设置拼接后图的大小和类型
		BufferedImage finalImg = new BufferedImage(imageWidth, imageHeight, type);
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				finalImg.createGraphics().drawImage(imgs[i][j], chunkWidth * j, chunkHeight * i, null);
			}
		}
		return finalImg;
	}
	
	//得到二维随机数列
	private static String[][] getCode(int rows,int cols){
		String[][] ss =new String[rows][cols];
		
		//非边缘
		int count = (rows-1)*(cols-1);
		int[] randoms = RandomUtil.getRandoms(count);
		for (int i=0;i< randoms.length;i++) {
			String num = ""+(randoms[i]/(cols-1))+"_"+(randoms[i]%(cols-1));
			ss[i/(cols-1)][i%(cols-1)] = num;
		}
		//右边缘
		 randoms = RandomUtil.getRandoms(rows-1);
		 for(int i=0;i<randoms.length;i++){
			 ss[i][cols-1] =  ""+randoms[i]+"_"+(cols-1);
		 }
		 //下边缘
		 randoms = RandomUtil.getRandoms(cols-1);
		 for(int i=0;i<randoms.length;i++){
			 ss[(rows-1)][i] =  ""+(rows-1)+"_"+randoms[i];
		 }
		 
		 //最后一个
		 ss[rows-1][cols-1]=""+(rows-1)+"_"+(cols-1);
		 
	/*	 for(int i=0;i<rows;i++){
			 for(int j=0;j<cols;j++){
				 System.out.print(ss[i][j]+",");
			 }
			 System.out.println();
		 }*/
		return ss;
	}
	
	private static String getCodeString(String[][] code){
		String returnString = "";
		for (String[] rows : code) {
			for (String col : rows) {
				returnString +=col+"#";
			}
			if(returnString.endsWith("#")){
				returnString = returnString.substring(0, returnString.length()-1);
			}
			returnString +="@";
		}
		if(returnString.endsWith("@")){
			returnString = returnString.substring(0, returnString.length()-1);
		}
		return returnString;
	}

	private static String[][] getCode(String codeString){
		String[] rows = codeString.split("@");
		int rowsNum = rows.length;
		int colsNum = rows[0].split("#").length;
		String[][] code = new String[rowsNum][colsNum];
		for(int i=0;i< rows.length;i++){
			String[] cols = rows[i].split("#");
			for(int j=0;j<cols.length;j++){
				code[i][j] = cols[j];
			}
		}
		return code;
	}


}
