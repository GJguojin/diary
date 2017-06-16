package test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TestImage {

	public static void main(String[] args) {
		String originalImg = "E:\\test\\test.jpg";
		File file = new File(originalImg);
		BufferedImage[] imgs;
		try {
			imgs = TestImage.splitImage(file, 80, 70,2);
			// 输出小图
			for (int i = 0; i < imgs.length; i++) {
					ImageIO.write(imgs[i], "jpg", new File("E:\\test\\split\\img" + i + ".jpg"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static BufferedImage[] splitImage(File file, Integer chunkWidth, Integer chunkHeight,int ss) throws IOException {
		if (chunkWidth == null || chunkWidth == 0) {
			chunkWidth = 20;
		}
		if (chunkHeight == null || chunkHeight == 0) {
			chunkWidth = 20;
		}
		FileInputStream fis = new FileInputStream(file);
		BufferedImage image = ImageIO.read(fis);
		int width = image.getWidth(); // 原图宽
		int height = image.getHeight();// 原图高
		int rows = (int) Math.ceil(1.0 * height / chunkHeight);
		int cols = (int) Math.ceil(1.0 * width / chunkWidth);
		BufferedImage imgs[] = new BufferedImage[rows*cols];
		int count=0;
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				// 设置小图的大小和类型
				imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());
				// 写入图像内容
				Graphics2D gr = imgs[count].createGraphics();
				gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
				gr.dispose();
				count++;
			}
		}
		return imgs;
	}
	

	/**
	 * 
	 * @param file 要分割的图片
	 * @param chunkWidth   分割块的宽度
	 * @return chunkHeight 分割快的高度
	 * @throws IOException
	 */
	public static BufferedImage[][] splitImage(File file, Integer chunkWidth, Integer chunkHeight) throws IOException {
		if (chunkWidth == null || chunkWidth == 0) {
			chunkWidth = 20;
		}
		if (chunkHeight == null || chunkHeight == 0) {
			chunkWidth = 20;
		}
		FileInputStream fis = new FileInputStream(file);
		BufferedImage image = ImageIO.read(fis);
		int width = image.getWidth(); // 原图宽
		int height = image.getHeight();// 原图高

		int rows = (int) Math.ceil(1.0 * height / chunkHeight);
		int cols = (int) Math.ceil(1.0 * width / chunkWidth);

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
					smallHeight = height % chunkHeight;
				}else if(rows-x != 1 && cols-y == 1){//最后一列
					smallWidth =  width % chunkWidth;
					smallHeight = chunkHeight;
				}else{
					smallWidth =  width % chunkWidth;
					smallHeight = height % chunkHeight;
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

	public static void splitImage() throws IOException {

		String originalImg = "C:\\Users\\jguo\\Desktop\\new\\getcap-old.jpg";

		// 读入大图
		File file = new File(originalImg);
		FileInputStream fis = new FileInputStream(file);
		BufferedImage image = ImageIO.read(fis);

		// 分割成4*4(16)个小图
		int rows = 2;
		int cols = 20;
		int chunks = rows * cols;

		// 计算每个小图的宽度和高度
		int chunkWidth = image.getWidth() / cols;
		int chunkHeight = image.getHeight() / rows;

		int count = 0;
		BufferedImage imgs[] = new BufferedImage[chunks];
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				// 设置小图的大小和类型
				imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

				// 写入图像内容
				Graphics2D gr = imgs[count++].createGraphics();
				gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
				gr.dispose();
			}
		}

		// 输出小图
		for (int i = 0; i < imgs.length; i++) {
			ImageIO.write(imgs[i], "jpg", new File("C:\\Users\\jguo\\Desktop\\new\\split1\\img" + i + ".jpg"));
		}

		System.out.println("完成分割！");
	}
	
	public static BufferedImage mergeImage(BufferedImage[][] imgs) throws IOException {
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
	
	private String[][] getCode(int rows,int cols){
		String[][] ss =new String[][]{};
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				ss[i][j]=""+i+"_"+j;
			}
		}
		
		return null;
		
	}

	public static void mergeImage() throws IOException {

		Integer[] ss = new Integer[] { 10, 17, 14, 8, 1, 9, 4, 2, 3, 12, 19, 15, 11, 13, 6, 0, 5, 7, 16, 18, 35, 26, 37, 34, 22, 30, 29, 33, 23, 27, 24, 31, 39, 32, 38, 21, 20, 36, 28, 25 };

		int rows = 2;
		int cols = 20;
		int chunks = rows * cols;

		int chunkWidth, chunkHeight;
		int type;

		// 读入小图
		File[] imgFiles = new File[chunks];
		for (int i = 0; i < chunks; i++) {
			imgFiles[i] = new File("C:\\Users\\jguo\\Desktop\\new\\split1\\img" + i + ".jpg");
		}

		// 创建BufferedImage
		BufferedImage[] buffImages = new BufferedImage[chunks];
		for (int i = 0; i < chunks; i++) {
			buffImages[i] = ImageIO.read(imgFiles[ss[i]]);
		}

		type = buffImages[0].getType();
		chunkWidth = buffImages[0].getWidth();
		chunkHeight = buffImages[0].getHeight();

		// 设置拼接后图的大小和类型
		BufferedImage finalImg = new BufferedImage(chunkWidth * cols, chunkHeight * rows, type);

		// 写入图像内容
		int num = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				finalImg.createGraphics().drawImage(buffImages[num], chunkWidth * j, chunkHeight * i, null);
				num++;
			}
		}

		// 输出拼接后的图像
		ImageIO.write(finalImg, "jpeg", new File("C:\\Users\\jguo\\Desktop\\new\\finalImg1.jpg"));

		System.out.println("完成拼接！");
	}

}
