package com.happy.ui;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.happy.common.Constants;
import com.happy.event.PanelMoveFrame;
import com.happy.manage.MakeLrcDialogManage;
import com.happy.model.MessageIntent;
import com.happy.model.SongInfo;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;
import com.happy.service.MediaPlayerService;
import com.happy.util.DataUtil;
import com.happy.widget.dialog.DesLrcDialog;
import com.happy.widget.dialog.SkinDialog;
import com.happy.widget.dialog.TrayDialog;
import com.happy.widget.panel.MainLrcOperatePanel;
import com.happy.widget.panel.MainLrcPanel;
import com.happy.widget.panel.MainMenuPanel;
import com.happy.widget.panel.MainPanel;

/**
 * 主界面窗口
 * 
 * @author zhangliangming
 * 
 */
public class MainFrame extends JFrame implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 背景图片
	 */
	private JLabel bgJLabel;
	private String title;// 标题

	/**
	 * 皮肤窗口
	 */
	private SkinDialog skinDialog;

	/**
	 * 桌面歌词窗口
	 */
	private DesLrcDialog desktopLrcDialog;
	/**
	 * 系统托盘
	 */
	private TrayIcon trayIcon;
	/**
	 * 是否支持托盘
	 */
	private boolean trayIsSupported = false;

	private TrayDialog trayFrame;

	public MainFrame() {
		ObserverManage.getObserver().addObserver(this);
		init();
		initDesktopLrcDialog();
		// 初始化组件
		initComponent();
		initSkin();
		initSkinDialogData();
		openDesLrcDialog();
		initTray();
	}

	/**
	 * 初始化
	 */
	private void init() {

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				close();
			}
		});

		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		title = Constants.APPTITLE;
		this.setTitle(title);
		this.setUndecorated(true);
		// this.setAlwaysOnTop(true);
		this.setSize(Constants.mainFrameWidth, Constants.mainFrameHeight);
		this.setLocation(Constants.mainFramelocaltionX,
				Constants.mainFramelocaltionY);
		// 状态栏图标
		String iconNamePath = Constants.PATH_ICON + File.separator
				+ Constants.iconName;
		this.setIconImage(new ImageIcon(iconNamePath).getImage());

	}

	/**
	 * 初始化系统托盘
	 */
	private void initTray() {
		// 判断系统是否支持系统托盘
		if (SystemTray.isSupported()) {
			trayIsSupported = true;
			String iconPath = Constants.PATH_ICON + File.separator
					+ "trayIcon.png";
			SystemTray tray = SystemTray.getSystemTray(); // 创建系统托盘
			ImageIcon icon = new ImageIcon(iconPath);
			trayIcon = new TrayIcon(icon.getImage());// 创建trayIcon
			/* 使托盘图片自动调整大小 */
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setAlwaysOnTop(true);
					setExtendedState(Frame.NORMAL);
					setAlwaysOnTop(false);
					setVisible(true);
				}
			});
			try {
				tray.add(trayIcon);
				trayIcon.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {

					}

					@Override
					public void mousePressed(MouseEvent e) {

					}

					@Override
					public void mouseExited(MouseEvent e) {

					}

					@Override
					public void mouseEntered(MouseEvent e) {

					}

					@Override
					public void mouseClicked(MouseEvent e) {
						// 右键
						if (e.getButton() == MouseEvent.BUTTON3) {
							if (trayFrame != null) {
								trayFrame.setVisible(false);
								trayFrame.dispose();
							}
							trayFrame = new TrayDialog();
							// 屏幕大小
							Dimension sd = Toolkit.getDefaultToolkit()
									.getScreenSize();
							// 除边框(如任务栏)外的屏幕可用大小
							Insets si = Toolkit
									.getDefaultToolkit()
									.getScreenInsets(getGraphicsConfiguration());
							// x, y为坐标定位
							int x = e.getX();
							int y = sd.height - si.bottom
									- trayFrame.getMHeight() - 3;
							trayFrame.setLocation(x, y);
							trayFrame.setVisible(true);
						}
					}
				});
				trayIcon.setToolTip(title);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		this.getContentPane().setLayout(null);

		// 主面板
		Constants.mainPanelWidth = Constants.mainFrameWidth / 5 * 2 - 50;
		Constants.mainPanelHeight = Constants.mainFrameHeight;
		MainPanel mainPanel = new MainPanel(this, desktopLrcDialog);
		// 添加拖动事件
		new PanelMoveFrame(mainPanel, this);
		mainPanel.setBounds(0, 0, Constants.mainPanelWidth,
				Constants.mainPanelHeight);

		// 主菜单面板
		MainMenuPanel mainMenuPanel = new MainMenuPanel();
		new PanelMoveFrame(mainMenuPanel, this);

		int mainMenuPanelWidth = Constants.mainFrameWidth
				- Constants.mainPanelWidth;
		int mainMenuPanelHeight = Constants.mainPanelHeight / 4 / 3;
		int mainMenuPanelX = Constants.mainPanelWidth;
		int mainMenuPanelY = 0;

		mainMenuPanel.setBounds(mainMenuPanelX, mainMenuPanelY,
				mainMenuPanelWidth, mainMenuPanelHeight);

		// 歌词面板
		MainLrcPanel mainLrcPanel = new MainLrcPanel(mainMenuPanelWidth,
				Constants.mainFrameHeight - mainMenuPanelHeight, mainPanel,
				desktopLrcDialog);
		mainLrcPanel.setBounds(mainMenuPanelX, mainMenuPanelHeight + 10,
				mainMenuPanelWidth - 10, Constants.mainFrameHeight
						- mainMenuPanelHeight - 10);

		// 歌词操作面板
		int loWidht = mainLrcPanel.getWidth() / 8 - 10;
		int loHeight = (loWidht - loWidht / 5 - 9) * 8;
		int loX = Constants.mainFrameWidth - loWidht;
		int loY = mainLrcPanel.getY() + (mainLrcPanel.getHeight() - loHeight)
				/ 2;
		MainLrcOperatePanel mainLrcOperatePanel = new MainLrcOperatePanel(
				loWidht, loHeight, loX, loY);
		mainLrcOperatePanel.setBounds(loX + mainLrcOperatePanel.getSeekX(),
				loY, loWidht, loHeight);

		this.getContentPane().add(mainLrcOperatePanel);
		this.getContentPane().add(mainLrcPanel);
		this.getContentPane().add(mainMenuPanel);
		this.getContentPane().add(mainPanel);
	}

	/**
	 * 初始化皮肤
	 */
	private void initSkin() {

		bgJLabel = new JLabel(getBackgroundImageIcon());// 把背景图片显示在一个标签里面
		// 把标签的大小位置设置为图片刚好填充整个面板
		bgJLabel.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.getContentPane().add(bgJLabel);
	}

	/**
	 * 初始化皮肤数据
	 */
	protected void initSkinDialogData() {
		skinDialog = new SkinDialog();
	}

	/**
	 * 桌面歌词窗口
	 */
	private void initDesktopLrcDialog() {
		desktopLrcDialog = new DesLrcDialog();
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
		background.setImage(background.getImage().getScaledInstance(
				this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
		return background;
	}

	@Override
	public void update(Observable o, final Object data) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updateUI(data);
			}
		});
	}

	/**
	 * 
	 * @param data
	 */
	protected void updateUI(Object data) {

		if (data instanceof MessageIntent) {
			MessageIntent messageIntent = (MessageIntent) data;
			if (messageIntent.getAction().equals(MessageIntent.FRAME_MIN)) {
				this.setExtendedState(Frame.ICONIFIED);
			} else if (messageIntent.getAction().equals(
					MessageIntent.FRAME_NORMAL)) {
				if (isVisible()) {
					if (getState() != Frame.ICONIFIED)
						setVisible(false);
					else if (getState() == Frame.ICONIFIED) {
						setAlwaysOnTop(true);
						setExtendedState(Frame.NORMAL);
						setAlwaysOnTop(false);
					} else {
						setExtendedState(Frame.NORMAL);
						setVisible(false);
					}
				} else {
					setAlwaysOnTop(true);
					setExtendedState(Frame.NORMAL);
					setAlwaysOnTop(false);
					setVisible(true);
				}
			} else if (messageIntent.getAction().equals(
					MessageIntent.OPEN_SKINDIALOG)) {
				onpenSkinDialog();
			} else if (messageIntent.getAction().equals(
					MessageIntent.UPDATE_SKIN)) {
				bgJLabel.setIcon(getBackgroundImageIcon());
			} else if (messageIntent.getAction().equals(
					MessageIntent.OPENORCLOSEDESLRC)) {
				openDesLrcDialog();
			} else if (messageIntent.getAction().equals(
					MessageIntent.CLOSEDESLRC)) {
				// desktopLrcDialog.setVisible(false);
				openDesLrcDialog();
			} else if (messageIntent.getAction().equals(
					MessageIntent.FRAME_CLOSE)) {
				close();
			} else if (messageIntent.getAction().equals(
					MessageIntent.OPEN_MAKELRCDIALOG)) {
				openMakeLrcDialog();
			} else if (messageIntent.getAction().equals(
					MessageIntent.CLOSE_MAKELRCDIALOG)) {
				hideMakeLrcDialog();
			}
		} else if (data instanceof SongMessage) {
			SongMessage songMessage = (SongMessage) data;
			if (songMessage.getType() == SongMessage.INITMUSIC) {
				SongInfo mSongInfo = songMessage.getSongInfo();
				if (mSongInfo != null) {
					// 更新状态栏的标题
					// this.setTitle(mSongInfo.getDisplayName());
					title = mSongInfo.getDisplayName();
				} else {
					// this.setTitle(Constants.APPTITLE);
					title = Constants.APPTITLE;
				}
				setTitle(title);
				if (trayIcon != null) {
					trayIcon.setToolTip(title);
				}
				// this.repaint();
			}
		}
	}

	/**
	 * 隐藏制作歌词窗口
	 */
	private void hideMakeLrcDialog() {
		MakeLrcDialogManage.hideMakeLrcDialog();
	}

	/**
	 * 打开制作歌词窗口
	 */
	private void openMakeLrcDialog() {
		MakeLrcDialogManage.initMakeLrcDialog();

		int x = this.getX()
				+ (this.getWidth() - MakeLrcDialogManage.getWidth()) / 2;
		int y = this.getY()
				+ (this.getHeight() - MakeLrcDialogManage.getHeight()) / 2;

		MakeLrcDialogManage.showMakeLrcDialog(x, y);
	}

	/**
	 * 打开皮肤窗口
	 */
	private void onpenSkinDialog() {

		int x = this.getX() + (this.getWidth() - skinDialog.getMWidth()) / 2;
		int y = this.getY() + (this.getHeight() - skinDialog.getMHeight()) / 2;

		skinDialog.setLocation(x, y);
		skinDialog.setModal(true);
		skinDialog.setVisible(true);
	}

	/**
	 * 显示桌面窗口
	 */
	private void openDesLrcDialog() {

		if (Constants.showDesktopLyrics) {

			// 获取屏幕边界
			Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
					this.getGraphicsConfiguration());
			// 取得底部边界高度，即任务栏高度
			int taskHeight = screenInsets.bottom;

			int y = desktopLrcDialog.getmY() - taskHeight
					- desktopLrcDialog.getmHeight();
			desktopLrcDialog.setLocation(0, y);
			desktopLrcDialog.setVisible(true);
		} else {
			desktopLrcDialog.setVisible(false);
		}
	}

	/**
	 * 关闭
	 */
	protected void close() {
		if (!trayIsSupported) {
			exit();
		} else {
			setVisible(false);
		}
	}

	/**
	 * 退出
	 */
	private void exit() {
		// 关闭窗口时，保存数据
		DataUtil.saveData();
		MediaPlayerService.getMediaPlayerService().close();
		System.exit(0);
	}

}
