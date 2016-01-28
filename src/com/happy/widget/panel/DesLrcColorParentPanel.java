package com.happy.widget.panel;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import com.happy.common.Constants;
import com.happy.model.MessageIntent;
import com.happy.observable.ObserverManage;

/**
 * 桌面歌词颜色面板
 * 
 * @author Administrator
 * 
 */
public class DesLrcColorParentPanel extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width = 0;
	private int height = 0;
	private Color color;

	/**
	 * 桌面窗口事件
	 */
	private MouseInputListener desLrcDialogMouseListener;
	private MouseListener mouseListener = new MouseListener();

	private DesOperatePanel desOperatePanel;

	private DesLrcColorPanel desLrcColorPanel;

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
	public DesLrcColorParentPanel(int width, int height, Color color,
			MouseInputListener desLrcDialogMouseListener,
			DesOperatePanel desOperatePanel, int index, DesLrcEvent lrcEvent) {

		this.width = width;
		this.height = height;
		this.color = color;

		this.setOpaque(false);

		this.setLayout(null);
		//
		desLrcColorPanel = new DesLrcColorPanel(width / 3 * 2, width / 3 * 2,
				color, desLrcDialogMouseListener, desOperatePanel, index,
				lrcEvent);
		int x = (width - width / 3 * 2) / 2;
		int y = (height - width / 3 * 2) / 2;
		desLrcColorPanel.setBounds(x, y, width / 3 * 2, width / 3 * 2);

		//

		String statusIconFilePath = Constants.PATH_ICON + File.separator
				+ "skin_selected_bg_tip.png";
		int sWidth = width / 3 * 2 - 2;
		int sHeight = width / 3 * 2 - 2;
		jstatusLabel = new JLabel(getBackgroundImageIcon(sWidth, sHeight,
				statusIconFilePath));
		jstatusLabel.setBounds(x, y, sWidth, sHeight);
		jstatusLabel.setVisible(false);

		this.add(jstatusLabel);
		this.add(desLrcColorPanel);

		this.desLrcDialogMouseListener = desLrcDialogMouseListener;
		this.desOperatePanel = desOperatePanel;
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
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			desLrcDialogMouseListener.mouseReleased(e);
			// setCursor(null);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			desOperatePanel.setEnter(true);
			desLrcDialogMouseListener.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
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

	public void setSelect(boolean isSelect) {
		if (isSelect) {
			jstatusLabel.setVisible(true);
		} else {
			jstatusLabel.setVisible(false);
		}
	}

	interface DesLrcEvent {
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
