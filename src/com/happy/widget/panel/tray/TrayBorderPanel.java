package com.happy.widget.panel.tray;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.happy.common.Constants;
import com.happy.util.FontsUtil;

/**
 * 托盘边框
 * 
 * @author Administrator
 * 
 */
public class TrayBorderPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int width = 0;
	private int height = 0;

	public TrayBorderPanel(int width, int height) {
		this.width = width;
		this.height = height;
		this.setOpaque(false);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// 以达到边缘平滑的效果

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);

		g2d.setFont(FontsUtil.getBaseFont(Constants.APPFONTSIZE));

		g2d.setColor(new Color(206, 206, 206));
		g2d.setStroke(new BasicStroke(3));
		g2d.drawRect(0, 0, width, height);

	}

}
