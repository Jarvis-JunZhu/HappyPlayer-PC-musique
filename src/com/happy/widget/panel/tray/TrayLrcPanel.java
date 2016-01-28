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
import javax.swing.event.MouseInputListener;

import com.happy.common.Constants;
import com.happy.util.FontsUtil;
import com.happy.widget.dialog.TrayDialog;
import com.happy.widget.dialog.TrayDialog.TrayEvent;

/**
 * 是否显示歌词面板
 * 
 * @author zhangliangming
 * 
 */
public class TrayLrcPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 显示歌词进入文本
	 */
	private JLabel enterShowLrcLabel;

	/**
	 * 显示歌词退出文本
	 */
	private JLabel exitShowLrcLabel;

	/**
	 * 关闭歌词进入文本
	 */
	private JLabel enterHideLrcLabel;

	/**
	 * 关闭歌词退出文本
	 */
	private JLabel exitHideLrcLabel;

	private JLabel enterLrcIcon;
	private JLabel exitLrcIcon;

	private int width = 0;
	private int height = 0;

	/**
	 * 鼠标经过
	 */
	private boolean isEnter = false;

	/**
	 * 面板鼠标事件
	 */
	private PanelMouseListener panelMouseListener = new PanelMouseListener();

	private TrayEvent trayEvent;
	private MouseInputListener dialogMouseListener;

	private TrayDialog trayDialog;

	public TrayLrcPanel(int width, int height, TrayEvent trayEvent,
			MouseInputListener dialogMouseListener, TrayDialog trayDialog) {

		this.trayDialog = trayDialog;
		this.dialogMouseListener = dialogMouseListener;
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
		String enterPath = Constants.PATH_ICON + File.separator
				+ "trayLrc_enter.png";
		ImageIcon enterPathIcon = new ImageIcon(enterPath);
		enterPathIcon.setImage(enterPathIcon.getImage().getScaledInstance(
				height, height, Image.SCALE_SMOOTH));
		enterLrcIcon = new JLabel(enterPathIcon);
		enterLrcIcon.setBounds(5, 0, height, height);

		//

		String exitPath = Constants.PATH_ICON + File.separator
				+ "trayLrc_def.png";
		ImageIcon exitPathIcon = new ImageIcon(exitPath);
		exitPathIcon.setImage(exitPathIcon.getImage().getScaledInstance(height,
				height, Image.SCALE_SMOOTH));
		exitLrcIcon = new JLabel(exitPathIcon);
		exitLrcIcon.setBounds(5, 0, height, height);

		//
		enterShowLrcLabel = new JLabel("显示桌面歌词");
		enterShowLrcLabel.setForeground(Color.white);
		enterShowLrcLabel.setBounds(x, 0, 120, height);
		enterShowLrcLabel.setFont(font);

		exitShowLrcLabel = new JLabel("显示桌面歌词");
		exitShowLrcLabel.setForeground(new Color(0, 0, 0));
		exitShowLrcLabel.setBounds(x, 0, 120, height);
		exitShowLrcLabel.setFont(font);

		enterHideLrcLabel = new JLabel("关闭桌面歌词");
		enterHideLrcLabel.setForeground(Color.white);
		enterHideLrcLabel.setBounds(x, 0, 120, height);
		enterHideLrcLabel.setFont(font);

		exitHideLrcLabel = new JLabel("关闭桌面歌词");
		exitHideLrcLabel.setForeground(new Color(0, 0, 0));
		exitHideLrcLabel.setBounds(x, 0, 120, height);
		exitHideLrcLabel.setFont(font);

		initExit();

		this.add(exitLrcIcon);
		this.add(enterLrcIcon);
		this.add(exitHideLrcLabel);
		this.add(enterHideLrcLabel);
		this.add(exitShowLrcLabel);
		this.add(enterShowLrcLabel);

	}

	private void initExit() {
		if (Constants.showDesktopLyrics) {
			enterHideLrcLabel.setVisible(false);
			exitHideLrcLabel.setVisible(true);
			enterShowLrcLabel.setVisible(false);
			exitShowLrcLabel.setVisible(false);
			exitLrcIcon.setVisible(true);
			enterLrcIcon.setVisible(false);
		} else {
			enterHideLrcLabel.setVisible(false);
			exitHideLrcLabel.setVisible(false);
			enterShowLrcLabel.setVisible(false);
			exitShowLrcLabel.setVisible(true);
			exitLrcIcon.setVisible(true);
			enterLrcIcon.setVisible(false);
		}

	}

	private void initEnter() {
		if (Constants.showDesktopLyrics) {
			enterHideLrcLabel.setVisible(true);
			exitHideLrcLabel.setVisible(false);
			enterShowLrcLabel.setVisible(false);
			exitShowLrcLabel.setVisible(false);
			exitLrcIcon.setVisible(false);
			enterLrcIcon.setVisible(true);
		} else {
			enterHideLrcLabel.setVisible(false);
			exitHideLrcLabel.setVisible(false);
			enterShowLrcLabel.setVisible(true);
			exitShowLrcLabel.setVisible(false);
			exitLrcIcon.setVisible(false);
			enterLrcIcon.setVisible(true);
		}

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

			if (trayEvent != null) {
				Constants.showDesktopLyrics = !Constants.showDesktopLyrics;
				initEnter();
				trayEvent.lrcClick();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

			isEnter = true;
			initEnter();
			repaint();

			trayDialog.setEnter(true);
			dialogMouseListener.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {

			isEnter = false;
			initExit();
			repaint();

			dialogMouseListener.mouseExited(e);
			trayDialog.setEnter(false);
		}

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

		if (isEnter) {
			g2d.setPaint(new Color(182, 182, 182));
			g2d.fillRect(0, 0, width, height);
		}
	}

}
