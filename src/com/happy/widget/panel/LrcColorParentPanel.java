package com.happy.widget.panel;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import com.happy.common.Constants;

/**
 * 歌词颜色面板
 * 
 * @author Administrator
 * 
 */
public class LrcColorParentPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width = 0;
	private int height = 0;
	private Color color;
	private MouseInputListener mainLrcOPMouseListener;
	private MouseListener mouseListener = new MouseListener();

	private MainLrcOperatePanel mainLrcOperatePanel;

	private LrcColorPanel lrcColorPanel;

	private JLabel jstatusLabel;


	/**
	 * 
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param color
	 *            面板显示的颜色
	 * @param index
	 * @param lrcEvent 
	 */
	public LrcColorParentPanel(int width, int height, Color color,
			MouseInputListener mainLrcOPMouseListener,
			MainLrcOperatePanel mainLrcOperatePanel, int index,
			LrcEvent lrcEvent) {

		this.width = width;
		this.height = height;
		this.color = color;
		this.mainLrcOPMouseListener = mainLrcOPMouseListener;
		this.mainLrcOperatePanel = mainLrcOperatePanel;
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
		this.setOpaque(false);

		this.setLayout(null);
		//
		lrcColorPanel = new LrcColorPanel(width / 3 * 2, width / 3 * 2, color,
				mainLrcOPMouseListener, mainLrcOperatePanel, index, lrcEvent);
		int x = (width - width / 3 * 2) / 2;
		int y = (height - width / 3 * 2) / 2;
		lrcColorPanel.setBounds(x, y, width / 3 * 2, width / 3 * 2);

		//

		String statusIconFilePath = Constants.PATH_ICON + File.separator
				+ "skin_selected_bg_tip.png";
		int sWidth = width / 3 * 2 / 2;
		int sHeight = width / 3 * 2 / 2;
		jstatusLabel = new JLabel(getBackgroundImageIcon(sWidth, sHeight,
				statusIconFilePath));
		jstatusLabel.setBounds(x, y, sWidth, sHeight);
		jstatusLabel.setVisible(false);

		this.add(jstatusLabel);
		this.add(lrcColorPanel);
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

	public void setSelect(boolean isSelect) {
		if (isSelect) {
			jstatusLabel.setVisible(true);
		} else {
			jstatusLabel.setVisible(false);
		}
	}

	interface LrcEvent {
		public void select(int index);
	}

	/**
	 * 获取背景图片
	 * 
	 * @return
	 */
	private ImageIcon getBackgroundImageIcon(int width, int height, String path) {
		ImageIcon background = new ImageIcon(path);// 背景图片
		background.setImage(background.getImage().getScaledInstance(width,
				height, Image.SCALE_SMOOTH));
		return background;
	}
}
