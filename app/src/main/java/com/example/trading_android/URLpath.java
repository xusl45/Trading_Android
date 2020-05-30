package com.example.trading_android;

/**
 * 这里规定了一些可全局使用的常量
 *
 */

public class URLpath {
    /**
     * web端基础地址
     */
    //public static final String BASE_URL = "http://wishes-blog.cn/market";
    //本地测试url
    public static final String BASE_URL = "http://10.0.2.2:8080";

    //本地图片测试url
    public static final String BASE_IMAGES_URL = "http://10.0.2.2:80/images";

    /**
     * 基础广告地址
     */
    public static final String BASE_ADS_URL = "/findCommodityBanner";

    /**
     * 登录Url
     */
    public static final String LOGIN_URL = "/login";

    /**
     * 注册Url
     */
    public static final String REGISTER_URL = "/apply";

    /**
     * 根据用户username获取用户信息的Url
     */
    public static final String GET_USER_INFO_BY_UID_URL = "/findUserMessage";

    /**
     * 修改用户信息
     */
    public static final String MODIFY_USER_INFO_URL = "/setUserMessage";

    /**
            * 商品类型接口Url
     */
    public static final String GET_COMMODITY_TYPES_URL = "/findCommoditySort";

    /**
     * 帖子类型接口Url
     */
    public static final String GET_POSTTYPES_URL = "/findPostSort";
    /**
     * 根据商品类型寻找商品
     */
    public static final String GET_COMMODITY_BY_SORT_URL = "/findCommodityOfSort";
    /**
     * 根据输入的商品名模糊搜索商品列表
     */
    public static final String GET_COMMODITY_Hot_URL = "/findHotCommodity";
    /**
     * 根据输入的商品名模糊搜索商品列表
     */
    public static final String GET_COMMODITY_URL = "/findOneCommodity";

    /**
     * 根据商品id获取商品详情Url
     */
    public static final String GET_COMMODITY_BY_ID_URL = "/findOneCommodityById";
    /**
     * 根据商品id获取对应类别
     */
    public static final String GET_COMMODITYSORT_BY_ID_URL = "/findCommoditySortById";
    /**
     * 根据商品id获取商品预售信息
     */
    public static final String GET_COMMODITYSTORAGE_BY_ID_URL = "/showALLCommoditySizeById";

    /**
     * 增加挂售商品尺码 价格 用户id
     */
    public static final String POST_ADD_TO_COMMODITYSTORAGE_URL = "/insertCommoditySize";

    /**
     * 查询用户所有预售记录
     */
    public static final String GET_CommoditySELL_Order_URL = "/showALLCommoditySize";
    /**
     * 根据id取消预售记录
     */
    public static final String Delete_CommoditySELL_Order_URL = "/deleteCommoditySize";

    /**
     * 刷新得到用户所有收获地址Url
     */
    public static final String GET_USER_ADDRESS_URL = "/findALLUserAddress";
    /**
     * 插入收货地址URL
     */
    public static final String INSERT_USER_ADDRESS_URL = "/insertUserAddress";
    /**
     * 更新收货地址URL
     */
    public static final String UPDATE_USER_ADDRESS_URL = "/updateUserAddress";
    /**
     * 删除收货地址URL
     */
    public static final String Delete_USER_ADDRESS_URL = "/deleteUserAddress";
    /**
     * 上传图片
     */
    public static final String POST_IMG_URL = "/actiontUploadImage";

    /**
     * 购买商品Url
     */
    public static final String POST_BuyCOMMODITY_URL = "/insertCommodityOrder";

    /**
     * 查询用户所有购买记录
     */
    public static final String GET_CommodityBUY_URL = "/findCommodityOrderBidByID";
    /**
     * 查询用户所又出售记录
     */
    public static final String GET_CommoditySell_URL = "/findCommodityOrderUidByID";
    /**
     * 点击发货之后
     */
    public static final String POSR_CommodityAFTER_URL = "/sendCommodityAfter";
    /**
     * 点击收货之后
     */
    public static final String POSR_CommodityReceiving_URL = "/sendCommodityReceiving";


    /**
     * 查询单挑购买记录
     */
    public static final String GET_Commodity_BUY_HISTORY_URL = "/findCommodityOrderByID";
    /**
     * 查询单挑帖子
     */
    public static final String GET_Post_one_HISTORY_URL = "/findPostByID";
    /**
     * 查询某个分类中发帖记录
     */
    public static final String GET_Post_HISTORY_URL = "/findPostBySortID";
    /**
     * 查询某个分类中发帖记录
     */
    public static final String GET_Post_HISTORY_For_URL = "/findPostByUid";
    /**
     * 点赞
     */
    public static final String POSR_INSERT_GOOD_URL = "/UpdatePostGood";
    /**
     * 发帖
     */
    public static final String POSR_Post_INSERT_URL = "/insertPost";
    /**
     * 回复
     */
    public static final String POSR_Reply_INSERT_URL = "/insertReply";
    /**
     * 查询所有回帖
     */
    public static final String Get_Post_reply_URL = "/findReplyByTopicID";



    /**
     * 定义首页分页获取商品数量
     */
    public static final int HOME_PAGE_LIMIT = 6;

    /**
     * 定义出现的随机数最大值
     */
    public static final int MAX = 26;

    /**
     * 定义出现的随机数最小值
     */
    public static final int MIN = 0;
}
