package com.gj.diary;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.gj.diary.utils.BufferedImageUtil;


public class DiaryTreeCellRenderer extends DefaultTreeCellRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@SuppressWarnings("hiding")
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)value;
		if(treeNode.getUserObject() instanceof DiaryTreeLeaf){
			DiaryTreeLeaf treeLeaf = (DiaryTreeLeaf)treeNode.getUserObject();
			String leafName = treeLeaf.getLeafName();
			if(leafName.endsWith(".jpg")){
				leafName = leafName.substring(0, leafName.length()-4);
			}
			setText(leafName);
			if(leaf){
				BufferedImage bufferedImage = treeLeaf.getBufferedImage();
				if(bufferedImage == null){
					try {
						bufferedImage =  BufferedImageUtil.getBufferedImage(leafName);
						treeLeaf.setBufferedImage(bufferedImage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				String year = leafName.substring(0,4);
				String mouth = leafName.substring(0,7);
				if(DiaryBrowse.imageTreeSet.contains(year) || DiaryBrowse.imageTreeSet.contains(mouth)){
					setText(null);
					setIcon(treeLeaf.getIcon());
				}else{
					if(DiaryBrowse.leafShowType == LeafShowType.NAME ){
						setIcon(treeLeaf.getMinIcon());
					}
					if(DiaryBrowse.leafShowType == LeafShowType.IMAGE ){
						setText(null);
						setIcon(treeLeaf.getIcon());
					}
				}
			}
		}
		return this;
	}

}
