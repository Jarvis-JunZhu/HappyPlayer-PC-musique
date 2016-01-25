package com.happy.widget.dialog;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.happy.widget.panel.ToolTipPanel;

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
	 * 背景
	 */
	private ToolTipPanel tipPanel = new ToolTipPanel();

	public SongProgressTipDialog() {
		// 设定禁用窗体装饰，这样就取消了默认的窗体结构
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);
		initComponent();
	}

	private void initComponent() {
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(tipPanel, BorderLayout.CENTER);
	}

	public ToolTipPanel getTipPanel() {
		return tipPanel;
	}

}
