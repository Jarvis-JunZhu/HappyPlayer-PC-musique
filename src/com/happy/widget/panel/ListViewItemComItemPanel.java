package com.happy.widget.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.happy.common.Constants;
import com.happy.manage.MediaManage;
import com.happy.model.Category;
import com.happy.model.EventIntent;
import com.happy.model.SongInfo;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;
import com.happy.widget.button.BaseButton;

/**
 * 歌曲列表item面板
 * 
 * @author zhangliangming
 * 
 */
public class ListViewItemComItemPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;

	/**
	 * 歌曲信息
	 */
	private SongInfo songInfo;

	/**
	 * 默认高度
	 */
	private int defHeight = 50;
	/**
	 * 点击高度
	 */
	private int selectHeight = 80;
	/**
	 * 高度
	 */
	private int height = defHeight;

	/**
	 * 宽度
	 */
	private int width = 0;

	/**
	 * 播放列表面板
	 */
	private JPanel playListPanel;

	/**
	 * 鼠标经过
	 */
	private boolean isEnter = false;

	/**
	 * 双选
	 */
	private boolean isDoubSelect = false;
	/**
	 * 单选
	 */
	private boolean isSingleSelect = false;
	/**
	 * 播放列表索引
	 */
	private int pindex = 0;
	/**
	 * 歌曲列表索引
	 */
	private int sindex = 0;
	/**
	 * 歌曲名称
	 */
	private JLabel songName;

	/**
	 * 歌曲长度
	 */
	private JLabel songSize;
	/**
	 * 歌手图片
	 */
	private JLabel singerIconLabel;
	/**
	 * 删除按钮
	 */
	private BaseButton delButton;
	/**
	 * 
	 */
	private JPanel listViewPanel;

	public ListViewItemComItemPanel(JPanel mplayListPanel,
			JPanel mlistViewPanel, int mpindex, int msindex,
			SongInfo msongInfo, int mWidth) {
		this.pindex = mpindex;
		this.sindex = msindex;
		this.playListPanel = mplayListPanel;
		this.listViewPanel = mlistViewPanel;
		this.width = mWidth;
		this.songInfo = msongInfo;

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

				if (e.getClickCount() == 1) {

					// 如果当前歌曲正在播放，则对其它进行暂停操作
					if (Constants.sDoubleClickIndex == sindex
							&& Constants.pDoubleClickIndex == pindex) {
						if (songInfo.getSid().equals(Constants.playInfoID)) {

							if (MediaManage.getMediaManage().getPlayStatus() == MediaManage.PLAYING) {
								// 当前正在播放，发送暂停
								SongMessage msg = new SongMessage();
								msg.setSongInfo(songInfo);
								msg.setType(SongMessage.PAUSEMUSIC);
								ObserverManage.getObserver().setMessage(msg);
							} else {

								SongMessage songMessage = new SongMessage();
								songMessage.setType(SongMessage.PLAYMUSIC);
								// 通知
								ObserverManage.getObserver().setMessage(
										songMessage);
							}
						}
					}
					EventIntent eventIntent = new EventIntent();
					eventIntent.setType(EventIntent.SONGLIST);
					eventIntent.setpIndex(pindex);
					eventIntent.setsIndex(sindex);
					eventIntent.setClickCount(EventIntent.SINGLECLICK);

					ObserverManage.getObserver().setMessage(eventIntent);

				}

				if (e.getClickCount() == 2) {
					// 如果当前歌曲正在播放，则对其它进行暂停操作
					if (Constants.sDoubleClickIndex == sindex
							&& Constants.pDoubleClickIndex == pindex) {

						// 双击，播放歌曲
						if (songInfo.getSid().equals(Constants.playInfoID)) {

							return;
						}
					} else {

						EventIntent eventIntent = new EventIntent();
						eventIntent.setType(EventIntent.SONGLIST);
						eventIntent.setpIndex(pindex);
						eventIntent.setsIndex(sindex);
						eventIntent.setClickCount(EventIntent.DOUBLECLICK);

						ObserverManage.getObserver().setMessage(eventIntent);
					}

					Constants.playInfoID = songInfo.getSid();

					if (MediaManage.getMediaManage().getPindex() != pindex) {

						// 设置播放时的索引
						MediaManage.getMediaManage().setPindex(pindex);
						// 更新歌曲列表
						MediaManage.getMediaManage().upDateSongListData(pindex);
					}
					MediaManage.getMediaManage().setSindex(sindex);

					// 发送播放
					SongMessage msg = new SongMessage();
					msg.setSongInfo(songInfo);
					msg.setType(SongMessage.PLAYINFOMUSIC);
					ObserverManage.getObserver().setMessage(msg);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				isEnter = false;
				playListPanel.revalidate();
				playListPanel.repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				isEnter = true;
				playListPanel.revalidate();
				playListPanel.repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		initComponent();
		this.setOpaque(false);
	}

	public void initComponent() {
		this.setLayout(null);

		if (songInfo.getSid().equals(Constants.playInfoID)) {
			isDoubSelect = true;
			initSelectedComponent();
		} else {
			isDoubSelect = false;
			initDefComponent();
		}
	}

	/**
	 * 初始化选中布局
	 */
	public void initSelectedComponent() {
		this.removeAll();
		if (isDoubSelect) {
			height = selectHeight;
		} else {
			height = defHeight;
		}
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));

		songName = new JLabel(songInfo.getDisplayName());
		songSize = new JLabel(songInfo.getDurationStr());

		String singerIconPath = Constants.PATH_ICON + File.separator
				+ "singer_default_image.png";
		ImageIcon singerIcon = new ImageIcon(singerIconPath);
		singerIcon.setImage(singerIcon.getImage().getScaledInstance(
				height * 2 / 3, height * 2 / 3, Image.SCALE_SMOOTH));
		singerIconLabel = new JLabel(singerIcon);

		singerIconLabel.setBounds(10, (height - height * 2 / 3) / 2,
				height * 2 / 3, height * 2 / 3);

		songName.setBounds(
				10 + singerIconLabel.getX() + singerIconLabel.getWidth(), 0,
				width / 2, height);
		songSize.setBounds(width - 60 - 20, 0, 60, height);

		String iconPath = Constants.PATH_ICON + File.separator;
		String delButtonBaseIconPath = iconPath + "del1.png";
		String delButtonOverIconPath = iconPath + "del2.png";
		String delButtonPressedIconPath = iconPath + "del2.png";
		delButton = new BaseButton(delButtonBaseIconPath,
				delButtonOverIconPath, delButtonPressedIconPath, defHeight / 2,
				defHeight / 2);
		delButton.setBounds(songSize.getX() - songSize.getWidth(), height
				- defHeight / 2, defHeight / 2, defHeight / 2);

		initDelButtonEvent();
		this.add(delButton);
		this.add(singerIconLabel);
		this.add(songName);
		this.add(songSize);

		// this.revalidate();
		// this.repaint();
		// playListPanel.revalidate();
		// playListPanel.repaint();
		playListPanel.updateUI();
	}

	/**
	 * 初始化删除按钮事件
	 */
	private void initDelButtonEvent() {
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {

					@Override
					public void run() {
						int result = JOptionPane.showConfirmDialog(
								ListViewItemComItemPanel.this, "确认删除该歌曲?",
								"确认", JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.INFORMATION_MESSAGE);
						if (result == JOptionPane.OK_OPTION) {
							removeSongBySIndex(pindex, sindex);
						}
					}
				}.start();
			}
		});

		//
		delButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// playListPanel.revalidate();
				playListPanel.repaint();
				// playListPanel.updateUI();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// playListPanel.revalidate();
				playListPanel.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// playListPanel.revalidate();
				playListPanel.repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// playListPanel.revalidate();
				playListPanel.repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// playListPanel.revalidate();
				// playListPanel.repaint();
			}
		});

	}

	/**
	 * 根据播放列表索引和歌曲索引，删除歌曲
	 * 
	 * @param pindex
	 * @param sindex
	 */
	private void removeSongBySIndex(int pindex, int sindex) {
		if (pindex == -1 || pindex >= listViewPanel.getComponentCount()) {
			return;
		}

		ListViewItemPanel itemPanel = (ListViewItemPanel) listViewPanel
				.getComponent(pindex);
		ListViewItemHeadPanel listViewItemHeadPanel = (ListViewItemHeadPanel) itemPanel
				.getComponent(0);
		ListViewItemComPanel listViewItemComPanel = (ListViewItemComPanel) itemPanel
				.getComponent(1);
		if (sindex == -1 || sindex >= listViewItemComPanel.getComponentCount()) {
			return;
		}

		int currentPIndex = MediaManage.getMediaManage().getPindex();
		int currentSIndex = MediaManage.getMediaManage().getSindex();
		if (pindex == currentPIndex && sindex == currentSIndex) {
			MediaManage.getMediaManage().stopToPlay();
			MediaManage.getMediaManage().setSongInfo(null);
			// 当前播放列表含有当前播放的歌曲
			MediaManage.getMediaManage().setPindex(-1);
			MediaManage.getMediaManage().setSindex(-1);
		}

		// 更新数据
		List<Category> categorys = MediaManage.getMediaManage().getmCategorys();
		Category category = categorys.get(pindex);
		List<SongInfo> songInfos = category.getmCategoryItem();
		List<SongInfo> newSongInfos = new ArrayList<SongInfo>();
		int size = 0;
		for (int i = 0; i < songInfos.size(); i++) {
			SongInfo songInfo = songInfos.get(i);
			if (i == sindex) {
				songInfo.setStatus(SongInfo.DEL);
			}

			if (songInfo.getStatus() != SongInfo.DEL) {
				size++;
			}
			newSongInfos.add(songInfo);
		}

		category.setmCategoryItem(newSongInfos);
		categorys.remove(pindex);
		categorys.add(pindex, category);
		MediaManage.getMediaManage().setmCategorys(categorys);

		listViewItemHeadPanel.getTitleNameJLabel().setText(
				category.getmCategoryName() + "[" + size + "]");
		// 更新ui
		this.setVisible(false);
		this.validate();
		this.repaint();
		this.updateUI();
		playListPanel.updateUI();
	}

	/**
	 * 初始化默认布局
	 */
	public void initDefComponent() {
		this.removeAll();
		if (isDoubSelect) {
			height = selectHeight;
		} else {
			height = defHeight;
		}
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));

		songName = new JLabel(songInfo.getDisplayName());
		songSize = new JLabel(songInfo.getDurationStr());

		songName.setBounds(10, 0, width / 2, height);
		songSize.setBounds(width - 60 - 20, 0, 60, height);

		String iconPath = Constants.PATH_ICON + File.separator;
		String delButtonBaseIconPath = iconPath + "del1.png";
		String delButtonOverIconPath = iconPath + "del2.png";
		String delButtonPressedIconPath = iconPath + "del2.png";
		delButton = new BaseButton(delButtonBaseIconPath,
				delButtonOverIconPath, delButtonPressedIconPath, height / 2,
				height / 2);
		delButton.setBounds(songSize.getX() - songSize.getWidth(),
				(height - defHeight / 2) / 2, defHeight / 2, defHeight / 2);

		initDelButtonEvent();

		this.add(delButton);
		this.add(songName);
		this.add(songSize);
		// this.revalidate();
		// this.repaint();
		// playListPanel.revalidate();
		// playListPanel.repaint();
		// this.updateUI();
		playListPanel.updateUI();
	}

	public void setSingleSelect(boolean isSingleSelect) {
		this.isSingleSelect = isSingleSelect;
		playListPanel.revalidate();
		playListPanel.repaint();
	}

	public void setDoubSelect(boolean isDoubSelect) {
		this.isDoubSelect = isDoubSelect;
		if (isDoubSelect) {
			isSingleSelect = false;
			initSelectedComponent();
		} else {
			isSingleSelect = false;
			initDefComponent();
		}
	}

	// 绘制组件
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// 消除线条锯齿
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (isDoubSelect) {
			g2d.setPaint(new Color(0, 0, 0, 80));
		} else if (isSingleSelect) {
			g2d.setPaint(new Color(0, 0, 0, 50));
		} else if (isEnter) {
			g2d.setPaint(new Color(0, 0, 0, 20));
		} else {
			g2d.setPaint(new Color(0, 0, 0, 0));
		}
		g2d.fillRect(0, 0, width, height);
	}

}
