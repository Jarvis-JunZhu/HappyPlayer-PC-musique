package com.happy.widget.dialog;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.happy.common.Constants;
import com.happy.event.PanelMoveDialog;
import com.happy.model.MessageIntent;
import com.happy.observable.ObserverManage;
import com.happy.widget.button.BaseButton;

/**
 * 制作歌词窗口
 * 
 * @author zhangliangming
 * 
 */
public class MakeLrcDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width = 0;

	private int height = 0;
	/**
	 * 左右边距
	 */
	private int rightPad = 10;

	/**
	 * 基本图标路径
	 */
	private String iconPath = Constants.PATH_ICON + File.separator;
	/**
	 * 背景图片
	 */
	private JLabel bgJLabel;

	public MakeLrcDialog() {
		// 设定禁用窗体装饰，这样就取消了默认的窗体结构
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);

		width = Constants.mainFrameWidth / 4 * 3;
		height = Constants.mainFrameHeight / 5 * 4;

		this.setSize(width, height);
		this.setAlwaysOnTop(true);
		this.setModal(true);
		initComponent();
		initSkin();
	}

	private void initComponent() {

		this.getContentPane().setLayout(null);

		int titleHeight = height / 4 / 3;

		//标题面板
		JPanel titlePanel = new JPanel();
		titlePanel.setBounds(0, 0, width, titleHeight);
		new PanelMoveDialog(this, titlePanel);

		// 关闭按钮
		String closeButtonBaseIconPath = iconPath + "close_normal.png";
		String closeButtonOverIconPath = iconPath + "close_hot.png";
		String closeButtonPressedIconPath = iconPath + "close_down.png";

		int buttonSize = titleHeight / 3 * 2;

		int buttonY = (titleHeight - buttonSize) / 2;

		BaseButton closeButton = new BaseButton(closeButtonBaseIconPath,
				closeButtonOverIconPath, closeButtonPressedIconPath,
				buttonSize, buttonSize);
		closeButton.setBounds(width - buttonSize - rightPad, buttonY,
				buttonSize, buttonSize);
		closeButton.setToolTipText("关闭");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MessageIntent messageIntent = new MessageIntent();
				messageIntent.setAction(MessageIntent.CLOSE_MAKELRCDIALOG);
				ObserverManage.getObserver().setMessage(messageIntent);
			}
		});

		titlePanel.setLayout(null);

		JLabel titleJLabel = new JLabel("制作歌词窗口");
		titleJLabel.setForeground(Color.white);
		titleJLabel.setBounds(10, 0, width / 3, titleHeight);

		titlePanel.add(titleJLabel);
		titlePanel.add(closeButton);
		titlePanel.setOpaque(false);

		//内容面板
		JPanel comPanel = new JPanel();
		comPanel.setBackground(Color.white);
		comPanel.setBounds(2, titleHeight, width - 2 * 2, height - titleHeight
				- 2);

		this.getContentPane().add(titlePanel);
		this.getContentPane().add(comPanel);
	}

	/**
	 * 初始化皮肤
	 */
	private void initSkin() {

		bgJLabel = new JLabel(getBackgroundImageIcon());// 把背景图片显示在一个标签里面
		// 把标签的大小位置设置为图片刚好填充整个面板
		bgJLabel.setBounds(1, 1, width - 2, height - 2);
		this.getContentPane().setBackground(new Color(50, 50, 50));
		this.getContentPane().add(bgJLabel);
	}

	/**
	 * 获取背景图片
	 * 
	 * @return
	 */
	private ImageIcon getBackgroundImageIcon() {
		String backgroundPath = Constants.PATH_BACKGROUND + File.separator
				+ Constants.backGroundName;
		ImageIcon background = new ImageIcon(backgroundPath);// 背景图片
		background.setImage(background.getImage().getScaledInstance(width,
				height, Image.SCALE_SMOOTH));
		return background;
	}

	public int getMWidth() {
		return width;
	}

	public int getMHeight() {
		return height;
	}

}
