package com.happy.widget.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import com.happy.widget.panel.LrcColorParentPanel.LrcEvent;

/**
 * 歌词颜色面板
 * 
 * @author Administrator
 * 
 */
public class LrcColorPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 鼠标是否进入
	 */
	private boolean isEnter = false;

	/**
	 * 鼠标是否已经选择
	 */
	private boolean isSelected = false;

	private int width = 0;
	private int height = 0;
	private Color color;
	private MouseInputListener mainLrcOPMouseListener;
	private MouseListener mouseListener = new MouseListener();

	private MainLrcOperatePanel mainLrcOperatePanel;

	private int colorIndex = 0;

	private LrcEvent lrcEvent;

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
	public LrcColorPanel(int width, int height, Color color,
			MouseInputListener mainLrcOPMouseListener,
			MainLrcOperatePanel mainLrcOperatePanel, int index,
			LrcEvent lrcEvent) {

		this.lrcEvent = lrcEvent;
		this.colorIndex = index;
		this.width = width;
		this.height = height;
		this.color = color;
		this.mainLrcOPMouseListener = mainLrcOPMouseListener;
		this.mainLrcOperatePanel = mainLrcOperatePanel;
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);

		this.setBackground(color);
	}

	private class MouseListener implements MouseInputListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			mainLrcOPMouseListener.mouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mainLrcOPMouseListener.mousePressed(e);
			if (lrcEvent != null) {
				lrcEvent.select(colorIndex);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mainLrcOPMouseListener.mouseReleased(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			mainLrcOperatePanel.setEnter(true);
			mainLrcOPMouseListener.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setCursor(null);
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
