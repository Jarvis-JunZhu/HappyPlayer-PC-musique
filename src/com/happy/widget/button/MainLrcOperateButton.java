package com.happy.widget.button;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.MouseInputListener;

import com.happy.widget.panel.MainLrcOperatePanel;

/**
 * 歌词操作面板按钮
 * 
 * @author Administrator
 * 
 */
public class MainLrcOperateButton extends JButton {

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

	public MainLrcOperateButton(String baseIconPath, String overIconPath,
			String pressedIconPath, int width, int height,
			MouseInputListener mainLrcOPMouseListener,
			MainLrcOperatePanel mainLrcOperatePanel, boolean scale,
			boolean addMouseListener) {

		ImageIcon icon = new ImageIcon(baseIconPath);
		if (scale)
			icon.setImage(icon.getImage().getScaledInstance(width, height,
					Image.SCALE_SMOOTH));
		this.setIcon(icon);

		ImageIcon rolloverIcon = new ImageIcon(overIconPath);
		if (scale)
			rolloverIcon.setImage(rolloverIcon.getImage().getScaledInstance(
					width, height, Image.SCALE_SMOOTH));
		this.setRolloverIcon(rolloverIcon);

		ImageIcon pressedIcon = new ImageIcon(pressedIconPath);
		if (scale)
			pressedIcon.setImage(pressedIcon.getImage().getScaledInstance(
					width, height, Image.SCALE_SMOOTH));
		this.setPressedIcon(pressedIcon);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		this.setDoubleBuffered(false);
		this.setOpaque(false);
		this.setFocusable(false);
		// 设置鼠标的图标
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		this.mainLrcOPMouseListener = mainLrcOPMouseListener;
		this.mainLrcOperatePanel = mainLrcOperatePanel;

		if (addMouseListener)
			this.addMouseListener(mouseListener);
		if (addMouseListener)
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
