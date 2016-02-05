package com.happy.widget.label;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import com.happy.common.Constants;
import com.happy.model.MessageIntent;
import com.happy.observable.ObserverManage;
import com.happy.widget.panel.DesOperatePanel;

/**
 * 歌词操作面板背景图片
 * 
 * @author Administrator
 * 
 */
public class DesOperateLabel extends JLabel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 桌面窗口事件
	 */
	private MouseInputListener desLrcDialogMouseListener;
	private MouseListener mouseListener = new MouseListener();

	private DesOperatePanel desOperatePanel;

	private String backgroundPath;

	public DesOperateLabel(int width, int height,
			MouseInputListener desLrcDialogMouseListener,
			DesOperatePanel desOperatePanel) {

		this.desLrcDialogMouseListener = desLrcDialogMouseListener;
		this.desOperatePanel = desOperatePanel;

		initLockEvent();
		initSkin();
		ObserverManage.getObserver().addObserver(this);
	}

	private void initSkin() {
		backgroundPath = Constants.PATH_BACKGROUND + File.separator
				+ Constants.backGroundName;
	}

	protected void paintComponent(Graphics g) {

		ImageIcon icon = new ImageIcon(backgroundPath);
		Image img = icon.getImage();
		g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(),
				icon.getImageObserver());
	}

	private void initLockEvent() {
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);

	}

	private class MouseListener implements MouseInputListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (!Constants.desLrcIsLock)
				desLrcDialogMouseListener.mouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (!Constants.desLrcIsLock)
				desLrcDialogMouseListener.mousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (!Constants.desLrcIsLock)
				desLrcDialogMouseListener.mouseReleased(e);
			// setCursor(null);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (!Constants.desLrcIsLock) {
				desOperatePanel.setEnter(true);
				desLrcDialogMouseListener.mouseEntered(e);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (!Constants.desLrcIsLock) {
				desOperatePanel.setEnter(false);
				desLrcDialogMouseListener.mouseExited(e);
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (!Constants.desLrcIsLock)
				desLrcDialogMouseListener.mouseDragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (!Constants.desLrcIsLock)
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
							MessageIntent.UPDATE_SKIN)) {
						initSkin();
					}
				}
			}
		});
	}
}
