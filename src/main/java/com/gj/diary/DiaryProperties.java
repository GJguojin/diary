package com.gj.diary;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.gj.diary.utils.DiaryUtil;

public class DiaryProperties extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField filePathTextField;
	private JTextField curPathTextField;
	private JFileChooser filechooserOpen;

	/**
	 * Launch the application.
	 */
	public static void main( String[] args ) {
		try {
			DiaryProperties dialog = new DiaryProperties();
			dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
			dialog.setVisible( true );
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DiaryProperties() {
		setTitle( "配置文件" );
		setIconImage( DiaryCreate.SETTING.getImage() );
		setBounds( 100, 100, 450, 300 );

		DiaryUtil.setLocationCenter( this );
//		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
//		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
//		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
//		int screenHeight = screenSize.height / 2; // 获取屏幕的高
//		this.setLocation( screenWidth - this.getWidth() / 2, screenHeight - this.getHeight() / 2 );

		try {
			UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );
		} catch( Exception e1 ) {
			e1.printStackTrace();
		}
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );

		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout( null );

		JLabel lable1 = new JLabel( "日记更路径:" );
		lable1.setFont( new Font( "宋体", Font.PLAIN, 12 ) );
		lable1.setBounds( 10, 36, 80, 25 );
		contentPanel.add( lable1 );

		filePathTextField = new JTextField();
		filePathTextField.setBounds( 88, 38, 240, 21 );
		contentPanel.add( filePathTextField );
		filePathTextField.setText( DiaryUtil.getProperties( "filePath" ) );
		filePathTextField.setEditable( false );
		filePathTextField.setColumns( 10 );

		JButton openFileButton1 = new JButton( "打开文件" );
		openFileButton1.setBounds( 331, 37, 93, 23 );
		openFileButton1.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				// JOptionPane.showMessageDialog( frmSs, DiaryUtil.getProperties( "currentDirectory" )+"---"+System.getProperty( "user.dir"
				// )+"\\config\\diaryConfig.properties", "错误", JOptionPane.ERROR_MESSAGE );
				// DiaryUtil.modfiyProperties( "currentDirectory", "e:\\" );
				// 产生一个文件选择器
				filechooserOpen = new JFileChooser();
				SwingUtilities.updateComponentTreeUI( filechooserOpen );
				filechooserOpen.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );// 只能打开文件

				// 打开一个对话框
				int index = filechooserOpen.showDialog( null, "打开文件夹" );
				if( index == JFileChooser.APPROVE_OPTION ) {
					filePathTextField.setText( filechooserOpen.getSelectedFile().getAbsolutePath() );
				}
			}
		} );
		contentPanel.add( openFileButton1 );

		JLabel label = new JLabel( "打开文件路径:" );
		label.setFont( new Font( "宋体", Font.PLAIN, 12 ) );
		label.setBounds( 10, 83, 80, 25 );
		contentPanel.add( label );

		curPathTextField = new JTextField();
		curPathTextField.setColumns( 10 );
		curPathTextField.setBounds( 88, 85, 240, 21 );
		curPathTextField.setText( DiaryUtil.getProperties( "currentDirectory" ) );
		curPathTextField.setEditable( false );
		contentPanel.add( curPathTextField );

		JButton openFileButton2 = new JButton( "打开文件" );
		openFileButton2.setBounds( 331, 84, 93, 23 );
		openFileButton2.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				filechooserOpen = new JFileChooser();
				SwingUtilities.updateComponentTreeUI( filechooserOpen );
				filechooserOpen.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );// 只能打开文件
				// 打开一个对话框
				int index = filechooserOpen.showDialog( null, "打开文件夹" );
				if( index == JFileChooser.APPROVE_OPTION ) {
					curPathTextField.setText( filechooserOpen.getSelectedFile().getAbsolutePath() );
				}
			}
		} );
		contentPanel.add( openFileButton2 );
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout( new FlowLayout( FlowLayout.CENTER ) );
			getContentPane().add( buttonPane, BorderLayout.SOUTH );
			{
				JButton okButton = new JButton( "保存" );
				buttonPane.add( okButton );
				okButton.addActionListener( new ActionListener() {
					@Override
					public void actionPerformed( ActionEvent arg0 ) {
						String filePath = filePathTextField.getText();
						Properties properties = DiaryUtil.properties;
						String currentDirectory = curPathTextField.getText();
						properties.setProperty( "filePath", filePath );
						properties.setProperty( "currentDirectory", currentDirectory );
						if( DiaryUtil.modfiyProperties( properties ) ) {
							JOptionPane.showMessageDialog( null, "修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE );
						} else {
							JOptionPane.showMessageDialog( null, "修改失败！", "错误", JOptionPane.ERROR_MESSAGE );
						}
						dispose();
					}
				} );
			}
			{
				JButton cancelButton = new JButton( "取消" );
				cancelButton.addActionListener( new ActionListener() {
					@Override
					public void actionPerformed( ActionEvent arg0 ) {
						dispose();
					}
				} );
				buttonPane.add( cancelButton );
			}
		}
	}

}
