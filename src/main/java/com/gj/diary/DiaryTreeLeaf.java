package com.gj.diary;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class DiaryTreeLeaf{
	
	private String leafName;        //叶子节点名称
	private String leafPath;        //文件路径
	private BufferedImage bufferedImage;
	
	private ImageIcon icon;
	
	private ImageIcon minIcon;
	
	public String getLeafName() {
		return leafName;
	}
	public void setLeafName(String leafName) {
		this.leafName = leafName;
	}
	
	public String getLeafPath() {
		return leafPath;
	}
	public void setLeafPath(String leafPath) {
		this.leafPath = leafPath;
	}
	
	
	public DiaryTreeLeaf(String leafName,String filePath) {
		super();
		this.leafName = leafName;
		this.leafPath = filePath;
	}
	public ImageIcon getMinIcon() {
		try {
			if(minIcon == null && bufferedImage != null){
				BufferedImage minBufferedImage = new BufferedImage( 35, 20, BufferedImage.TYPE_INT_RGB );
				minBufferedImage.getGraphics().drawImage(bufferedImage.getScaledInstance(35, 20, Image.SCALE_SMOOTH), 0, 0,35,20, null ); // 绘制缩小后的图
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				ImageIO.write(minBufferedImage, "jpg", byteOut);
				minIcon = new ImageIcon(byteOut.toByteArray());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return minIcon;
		}
		return minIcon;
	}
	
	public ImageIcon getIcon() {
		try {
			if(icon == null && bufferedImage != null){
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "jpg", byteOut);
				icon = new ImageIcon(byteOut.toByteArray());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return icon;
		}
		return icon;
	}
	
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}
}
