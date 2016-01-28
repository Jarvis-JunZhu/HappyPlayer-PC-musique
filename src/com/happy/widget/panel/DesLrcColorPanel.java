package com.happy.widget.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import com.happy.common.Constants;
import com.happy.model.MessageIntent;
import com.happy.observable.ObserverManage;
import com.happy.widget.panel.DesLrcColorParentPanel.DesLrcEvent;

/**
 * 歌词颜色面板
 * 
 * @author Administrator
 * 
 */
public class DesLrcColorPanel extends JPanel implements Observer {

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

	/**
	 * 桌面窗口事件
	 */
	private MouseInputListener desLrcDialogMouseListener;
	private MouseListener mouseListener = new MouseListener();

	private DesOperatePanel desOperatePanel;

	private int colorIndex = 0;

	private DesLrcEvent desLrcEvent;

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
	public DesLrcColorPanel(int width, int height, Color color,
			MouseInputListener desLrcDialogMouseListener,
			DesOperatePanel desOperatePanel, int index, DesLrcEvent desLrcEvent) {

		this.desLrcEvent = desLrcEvent;
		this.colorIndex = index;
		this.width = width;
		this.height = height;
		this.color = color;
		this.desLrcDialogMouseListener = desLrcDialogMouseListener;
		this.desOperatePanel = desOperatePanel;

		this.setBackground(color);

		initLockEvent();
		ObserverManage.getObserver().addObserver(this);
	}

	private void initLockEvent() {
		if (!Constants.desLrcIsLock) {
			this.addMouseListener(mouseListener);
			this.addMouseMotionListener(mouseListener);
		} else {
			this.removeMouseListener(mouseListener);
			this.removeMouseMotionListener(mouseListener);
		}

	}

	private class MouseListener implements MouseInputListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			desLrcDialogMouseListener.mouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			desLrcDialogMouseListener.mousePressed(e);
			if (desLrcEvent != null) {
				desLrcEvent.select(colorIndex);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			desLrcDialogMouseListener.mouseReleased(e);
			// setCursor(null);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			desOperatePanel.setEnter(true);
			desLrcDialogMouseListener.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setCursor(null);
			desOperatePanel.setEnter(false);
			desLrcDialogMouseListener.mouseExited(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			desLrcDialogMouseListener.mouseDragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			desLrcDialogMouseListener.mouseMoved(e);
		}

	}

	@Override
	public void update(Observable o, final Object data) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (data instanceof MessageIntent) {
					MessageIntent messageIntent = (MessageIntent) data;
					if (messageIntent.getAction().equals(
							MessageIntent.LOCKDESLRC)) {
						initLockEvent();
					}
				}
			}
		});
	}
}
