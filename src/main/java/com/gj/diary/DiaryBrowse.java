package com.gj.diary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.gj.diary.utils.DiaryUtil;
import com.gj.diary.utils.ImageSplitUtil;
import com.gj.diary.utils.ImageUtil;


public class DiaryBrowse extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private static Map<String,DefaultMutableTreeNode> treeLeafMap = new HashMap<String,DefaultMutableTreeNode>();
	
	public static Set<String> imageTreeSet = new HashSet<String>();

	private int frameWidth;

	private int frameHeight;

	private String key;

	private JTree tree;

	private DefaultMutableTreeNode node = null;

	public static String FILE_PATH = DiaryUtil.getProperties( "filePath" );

	private JPanel rightPanel;
	private JLabel photoLabel;

	private String readPath = "";

	private Component leftComponent;

	private JSplitPane splitPane;

	private JButton getDiaryMessageButton;

	private DiaryMessage diaryMessage;

	private JScrollPane scrollPane;

	private JLabel buttonRightLabel;

	private JLabel buttonLeftLabel;

	private JButton decryptButton;
	
	private JButton dirOpenButton;

	private Diaryphoto diaryphoto;
	
	private JPopupMenu jPopupMenu;
	private JMenuItem decryptItem;
	private JMenuItem viewDiary;
	private JMenuItem winOpen;
	private JMenuItem refreshPhoto;
	private JMenuItem nextPhoto;
	private JMenuItem prePhoto;
	private JMenuItem nameTree;
	private JMenuItem imageTree;
	
	private JPopupMenu treeMenu; 
	private JMenuItem jNameTree;
	private JMenuItem jImageTree;
	
	public static LeafShowType leafShowType =LeafShowType.NAME;
	

	private static ImageIcon leftIcon = new ImageIcon( DiaryBrowse.class.getResource( "/image/arrow_left.png" ) );
	private static ImageIcon rightIcon = new ImageIcon( DiaryBrowse.class.getResource( "/image/arrow_right.png" ) );
	
	private static ImageIcon leftMinIcon = new ImageIcon( DiaryBrowse.class.getResource( "/image/arrow_left_min.png" ) );
	private static ImageIcon rightMinIcon = new ImageIcon( DiaryBrowse.class.getResource( "/image/arrow_right_min.png" ) );
	
	public static ImageIcon imageLeafIcon = new ImageIcon( DiaryBrowse.class.getResource( "/image/tree_leaf.png" ) );
	public static ImageIcon nameLeafIcon = new ImageIcon( DiaryBrowse.class.getResource( "/image/editcopy.png" ) );
	
	private static ImageIcon RELOAD = new ImageIcon( DiaryBrowse.class.getResource( "/image/reload.png" ) );

	private static ImageIcon UNLOCK = new ImageIcon( DiaryBrowse.class.getResource( "/image/unlock.png" ) );
	
	private static ImageIcon frameImage = new ImageIcon( DiaryBrowse.class.getResource( "/image/frame_image.png" ) );
	
	
	private Cursor leftCursor = Toolkit.getDefaultToolkit().createCustomCursor( leftIcon.getImage(), new Point( 16, 16 ), "arrow_left" );
	private Cursor rightCursor = Toolkit.getDefaultToolkit().createCustomCursor( rightIcon.getImage(), new Point( 16, 16 ), "arrow_right" );

	private Cursor defaultCursor = null;

	private int x = 0;

	/**
	 * Launch the application.
	 */
	public static void main( String[] args ) {
		EventQueue.invokeLater( new Runnable() {
			@Override
			public void run() {
				try {
					DiaryBrowse window = new DiaryBrowse();
					window.setVisible( true );
					window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				} catch( Exception e ) {
					e.printStackTrace();
				}
			}
		} );
	}

	/**
	 * Create the application.
	 */
	public DiaryBrowse() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			//UIManager.getDefaults().put("Tree.lineTypeDashed",false); //设置连接线为直线
		} catch( Exception e1 ) {
			e1.printStackTrace();
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		jPopupMenu = new JPopupMenu();
		decryptItem = new JMenuItem( "解密图片" );
		decryptItem.setIcon( UNLOCK );
		decryptItem.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		decryptItem.setVisible( false );
		decryptItem.addActionListener( new  ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
				decryptPhoto();
			}
		});

		viewDiary = new JMenuItem( "查看日记" );
		viewDiary.setIcon( DiaryCreate.IMAGE ); 
		viewDiary.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		viewDiary.addActionListener( new  ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
				getDiaryMessage();
			}
		});

		winOpen = new JMenuItem( "全图浏览" );
		winOpen.setIcon( frameImage );
		winOpen.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		winOpen.addActionListener( new  ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
				try {
					Runtime.getRuntime().exec( "rundll32 c:\\Windows\\System32\\shimgvw.dll,ImageView_Fullscreen " + readPath );
				} catch( IOException e1 ) {
					e1.printStackTrace();
				}
			}
		});

		refreshPhoto = new JMenuItem( "刷新图片" );
		refreshPhoto.setIcon( RELOAD );
		refreshPhoto.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		refreshPhoto.addActionListener( new  ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ) {
				if( leftComponent != null ) {
					splitPane.setLeftComponent( null );
					getTree();
				}
			}
		});
		

		nextPhoto = new JMenuItem( "下一张" );
		nextPhoto.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		nextPhoto.setIcon( rightMinIcon );
		nextPhoto.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				getNextPhoto();
			}
		});

		prePhoto = new JMenuItem( "上一张" );
		prePhoto.setIcon( leftMinIcon );
		prePhoto.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		prePhoto.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				getPreviousPhoto();
			}
		});
		
		nameTree = new JMenuItem( "文字树" );
		nameTree.setIcon( nameLeafIcon );
		nameTree.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		nameTree.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				leafShowType = LeafShowType.NAME;
				imageTreeSet.clear();
			    tree.updateUI();
			}
		});
		
		imageTree = new JMenuItem( "图片树" );
		imageTree.setIcon( imageLeafIcon );
		imageTree.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		imageTree.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				leafShowType = LeafShowType.IMAGE;
				imageTreeSet.clear();
			    tree.updateUI();
			}
		});
		
		
		jPopupMenu.add( viewDiary );
		jPopupMenu.add( decryptItem );
		jPopupMenu.addSeparator();
		jPopupMenu.add( nextPhoto );
		jPopupMenu.add( prePhoto );
		jPopupMenu.addSeparator();
		jPopupMenu.add( winOpen );
		jPopupMenu.add( refreshPhoto );
		jPopupMenu.addSeparator();
		jPopupMenu.add( nameTree );
		jPopupMenu.add( imageTree );
		
		// System.out.println( leftIcon.getDescription() );
		defaultCursor = this.getCursor();
		File file = new File( FILE_PATH );

		if( !file.exists() ) {
			JOptionPane.showMessageDialog( null, "日记根目录不存在，请先设置根目录!", "错误", JOptionPane.ERROR_MESSAGE );
			return;
		}
		this.setIconImage( DiaryCreate.IMAGE.getImage() );
		this.setBounds( 100, 100, 1200, 650 );
		this.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );

		frameWidth = this.getWidth();
		frameHeight = this.getHeight() - 28;

		DiaryUtil.setLocationCenter( this );
		this.getContentPane().setLayout( null );
		this.setResizable( false );

		splitPane = new JSplitPane();
	    
		this.getContentPane().add( splitPane );
		splitPane.setDividerLocation( 200 );
		splitPane.setEnabled( false ); // 分割线固定位置

		// 右边内容
		rightPanel = new JPanel();
		splitPane.setRightComponent( rightPanel );
		rightPanel.setLayout( null );

		JPanel panel = new JPanel( new BorderLayout() );
		panel.setBounds( 15, ( frameHeight - 150 ) / 2, frameWidth - splitPane.getDividerLocation() - 40, 150 );
		panel.setOpaque( false );
		rightPanel.add( panel );

		buttonLeftLabel = new ButtonJlabel( "<<" );
		// buttonLeftLabel.addMouseListener( new LabelMouseListener());
		panel.add( buttonLeftLabel, BorderLayout.WEST );

		buttonRightLabel = new ButtonJlabel( ">>" );
		panel.add( buttonRightLabel, BorderLayout.EAST );

		getDiaryMessageButton = new JButton( "查看日记信息" );
		getDiaryMessageButton.setFont( new Font( "宋体", Font.PLAIN, 13 ) );
		getDiaryMessageButton.setBounds( 5, 0, 120, 30 );
		getDiaryMessageButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				getDiaryMessage();
			}
		} );
		rightPanel.add( getDiaryMessageButton );

		JButton refeshButton = new JButton( "刷新照片" );
		refeshButton.setFont( new Font( "宋体", Font.PLAIN, 13 ) );
		refeshButton.setBounds( 134, 0, 120, 30 );
		refeshButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				if( leftComponent != null ) {
					splitPane.setLeftComponent( null );
					getTree();
				}
			}
		} );
		rightPanel.add( refeshButton );
		
		
		dirOpenButton = new JButton( "打开文件夹" );
		dirOpenButton.setFont( new Font( "宋体", Font.PLAIN, 13 ) );
		dirOpenButton.setBounds( 264, 0, 120, 30 );
		dirOpenButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				String[] cmd = new String[5];  
	            cmd[0] = "cmd";  
	            cmd[1] = "/c";  
	            cmd[2] = "start";  
	            cmd[3] = " ";  
	            String dirString ="";
	            DefaultMutableTreeNode note = ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent();
	            if(!note.isRoot()){
	            	note = (DefaultMutableTreeNode) note.getParent();
	            	DiaryTreeLeaf diaryTreeLeaf =  (DiaryTreeLeaf) note.getUserObject();
	 	            String leafPath = diaryTreeLeaf.getLeafPath();
	 	            if(leafPath != null){
	 	            	dirString =leafPath;
	 	            }
	            }
	            if("".equals(dirString)){
	            	dirString = DiaryCreate.FILE_PATH;
	            }
	            cmd[4] = dirString;  
	            try {
					Runtime.getRuntime().exec(cmd);
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
			}
		} );
		rightPanel.add( dirOpenButton );
		
		

		decryptButton = new JButton( "解密图片" );
		decryptButton.setVisible( false );
		decryptButton.setFont( new Font( "宋体", Font.PLAIN, 13 ) );
		decryptButton.setBounds( 394, 0, 120, 30 );
		decryptButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				String text = decryptButton.getText();
				if("解密图片".equals(text)){
					decryptPhoto();
				}else{
					mergePhoto();
				}
				
			}
		} );
		rightPanel.add( decryptButton );

		

		photoLabel = new JLabel();
		photoLabel.setForeground( Color.DARK_GRAY );
		photoLabel.setAlignmentX( Component.CENTER_ALIGNMENT );
		photoLabel.setHorizontalAlignment( JLabel.CENTER );
		photoLabel.setBounds( 5, 0, frameWidth - splitPane.getDividerLocation() - 20, frameHeight );
		// photoLabel.setCursor( Cursor.HAND_CURSOR );
		photoLabel.addMouseListener( new MouseListener() {
			@Override
			public void mouseClicked( MouseEvent event ) {
				if( event.isMetaDown() ) {// 检测鼠标右键单击
					jPopupMenu.show( photoLabel, event.getX(), event.getY() );

				} else {
					if( event.getClickCount() == 2 ) {
						try {
							Runtime.getRuntime().exec( "rundll32 c:\\Windows\\System32\\shimgvw.dll,ImageView_Fullscreen " + readPath );
						} catch( IOException e ) {
							e.printStackTrace();
						}
					} else if( event.getClickCount() == 1 ) {
						String cursorName = getCursor().getName();
						if( "arrow_left".equals( cursorName ) ) {
							getPreviousPhoto();
						} else if( "arrow_right".equals( cursorName ) ) {
							getNextPhoto();
						}
					}
				}
			}

			@Override
			public void mouseEntered( MouseEvent arg0 ) {
			}

			@Override
			public void mouseExited( MouseEvent arg0 ) {
				setCursor( defaultCursor );
			}

			@Override
			public void mousePressed( MouseEvent arg0 ) {
			}

			@Override
			public void mouseReleased( MouseEvent arg0 ) {
			}
		} );

		photoLabel.addMouseMotionListener( new MouseMotionListener() {

			@Override
			public void mouseDragged( MouseEvent arg0 ) {
			}

			@Override
			public void mouseMoved( MouseEvent event ) {
				if( x == 0 ) {
					x = event.getX();
				}
				if( event.getX() - x > 100 ) {
					// System.out.println((event.getX() - x)+ "右移" );
					setCursor( rightCursor );
					x = event.getX();
				} else if( event.getX() - x < -100 ) {
					// System.out.println((event.getX() - x ) +"左移" );
					setCursor( leftCursor );
					x = event.getX();
				}
			}

		} );

		rightPanel.add( photoLabel );

		/* 生成树 */
		getTree();
	}

	/**
	 * 生成树 void
	 * 
	 * @author jin.guo 2016年5月31日
	 */
	public void getTree() {
		imageTreeSet.clear();
		splitPane.setDividerLocation( 200 );
		splitPane.setEnabled( false ); // 分割线固定位置
		splitPane.setBounds( 0, 0, frameWidth, frameHeight);
		
		
		scrollPane = new JScrollPane();
		splitPane.setLeftComponent( scrollPane );
		
		leftComponent = splitPane.getLeftComponent();
		// leftComponent.setPreferredSize( new Dimension( 200, frameHeight ) );

		node = new DefaultMutableTreeNode(FILE_PATH);
		tree = new JTree( node );
		tree.setRowHeight(0);
//		if(leafShowType == LeafShowType.IMAGE ){
//			tree.setRowHeight(0);
//		}else{
//			tree.setRowHeight(21);
//		}
		DiaryTreeCellRenderer diaryTreeCellRenderer = new DiaryTreeCellRenderer();
		tree.setCellRenderer(diaryTreeCellRenderer);
		tree.setFont( new Font( "宋体", Font.PLAIN, 15) );
		tree.setRootVisible( true );
		loadingTree( node );
		initExpandPath( node );
		DefaultMutableTreeNode note = ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent();
		setPhoto( note );
		
		
		treeMenu = new JPopupMenu();
		jNameTree = new JMenuItem("文字树");
		jImageTree  = new JMenuItem("图片树");
		treeMenu.add(jNameTree);
		treeMenu.add(jImageTree);
		
		jNameTree.setIcon( nameLeafIcon );
		jNameTree.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		jNameTree.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				DefaultMutableTreeNode note = ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent();
				leafShowType = LeafShowType.NAME;
				if(note.isRoot()){
					imageTreeSet.clear();
				}else{
					DiaryTreeLeaf treeLeaf =(DiaryTreeLeaf) note.getUserObject();
					String leafName = treeLeaf.getLeafName();
					if(note.isLeaf()){
						leafName = leafName.substring(0, 7);
					}
					if(imageTreeSet.contains(leafName)){
						imageTreeSet.remove(leafName);
					}
				}
				tree.updateUI();
			}
		});
		
		jImageTree.setIcon( imageLeafIcon );
		jImageTree.setFont( new Font( "新宋体", Font.BOLD, 13 ) );
		jImageTree.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent arg0 ) {
				DefaultMutableTreeNode note = ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent();
				if(note.isRoot()){
					leafShowType = LeafShowType.IMAGE;
					imageTreeSet.clear();
				}else{
					DiaryTreeLeaf treeLeaf =(DiaryTreeLeaf) note.getUserObject();
					String leafName = treeLeaf.getLeafName();
					if(note.isLeaf()){
						leafName = leafName.substring(0, 7);
						imageTreeSet.add(leafName);
					}
					imageTreeSet.add(leafName);
				}
				tree.updateUI();
			}
		});

		tree.addTreeSelectionListener( new TreeSelectionListener() {
			@Override
			public void valueChanged( TreeSelectionEvent arg0 ) {
				DefaultMutableTreeNode node = ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent();
				decryptButton.setVisible( false );
				decryptItem.setVisible( false );
				if( node == null ) {
					return;
				}
				if( node.isLeaf() ) {
					setPhoto( node );
				} else {
					readPath = "";
				}
			}
		});
		
		tree.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					treeMenu.show(tree, e.getX(), e.getY());
			    }
			}
			@Override
			public void mousePressed(MouseEvent e) {
				TreePath path = tree.getPathForLocation(e.getX(),e.getY());
				tree.setSelectionPath(path);
			}
			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		scrollPane.setViewportView( tree );
	}

	/**
	 * 生成树
	 * 
	 * @param root void
	 * @author jin.guo 2016年5月29日
	 */
	private void loadingTree( DefaultMutableTreeNode root ) {
		File rootFile = new File( FILE_PATH );
		File[] roots = rootFile.listFiles();
		DefaultMutableTreeNode node = null;
		for( int i = roots.length - 1; i >= 0; i-- ) {
			node = new DefaultMutableTreeNode( new DiaryTreeLeaf(roots[i].getName(),roots[i].getPath()));
			root.add( node );
			loadingTree( roots[i], node );
		}
	}

	/**
	 * 生成树
	 * 
	 * @param root
	 * @param node void
	 * @author jin.guo 2016年5月29日
	 */
	private void loadingTree( File root, DefaultMutableTreeNode node ) {
		File[] files = root.listFiles();
		DefaultMutableTreeNode subNode = null;
		if( files == null ) {
			return;
		}
		for( int i = files.length - 1; i >= 0; i-- ) {
			String fileName = files[i].getName();
			String filePath = files[i].getPath();
			if(files[i].isFile() && fileName.endsWith("temp")){
				continue;
			}
			subNode = new DefaultMutableTreeNode();
			DiaryTreeLeaf leaf = new DiaryTreeLeaf(fileName,filePath);
			subNode.setUserObject(leaf);
			/*if(treeLeafMap.containsKey(fileName)){
				subNode = treeLeafMap.get(fileName);
			}else{
				subNode = new DefaultMutableTreeNode();
				DiaryTreeLeaf leaf = new DiaryTreeLeaf(fileName,filePath);
				subNode.setUserObject(leaf);
				if( files[i].isFile() ) {
					try {
						BufferedImage bufferedImage = BufferedImageUtil.getBufferedImage(fileName);
						leaf.setBufferedImage(bufferedImage);
					} catch (IOException e) {
						e.printStackTrace();
					}
					treeLeafMap.put(fileName, subNode);
				}
			}*/
			node.add( subNode );
			if( files[i].isDirectory() ) {
				loadingTree( files[i], subNode );
			}
		}

	}

	/**
	 * 设置图片
	 * 
	 * @param node void
	 * @author jin.guo 2016年5月29日
	 */
	private void setPhoto( DefaultMutableTreeNode node ) {
		if( node == null ) {
			return;
		}
		readPath = "";
		photoLabel.setBounds( 5, 0, frameWidth - splitPane.getDividerLocation() - 20, frameHeight );
		if(node.isLeaf()){
			DiaryTreeLeaf leaf = (DiaryTreeLeaf) node.getUserObject();
			readPath = leaf.getLeafPath();
		}
		ImageIcon icon = new ImageIcon( readPath );
		Map<String, Integer> imageWH = DiaryUtil.getImageWH( icon.getIconWidth(), icon.getIconHeight(), photoLabel.getWidth(), photoLabel.getHeight() );
		icon.setImage( icon.getImage().getScaledInstance( imageWH.get( "width" ), imageWH.get( "height" ), Image.SCALE_SMOOTH ) );
		photoLabel.setIcon( icon );
	}

	/**
	 * 设置展开
	 * 
	 * @param node void
	 * @author jin.guo 2016年5月29日
	 */
	private void initExpandPath( DefaultMutableTreeNode node ) {
		if( node.isLeaf() ) {
			tree.addSelectionPath( new TreePath( node.getPath() ) );
			return;
		} else {
			tree.expandPath( new TreePath( node.getPath() ) );
			initExpandPath( ( DefaultMutableTreeNode )node.getChildAt( 0 ) );
		}
	}

	class ButtonJlabel extends JLabel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		ButtonJlabel( String tetx ) {
			this.setText( tetx );
			this.setHorizontalAlignment( SwingConstants.CENTER );
			this.setForeground( Color.GRAY );
			this.setFont( new Font( "华文彩云", Font.PLAIN, 70 ) );
			this.setBackground( new Color( 180, 180, 180 ) );
			this.setPreferredSize( new Dimension( 100, 150 ) );
			this.setSize( new Dimension( 100, 150 ) );
			// this.setOpaque( true );
			this.addMouseListener( new MouseListener() {
				@Override
				public void mouseClicked( MouseEvent e ) {
					if( ">>".equals( getText() ) ) {
						getNextPhoto();
					} else {
						getPreviousPhoto();
					}
				}

				@Override
				public void mouseEntered( MouseEvent e ) {
					setOpaque( true );
					repaint();

				}

				@Override
				public void mouseExited( MouseEvent e ) {
					setOpaque( false );
					repaint();
				}

				@Override
				public void mousePressed( MouseEvent e ) {

				}

				@Override
				public void mouseReleased( MouseEvent e ) {
				}
			} );
		}

	}

	/**
	 * 得到下一张图片 void
	 * 
	 * @author jin.guo 2016年6月8日
	 */
	private void getNextPhoto() {
		DefaultMutableTreeNode note = ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent();
		note = note.getNextNode();
		tree.getSelectionModel().setSelectionPath( new TreePath( note.getPath() ) );
	}

	/**
	 *  得到上一张图片 
	 * 
	 * void
	 * @author jin.guo 2016年6月8日
	 */
	private void getPreviousPhoto() {
		DefaultMutableTreeNode note = ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent();
		note = note.getPreviousNode();
		tree.getSelectionModel().setSelectionPath( new TreePath( note.getPath() ) );
	}
	
	/**
	 * 查看日记方法
	 * 
	 * void
	 * @author jin.guo 2016年6月8日
	 */
	private void getDiaryMessage(){
		if( "".equals( readPath ) ) {
			JOptionPane.showMessageDialog( null, "请选择要查看的照片！", "错误", JOptionPane.ERROR_MESSAGE );
			return;
		}
		if( DiaryUtil.password == "" || DiaryUtil.password == null ) {
			String password = JOptionPane.showInputDialog( null, "请输入日记解密密码", "密码", JOptionPane.QUESTION_MESSAGE );
			if( password == null ) {
				return;
			}
			if( !DiaryUtil.checkPassword( password, "1" ) ) {
				JOptionPane.showMessageDialog( null, "密码错误，不允许查看日记内容！", "错误", JOptionPane.ERROR_MESSAGE );
				return;
			}
		}
		Map<String, String> returnMap = ImageUtil.getMessage( readPath );
		String text = returnMap.get( "message" );

		String keyString = returnMap.get( "key" );
		if( keyString.endsWith( "TRUE" ) ) {
			decryptButton.setText("解密图片");
			decryptButton.setVisible( true );
			decryptItem.setVisible( true );
			key = keyString;
		} else if( keyString.endsWith( "TRUE1" )){
			decryptButton.setText("恢复图片");
			decryptButton.setVisible( true );
			decryptItem.setVisible( true );
			key = keyString;
		}else {
			decryptButton.setVisible( false );
			decryptItem.setVisible( false );
			key = null;
		}

		if( diaryMessage == null ) {
			diaryMessage = new DiaryMessage();
		}
		diaryMessage.setDiaryMessage( text );
		diaryMessage.setVisible( true );
	}
	
	/**
	 * 解密图片
	 * 
	 * void
	 * @author jin.guo 2016年6月8日
	 */
	private void decryptPhoto(){
		try {
			String password = JOptionPane.showInputDialog( null, "请输入照片解密密码", "密码", JOptionPane.QUESTION_MESSAGE );
			if( password == null ) {
				return;
			}
			if( !DiaryUtil.checkPassword( password, "2" ) ) {
				JOptionPane.showMessageDialog( null, "密码错误，不允许查看照片！", "错误", JOptionPane.ERROR_MESSAGE );
				return;
			}

			File photoMessage = ImageUtil.getPhotoMessage( readPath, key );
			if( photoMessage != null ) {
				if( diaryphoto != null ) {
					diaryphoto.dispose();
				}
				diaryphoto = new Diaryphoto( photoMessage );
				diaryphoto.setVisible( true );
				photoMessage.delete();
			}
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 恢复图片
	 */
	private void mergePhoto(){
		try {
			String password = JOptionPane.showInputDialog( null, "请输入照片解密密码", "密码", JOptionPane.QUESTION_MESSAGE );
			if( password == null ) {
				return;
			}
			if( !DiaryUtil.checkPassword( password, "2" ) ) {
				JOptionPane.showMessageDialog( null, "密码错误，无法恢复照片！", "错误", JOptionPane.ERROR_MESSAGE );
				return;
			}

			BufferedImage mergeImage = ImageSplitUtil.mergeImage(readPath, key );
			if( mergeImage != null ) {
				if( diaryphoto != null ) {
					diaryphoto.dispose();
				}
				diaryphoto = new Diaryphoto( mergeImage );
				diaryphoto.setVisible( true );
			}
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
}
