package com.happy.widget.panel.tray;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.happy.util.FontsUtil;
import com.happy.widget.dialog.TrayDialog.TrayEvent;

/**
 * 退出面板
 * 
 * @author zhangliangming
 * 
 */
public class TrayExitPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 进入文本
	 */
	private JLabel enterLabel;
	/**
	 * 退出文本
	 */
	private JLabel exitLabel;

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

	public TrayExitPanel(int width, int height, TrayEvent trayEvent) {
		this.width = width;
		this.height = height;
		this.trayEvent = trayEvent;
		initComponent();
		this.setOpaque(false);
		this.addMouseListener(panelMouseListener);
	}

	private void initComponent() {
		int x = height * 2 + 5;
		this.setLayout(null);
		Font font = FontsUtil.getBaseFont(height / 2 + 2);
		enterLabel = new JLabel("退出");
		enterLabel.setForeground(Color.white);
		enterLabel.setBounds(x, 0, 100, height);
		enterLabel.setFont(font);
		enterLabel.setVisible(false);

		exitLabel = new JLabel("退出");
		exitLabel.setForeground(new Color(0, 0, 0));
		exitLabel.setBounds(x, 0, 100, height);
		exitLabel.setFont(font);

		this.add(exitLabel);
		this.add(enterLabel);
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
				trayEvent.exitClick();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			isEnter = true;
			enterLabel.setVisible(true);
			exitLabel.setVisible(false);
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			isEnter = false;
			enterLabel.setVisible(false);
			exitLabel.setVisible(true);
			repaint();
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
