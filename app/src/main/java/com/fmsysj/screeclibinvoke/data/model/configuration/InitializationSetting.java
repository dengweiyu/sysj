package com.fmsysj.screeclibinvoke.data.model.configuration;


import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：初始化设置
 */
@SuppressWarnings("serial")
public class InitializationSetting extends BaseEntity {

	public static final InitializationSetting DEFAULT = new InitializationSetting();

	public static final String QQ_NUMBER = "319157915";
	public static final String QQ_KEY = "5spMbOcFlJKLYqqTPV7yv54w65DGmel1";
	public static final String QQ_ONLINE_SERVICE = "1760388961";
	public static final int AD_LOCATION_LOCAL = 1;
	public static final int AD_LOCATION_CLOUD = 1;
	public static final int AD_LOCATION_PICTURE = 1;

	/**
	 * 玩家QQ群
	 */
	private String qq_number;

	/**
	 * 玩家QQ群KEY
	 */
	private String qq_key;

	/**
	 * QQ客服号码
	 */
	private String qq_online_service;

	private int start_lpds_ad;
	private int start_lpds_fm_ad;
	private int start_lpds_gdt_ad;
	private int start_lpds_bd_ad;

	/**
	 * 广告通栏：视频管理--本地视频
	 *
	 * 		1：后台广告
	 * 		0：腾讯广告联盟
	 */
	private int ad_location_local;

	/**
	 * 广告通栏：视频管理--云端视频
	 */
	private int ad_location_cloud;

	/**
	 * 广告通栏：视频管理--截图
	 */
	private int ad_location_picture;

	/**
	 * 信息流广告：视频管理
	 * 1：后台广告
	 * 0：腾讯广告联盟
	 */
	private int stream_ad_local;
	private int stream_ad_cloud;
	private int stream_ad_picture;

	/**
	 * 0:隐藏
	 * 1:显示
	 */
	private int discoverDownload;
	private int discoverSysj;
	private int discoverShop;
	private int discoverActivity;
	private int discoverGame;

	private String bug_service;
	private String bug_exchange;
	private String operation_service;
	private String charge_service;
	private String cooperation_service;

	// 小米、华为、vivo广告隐藏
	private int xiaomi_stream_local;
	private int xiaomi_stream_cloud;
	private int xiaomi_stream_picture;

	private String taobao_url;

	public InitializationSetting() {
		super();
		setQq_number(QQ_NUMBER);
		setQq_key(QQ_KEY);
		setQq_online_service(QQ_ONLINE_SERVICE);

		setStart_lpds_ad(1);
		setStart_lpds_fm_ad(1);
		setStart_lpds_bd_ad(1);
		setStart_lpds_gdt_ad(1);

		setAd_location_local(AD_LOCATION_LOCAL);
		setAd_location_cloud(AD_LOCATION_CLOUD);
		setAd_location_picture(AD_LOCATION_PICTURE);

		setStream_ad_local(1);
		setStream_ad_cloud(1);
		setStream_ad_picture(1);

		setDiscoverActivity(1);
		setDiscoverDownload(1);
		setDiscoverGame(1);
		setDiscoverShop(1);
		setDiscoverSysj(1);
		// 1不显示， 0显示
		setXiaomi_stream_local(1);
		setXiaomi_stream_cloud(1);
		setXiaomi_stream_picture(1);

		setBug_service("3422173762");
		setBug_exchange("552123462");
		setOperation_service("3503637243");
		setCharge_service("276848684");
		setCooperation_service("3503727161");

		setTaobao_url("https://shop480383064.taobao.com/");
	}

	public String getQq_number() {
		return qq_number;
	}

	public void setQq_number(String qq_number) {
		this.qq_number = qq_number;
	}

	public String getQq_key() {
		return qq_key;
	}

	public void setQq_key(String qq_key) {
		this.qq_key = qq_key;
	}

	public String getQq_online_service() {
		return qq_online_service;
	}
	public void setQq_online_service(String qq_online_service) {
		this.qq_online_service = qq_online_service;
	}

	public void setStart_lpds_ad(int start_lpds_ad) { this.start_lpds_ad = start_lpds_ad; }
	public int getStart_lpds_ad() { return start_lpds_ad; }

	public void setStart_lpds_fm_ad(int start_lpds_fm_ad) { this.start_lpds_fm_ad = start_lpds_fm_ad; }
	public int getStart_lpds_fm_ad() { return start_lpds_fm_ad; }

	public void setStart_lpds_gdt_ad(int start_lpds_gdt_ad) { this.start_lpds_gdt_ad = start_lpds_gdt_ad; }
	public int getStart_lpds_gdt_ad() { return start_lpds_gdt_ad; }

	public void setStart_lpds_bd_ad(int start_lpds_bd_ad) { this.start_lpds_bd_ad = start_lpds_bd_ad; }
	public int getStart_lpds_bd_ad() { return start_lpds_bd_ad; }

	public int getAd_location_local() {
		return ad_location_local;
	}

	public void setAd_location_local(int ad_location_local) {
		this.ad_location_local = ad_location_local;
	}

	public int getAd_location_cloud() {
		return ad_location_cloud;
	}

	public void setAd_location_cloud(int ad_location_cloud) {
		this.ad_location_cloud = ad_location_cloud;
	}

	public int getAd_location_picture() {
		return ad_location_picture;
	}

	public void setAd_location_picture(int ad_location_picture) {
		this.ad_location_picture = ad_location_picture;
	}

	public void setStream_ad_local(int stream_ad_local) { this.stream_ad_local = stream_ad_local; }
	public int getStream_ad_local() { return stream_ad_local; }

	public void setStream_ad_cloud(int stream_ad_cloud) { this.stream_ad_cloud = stream_ad_cloud; }
	public int getStream_ad_cloud() { return stream_ad_cloud; }

	public void setStream_ad_picture(int stream_ad_picture) { this.stream_ad_picture = stream_ad_picture; }
	public int getStream_ad_picture() { return stream_ad_picture; }

	public int getDiscoverGame() {
		return discoverGame;
	}

	public void setDiscoverGame(int discoverGame) {
		this.discoverGame = discoverGame;
	}

	public int getDiscoverActivity() {
		return discoverActivity;
	}

	public void setDiscoverActivity(int discoverActivity) {
		this.discoverActivity = discoverActivity;
	}

	public int getDiscoverShop() {
		return discoverShop;
	}

	public void setDiscoverShop(int discoverShop) {
		this.discoverShop = discoverShop;
	}

	public int getDiscoverSysj() {
		return discoverSysj;
	}

	public void setDiscoverSysj(int discoverSysj) {
		this.discoverSysj = discoverSysj;
	}

	public int getDiscoverDownload() {
		return discoverDownload;
	}

	public void setDiscoverDownload(int discoverDownload) {
		this.discoverDownload = discoverDownload;
	}

	public void setXiaomi_stream_local(int xiaomi_stream_local) { this.xiaomi_stream_local = xiaomi_stream_local; }
	public int getXiaomi_stream_local() { return xiaomi_stream_local; }

	public void setXiaomi_stream_cloud(int xiaomi_stream_cloud) { this.xiaomi_stream_cloud = xiaomi_stream_cloud; }
	public int getXiaomi_stream_cloud() { return xiaomi_stream_cloud; }

	public void setXiaomi_stream_picture(int xiaomi_stream_picture) { this.xiaomi_stream_picture = xiaomi_stream_picture; }
	public int getXiaomi_stream_picture() { return xiaomi_stream_picture; }

	public void setBug_service(String bug_service) { this.bug_service = bug_service; }
	public String getBug_service() { return bug_service; }

	public void setBug_exchange(String bug_exchange) { this.bug_exchange = bug_exchange; }
	public String getBug_exchange() { return bug_exchange; }

	public void setOperation_service(String operation_service) { this.operation_service = operation_service; }
	public String getOperation_service() { return operation_service; }

	public void setCharge_service(String charge_service) { this.charge_service = charge_service; }
	public String getCharge_service() { return charge_service; }

	public void setCooperation_service(String cooperation_service) { this.cooperation_service = cooperation_service; }
	public String getCooperation_service() { return cooperation_service; }

	public void setTaobao_url(String taobao_url) { this.taobao_url = taobao_url; }
	public String getTaobao_url() { return taobao_url; }
}
