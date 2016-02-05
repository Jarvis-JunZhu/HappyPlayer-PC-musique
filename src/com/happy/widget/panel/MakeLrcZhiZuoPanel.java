package com.happy.widget.panel;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.happy.common.Constants;
import com.happy.model.SongInfo;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;
import com.happy.util.MediaUtils;
import com.happy.widget.slider.MakeLrcSlider;

/**
 * 录入面板
 * 
 * @author zhangliangming
 * 
 */
public class MakeLrcZhiZuoPanel extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int width = 0;
	private int height = 0;
	private int bHSize = 0;

	private int padding = 10;

	/**
	 * 歌曲进度
	 * 
	 */
	private JLabel songLabel;

	/**
	 * 歌曲进度条
	 */
	private MakeLrcSlider songSlider;

	/**
	 * 判断其是否是正在拖动
	 */
	private boolean isStartTrackingTouch = false;
	/**
	 * 录入面板
	 */
	private MakeLrcLvRuPanel makeLrcLvRuPanel;

	public MakeLrcZhiZuoPanel(int width, int height, int bHSize,
			MakeLrcLvRuPanel makeLrcLvRuPanel) {
		this.makeLrcLvRuPanel = makeLrcLvRuPanel;
		this.width = width;
		this.height = height;
		this.bHSize = bHSize;
		this.setBackground(Color.white);

		initComponent();

		ObserverManage.getObserver().addObserver(this);
	}

	private void initComponent() {
		this.setLayout(null);

		int oH = bHSize * 3;
		// 操作面板背景
		String obgBackgroundPath = Constants.PATH_ICON + File.separator
				+ "zhizuo_op.png";
		ImageIcon obgBackground = new ImageIcon(obgBackgroundPath);
		obgBackground.setImage(obgBackground.getImage().getScaledInstance(
				width, oH, Image.SCALE_SMOOTH));
		JLabel bg = new JLabel(obgBackground);
		bg.setBounds(padding, 0, width - padding * 2, oH);

		// 歌曲总进度
		songLabel = new JLabel();
		songLabel.setForeground(Color.black);
		songLabel.setBounds(width - 120 + padding / 2, oH + padding / 2, 120,
				bHSize);
		songLabel.setText(MediaUtils.formatTime(0) + "/"
				+ MediaUtils.formatTime(0));
		//
		songSlider = new MakeLrcSlider();
		songSlider.setOpaque(false); // slider的背景透明
		songSlider.setFocusable(false);
		songSlider.setValue(0);
		songSlider.setMaximum(100);

		int sw = width - padding * 3 - songLabel.getWidth();

		songSlider.setBounds(padding, oH + padding, sw, bHSize);

		songSlider.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isStartTrackingTouch = true;
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {

				final int x = e.getX();

				// // /* 获取鼠标的位置 */
				songSlider.setValue(songSlider.getMaximum() * x
						/ songSlider.getWidth());
				new Thread() {

					@Override
					public void run() {
						SongMessage songMessage = new SongMessage();
						songMessage.setType(SongMessage.SEEKTOMUSIC);
						songMessage.setProgress(songSlider.getValue());
						ObserverManage.getObserver().setMessage(songMessage);

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						isStartTrackingTouch = false;

					}

				}.start();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {

			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		});

		songSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (isStartTrackingTouch) {
					songLabel.setText(MediaUtils.formatTime(((JSlider) e
							.getSource()).getValue())
							+ "/"
							+ MediaUtils.formatTime(songSlider.getMaximum()));
				}
			}
		});

		JScrollPane jScrollPane = new JScrollPane();

		jScrollPane.getVerticalScrollBar().setUI(new ScrollBarUI(100));
		jScrollPane.getVerticalScrollBar().setUnitIncrement(30);
		// 不显示水平的滚动条
		jScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		int jh = height - oH - padding * 3 - bHSize;
		jScrollPane.setBounds(padding,
				songSlider.getY() + songSlider.getHeight() + padding, width
						- padding * 2, jh);

		this.add(jScrollPane);
		this.add(songSlider);
		this.add(songLabel);
		this.add(bg);
	}

	/**
	 * 
	 * @param songInfo
	 */
	public void initData(SongInfo songInfo) {
		SongMessage songMessage = new SongMessage();
		songMessage.setSongInfo(songInfo);
		songMessage.setType(SongMessage.INITMUSIC);
		refreshUI(songMessage);
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
		if (data instanceof SongMessage) {
			SongMessage songMessage = (SongMessage) data;
			if (songMessage.getType() == SongMessage.INITMUSIC
					|| songMessage.getType() == SongMessage.SERVICEPLAYMUSIC
					|| songMessage.getType() == SongMessage.SERVICEPLAYINGMUSIC
					|| songMessage.getType() == SongMessage.SERVICEPAUSEEDMUSIC
					|| songMessage.getType() == SongMessage.SERVICESTOPEDMUSIC
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

				songSlider.setValue(0);
				songSlider.setMaximum((int) mSongInfo.getDuration());

				songLabel.setText(MediaUtils.formatTime(0) + "/"
						+ MediaUtils.formatTime((int) mSongInfo.getDuration()));

			} else if (songMessage.getType() == SongMessage.SERVICEPLAYMUSIC) {

			} else if (songMessage.getType() == SongMessage.SERVICEPLAYINGMUSIC) {

				if (!isStartTrackingTouch) {
					songSlider.setValue((int) mSongInfo.getPlayProgress());
					songLabel.setText(MediaUtils.formatTime((int) mSongInfo
							.getPlayProgress())
							+ "/"
							+ MediaUtils.formatTime((int) mSongInfo
									.getDuration()));
				}

			} else if (songMessage.getType() == SongMessage.SERVICEPAUSEEDMUSIC
					|| songMessage.getType() == SongMessage.SERVICESTOPEDMUSIC) {

				songSlider.setValue((int) mSongInfo.getPlayProgress());
				songLabel.setText(MediaUtils.formatTime((int) mSongInfo
						.getPlayProgress())
						+ "/"
						+ MediaUtils.formatTime((int) mSongInfo.getDuration()));
			}
		} else {

			songSlider.setValue(0);
			songSlider.setMaximum(0);
			songLabel.setText(MediaUtils.formatTime(0) + "/"
					+ MediaUtils.formatTime(0));

		}
	}
}
