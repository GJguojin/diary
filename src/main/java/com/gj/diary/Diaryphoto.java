package com.gj.diary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.gj.diary.utils.DiaryUtil;

public class Diaryphoto extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	JLabel photoLabel = new JLabel("");

	/**
	 * Launch the application.
	 */
	public static void main( String[] args ) {
		EventQueue.invokeLater( new Runnable() {
			@Override
			public void run() {
				try {
					Diaryphoto frame = new Diaryphoto();
					frame.setVisible( true );
				} catch( Exception e ) {
					e.printStackTrace();
				}
			}
		} );
	}
	
	public Diaryphoto() {

	}

	/**
	 * Create the frame.
	 */
	public Diaryphoto(File file) {
		setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
		setBounds( 0, 0, 1200, 675 );
		contentPane = new JPanel();
		contentPane.setBorder( new EmptyBorder( 0,0,0,0 ) );
		contentPane.setLayout( new BorderLayout( 0, 0 ) );
		setTitle("照片浏览");
		setContentPane( contentPane );
		setIconImage( DiaryCreate.IMAGE.getImage() );
		contentPane.add(photoLabel, BorderLayout.CENTER);
		photoLabel.setSize( new Dimension( this.getWidth(), this.getHeight()) );
		DiaryUtil.setLocationCenter( this );
		photoLabel.setHorizontalAlignment( JLabel.CENTER );
		if(file != null){
			ImageIcon icon = new ImageIcon( file.getAbsolutePath() );
			Map<String, Integer> imageWH = DiaryUtil.getImageWH( icon.getIconWidth(), icon.getIconHeight(), photoLabel.getWidth(), photoLabel.getHeight());
			icon.setImage( icon.getImage().getScaledInstance( imageWH.get( "width" ), imageWH.get( "height" ), Image.SCALE_SMOOTH ) );
			photoLabel.setIcon( icon );
		}
	}
	
	public Diaryphoto(BufferedImage mergeImage) {
		setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
		setBounds( 0, 0, 1200, 675 );
		contentPane = new JPanel();
		contentPane.setBorder( new EmptyBorder( 0,0,0,0 ) );
		contentPane.setLayout( new BorderLayout( 0, 0 ) );
		setTitle("照片浏览");
		setContentPane( contentPane );
		setIconImage( DiaryCreate.IMAGE.getImage() );
		contentPane.add(photoLabel, BorderLayout.CENTER);
		photoLabel.setSize( new Dimension( this.getWidth(), this.getHeight()) );
		DiaryUtil.setLocationCenter( this );
		photoLabel.setHorizontalAlignment( JLabel.CENTER );
		if(mergeImage != null){
			ImageIcon icon = new ImageIcon( mergeImage );
			Map<String, Integer> imageWH = DiaryUtil.getImageWH( icon.getIconWidth(), icon.getIconHeight(), photoLabel.getWidth(), photoLabel.getHeight());
			icon.setImage( icon.getImage().getScaledInstance( imageWH.get( "width" ), imageWH.get( "height" ), Image.SCALE_SMOOTH ) );
			photoLabel.setIcon( icon );
		}
	}

}
