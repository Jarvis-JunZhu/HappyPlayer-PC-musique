package com.happy.widget.panel.tray;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.happy.common.Constants;
import com.happy.util.FontsUtil;
import com.happy.widget.dialog.TrayDialog.TrayEvent;

/**
 * 锁歌词面板
 * 
 * @author zhangliangming
 * 
 */
public class TrayLockPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 锁定歌词进入文本
	 */
	private JLabel enterLockedLabel;
	/**
	 * 锁定歌词进入图标
	 */
	private JLabel enterLockedIcon;
	/**
	 * 锁定歌词退出文本
	 */
	private JLabel exitLockedLabel;

	/**
	 * 锁定歌词退出图标
	 */
	private JLabel exitLockedIcon;

	/**
	 * 解锁歌词进入文本
	 */
	private JLabel enterUnLockLabel;
	/**
	 * 解锁歌词进入图标
	 */
	private JLabel enterUnLockIcon;
	/**
	 * 解锁歌词退出文本
	 */
	private JLabel exitUnLockLabel;
	/**
	 * 解锁歌词退出图标
	 */
	private JLabel exitUnLockIcon;

	private int width = 0;
	private int height = 0;

	/**
	 * 鼠标经过
	 */
	private boolean isEnter = false;
	/**
	 * 是否显示
	 */
	private boolean isShow = true;

	/**
	 * 不显示歌词锁定文本
	 */
	private JLabel disLockedLabel;
	/**
	 * 不显示歌词锁定图标
	 */
	private JLabel disLockedIcon;
	/**
	 * 不显示歌词解锁文本
	 */
	private JLabel disUnLockedLabel;
	/**
	 * 不显示歌词解锁图标
	 */
	private JLabel disUnLockIcon;

	/**
	 * 面板鼠标事件
	 */
	private PanelMouseListener panelMouseListener = new PanelMouseListener();

	private TrayEvent trayEvent;

	public TrayLockPanel(int width, int height, TrayEvent trayEvent) {
		this.width = width;
		this.height = height;
		this.trayEvent = trayEvent;
		initComponent();
		this.setOpaque(false);
		this.addMouseListener(panelMouseListener);
	}

	private void initComponent() {
		int x = height * 2;
		this.setLayout(null);
		Font font = FontsUtil.getBaseFont(height / 2 + 2);

		//
		String eliPath = Constants.PATH_ICON + File.separator
				+ "trayLock_enter.png";
		ImageIcon eliPathIcon = new ImageIcon(eliPath);
		eliPathIcon.setImage(eliPathIcon.getImage().getScaledInstance(height,
				height, Image.SCALE_SMOOTH));
		enterLockedIcon = new JLabel(eliPathIcon);
		enterLockedIcon.setBounds(5, 0, height, height);

		//
		String exliPath = Constants.PATH_ICON + File.separator
				+ "trayLock_def.png";
		ImageIcon exliPathIcon = new ImageIcon(exliPath);
		exliPathIcon.setImage(exliPathIcon.getImage().getScaledInstance(height,
				height, Image.SCALE_SMOOTH));
		exitLockedIcon = new JLabel(exliPathIcon);
		exitLockedIcon.setBounds(5, 0, height, height);

		//
		String euliPath = Constants.PATH_ICON + File.separator
				+ "trayUnLock_enter.png";
		ImageIcon euliPathIcon = new ImageIcon(euliPath);
		euliPathIcon.setImage(euliPathIcon.getImage().getScaledInstance(height,
				height, Image.SCALE_SMOOTH));
		enterUnLockIcon = new JLabel(euliPathIcon);
		enterUnLockIcon.setBounds(5, 0, height, height);

		//

		String exitliPath = Constants.PATH_ICON + File.separator
				+ "trayUnLock_def.png";
		ImageIcon exitliPathIcon = new ImageIcon(exitliPath);
		exitliPathIcon.setImage(exitliPathIcon.getImage().getScaledInstance(
				height, height, Image.SCALE_SMOOTH));
		exitUnLockIcon = new JLabel(exitliPathIcon);
		exitUnLockIcon.setBounds(5, 0, height, height);

		//
		String dislPath = Constants.PATH_ICON + File.separator
				+ "trayDisLocked.png";
		ImageIcon dislPathIcon = new ImageIcon(dislPath);
		dislPathIcon.setImage(dislPathIcon.getImage().getScaledInstance(height,
				height, Image.SCALE_SMOOTH));
		disLockedIcon = new JLabel(dislPathIcon);
		disLockedIcon.setBounds(5, 0, height, height);

		//
		String disulPath = Constants.PATH_ICON + File.separator
				+ "trayDisUnLock.png";
		ImageIcon disulPathIcon = new ImageIcon(disulPath);
		disulPathIcon.setImage(disulPathIcon.getImage().getScaledInstance(
				height, height, Image.SCALE_SMOOTH));
		disUnLockIcon = new JLabel(disulPathIcon);
		disUnLockIcon.setBounds(5, 0, height, height);

		//
		enterLockedLabel = new JLabel("锁定歌词");
		enterLockedLabel.setForeground(Color.white);
		enterLockedLabel.setBounds(x, 0, 100, height);
		enterLockedLabel.setFont(font);

		exitLockedLabel = new JLabel("锁定歌词");
		exitLockedLabel.setForeground(new Color(0, 0, 0));
		exitLockedLabel.setBounds(x, 0, 100, height);
		exitLockedLabel.setFont(font);

		enterUnLockLabel = new JLabel("解锁歌词");
		enterUnLockLabel.setForeground(Color.white);
		enterUnLockLabel.setBounds(x, 0, 100, height);
		enterUnLockLabel.setFont(font);

		exitUnLockLabel = new JLabel("解锁歌词");
		exitUnLockLabel.setForeground(new Color(0, 0, 0));
		exitUnLockLabel.setBounds(x, 0, 100, height);
		exitUnLockLabel.setFont(font);

		disLockedLabel = new JLabel("锁定歌词");
		disLockedLabel.setForeground(new Color(136, 136, 136));
		disLockedLabel.setBounds(x, 0, 100, height);
		disLockedLabel.setFont(font);

		disUnLockedLabel = new JLabel("解锁歌词");
		disUnLockedLabel.setForeground(new Color(136, 136, 136));
		disUnLockedLabel.setBounds(x, 0, 100, height);
		disUnLockedLabel.setFont(font);
		init();

		this.add(disUnLockIcon);
		this.add(disLockedIcon);
		this.add(disUnLockedLabel);
		this.add(disLockedLabel);
		this.add(exitUnLockIcon);
		this.add(enterUnLockIcon);
		this.add(exitLockedIcon);
		this.add(enterLockedIcon);
		this.add(exitUnLockLabel);
		this.add(enterUnLockLabel);
		this.add(exitLockedLabel);
		this.add(enterLockedLabel);

	}

	/**
 * 
 */
	private void init() {
		if (Constants.showDesktopLyrics) {
			isShow = true;
			initExit();
		} else {
			isShow = false;
			initDis();
		}
	}

	private void initDis() {
		if (!Constants.desLrcIsLock) {
			disUnLockIcon.setVisible(false);
			disLockedIcon.setVisible(true);
			disUnLockedLabel.setVisible(false);
			disLockedLabel.setVisible(true);
		} else {
			disUnLockIcon.setVisible(true);
			disLockedIcon.setVisible(false);
			disUnLockedLabel.setVisible(true);
			disLockedLabel.setVisible(false);
		}
		exitLockedIcon.setVisible(false);
		enterLockedIcon.setVisible(false);
		exitLockedLabel.setVisible(false);
		enterLockedLabel.setVisible(false);
		enterUnLockLabel.setVisible(false);
		exitUnLockLabel.setVisible(false);
		exitUnLockIcon.setVisible(false);
		enterUnLockIcon.setVisible(false);
	}

	private void initExit() {
		if (Constants.desLrcIsLock) {
			disUnLockedLabel.setVisible(false);
			disLockedLabel.setVisible(false);
			exitLockedIcon.setVisible(false);
			enterLockedIcon.setVisible(false);
			exitLockedLabel.setVisible(false);
			enterLockedLabel.setVisible(false);
			enterUnLockLabel.setVisible(false);
			exitUnLockLabel.setVisible(true);
			exitUnLockIcon.setVisible(true);
			enterUnLockIcon.setVisible(false);
		} else {
			disUnLockedLabel.setVisible(false);
			disLockedLabel.setVisible(false);
			exitLockedIcon.setVisible(true);
			enterLockedIcon.setVisible(false);
			exitLockedLabel.setVisible(true);
			enterLockedLabel.setVisible(false);
			enterUnLockLabel.setVisible(false);
			exitUnLockLabel.setVisible(false);
			exitUnLockIcon.setVisible(false);
			enterUnLockIcon.setVisible(false);
		}
		disUnLockIcon.setVisible(false);
		disLockedIcon.setVisible(false);
	}

	private void initEnter() {
		if (Constants.desLrcIsLock) {
			disUnLockedLabel.setVisible(false);
			disLockedLabel.setVisible(false);
			exitLockedIcon.setVisible(false);
			enterLockedIcon.setVisible(false);
			exitLockedLabel.setVisible(false);
			enterLockedLabel.setVisible(false);
			enterUnLockLabel.setVisible(true);
			exitUnLockLabel.setVisible(false);
			exitUnLockIcon.setVisible(false);
			enterUnLockIcon.setVisible(true);
		} else {
			disUnLockedLabel.setVisible(false);
			disLockedLabel.setVisible(false);
			exitLockedIcon.setVisible(false);
			enterLockedIcon.setVisible(true);
			exitLockedLabel.setVisible(false);
			enterLockedLabel.setVisible(true);
			enterUnLockLabel.setVisible(false);
			exitUnLockLabel.setVisible(false);
			exitUnLockIcon.setVisible(false);
			enterUnLockIcon.setVisible(false);
		}
		disUnLockIcon.setVisible(false);
		disLockedIcon.setVisible(false);
	}

	/**
	 * 面板鼠标事件
	 * 
	 * @author zhangliangming
	 * 
	 */
	class PanelMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (!isShow)
				return;
			if (trayEvent != null) {
				Constants.desLrcIsLock = !Constants.desLrcIsLock;
				initEnter();
				trayEvent.lockClick();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (!isShow)
				return;
			isEnter = true;
			initEnter();
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (!isShow)
				return;
			isEnter = false;
			initExit();
			repaint();
		}

	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
		init();
	}

	@Override
	protected void paintBorder(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// 以达到边缘平滑的效果

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);

		if (isEnter && isShow) {
			g2d.setPaint(new Color(182, 182, 182));
			g2d.fillRect(0, 0, width, height);
		}
	}

}
