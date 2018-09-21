package com.gj.diary;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.gj.diary.utils.DiaryUtil;

public class DiaryMessage extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JTextArea textArea;
	
	private JPopupMenu jPopupMenu;
	

	/**	
	 * Create the frame.
	 */
	public DiaryMessage() {
		setTitle( "日记信息" );
		setIconImage( DiaryCreate.TEXT.getImage() );
		setResizable( false );
		setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE);
		setBounds( 0, 0, 450, 300 );
		contentPane = new JPanel();
		contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		setContentPane( contentPane );
		
		jPopupMenu = new JPopupMenu();
		JMenuItem copy = new JMenuItem( "复制内容" );
		jPopupMenu.add( copy );
		copy.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		copy.setIcon( DiaryCreate.COPY);
		copy.addActionListener( new  ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
				Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection text = new StringSelection(textArea.getText());  
				systemClipboard.setContents( text, null );
			}
		});
		
		
		DiaryUtil.setLocationCenter( this );
		this.getContentPane().setLayout( null );
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setSize(this.getWidth()-5, this.getHeight()-25);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont( new Font("楷体", Font.PLAIN, 18) );
		textArea.setLineWrap( true );
		textArea.setEnabled( false );
		textArea.addMouseListener( new MouseListener() {
			@Override
			public void mouseClicked( MouseEvent event ) {
				if( event.isMetaDown() ) {// 检测鼠标右键单击
					jPopupMenu.show( textArea, event.getX(), event.getY() );
				}
			}
			@Override
			public void mouseEntered( MouseEvent arg0 ) {
			}

			@Override
			public void mouseExited( MouseEvent arg0 ) {
			}

			@Override
			public void mousePressed( MouseEvent arg0 ) {
			}

			@Override
			public void mouseReleased( MouseEvent arg0 ) {
			}
		});

			
			
		
		scrollPane.setViewportView( textArea );
	}
	
	public void setDiaryMessage(String message){
		this.textArea.setText( message);
	}
}
