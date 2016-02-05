package com.happy.widget.dialog;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.happy.common.Constants;
import com.happy.event.PanelMoveDialog;
import com.happy.manage.MediaManage;
import com.happy.model.SongInfo;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;
import com.happy.widget.button.BaseButton;
import com.happy.widget.button.DefButton;
import com.happy.widget.panel.MakeLrcLvRuPanel;
import com.happy.widget.panel.MakeLrcZhiZuoPanel;

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
	/**
	 * 步骤图片
	 */
	private JLabel stepJLabel;

	/**
	 * 定义卡片布局对象
	 */
	private CardLayout card = new CardLayout();
	/**
	 * 卡片索引
	 */
	private int cardIndex = 1;
	/**
	 * 上一步
	 */
	private DefButton preButton;

	/**
	 * 下一步
	 */
	private DefButton nextButton;

	/**
	 * 完成
	 */
	private DefButton finishButton;
	/**
	 * 录入面板
	 */
	private MakeLrcLvRuPanel makeLrcLvRuPanel;
	/**
	 * 歌词制作面板
	 */
	private MakeLrcZhiZuoPanel makeLrcZhiZuoPanel;

	public MakeLrcDialog() {
		// 设定禁用窗体装饰，这样就取消了默认的窗体结构
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);

		width = Constants.mainFrameWidth / 4 * 3;
		height = Constants.mainFrameHeight / 5 * 4 + 10;

		this.setSize(width, height);
		this.setAlwaysOnTop(true);
		this.setModal(true);
		initComponent();
		initSkin();
		Constants.makeLrcDialogIsShow = true;
		loadData();
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SongInfo songInfo = MediaManage.getMediaManage().getSongInfo();
				makeLrcLvRuPanel.initData(songInfo);
				makeLrcZhiZuoPanel.initData(songInfo);
			}
		});
	}

	private void initComponent() {

		this.getContentPane().setLayout(null);

		int titleHeight = height / 4 / 3 - 10;

		// 标题面板
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
				close();
			}
		});

		titlePanel.setLayout(null);
		// 标题
		JLabel titleJLabel = new JLabel("制作歌词");
		titleJLabel.setForeground(Color.white);
		titleJLabel.setBounds(10, 0, width / 4, titleHeight);

		titlePanel.add(titleJLabel);
		titlePanel.add(closeButton);
		titlePanel.setOpaque(false);

		// 内容面板
		JPanel comPanel = new JPanel();
		comPanel.setBackground(Color.white);
		comPanel.setBounds(1, titleHeight, width - 1 * 2, height - titleHeight
				- 1);
		// 高度
		int lH = titleHeight + 10;
		// 步骤图片
		stepJLabel = new JLabel();
		stepJLabel.setBounds(rightPad * 2, rightPad / 2, width - rightPad * 4,
				lH);
		initStepPic(cardIndex);
		//
		final Panel cardPanel = new Panel();
		// 设置cardPanel面板对象为卡片布局
		cardPanel.setLayout(card);

		int wC = width - rightPad * 2;
		int hC = comPanel.getHeight() - lH * 2 - rightPad;
		cardPanel.setBounds(rightPad,
				stepJLabel.getY() + stepJLabel.getHeight() + rightPad / 2, wC,
				hC);

		// 底部面板
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBounds(rightPad,
				cardPanel.getY() + cardPanel.getHeight(), width - rightPad * 2,
				lH);
		bottomPanel.setOpaque(false);
		//
		int bHSize = bottomPanel.getHeight() / 2;
		int bWSize = bHSize * 4;
		int oBWSize = bHSize * 3;
		int y = (bottomPanel.getHeight() - bHSize) / 2;

		//
		makeLrcLvRuPanel = new MakeLrcLvRuPanel(wC, hC, oBWSize, bHSize);

		makeLrcZhiZuoPanel = new MakeLrcZhiZuoPanel(wC, hC, bHSize,
				makeLrcLvRuPanel);

		JPanel panel3 = new JPanel();
		panel3.setBackground(Color.blue);

		cardPanel.add(makeLrcLvRuPanel, "lvru");
		cardPanel.add(makeLrcZhiZuoPanel, "zhizuo");
		cardPanel.add(panel3, "3");

		// 取消按钮
		DefButton chanelButton = new DefButton(bWSize, bHSize);
		chanelButton.setText("取消");
		chanelButton.setBounds(bottomPanel.getWidth() - rightPad - bWSize, y,
				bWSize, bHSize);
		chanelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		//
		nextButton = new DefButton(bWSize, bHSize);
		nextButton.setText("下一步");
		nextButton.setBounds(chanelButton.getX() - rightPad - bWSize, y,
				bWSize, bHSize);
		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cardIndex++;
				if (cardIndex > 3)
					cardIndex = 3;
				if (cardIndex == 2) {
					String lrcComText = makeLrcLvRuPanel.getLrcComTextArea()
							.getText();
					if (lrcComText == null || lrcComText.equals("")) {
						cardIndex--;
						JOptionPane.showMessageDialog(null, "歌词内容不能为空", "提示",
								JOptionPane.WARNING_MESSAGE);
					} else {

						SongMessage songMessage = new SongMessage();
						songMessage.setType(SongMessage.STOPMUSIC);
						// 通知
						ObserverManage.getObserver().setMessage(songMessage);

						initBottomUI();
						card.next(cardPanel);
					}
				} else {
					initBottomUI();
					card.next(cardPanel);
				}
			}
		});
		//
		preButton = new DefButton(bWSize, bHSize);
		preButton.setText("上一步");
		preButton.setBounds(nextButton.getX() - rightPad - bWSize, y, bWSize,
				bHSize);
		preButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cardIndex--;
				if (cardIndex <= 0)
					cardIndex = 1;
				if (cardIndex == 1) {
					SongMessage songMessage = new SongMessage();
					songMessage.setType(SongMessage.STOPMUSIC);
					// 通知
					ObserverManage.getObserver().setMessage(songMessage);

				}
				initBottomUI();
				card.previous(cardPanel);
			}
		});

		finishButton = new DefButton(bWSize, bHSize);
		finishButton.setText("保存");
		finishButton.setBounds(chanelButton.getX() - rightPad - bWSize, y,
				bWSize, bHSize);
		finishButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		//
		bottomPanel.setLayout(null);
		bottomPanel.add(chanelButton);
		bottomPanel.add(nextButton);
		bottomPanel.add(preButton);
		bottomPanel.add(finishButton);
		//
		// 初始化底部ui
		initBottomUI();
		//

		comPanel.setLayout(null);
		comPanel.add(stepJLabel);
		comPanel.add(cardPanel);
		comPanel.add(bottomPanel);

		this.getContentPane().add(titlePanel);
		this.getContentPane().add(comPanel);
	}

	protected void close() {
		Constants.makeLrcDialogIsShow = false;
		dispose();

		SongMessage songMessage = new SongMessage();
		songMessage.setType(SongMessage.PLAYMUSIC);
		// 通知
		ObserverManage.getObserver().setMessage(songMessage);
		// MessageIntent messageIntent = new MessageIntent();
		// messageIntent.setAction(MessageIntent.CLOSE_MAKELRCDIALOG);
		// ObserverManage.getObserver().setMessage(messageIntent);
	}

	/**
	 * 初始化底部菜单
	 */
	private void initBottomUI() {
		if (cardIndex == 1) {
			preButton.setVisible(false);
		} else {
			preButton.setVisible(true);
		}
		if (cardIndex == 3) {
			finishButton.setVisible(true);
			nextButton.setVisible(false);
		} else {
			finishButton.setVisible(false);
			nextButton.setVisible(true);
		}
		initStepPic(cardIndex);
	}

	/**
	 * 初始化图片
	 * 
	 * @param i
	 */
	private void initStepPic(int i) {
		String backgroundPath = Constants.PATH_ICON + File.separator + "step_"
				+ i + ".png";
		ImageIcon background = new ImageIcon(backgroundPath);// 背景图片
		stepJLabel.setIcon(background);
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
