package com.gj.diary;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import com.gj.diary.utils.DiaryUtil;

public class DiaryPassword extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JPasswordField oldTextField;
	private JPasswordField newTextField;
	
	private String modelString="1";

	/**
	 * Launch the application.
	 */
	public static void main( String[] args ) {
		try {
			DiaryPassword dialog = new DiaryPassword();
			dialog.setVisible( true );
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Create the dialog.
	 * @throws  
	 */
	public DiaryPassword() {
		setIconImage( DiaryCreate.KEY.getImage() );
		setTitle( "修改密码" );
		setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		setBounds( 100, 100, 450, 300 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(null);
		
		DiaryUtil.setLocationCenter( this );
		
		JLabel oldPassword = new JLabel("旧密码：");
		oldPassword.setFont(new Font("宋体", Font.PLAIN, 14));
		oldPassword.setBounds(56, 60, 70, 25);
		contentPanel.add(oldPassword);
		
		oldTextField = new JPasswordField();
		oldTextField.setBounds(118, 60, 219, 25);
		contentPanel.add(oldTextField);
		oldTextField.setColumns(10);
		
		JLabel newPassword = new JLabel("新密码：");
		newPassword.setFont(new Font("宋体", Font.PLAIN, 14));
		newPassword.setBounds(56, 126, 70, 25);
		contentPanel.add(newPassword);
		
		newTextField = new JPasswordField();
		newTextField.setColumns(10);
		newTextField.setBounds(118, 126, 219, 25);
		contentPanel.add(newTextField);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout( new FlowLayout( FlowLayout.CENTER ) );
			getContentPane().add( buttonPane, BorderLayout.SOUTH );
			{
				JButton okButton = new JButton( "确认" );
				okButton.setFont(new Font("宋体", Font.PLAIN, 14));
				okButton.addActionListener( new  ActionListener(){
				
					@SuppressWarnings("hiding")
					@Override
					public void actionPerformed( ActionEvent arg0 ) {
						String oldPassword = new String(oldTextField.getPassword());
						String newPassword = new String(newTextField.getPassword());
						if(!DiaryUtil.checkPassword(oldPassword,modelString)){
							JOptionPane.showMessageDialog( null, "输入的旧密码错误，不允许修改密码", "错误", JOptionPane.ERROR_MESSAGE );
							return;
						}
						int n = JOptionPane.showConfirmDialog( null, "确认修改密码？", "提示", JOptionPane.INFORMATION_MESSAGE );
						if(n == JOptionPane.YES_OPTION){
							String passwordString ="";
							if("1".equals( modelString )){
								passwordString ="password";
							}else{
								passwordString ="passwordPhoto";
							}
							if(DiaryUtil.modfiyProperties( passwordString,DiaryUtil.getMd5String( newPassword ))){
								JOptionPane.showMessageDialog( null, "修改密码成功！", "错误", JOptionPane.INFORMATION_MESSAGE );
								DiaryUtil.password = newPassword;
								dispose();
							}else{
								JOptionPane.showMessageDialog( null, "修改密码失败！", "错误", JOptionPane.ERROR_MESSAGE );
							}
							
					    }
					}
				});
				buttonPane.add( okButton );
			}
			{
				JButton cancelButton = new JButton( "取消" );
				cancelButton.setFont(new Font("宋体", Font.PLAIN, 14));
				cancelButton.addActionListener( new  ActionListener(){
					@Override
					public void actionPerformed( ActionEvent arg0 ) {
					    	dispose();
					}
				});
				buttonPane.add( cancelButton );
			}
		}
	}

	public String getModelString() {
		return modelString;
	}

	public void setModelString( String modelString ) {
		this.modelString = modelString;
	}

}
