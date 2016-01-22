package com.happy.widget.dialog;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * 歌曲进度提示窗口
 * 
 * @author zhangliangming
 * 
 */
public class SongProgressTipDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 提示文本
	 */
	private JLabel tipLabel;

	public SongProgressTipDialog() {
		// 设定禁用窗体装饰，这样就取消了默认的窗体结构
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);
		initComponent();
	}

	private void initComponent() {
		this.getContentPane().setLayout(new BorderLayout());
		tipLabel = new JLabel("00:00", JLabel.CENTER);
		this.getContentPane().add(tipLabel, BorderLayout.CENTER);
	}

	public JLabel getTipLabel() {
		return tipLabel;
	}

}
