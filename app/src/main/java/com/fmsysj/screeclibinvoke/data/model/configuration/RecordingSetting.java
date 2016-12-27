package com.fmsysj.screeclibinvoke.data.model.configuration;


import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：录屏设置
 */
@SuppressWarnings("serial")
public class RecordingSetting extends BaseEntity {

	public static final RecordingSetting DEFAULT = new RecordingSetting();

	public static final String QUALITY_LOW = "low";
	public static final String QUALITY_STANDARD = "standard";
	public static final String QUALITY_HIGH = "high";
	public static final String QUALITY_ULTRA_HIGH = "ultraHigh";

	/**
	 * 声音选项，开启声音录制
	 */
	private boolean soundRecording = true;

	/**
	 * 摇晃截屏
	 */
	private boolean shakeRecording = false;

	/**
	 * 前摄像头选项，开启主播模式
	 */
	private boolean anchorModel = false;

	/**
	 * 触摸选项， 显示触摸位置
	 */
	private boolean touchPosition = false;

	/**
	 * 游戏列表框，启动游戏扫描
	 */
	private boolean gameScan = true;

	/**
	 * 开启悬浮窗
	 */
	private boolean floatingWindiws = true;

	/**
	 * 录屏后跳转
	 */
	private boolean recordedJump = false;

	/**
	 * 是否横屏
	 */
	private int landscape;

	/**
	 * 录屏清晰度
	 */
	private String quality = QUALITY_HIGH;

	public RecordingSetting() {
		super();
	}

	public RecordingSetting(boolean soundRecording,
							boolean shakeRecording,
							boolean anchorModel,
							boolean touchPosition,
							boolean gameScan,
							String quality,
							boolean floatingWindiws,
							boolean recordedJump) {
		setSoundRecording(soundRecording);
		setShakeRecording(shakeRecording);
		setAnchorModel(anchorModel);
		setTouchPosition(touchPosition);
		setGameScan(gameScan);
		setFloatingWindiws(floatingWindiws);
		setRecordedJump(recordedJump);
		setQuality(quality);
	}

	public boolean isSoundRecording() {
		return soundRecording;
	}

	public void setSoundRecording(boolean soundRecording) {
		this.soundRecording = soundRecording;
	}

	public boolean isShakeRecording() {
		return shakeRecording;
	}

	public void setShakeRecording(boolean shakeRecording) {
		this.shakeRecording = shakeRecording;
	}

	public boolean isAnchorModel() {
		return anchorModel;
	}

	public void setAnchorModel(boolean anchorModel) {
		this.anchorModel = anchorModel;
	}

	public boolean isTouchPosition() {
		return touchPosition;
	}

	public void setTouchPosition(boolean touchPosition) {
		this.touchPosition = touchPosition;
	}

	public boolean isGameScan() {
		return gameScan;
	}

	public void setGameScan(boolean gameScan) {
		this.gameScan = gameScan;
	}

	public String getQuality() {
		return quality;
	}

	public String getQualityText() {
		if (quality.equals(QUALITY_STANDARD))
			return "标清";
		if (quality.equals(QUALITY_HIGH))
			return "高清";
		if (quality.equals(QUALITY_ULTRA_HIGH))
			return "超高清";
		return "高清";
	}

	public void setQuality(String quality) {
		if (!quality.equals(QUALITY_LOW) &&
				!quality.equals(QUALITY_STANDARD) &&
				!quality.equals(QUALITY_HIGH) &&
				!quality.equals(QUALITY_ULTRA_HIGH))
			throw new IllegalArgumentException("quality " + quality + " is not support");
		this.quality = quality;
	}

	public boolean isFloatingWindiws() {
		return floatingWindiws;
	}

	public void setFloatingWindiws(boolean floatingWindiws) {
		this.floatingWindiws = floatingWindiws;
	}

	public boolean isRecordedJump() {
		return recordedJump;
	}

	public void setRecordedJump(boolean recordedJump) {
		this.recordedJump = recordedJump;
	}

	public int getLandscape() {
		return landscape;
	}

	public void setLandscape(int landscape) {
		this.landscape = landscape;
	}
}
