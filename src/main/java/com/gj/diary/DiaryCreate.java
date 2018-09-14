package com.gj.diary;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import com.gj.diary.utils.DiaryUtil;
import com.gj.diary.utils.ImageSplitUtil;
import com.gj.diary.utils.ImageUtil;

public class DiaryCreate {

	private JFrame frmSs;

	private JFileChooser filechooserOpen;

	private JTextField photoPath;

	private JLabel photoLabel;

	private String readPath = "";

	private JTextArea textArea;

	private JTextField dateField;

	public static String FILE_PATH = DiaryUtil.getProperties( "filePath" );
	private JPanel mainPanel;
	
	private DiaryBrowse diaryBrowse;
	
	private DiaryProperties diaryProperties;
	
	@SuppressWarnings("unused")
	private JCheckBox checkBox;
	
	private JButton cratetButton;
	
	private JPopupMenu jPopupMenu;
	
	private JRadioButton defauleRadio;
	
	private JRadioButton  encryptRadio;
	
	private JRadioButton  spiltRadio;
	
	public static ImageIcon KEY = new ImageIcon( DiaryCreate.class.getResource( "/image/key.png" ) );
	public static ImageIcon background = new ImageIcon( DiaryUtil.outerConfigPath+"background.jpg"  );
	public static ImageIcon HOME = new ImageIcon( DiaryCreate.class.getResource( "/image/home.png" ) );
	public static ImageIcon LOGIN = new ImageIcon( DiaryCreate.class.getResource( "/image/login.png" ) );
	public static ImageIcon LOGOUT = new ImageIcon( DiaryCreate.class.getResource( "/image/logout.png" ) );
	public static ImageIcon CLOSE = new ImageIcon( DiaryCreate.class.getResource( "/image/exit.png" ) );
	public static ImageIcon HELP = new ImageIcon( DiaryCreate.class.getResource( "/image/help.png" ) );
	public static ImageIcon SETTING = new ImageIcon( DiaryCreate.class.getResource( "/image/settings.png" ) );
	public static ImageIcon TEXT = new ImageIcon( DiaryCreate.class.getResource( "/image/text.png" ) );
	public static ImageIcon IMAGE = new ImageIcon( DiaryCreate.class.getResource( "/image/image.png" ) );
	public static ImageIcon ADMIN = new ImageIcon( DiaryCreate.class.getResource( "/image/admin.png" ) );
	
	public static ImageIcon COPY = new ImageIcon( DiaryCreate.class.getResource( "/image/editcopy.png" ) );
	public static ImageIcon CUT = new ImageIcon( DiaryCreate.class.getResource( "/image/editcut.png" ) );
	public static ImageIcon PASTE = new ImageIcon( DiaryCreate.class.getResource( "/image/editpaste.png" ) );
	public static ImageIcon CLEAR = new ImageIcon( DiaryCreate.class.getResource( "/image/editclear.png" ) );
	
	/**
	 * Launch the application.
	 */
	public static void main( String[] args ) {
		EventQueue.invokeLater( new Runnable() {
			@Override
			public void run() {
				try {
					DiaryCreate window = new DiaryCreate();
					window.frmSs.setVisible( true );
				} catch( Exception e ) {
					e.printStackTrace();
				}
			}
		} );
	}

	/**
	 * Create the application.
	 */
	public DiaryCreate() {
		initialize();
		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );
		} catch( Exception e1 ) {
			e1.printStackTrace();
		} 
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSs = new JFrame();
		frmSs.setIconImage( ADMIN.getImage() );
		frmSs.setTitle( "日记" );
		frmSs.setBounds( 100, 100, 650, 666 );
		frmSs.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frmSs.getContentPane().setLayout( new GridLayout( 1, 0, 0, 0 ) );
		frmSs.setResizable( false );

		DiaryUtil.setLocationCenter( frmSs );
		
		jPopupMenu = new JPopupMenu();
		JMenuItem copy = new JMenuItem( "复制" );
		jPopupMenu.add( copy );
		copy.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		copy.setIcon( COPY );
		copy.addActionListener( new  ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
				Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				String selectedText = textArea.getSelectedText();
				StringSelection text = new StringSelection(selectedText);  
				if(selectedText == null || "".equals( selectedText )){
					return;
				}
				systemClipboard.setContents( text, null );
			}
		});
		
		JMenuItem cut = new JMenuItem( "剪切" );
		jPopupMenu.add( cut );
		cut.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		cut.setIcon( CUT );
		cut.addActionListener( new  ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
				Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				String selectedText = textArea.getSelectedText();
				if(selectedText == null || "".equals( selectedText )){
					return;
				}
				StringSelection text = new StringSelection(selectedText);  
				systemClipboard.setContents( text, null );
				int start = textArea.getSelectionStart();
                int end = textArea.getSelectionEnd();
                textArea.replaceRange("",start,end);//从Text1中删除被选的文
			}
		});
		
		JMenuItem paste = new JMenuItem( "粘贴" );
		jPopupMenu.add( paste );
		paste.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		paste.setIcon( PASTE );
		paste.addActionListener( new  ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
				Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				Transferable contents = systemClipboard.getContents( textArea );
				if(contents != null && contents.isDataFlavorSupported( DataFlavor.stringFlavor )){
					try {
						String data = ( String )contents.getTransferData( DataFlavor.stringFlavor );
						textArea.append(data);
					} catch( UnsupportedFlavorException | IOException e1 ) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		JMenuItem clear = new JMenuItem( "清空" );
		jPopupMenu.add( clear );
		clear.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		clear.setIcon( CLEAR );
		clear.addActionListener( new  ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
				textArea.setText( "    这张照片还记得吗？这一天是" + DiaryUtil.getDateString( "yyyy年MM月dd日" ) + ",");
				dateField.setText( DiaryUtil.getDateString("yyyy-MM-dd" ) );
			}
		});

		mainPanel = new JPanel();
		mainPanel.setAlignmentY( Component.TOP_ALIGNMENT );
		mainPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		mainPanel.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		mainPanel.setPreferredSize( new Dimension( 0, 0 ) );
		// mainPanel.setBackground( Color.LIGHT_GRAY );
		frmSs.getContentPane().add( mainPanel );
		mainPanel.setLayout( null );

		photoLabel = new JLabel();
		photoLabel.setBounds( 0, 30, frmSs.getWidth(), 380 );
		
		
		Map<String, Integer> imageWH = DiaryUtil.getImageWH( background.getIconWidth(), background.getIconHeight(), photoLabel.getWidth(),
						photoLabel.getHeight() );
		background.setImage( background.getImage().getScaledInstance( imageWH.get( "width" ), imageWH.get( "height" ), Image.SCALE_SMOOTH ) );
		photoLabel.setIcon( background );
		
		photoLabel.setHorizontalAlignment( JLabel.CENTER );
		mainPanel.add( photoLabel );

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("新宋体", Font.PLAIN, 14));
		menuBar.setBounds( 0, 0,frmSs.getWidth(), 30 );
		mainPanel.add( menuBar );
		
		JMenu systemMenu = new JMenu("系统");
		
		systemMenu.setIcon( HOME );
		systemMenu.setFont(new Font("新宋体", Font.BOLD, 13));
		menuBar.add(systemMenu);
		
		JMenuItem loginItem = new JMenuItem("登陆");
		
		loginItem.setIcon( LOGIN );
		loginItem.setFont(new Font("新宋体", Font.PLAIN, 12));
		loginItem.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				String password = JOptionPane.showInputDialog( null, "请输入密码", "密码", JOptionPane.QUESTION_MESSAGE );
				if(password == null){
					return;
				}
				if(!DiaryUtil.checkPassword( password,"1" )){
					JOptionPane.showMessageDialog( null, "密码错误,无法登陆！", "错误", JOptionPane.ERROR_MESSAGE );
					return;
				}else{
					JOptionPane.showMessageDialog( null, "登陆成功！", "提示", JOptionPane.INFORMATION_MESSAGE );
					DiaryUtil.password = password;
				}
			}
		} );
		
		systemMenu.add(loginItem);
		
		JMenuItem logoutItem = new JMenuItem("登出");
		
		logoutItem.setIcon( LOGOUT );
		logoutItem.setFont(new Font("新宋体", Font.PLAIN, 12));
		logoutItem.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				DiaryUtil.password = null;
				JOptionPane.showMessageDialog( null, "已经退出登陆状态！", "提示", JOptionPane.INFORMATION_MESSAGE );
				return;
			}
		} );
		systemMenu.add(logoutItem);
		
		JMenuItem closeItem = new JMenuItem("关闭");
		closeItem.setFont(new Font("新宋体", Font.PLAIN, 12));
		
		closeItem.setIcon( CLOSE );
		closeItem.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				System.exit(0);
			}
		});
		systemMenu.add(closeItem);
		
		
		JMenu helpMenu = new JMenu("帮助");
		
		helpMenu.setIcon( HELP );
		helpMenu.setFont(new Font("新宋体", Font.BOLD, 13));
		menuBar.add(helpMenu);
		
		JMenuItem propItem = new JMenuItem("配置");
		
		propItem.setIcon( SETTING );
		propItem.setFont(new Font("新宋体", Font.PLAIN, 12));
		propItem.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
//				JOptionPane.showMessageDialog( frmSs, "点击" , "提示", JOptionPane.PLAIN_MESSAGE );
			    if(diaryProperties == null){
			    	diaryProperties = new DiaryProperties();
			    }
			    diaryProperties.setVisible( true );
			}
		});
		helpMenu.add(propItem);
		
		JMenuItem modfyPasswordItem = new JMenuItem("日记密码");
		modfyPasswordItem.setIcon( KEY );
		modfyPasswordItem.setFont(new Font("新宋体", Font.PLAIN, 12));
		modfyPasswordItem.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				DiaryPassword diaryPassword = new DiaryPassword();
				diaryPassword.setTitle( "修改日记解密密码" );
				diaryPassword.setModelString( "1" );
				diaryPassword.setVisible( true );
			}
		});
		helpMenu.add(modfyPasswordItem);
		
		JMenuItem modfyPasswordPhotoItem = new JMenuItem("图片密码");
		modfyPasswordPhotoItem.setIcon( KEY );
		modfyPasswordPhotoItem.setFont(new Font("新宋体", Font.PLAIN, 12));
		modfyPasswordPhotoItem.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				DiaryPassword diaryPassword = new DiaryPassword();
				diaryPassword.setModelString( "2" );
				diaryPassword.setTitle( "修改图片解密密码" );
				diaryPassword.setVisible( true );
			}
		});
		helpMenu.add(modfyPasswordPhotoItem);

		photoPath = new JTextField();
		photoPath.setBounds( 20, 417, 507, 31 );
		mainPanel.add( photoPath );
		photoPath.setColumns( 10 );
		photoPath.setEditable( false );

		JButton photoButton = new JButton( "浏览图片" );
		photoButton.setBounds( 524, 417, 93, 31 );
		photoButton.addActionListener( new ActionListener() {
			@SuppressWarnings("hiding")
			@Override
			public void actionPerformed( ActionEvent e ) {
//				JOptionPane.showMessageDialog( frmSs, DiaryUtil.getProperties( "currentDirectory" )+"---"+System.getProperty( "user.dir" )+"\\config\\diaryConfig.properties", "错误", JOptionPane.ERROR_MESSAGE );
//				DiaryUtil.modfiyProperties(  "currentDirectory", "e:\\" );
				// 产生一个文件选择器
				filechooserOpen = new JFileChooser();
				SwingUtilities.updateComponentTreeUI( filechooserOpen );
				// 设置默认的打开目录,如果不设的话按照window的默认目录(我的文档)
				filechooserOpen.setCurrentDirectory( new File( DiaryUtil.getProperties( "currentDirectory" ) ) );
				// 设置打开文件类型,此处设置成只能选择文件夹，不能选择文件
				filechooserOpen.setFileSelectionMode( JFileChooser.FILES_ONLY );// 只能打开文件
				filechooserOpen.setAcceptAllFileFilterUsed( false );
				filechooserOpen.setFileFilter( new JpgFileFilter() );
				// filechooserOpen.setFileFilter( new AllFileFilter() );

				// 打开一个对话框
				int index = filechooserOpen.showDialog( null, "打开图片" );
				if( index == JFileChooser.APPROVE_OPTION ) {
					// 把获取到的文件的绝对路径显示在文本编辑框中
					photoPath.setText( filechooserOpen.getSelectedFile().getAbsolutePath() );
					readPath = photoPath.getText() + "\\";
					// 该方法会将图像加载到内存，从而拿到图像的详细信息。
					ImageIcon icon = new ImageIcon( readPath );
					Map<String, Integer> imageWH = DiaryUtil.getImageWH( icon.getIconWidth(), icon.getIconHeight(), photoLabel.getWidth(),
									photoLabel.getHeight() );
					icon.setImage( icon.getImage().getScaledInstance( imageWH.get( "width" ), imageWH.get( "height" ), Image.SCALE_DEFAULT ) );
					photoLabel.setIcon( icon );

				}
			}
		} );
		mainPanel.add( photoButton );

		JLabel lblNewLabel = new JLabel( "2050年的自己：" );
		lblNewLabel.setFont( new Font( "宋体", Font.PLAIN, 14 ) );
		lblNewLabel.setBounds( 20, 450, 151, 23 );
		mainPanel.add( lblNewLabel );

		cratetButton = new JButton( "保存日记" );
		cratetButton.setFont(new Font("宋体", Font.PLAIN, 14));
		cratetButton.setBounds( 278, 590, 105, 30 );
		cratetButton.addActionListener( new ActionListener() {
			@SuppressWarnings("hiding")
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				if( readPath == null || "".equals( readPath ) ) {
					JOptionPane.showMessageDialog( frmSs, "请选择图片！！", "错误", JOptionPane.ERROR_MESSAGE );
					return;
				}
				try {
					String dateSting = dateField.getText();
					if( "".equals( dateSting ) ) {
						dateSting = DiaryUtil.getDateString();
					}
					String filePath = (FILE_PATH.endsWith( "\\" )|| FILE_PATH.endsWith( "/" ) )?FILE_PATH:(FILE_PATH+"\\")+dateSting.split( "-" )[0] + "\\" + dateSting.split( "-" )[0] + "-" + dateSting.split( "-" )[1];
					File file = new File( filePath );
					if( !file.exists() ) {
						file.mkdirs();
					}
					file = new File( filePath + "\\" + dateSting + ".jpg" );
					if( !file.exists() ) {
						file.createNewFile();
					}
					if(encryptRadio.isSelected()){//加密保存
					    String background = DiaryUtil.outerConfigPath+"background.jpg";
					    ImageUtil.coptFile( background, file );
					    
					    File temp = new File(DiaryUtil.outerConfigPath+"temp");
					    if(!temp.exists()){
					    	temp.createNewFile();
					    }
					    ImageUtil.coptFile( readPath, temp );
					    
					    String text = textArea.getText();
					    ImageUtil.writePhotoMessage( temp ,file, text );
					    ImageUtil.writeMessage( file,text,1);
					    temp.delete();
					}else if(spiltRadio.isSelected()){
						//JOptionPane.showMessageDialog( frmSs, "分割保存","提示",JOptionPane.INFORMATION_MESSAGE );
						String width = JOptionPane.showInputDialog( null, "请输入分块的宽度(默认50)", "大小", JOptionPane.QUESTION_MESSAGE );
						String height = JOptionPane.showInputDialog( null, "请输入分块的高度(默认50)", "大小", JOptionPane.QUESTION_MESSAGE );
						Integer w = 50;
						try {
							 w = Integer.parseInt(width);
						} catch (Exception e) {
							w =50;
						}
						Integer h = 50;
						try {
							 h = Integer.parseInt(height);
						} catch (Exception e) {
							h =50;
						}
						String returnString = ImageSplitUtil.splitImage(file,new File(readPath), w, h);
						String text = textArea.getText();
						ImageUtil.writeSplitMessage( returnString ,file, text );
						ImageUtil.writeMessage( file,text,2);
					}else{//非加密保存
						ImageUtil.coptFile( readPath, file );
						String text = textArea.getText();
						ImageUtil.writeMessage( file,text,0);
					}
					
					int n = JOptionPane.showConfirmDialog( frmSs, "生成图片完成,是否浏览？", "结果", JOptionPane.INFORMATION_MESSAGE );
					if(diaryBrowse != null){
						diaryBrowse.dispose();
					}
					diaryBrowse = new DiaryBrowse();
					if(n == JOptionPane.YES_OPTION){
						diaryBrowse.setVisible( true );
				    }
				} catch( Exception e ) {
					JOptionPane.showMessageDialog( frmSs, "生成图片发生异常，原因：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE );
					e.printStackTrace();
				}
			}
		} );
		mainPanel.add( cratetButton );

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds( 20, 473, 597, 107 );
		mainPanel.add( scrollPane );

		textArea = new JTextArea();
		textArea.setFont( new Font( "宋体", Font.PLAIN, 14 ) );
		textArea.setText( "    这张照片还记得吗？这一天是" + DiaryUtil.getDateString( "yyyy年MM月dd日" ) + "," );
		textArea.setLineWrap( true );
		textArea.addMouseListener( new MouseListener() {

			@Override
			public void mouseClicked( MouseEvent event ) {
				if( event.isMetaDown() ) {// 检测鼠标右键单击
					jPopupMenu.show( textArea, event.getX(), event.getY() );
				}
			}

			@Override
			public void mouseEntered( MouseEvent e ) {
			}
			@Override
			public void mouseExited( MouseEvent e ) {
			}
			@Override
			public void mousePressed( MouseEvent e ) {
			}
			@Override
			public void mouseReleased( MouseEvent e ) {
			}
		});
		
		scrollPane.setViewportView( textArea );

		dateField = new JTextField();
		dateField.setFont( new Font( "宋体", Font.PLAIN, 14 ) );
		dateField.setBounds( 128, 590, 113, 30 );
		dateField.setText( DiaryUtil.getDateString() );
		dateField.setEditable(false );
		mainPanel.add( dateField );
		dateField.setColumns( 10 );
		dateField.setHorizontalAlignment(JTextField.CENTER) ;
		dateField.setBorder(BorderFactory.createLineBorder(new Color(170, 170, 170)));

		JLabel label = new JLabel( "图片名日期：" );
		label.setFont( new Font( "宋体", Font.PLAIN, 14 ) );
		label.setBounds( 20, 590, 84, 30 );
		mainPanel.add( label );

		JButton dateCut = new JButton( "<<" );
		dateCut.setFont(new Font("宋体", Font.PLAIN, 14));
		dateCut.setBounds( 100, 590, 30, 30 );
		dateCut.setBorder(new LineBorder(new Color(170, 170, 170), 1, true));
		dateCut.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				String fieldString = dateField.getText();
				Date oldDate = DiaryUtil.getDate( fieldString, "yyyy-MM-dd" );
				Date date = DiaryUtil.getDateAdd( fieldString,-1);
				dateField.setText( DiaryUtil.getDateString( date, "yyyy-MM-dd" ) );
				String text = textArea.getText();
				text = text.replace( DiaryUtil.getDateString( oldDate, "yyyy年MM月dd日" ), DiaryUtil.getDateString( date, "yyyy年MM月dd日" ) );
				textArea.setText( text );
			}
		} );
		mainPanel.add( dateCut );

		JButton dateAdd = new JButton( ">>" );
		dateAdd.setFont(new Font("宋体", Font.PLAIN, 14));
		dateAdd.setBounds( 238, 590, 30, 30 );
		dateAdd.setBorder(new LineBorder(new Color(170, 170, 170), 1, true));
		dateAdd.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				String fieldString = dateField.getText();
				Date oldDate = DiaryUtil.getDate( fieldString, "yyyy-MM-dd" );
				Date date = DiaryUtil.getDateAdd( fieldString,1);
				dateField.setText( DiaryUtil.getDateString( date, "yyyy-MM-dd" ) );
				String text = textArea.getText();
				text = text.replace( DiaryUtil.getDateString( oldDate, "yyyy年MM月dd日" ), DiaryUtil.getDateString( date, "yyyy年MM月dd日" ) );
				textArea.setText( text );
			}
		} );
		mainPanel.add( dateAdd );
		
		JButton lookButton = new JButton("浏览日记");
		lookButton.setFont(new Font("宋体", Font.PLAIN, 14));
		lookButton.setActionCommand("浏览日记");
		lookButton.setBounds(511, 590, 105, 30);
		lookButton.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
				if(diaryBrowse == null){
					diaryBrowse = new DiaryBrowse();
				}
				diaryBrowse.setVisible( true );
			}
		});
		mainPanel.add(lookButton);
		
/*		checkBox = new JCheckBox("");
		checkBox.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
				if(checkBox.isSelected()){
					cratetButton.setText( "加密保存" );
				}else{
					cratetButton.setText( "保存日记" );
				}
			}
		});
		checkBox.setBounds(384, 590, 21, 30);
		mainPanel.add(checkBox);*/
		
		
		ButtonGroup group = new ButtonGroup();// 创建单选按钮组
		defauleRadio = new JRadioButton("");// 创建单选按钮 默认
		defauleRadio.setSelected(true);
		defauleRadio.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
					cratetButton.setText( "保存日记" );
			}
		});
		defauleRadio.setBounds(383, 590, 21, 30);
		group.add(defauleRadio);
		mainPanel.add(defauleRadio);
		
		encryptRadio = new JRadioButton("");// 创建单选按钮 加密
		encryptRadio.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
					cratetButton.setText( "加密保存" );
			}
		});
		encryptRadio.setBounds(400, 590, 21, 30);
		group.add(encryptRadio);
		mainPanel.add(encryptRadio);
		
		spiltRadio = new JRadioButton("");// 创建单选按钮 加密
		spiltRadio.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
					cratetButton.setText( "分割保存" );
			}
		});
		spiltRadio.setBounds(417, 590, 21, 30);
		group.add(spiltRadio);
		mainPanel.add(spiltRadio);
		

	}

	// 重写文件过滤器，设置打开类型中几种可选的文件类型，这里设了两种，一种txt，一种xls
	class JpgFileFilter extends FileFilter {

		@Override
		public boolean accept( File f ) {
			if( f.isDirectory() ) {
				return true;
			} else {
				String nameString = f.getName();
				return nameString.toLowerCase().endsWith( ".jpg" ) || nameString.toLowerCase().endsWith( ".png" );
			}
		}

		@Override
		public String getDescription() {
			return "*.jpg(图片文件)";
		}
	}
	
}
