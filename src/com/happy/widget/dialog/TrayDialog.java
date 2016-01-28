package com.happy.widget.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import com.happy.common.Constants;
import com.happy.manage.MediaManage;
import com.happy.model.MessageIntent;
import com.happy.model.SongInfo;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;
import com.happy.service.MediaPlayerService;
import com.happy.util.DataUtil;
import com.happy.util.FontsUtil;
import com.happy.widget.button.BaseButton;
import com.happy.widget.panel.tray.TrayBorderPanel;
import com.happy.widget.panel.tray.TrayExitPanel;
import com.happy.widget.panel.tray.TrayLockPanel;
import com.happy.widget.panel.tray.TrayLrcPanel;

/**
 * 系统托盘窗口
 * 
 * @author Administrator
 * 
 */
public class TrayDialog extends JDialog implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int width = 0;

	private int height = 0;
	/**
	 * 锁面板
	 */
	private TrayLockPanel trayLockPanel;

	/**
	 * 判断是否进入
	 */
	private boolean isEnter = false;

	private TrayEvent trayEvent = new TrayEvent() {

		@Override
		public void exitClick() {
			setVisible(false);
			// 关闭窗口时，保存数据
			DataUtil.saveData();
			MediaPlayerService.getMediaPlayerService().close();
			System.exit(0);
		}

		@Override
		public void lockClick() {
			MessageIntent messageIntent = new MessageIntent();
			messageIntent.setAction(MessageIntent.LOCKDESLRC);
			ObserverManage.getObserver().setMessage(messageIntent);
			// setVisible(false);
		}

		@Override
		public void lrcClick() {
			if (Constants.showDesktopLyrics) {
				setVisible(false);
				trayLockPanel.setShow(true);
			} else {
				trayLockPanel.setShow(false);
			}
			MessageIntent messageIntent = new MessageIntent();
			messageIntent.setAction(MessageIntent.CLOSEDESLRC);
			ObserverManage.getObserver().setMessage(messageIntent);
		}
	};

	private DialogMouseListener dialogMouseListener = new DialogMouseListener();
	/**
	 * 歌曲标题
	 */
	private JLabel songTitleLabel;

	/**
	 * 播放按钮
	 */
	private BaseButton playButton;
	/**
	 * 暂停按钮
	 */
	private BaseButton pauseButton;

	private int cHeight = 0;

	private int padding = 4;

	public TrayDialog() {
		this.setUndecorated(true);
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) (screenDimension.getWidth() / 8);
		height = (int) (screenDimension.getHeight() / 4 + 10);
		cHeight = height / 5 - 6 * padding;
		height = height - cHeight * 2;
		this.setSize(width, height);
		this.setVisible(false);
		this.setAlwaysOnTop(true);
		this.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {

			}

			public void focusLost(FocusEvent e) {
				if (!isEnter) {
					ObserverManage.getObserver()
							.deleteObserver(TrayDialog.this);
					dispose();
				}
			}

		});
		initComponent();

		this.addMouseListener(dialogMouseListener);
		this.addMouseMotionListener(dialogMouseListener);
		ObserverManage.getObserver().addObserver(this);

		loadData();
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		new Thread() {

			@Override
			public void run() {

				SongInfo songInfo = MediaManage.getMediaManage().getSongInfo();
				SongMessage songMessage = new SongMessage();
				songMessage.setType(SongMessage.INITMUSIC);
				songMessage.setSongInfo(songInfo);

				refreshUI(songMessage);
			}

		}.start();

	}

	private void initComponent() {
		int cWidth = width - padding * 2;
		// 退出面板
		TrayExitPanel trayExitPanel = new TrayExitPanel(cWidth, cHeight,
				trayEvent);
		trayExitPanel.setBounds(padding, height - padding - cHeight, cWidth,
				cHeight);

		// 分隔线面板
		JPanel separatorJPanel = new JPanel();
		separatorJPanel.setBackground(new Color(207, 207, 207));
		separatorJPanel.setBounds(padding * 2, trayExitPanel.getY() - 1
				- padding, cWidth - padding * 2, 1);

		// 锁面板
		trayLockPanel = new TrayLockPanel(cWidth, cHeight, trayEvent);
		trayLockPanel.setBounds(padding, separatorJPanel.getY() - padding
				- cHeight, cWidth, cHeight);

		// 歌词面板
		TrayLrcPanel trayLrcPanel = new TrayLrcPanel(cWidth, cHeight,
				trayEvent, dialogMouseListener, this);
		trayLrcPanel.setBounds(padding, trayLockPanel.getY() - padding
				- cHeight, cWidth, cHeight);

		//
		JPanel separatorJPanel2 = new JPanel();
		separatorJPanel2.setBackground(new Color(207, 207, 207));
		separatorJPanel2.setBounds(padding * 2, trayLrcPanel.getY() - 1
				- padding, cWidth - padding * 2, 1);

		//
		songTitleLabel = new JLabel("", JLabel.CENTER);
		songTitleLabel.setForeground(new Color(102, 102, 102));
		Font font = FontsUtil.getBaseFont(cHeight / 2 + 2);
		songTitleLabel.setFont(font);
		songTitleLabel.setBounds(padding, separatorJPanel2.getY() - padding
				- cHeight, cWidth, cHeight);

		//
		JPanel oPanel = new JPanel();
		oPanel.setOpaque(false);
		oPanel.setBounds(padding, songTitleLabel.getY() - padding - cHeight,
				cWidth, cHeight);
		// 初始化操作面板控件
		initOPanelComponent(oPanel, cHeight);

		// 边框面板
		TrayBorderPanel trayBorderPanel = new TrayBorderPanel(width, height);
		trayBorderPanel.setBounds(0, 0, width, height);

		this.getContentPane().setLayout(null);
		this.getContentPane().add(oPanel);
		this.getContentPane().add(songTitleLabel);
		this.getContentPane().add(separatorJPanel2);
		this.getContentPane().add(trayLrcPanel);
		this.getContentPane().add(trayLockPanel);
		this.getContentPane().add(separatorJPanel);
		this.getContentPane().add(trayExitPanel);
		this.getContentPane().add(trayBorderPanel);
		this.getContentPane().setBackground(Color.white);
	}

	private void initOPanelComponent(JPanel oPanel, int bSize) {
		oPanel.setLayout(null);
		String iconPath = Constants.PATH_ICON + File.separator;

		// 播放按钮

		String playButtonBaseIconPath = iconPath + "trayPlay_def.png";
		String playButtonOverIconPath = iconPath + "trayPlay_hot.png";
		String playButtonPressedIconPath = iconPath + "trayPlay_down.png";

		playButton = new BaseButton(playButtonBaseIconPath,
				playButtonOverIconPath, playButtonPressedIconPath, bSize, bSize);
		int x = (oPanel.getWidth() - bSize) / 2;
		int y = (oPanel.getHeight() - bSize) / 2;
		playButton.setBounds(x, y, bSize, bSize);
		playButton.setToolTipText("播放");

		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SongMessage songMessage = new SongMessage();
				songMessage.setType(SongMessage.PLAYMUSIC);
				// 通知
				ObserverManage.getObserver().setMessage(songMessage);
			}
		});

		// 暂停按钮
		String pauseButtonBaseIconPath = iconPath + "trayPause_def.png";
		String pauseButtonOverIconPath = iconPath + "trayPause_hot.png";
		String pauseButtonPressedIconPath = iconPath + "trayPause_down.png";

		pauseButton = new BaseButton(pauseButtonBaseIconPath,
				pauseButtonOverIconPath, pauseButtonPressedIconPath, bSize,
				bSize);

		pauseButton.setBounds(x, y, bSize, bSize);
		pauseButton.setToolTipText("暂停");

		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SongMessage songMessage = new SongMessage();
				songMessage.setType(SongMessage.PAUSEMUSIC);
				// 通知
				ObserverManage.getObserver().setMessage(songMessage);
			}
		});

		// 上一首
		String preButtonBaseIconPath = iconPath + "trayPre_def.png";
		String preButtonOverIconPath = iconPath + "trayPre_hot.png";
		String preButtonPressedIconPath = iconPath + "trayPre_down.png";

		BaseButton preButton = new BaseButton(preButtonBaseIconPath,
				preButtonOverIconPath, preButtonPressedIconPath,
				(int) (bSize * 1.5), (int) (bSize * 1.5));

		int px = pauseButton.getX() - (int) (bSize * 1.5) - padding * 2;
		int py = (oPanel.getHeight() - (int) (bSize * 1.5)) / 2;
		preButton.setBounds(px, py, (int) (bSize * 1.5), (int) (bSize * 1.5));
		preButton.setToolTipText("上一首");

		preButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SongMessage songMessage = new SongMessage();
				songMessage.setType(SongMessage.PREMUSIC);
				// 通知
				ObserverManage.getObserver().setMessage(songMessage);
			}
		});

		// 下一首

		String nextButtonBaseIconPath = iconPath + "trayNext_def.png";
		String nextButtonOverIconPath = iconPath + "trayNext_hot.png";
		String nextButtonPressedIconPath = iconPath + "trayNext_down.png";

		BaseButton nextButton = new BaseButton(nextButtonBaseIconPath,
				nextButtonOverIconPath, nextButtonPressedIconPath,
				(int) (bSize * 1.5), (int) (bSize * 1.5));

		int nx = playButton.getX() + playButton.getWidth() + padding * 2;

		nextButton.setBounds(nx, py, (int) (bSize * 1.5), (int) (bSize * 1.5));
		nextButton.setToolTipText("下一首");

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SongMessage songMessage = new SongMessage();
				songMessage.setType(SongMessage.NEXTMUSIC);
				// 通知
				ObserverManage.getObserver().setMessage(songMessage);
			}
		});

		oPanel.add(pauseButton);
		oPanel.add(playButton);
		oPanel.add(preButton);
		oPanel.add(nextButton);
	}

	public interface TrayEvent {
		public void exitClick();

		public void lockClick();

		public void lrcClick();
	}

	private class DialogMouseListener implements MouseInputListener {

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
			isEnter = true;
		}

		@Override
		public void mouseExited(MouseEvent e) {
			isEnter = false;
		}

		@Override
		public void mouseDragged(MouseEvent e) {

		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}
	}

	public boolean isEnter() {
		return isEnter;
	}

	public void setEnter(boolean isEnter) {
		this.isEnter = isEnter;
	}

	public int getMHeight() {
		return height;
	};

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
		if (data instanceof SongMessage) {
			SongMessage songMessage = (SongMessage) data;
			if (songMessage.getType() == SongMessage.INITMUSIC
					|| songMessage.getType() == SongMessage.SERVICEPLAYMUSIC
					|| songMessage.getType() == SongMessage.SERVICEPLAYINGMUSIC
					|| songMessage.getType() == SongMessage.SERVICEPAUSEEDMUSIC
					|| songMessage.getType() == SongMessage.ERRORMUSIC
					|| songMessage.getType() == SongMessage.SERVICEERRORMUSIC) {
				refreshUI(songMessage);
			}
		}
	}

	/**
	 * 刷新ui
	 * 
	 * @param songMessage
	 */
	private void refreshUI(SongMessage songMessage) {

		SongInfo mSongInfo = songMessage.getSongInfo();
		if (mSongInfo != null) {
			if (songMessage.getType() == SongMessage.INITMUSIC) {

				if (MediaManage.PLAYING == MediaManage.getMediaManage()
						.getPlayStatus()) {
					playButton.setVisible(false);
					pauseButton.setVisible(true);

				} else {
					playButton.setVisible(true);
					pauseButton.setVisible(false);

				}

				songTitleLabel.setText(mSongInfo.getDisplayName());
			} else if (songMessage.getType() == SongMessage.SERVICEPLAYMUSIC) {
				playButton.setVisible(false);
				pauseButton.setVisible(true);

			} else if (songMessage.getType() == SongMessage.SERVICEPLAYINGMUSIC) {

			} else if (songMessage.getType() == SongMessage.SERVICEPAUSEEDMUSIC) {
				playButton.setVisible(true);
				pauseButton.setVisible(false);

			}
		} else {
			songTitleLabel.setText(Constants.APPTITLE);
			playButton.setVisible(true);
			pauseButton.setVisible(false);

		}
	}
}
