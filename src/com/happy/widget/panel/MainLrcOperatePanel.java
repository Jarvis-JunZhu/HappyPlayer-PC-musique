package com.happy.widget.panel;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import com.happy.common.Constants;
import com.happy.model.MessageIntent;
import com.happy.observable.ObserverManage;
import com.happy.widget.button.MainLrcOperateButton;
import com.happy.widget.label.MainLrcOperateLabel;
import com.happy.widget.panel.LrcColorParentPanel.LrcEvent;

/**
 * 歌词操作面板
 * 
 * @author Administrator
 * 
 */
public class MainLrcOperatePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 宽度
	 */
	private int width = 0;
	/**
	 * 高度
	 */
	private int height = 0;

	/**
	 * 鼠标事件
	 */
	private MouseListener mouseListener = new MouseListener();
	/**
	 * 判断是否进入
	 */
	private boolean isEnter = false;

	/**
	 * 基本图标路径
	 */
	private String iconPath = Constants.PATH_ICON + File.separator;
	/**
	 * 显示按钮
	 */
	private MainLrcOperateButton showButton;
	/**
	 * 隐藏按钮
	 */
	private MainLrcOperateButton hideButton;
	/**
	 * 增加按钮
	 */
	private MainLrcOperateButton increaseButton;

	/**
	 * 减少按钮
	 */
	private MainLrcOperateButton decreaseButton;

	private int loX = 0;
	private int loY = 0;

	private int seekX = 0;
	/**
	 * 歌词颜色面板
	 */
	private LrcColorParentPanel[] lrcColorPanels;
	/**
	 * 歌词颜色索引
	 */
	private int lrcColorIndex = Constants.lrcColorIndex;
	/**
	 * 制作歌词按钮
	 */
	private MainLrcOperateButton makeLrcButton;

	public MainLrcOperatePanel(int width, int height, int loX, int loY) {
		this.loX = loX;
		this.loY = loY;
		this.width = width;
		this.height = height;
		this.setOpaque(false);
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);

		// 初始化组件
		initComponent();
	}

	private void initComponent() {
		// 隐藏按钮
		String hideButtonBaseIconPath = iconPath + "hide_def.png";
		String hideButtonOverIconPath = iconPath + "hide_hot.png";
		String hideButtonPressedIconPath = iconPath + "hide_down.png";

		int hideWidth = width / 5;
		int hideHeight = height / 10;
		int hideX = 0;
		int hideY = (height - hideHeight) / 2;

		seekX = width - hideWidth;

		// 隐藏按钮
		hideButton = new MainLrcOperateButton(hideButtonBaseIconPath,
				hideButtonOverIconPath, hideButtonPressedIconPath, width,
				height, mouseListener, this, false, false);
		hideButton.setBounds(hideX, hideY, hideWidth, hideHeight);
		hideButton.setVisible(false);
		hideButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {

					@Override
					public void run() {
						anHide();
					}
				}.start();
			}
		});

		// 显示按钮
		String showButtonBaseIconPath = iconPath + "show_def.png";
		String showButtonOverIconPath = iconPath + "show_hot.png";
		String showButtonPressedIconPath = iconPath + "show_down.png";

		// 隐藏按钮
		showButton = new MainLrcOperateButton(showButtonBaseIconPath,
				showButtonOverIconPath, showButtonPressedIconPath, width,
				height, mouseListener, this, false, false);
		showButton.setBounds(hideX, hideY, hideWidth, hideHeight);
		showButton.setToolTipText("显示菜单");
		showButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {

					@Override
					public void run() {
						anShow();
					}
				}.start();
			}
		});

		String jbgIconPath = iconPath + "menu_bg.png";
		ImageIcon jbgIcon = new ImageIcon(jbgIconPath);
		jbgIcon.setImage(jbgIcon.getImage().getScaledInstance(
				width - hideWidth, height, Image.SCALE_SMOOTH));
		MainLrcOperateLabel jbg = new MainLrcOperateLabel(jbgIcon,
				mouseListener, this);
		jbg.setBounds(hideWidth - 14, 0, width - hideWidth + 22, height);

		int bWidth = width - hideWidth - 10;
		int bx = hideWidth + ((width - hideWidth) - bWidth) / 2;

		// 增加按钮
		String increaseButtonBaseIconPath = iconPath + "increase_def.png";
		String increaseButtonOverIconPath = iconPath + "increase_hot.png";
		String increaseButtonPressedIconPath = iconPath + "increase_down.png";

		// 增加按钮
		increaseButton = new MainLrcOperateButton(increaseButtonBaseIconPath,
				increaseButtonOverIconPath, increaseButtonPressedIconPath,
				bWidth / 3 * 2, bWidth / 3 * 2, mouseListener, this, true, true);
		increaseButton.setBounds(bx, 0, bWidth, bWidth);
		increaseButton.setToolTipText("字体增大");
		increaseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int fontSize = Constants.lrcFontSize;
				fontSize = fontSize + 5;
				if (fontSize >= Constants.lrcFontMaxSize) {
					fontSize = Constants.lrcFontMaxSize;
				}
				Constants.lrcFontSize = fontSize;

				// 通知歌词界面去刷新view

				MessageIntent messageIntent = new MessageIntent();
				messageIntent.setAction(MessageIntent.KSCMANYLINEFONTSIZE);
				ObserverManage.getObserver().setMessage(messageIntent);
			}
		});

		// 减少按钮
		String decreaseButtonBaseIconPath = iconPath + "decrease_def.png";
		String decreaseButtonOverIconPath = iconPath + "decrease_hot.png";
		String decreaseButtonPressedIconPath = iconPath + "decrease_down.png";

		// 减少按钮
		decreaseButton = new MainLrcOperateButton(decreaseButtonBaseIconPath,
				decreaseButtonOverIconPath, decreaseButtonPressedIconPath,
				bWidth / 3 * 2, bWidth / 3 * 2, mouseListener, this, true, true);
		decreaseButton.setBounds(bx,
				increaseButton.getY() + increaseButton.getHeight(), bWidth,
				bWidth);
		decreaseButton.setToolTipText("字体减少");
		decreaseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int fontSize = Constants.lrcFontSize;
				fontSize = fontSize - 5;
				if (fontSize <= Constants.lrcFontMinSize) {
					fontSize = Constants.lrcFontMinSize;
				}

				Constants.lrcFontSize = fontSize;

				// 通知歌词界面去刷新view

				MessageIntent messageIntent = new MessageIntent();
				messageIntent.setAction(MessageIntent.KSCMANYLINEFONTSIZE);
				ObserverManage.getObserver().setMessage(messageIntent);
			}
		});

		this.setLayout(null);
		lrcColorPanels = new LrcColorParentPanel[Constants.lrcColorStr.length];
		for (int i = 0; i < lrcColorPanels.length; i++) {
			lrcColorPanels[i] = new LrcColorParentPanel(bWidth, hideHeight,
					Constants.lrcColorStr[i], mouseListener, this, i, lrcEvent);
			int y = decreaseButton.getY() + decreaseButton.getHeight()
					* (i + 1);
			lrcColorPanels[i].setBounds(bx, y, bWidth, bWidth);
			this.add(lrcColorPanels[i]);
		}
		lrcColorPanels[lrcColorIndex].setSelect(true);
		// 制作歌词
		String makeLrcButtonBaseIconPath = iconPath + "makeLrc_def.png";
		String makeLrcButtonOverIconPath = iconPath + "makeLrc_hot.png";
		String makeLrcButtonPressedIconPath = iconPath + "makeLrc_down.png";

		// 制作歌词按钮
		makeLrcButton = new MainLrcOperateButton(makeLrcButtonBaseIconPath,
				makeLrcButtonOverIconPath, makeLrcButtonPressedIconPath,
				bWidth / 3 * 2, bWidth / 3 * 2 * 2 + 10, mouseListener, this,
				true, true);
		int y = decreaseButton.getY() + bWidth * (lrcColorPanels.length + 1);
		makeLrcButton.setBounds(bx, y, bWidth, bWidth * 2);
		makeLrcButton.setToolTipText("制作歌词");
		makeLrcButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		this.add(makeLrcButton);
		this.add(hideButton);
		this.add(showButton);
		this.add(increaseButton);
		this.add(decreaseButton);
		this.add(jbg);
	}

	private LrcEvent lrcEvent = new LrcEvent() {

		@Override
		public void select(int index) {
			if (index != lrcColorIndex) {
				lrcColorPanels[lrcColorIndex].setSelect(false);
				lrcColorIndex = index;
				Constants.lrcColorIndex = index;
				lrcColorPanels[lrcColorIndex].setSelect(true);

				MessageIntent messageIntent = new MessageIntent();
				messageIntent.setAction(MessageIntent.KSCMANYLINELRCCOLOR);
				ObserverManage.getObserver().setMessage(messageIntent);
			}
		}

	};

	/**
	 * 显示
	 */
	private void anShow() {
		setLocation(loX, loY);
		showButton.setVisible(false);
		hideButton.setVisible(true);

	}

	/**
	 * 隐藏
	 * 
	 * @param isInt
	 */
	private void anHide() {

		setLocation(loX + seekX, loY);
		hideButton.setVisible(false);
		showButton.setVisible(true);

	}

	private class MouseListener implements MouseInputListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (isEnter) {
				anShow();
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			anHide();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

	}

	public boolean getEnter() {
		return isEnter;
	}

	public void setEnter(boolean isEnter) {
		this.isEnter = isEnter;
		repaint();
	}

	public int getSeekX() {
		return seekX;
	}

}
