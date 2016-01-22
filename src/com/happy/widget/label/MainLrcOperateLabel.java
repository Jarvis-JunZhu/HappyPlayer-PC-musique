package com.happy.widget.label;

import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;

import com.happy.widget.panel.MainLrcOperatePanel;

/**
 * 歌词操作面板背景图片
 * 
 * @author Administrator
 * 
 */
public class MainLrcOperateLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private MouseInputListener mainLrcOPMouseListener;
	private MouseListener mouseListener = new MouseListener();

	private MainLrcOperatePanel mainLrcOperatePanel;

	public MainLrcOperateLabel(ImageIcon imageIcon,
			MouseInputListener mainLrcOPMouseListener,
			MainLrcOperatePanel mainLrcOperatePanel) {

		super(imageIcon);

		this.mainLrcOPMouseListener = mainLrcOPMouseListener;
		this.mainLrcOperatePanel = mainLrcOperatePanel;

		this.addMouseListener(mouseListener);

		this.addMouseMotionListener(mouseListener);
	}

	private class MouseListener implements MouseInputListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			mainLrcOPMouseListener.mouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mainLrcOPMouseListener.mousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mainLrcOPMouseListener.mouseReleased(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			mainLrcOperatePanel.setEnter(true);
			mainLrcOPMouseListener.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			mainLrcOPMouseListener.mouseExited(e);
			mainLrcOperatePanel.setEnter(false);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			mainLrcOPMouseListener.mouseDragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mainLrcOPMouseListener.mouseMoved(e);
		}

	}
}
