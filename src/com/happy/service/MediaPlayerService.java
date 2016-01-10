package com.happy.service;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import com.happy.common.Constants;
import com.happy.logger.LoggerManage;
import com.happy.manage.MediaManage;
import com.happy.model.MessageIntent;
import com.happy.model.SongInfo;
import com.happy.model.SongMessage;
import com.happy.observable.ObserverManage;
import com.tulskiy.musique.audio.AudioFileReader;
import com.tulskiy.musique.audio.player.Player;
import com.tulskiy.musique.audio.player.PlayerEvent;
import com.tulskiy.musique.audio.player.PlayerEvent.PlayerEventCode;
import com.tulskiy.musique.audio.player.PlayerListener;
import com.tulskiy.musique.playlist.Track;
import com.tulskiy.musique.system.TrackIO;
import com.tulskiy.musique.util.AudioMath;

/**
 * 播放服务
 * 
 * @author Administrator
 * 
 */
public class MediaPlayerService implements Observer {

	private static MediaPlayerService _MediaPlayerService;
	private static LoggerManage logger;
	/**
	 * 当前播放歌曲
	 */
	private SongInfo songInfo;

	/**
	 * 播放器
	 */
	private Player mediaPlayer;

	/**
	 * 歌词线程
	 */
	private Thread lrcThread;
	/**
	 * 
	 */
	private boolean isSeekFinish = true;

	public static MediaPlayerService getMediaPlayerService() {
		if (_MediaPlayerService == null) {
			_MediaPlayerService = new MediaPlayerService();
		}
		return _MediaPlayerService;
	}

	public MediaPlayerService() {
		ObserverManage.getObserver().addObserver(this);
	}

	public void init() {
		logger = LoggerManage.getZhangLogger();
	}

	public void close() {
		initPlayer();
	}

	@Override
	public void update(Observable o, final Object data) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (data instanceof SongMessage) {
					SongMessage songMessage = (SongMessage) data;
					if (songMessage.getType() == SongMessage.SERVICEPLAYMUSIC) {
						playInfoMusic(songMessage.getSongInfo());
					} else if (songMessage.getType() == SongMessage.SERVICEPAUSEMUSIC) {
						initMusic();
					} else if (songMessage.getType() == SongMessage.INITMUSIC
							|| songMessage.getType() == SongMessage.SERVICEPLAYINIT) {
						initPlayer();
					} else if (songMessage.getType() == SongMessage.SERVICESEEKTOMUSIC) {
						int progress = songMessage.getProgress();
						seekTo(progress);
					}
				} else if (data instanceof MessageIntent) {
					MessageIntent messageIntent = (MessageIntent) data;
					if (messageIntent.getAction().equals(
							MessageIntent.PLAYERVOLUME)) {
						initVolume();
					}
				}
			}
		});
	}

	/**
	 * 初始化声音
	 */
	protected void initVolume() {
		if (mediaPlayer != null) {
			mediaPlayer.getAudioOutput().setVolume(
					(float) (Constants.volumeSize * 1.0 / 100));
		}
	}

	/**
	 * 快进
	 * 
	 * @param progress
	 */
	private void seekTo(int progress) {
		try {
			if (mediaPlayer != null) {
				isSeekFinish = false;

				int seekBytes = AudioMath.millisToSamples((int) progress,
						mediaPlayer.getTrack().getTrackData().getSampleRate());
				//
				mediaPlayer.seek(seekBytes);

				MediaManage.getMediaManage().setPlayStatus(MediaManage.PLAYING);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化播放器
	 */
	public void initPlayer() {

		try {
			if (mediaPlayer != null) {
				mediaPlayer.pause();
				mediaPlayer = null;
			}
			if (lrcThread != null) {
				lrcThread = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化播放器
	 */
	private void initMusic() {
		try {
			if (mediaPlayer != null) {
				mediaPlayer.pause();
				mediaPlayer = null;

				SongMessage msg = new SongMessage();
				msg.setSongInfo(songInfo);
				msg.setType(SongMessage.SERVICEPAUSEEDMUSIC);
				ObserverManage.getObserver().setMessage(msg);
			}
			if (lrcThread != null) {
				lrcThread = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 播放歌曲
	 * 
	 * @param songInfo
	 */
	private void playInfoMusic(SongInfo msongInfo) {
		this.songInfo = msongInfo;
		if (songInfo == null) {

			SongMessage msg = new SongMessage();
			msg.setType(SongMessage.SERVICEERRORMUSIC);
			msg.setErrorMessage(SongMessage.ERRORMESSAGEPLAYSONGNULL);
			ObserverManage.getObserver().setMessage(msg);

			return;
		}
		File songFile = new File(songInfo.getFilePath());

		if (!songFile.exists()) {
			logger.error("播放文件不存在!");
			// 下一首
			SongMessage songMessage = new SongMessage();
			songMessage.setType(SongMessage.NEXTMUSIC);
			ObserverManage.getObserver().setMessage(songMessage);

			return;
		}

		try {
			if (mediaPlayer == null) {
				mediaPlayer = new Player();
				mediaPlayer.addListener(new PlayerListener() {

					@Override
					public void onEvent(PlayerEvent e) {
						if (e.getEventCode() == PlayerEventCode.STOPPED) {
							SongMessage songMessage = new SongMessage();
							songMessage.setType(SongMessage.NEXTMUSIC);
							ObserverManage.getObserver()
									.setMessage(songMessage);
						} else if (e.getEventCode() == PlayerEventCode.SEEK_FINISHED) {
							isSeekFinish = true;
						}
					}
				});
				initVolume();

				AudioFileReader audioFileReader = TrackIO
						.getAudioFileReader(songFile.getName());
				Track track = audioFileReader.read(songFile);

				mediaPlayer.open(track);
				long millis = songInfo.getPlayProgress();
				if (millis != 0) {
					isSeekFinish = false;
					int seekBytes = AudioMath.millisToSamples((int) millis,
							track.getTrackData().getSampleRate());
					mediaPlayer.seek(seekBytes);
				}
			}
		} catch (Exception e) {
			logger.error("不能播放此文件:" + songInfo.getFilePath());
			e.printStackTrace();

			SongMessage songMessage = new SongMessage();
			songMessage.setType(SongMessage.NEXTMUSIC);
			ObserverManage.getObserver().setMessage(songMessage);
		}

		if (lrcThread == null) {
			lrcThread = new Thread(new LrcRunable());
			lrcThread.start();
		}

	}

	/**
	 * 歌词绘画线程，歌词绘画每隔100ms去刷新歌词页面
	 * 
	 * @author Administrator
	 * 
	 */
	private class LrcRunable implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					// 一般情况是100ms去刷新一次，现在基于musique所以，会根据实际情况去刷新
					Thread.sleep(200);
					if (mediaPlayer != null && mediaPlayer.isPlaying()
							&& isSeekFinish) {
						int status = MediaManage.getMediaManage()
								.getPlayStatus();

						// 只有当前正在播放才去刷新页面，如果当前正在快进，则不要刷新页面，免得页面出现闪烁
						if (songInfo != null && status == MediaManage.PLAYING) {

							double currentMS = mediaPlayer.getCurrentMillis();
							long progress = Math.round(currentMS);
							// 等于0时不刷新，防止快进时闪屏
							if (progress == 0)
								continue;

							songInfo.setPlayProgress(progress);
							SongMessage msg = new SongMessage();
							msg.setSongInfo(songInfo);
							msg.setType(SongMessage.SERVICEPLAYINGMUSIC);
							ObserverManage.getObserver().setMessage(msg);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
