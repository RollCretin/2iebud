package com.hxjf.dubei.network;

import com.hxjf.dubei.bean.AchievementBean;
import com.hxjf.dubei.bean.ActivityDetailBean;
import com.hxjf.dubei.bean.ActivityListBean;
import com.hxjf.dubei.bean.AddCollectBookBean;
import com.hxjf.dubei.bean.ApplyBean;
import com.hxjf.dubei.bean.AttentionListBean;
import com.hxjf.dubei.bean.BSBookListBean;
import com.hxjf.dubei.bean.BSNoteListBean;
import com.hxjf.dubei.bean.BalanceBean;
import com.hxjf.dubei.bean.BalanceListBean;
import com.hxjf.dubei.bean.BannerBean;
import com.hxjf.dubei.bean.BookClassifyBean;
import com.hxjf.dubei.bean.BookDetailBean;
import com.hxjf.dubei.bean.BookFriendBean;
import com.hxjf.dubei.bean.BookListClassifyBean;
import com.hxjf.dubei.bean.BookListDetailBean;
import com.hxjf.dubei.bean.BookListbean;
import com.hxjf.dubei.bean.BookTagBean;
import com.hxjf.dubei.bean.BookbarDetailBean;
import com.hxjf.dubei.bean.BookbarListBean;
import com.hxjf.dubei.bean.CanJoinChallengeBean;
import com.hxjf.dubei.bean.ChaiduListBean;
import com.hxjf.dubei.bean.ChallengeDetailBean;
import com.hxjf.dubei.bean.ChallengeJoinUserListBean;
import com.hxjf.dubei.bean.ChallengeNumBean;
import com.hxjf.dubei.bean.ChallengeNumTimeBean;
import com.hxjf.dubei.bean.ChangDuParamBean;
import com.hxjf.dubei.bean.ChangduInfoBean;
import com.hxjf.dubei.bean.CheckVersionBean;
import com.hxjf.dubei.bean.DiscoveryBean;
import com.hxjf.dubei.bean.DoubanDetailBean;
import com.hxjf.dubei.bean.DuBeiBean;
import com.hxjf.dubei.bean.FavoriteBookDetailBean;
import com.hxjf.dubei.bean.FavoriteBookListbean;
import com.hxjf.dubei.bean.HotBookBean;
import com.hxjf.dubei.bean.LauncherChallengeBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.MyBookListBean;
import com.hxjf.dubei.bean.MyChallengeBean;
import com.hxjf.dubei.bean.MyMessageBean;
import com.hxjf.dubei.bean.NewDiscoveryBean;
import com.hxjf.dubei.bean.NoteFactoryClassifyBean;
import com.hxjf.dubei.bean.NoteFactoryDetailBean;
import com.hxjf.dubei.bean.NoteFactoryListBean;
import com.hxjf.dubei.bean.PKListBean;
import com.hxjf.dubei.bean.ProtraitBean;
import com.hxjf.dubei.bean.ReadTimeBean;
import com.hxjf.dubei.bean.SMSBean;
import com.hxjf.dubei.bean.SearchBookBean;
import com.hxjf.dubei.bean.SearchBookListBean;
import com.hxjf.dubei.bean.SearchNoteBean;
import com.hxjf.dubei.bean.ShareInfoBean;
import com.hxjf.dubei.bean.ShudanDetailBean;
import com.hxjf.dubei.bean.TagBean;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.bean.WithdrawBean;
import com.hxjf.dubei.bean.hotWordBean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Chen_Zhang on 2017/7/18.
 */

public interface ReaderApi {
    //微信登录
    @POST("/user/login")
    @FormUrlEncoded
    Call<UserDetailBean> loginCall(@Field("openid") String openid,
                                   @Field("unionid") String unionid,
                                   @Field("country") String country,
                                   @Field("province") String province,
                                   @Field("city") String city,
                                   @Field("gender") int gender,
                                   @Field("headimgurl") String headimgurl,
                                   @Field("nickname") String nickname,
                                   @Field("accessToken") String accessToken,
                                   @Field("refreshToken") String refreshToken,
                                   @Field("registrationId") String registrationId);

    //查询用户
    @POST("/user/detail")
    @FormUrlEncoded
    Call<UserDetailBean> usertDetailCall(@Field("userId") String userid);

    //查询自身信息
    @POST("/user/detail")
    Call<UserDetailBean> myDetailCall();

    @POST("/user/login/sms")
    @FormUrlEncoded
    Call<SMSBean> getSMSBean(@Field("telephone") String telephone);

    //登录用户返回bean
    @POST("/user/bind")
    @FormUrlEncoded
    Call<ModifyBean> getBindBean(@Field("telephone") String telephone,
                                 @Field("validNum") String validnum);

    //修改用户昵称
    @POST("/user/modifyNickName")
    @FormUrlEncoded
    Call<ModifyBean> getModifyNickBean(@Field("nickName") String nickName);

    //头像上传
    @Multipart
    @POST("/user/headPortrait/upload")
    Call<ProtraitBean> uploadPortraitCall(@Part MultipartBody.Part file);

    //修改用户性别
    @POST("/user/modifySex/{sex}")
    Call<ModifyBean> getModifySexBean(@Path("sex") String sex);

    //查询用户可添加标签
    @POST("/user/tag/list")
    Call<TagBean> getTagBean();

    //保存用户简介
    @POST("/user/modifyIntro")
    @FormUrlEncoded
    Call<ModifyBean> modifyIntroCall(@Field("intro") String intro);

    //用户保存标签
    @POST("/user/saveTags")
    @FormUrlEncoded
    Call<ModifyBean> getSaveTagBean(@Field("tags") String tags);

    //用户添加自定义标签
    @POST("/user/tag/add")
    @FormUrlEncoded
    Call<ModifyBean> getaddTagBean(@Field("tagName") String tagname);

    //获取书标签
    @POST("/book/tag/list")
    Call<BookTagBean> getBookTagCall();

    //绑定书籍标签
    @POST("/book/tag/bind")
    @FormUrlEncoded
    Call<ModifyBean> ModifybooktagCall(@Field("tagClassify") String tagclassify);

    //成就
    @POST("/user/achievement")
    Call<AchievementBean> achievementCall();

    @POST("/user/achievement")
    @FormUrlEncoded
    Call<AchievementBean> personAchievementCall(@Field("userId") String userId);

    //我的书单
    @POST("/user/myBooklist")
    Call<MyBookListBean> myBookListCall();

    //我的通知
    @POST("/msg/list")
    @FormUrlEncoded
    Call<MyMessageBean> myMessageCall(@Field("page") int pag, @Field("rows") int rows);

    //更新通知为已读
    @POST("/msg/update")
    Call<ModifyBean> updateMessageCall();

    //删除通知消息
    @POST("/msg/delete/{id}")
    Call<ModifyBean> deleteMessageCall(@Path("id") String id);

    //热词
    @POST("/search/list/hotWords")
    Call<hotWordBean> hotWordCall();

    //搜索--书籍、拆读
    @POST("/search/find")
    @FormUrlEncoded
    Call<SearchBookBean> SearchBookCall(@Field("page") int page,
                                        @Field("rows") int rows,
                                        @Field("name") String name,
                                        @Field("type") String type);

    //搜索 -- 笔记工厂
    @POST("/search/find")
    @FormUrlEncoded
    Call<SearchNoteBean> SearchNoteCall(@Field("page") int page,
                                        @Field("rows") int rows,
                                        @Field("name") String name,
                                        @Field("type") String type);

    //搜索--书单
    @POST("/search/find")
    @FormUrlEncoded
    Call<SearchBookListBean> SearchBookListCall(@Field("page") int page,
                                                @Field("rows") int rows,
                                                @Field("name") String name,
                                                @Field("type") String type);

    //我的关注列表
    @POST("/user/attention/list")
    Call<AttentionListBean> MyAttentionList();

    //添加关注
    @POST("/user/attention/add/{attentionUserId}")
    Call<ModifyBean> addAttentionCall(@Path("attentionUserId") String attentionUserId);

    //取消关注
    @POST("/user/attention/cancel/{attentionUserId}")
    Call<ModifyBean> cancelAttentionCall(@Path("attentionUserId") String attentionUserId);


    //-------------------------------------发现-----------------------------------------
    //banner
    @POST("/banner/find")
    Call<BannerBean> bannerCall();

    //发现页面数据
    @POST("/dubei/home")
    Call<DiscoveryBean> discoveryCall();

    //书单类型
    @POST("/data/menu")
    @FormUrlEncoded
    Call<BookListClassifyBean> BookListClassifyCall(@Field("type") String bookListClassify);

    //书单列表
    @POST("/booklist/list")
    @FormUrlEncoded
    Call<BookListbean> BookListCall(@Field("classify") int classify,
                                    @Field("pageNum") int pagenum,
                                    @Field("pageSize") int pagesize);

    //最新书单列表
    @POST("/booklist/list/recent")
    @FormUrlEncoded
    Call<BookListbean> RecentListCall(@Field("pageNum") int pageNum,
                                      @Field("pageSize") int pageSize);


    //书籍列表
    @POST("/book/list")
    @FormUrlEncoded
    Call<BookListDetailBean> BookListDetailCall(@Field("bookListId") String booklistid,
                                                @Field("type") int type,
                                                @Field("pageNum") int pagenum,
                                                @Field("pageSize") int pagesize);

    //书单详情
    @POST("/booklist/detail")
    @FormUrlEncoded
    Call<ShudanDetailBean> ShudanDetailCall(@Field("bookListId") String bookListId,
                                            @Field("pageNum") int pageNum,
                                            @Field("pageSize") int pageSize,
                                            @Field("queryBooks") boolean queryBooks);

    //书籍和拆读的最新列表
    @POST("/book/list/recent")
    @FormUrlEncoded
    Call<ChaiduListBean> RecentBookCall(@Field("type") int type,
                                        @Field("pageNum") int pageNum,
                                        @Field("pageSize") int pageSize);

    //书单收藏
    @POST("/booklist/collect/add/{bookListId}")
    Call<ModifyBean> collectBooklist(@Path("bookListId") String booklistid);

    //书单取消收藏
    @POST("/booklist/collect/cancel/{bookListId}")
    Call<ModifyBean> cancelCollectBooklist(@Path("bookListId") String booklistid);

    //书籍添加到书架
    @POST("bookShelf/book/add/{bookId}")
    Call<ModifyBean> bookAddBookshelf(@Path("bookId") String bookid);

    //书籍、好书拆读类型
    @POST("/data/menu")
    @FormUrlEncoded
    Call<BookClassifyBean> bookClassifyCall(@Field("type") String bookClassify);

    //拆读列表
    @POST("/book/list")
    @FormUrlEncoded
    Call<ChaiduListBean> ChaiduListCall(@Field("classify") int classify,
                                        @Field("type") int type,
                                        @Field("pageNum") int pagenum,
                                        @Field("pageSize") int pagesize);

    //笔记工厂类型
    @POST("/data/menu")
    @FormUrlEncoded
    Call<NoteFactoryClassifyBean> noteFactoryClassifyCall(@Field("type") String noteFactoryClassify);

    //笔记工厂列表
    @POST("/notefactory/list")
    @FormUrlEncoded
    Call<NoteFactoryListBean> noteFactoryListCall(@Field("classify") int classify,
                                                  @Field("pageNum") int pageNum,
                                                  @Field("pageSize") int pageSize);

    //最新笔记工厂
    @POST("/notefactory/list/recent")
    @FormUrlEncoded
    Call<NoteFactoryListBean> recentNoteFactoryListCall(@Field("pageNum") int pageNum,
                                                        @Field("pageSize") int pageSize);

    //笔记工厂详情
    @POST("/notefactory/detail")
    @FormUrlEncoded
    Call<NoteFactoryDetailBean> noteFactoryDetailCall(@Field("noteFactoryId") String noteFactoryId);

    //笔记工厂的理论阅读时间和当前的阅读总时间
    @POST("/factory/read/getTime/{factoryId}")
    Call<ReadTimeBean> noteReadTimeCall(@Path("factoryId") String factoryid);

    //笔记工厂同步阅读时间
    @POST("/factory/read/update")
    @FormUrlEncoded
    Call<ModifyBean> updatenNoteFactoryCall(@Field("factoryId") String factoryid,
                                            @Field("readTime") int readtime);

    //笔记工厂完成阅读
    @POST("/factory/read/readOver")
    @FormUrlEncoded
    Call<ModifyBean> noteReadDoneCall(@Field("factoryId") String factoryid,
                                      @Field("readTime") int readtime);

    //笔记工厂加入书架
    @POST("bookShelf/noteFactory/add/{factoryId}")
    Call<ModifyBean> noteAddBookshelfCall(@Path("factoryId") String factoryid);

    //书籍详情、拆读详情
    @POST("/book/detail")
    @FormUrlEncoded
    Call<BookDetailBean> BookDetailCall(@Field("bookId") String bookid);

    //书籍加入书架
    @POST("bookShelf/book/add/{bookId}")
    Call<ModifyBean> bookAddBookshelfCall(@Path("bookId") String bookid);

    //发现热门书籍
    @POST("/book/hot/list")
    Call<HotBookBean> discoveryHotBookCall();

    //阅读书籍前调用接口
    @POST("book/read/start/{bookId}")
    Call<ModifyBean> startReadCall(@Path("bookId") String bookid);

    //---------------------------------书架----------------------------------------------
    //书架类型
    @POST("/data/menu")
    @FormUrlEncoded
    Call<BookClassifyBean> bookshelfClassifyCall(@Field("type") String bookshelfType);

    //书籍列表
    @POST("bookShelf/list/book")
    Call<BSBookListBean> BSBookListCall();

    //他人书架
    @POST("bookShelf/list/book")
    @FormUrlEncoded
    Call<BSBookListBean> personBookListCall(@Field("userId") String userId);

    //书籍移出书架
    @POST("/bookShelf/book/remove/{bookId}")
    Call<ModifyBean> bookRemoveBS(@Path("bookId") String bookid);

    //好书拆读列表
    @POST("bookShelf/list/divideBook")
    Call<BSBookListBean> BSChaiduListCall();

    //他人好书拆读列表
    @POST("bookShelf/list/divideBook")
    @FormUrlEncoded
    Call<BSBookListBean> PersonChaiduListCall(@Field("userId") String userId);

    //笔记工厂列表
    @POST("bookShelf/list/noteFactory")
    Call<BSNoteListBean> BSNoteListCall();

    //他人笔记工厂
    @POST("bookShelf/list/noteFactory")
    @FormUrlEncoded
    Call<BSNoteListBean> personNoteListCall(@Field("userId") String userId);

    //笔记移出书架
    @POST("/bookShelf/noteFactory/remove/{factoryId}")
    Call<ModifyBean> noteRemoveBS(@Path("factoryId") String factoryid);


    //-------------------------------正常阅读的计时----------------------------------------------
    //获取书籍的阅读时间和理论时间
    @POST("book/read/getTime/{bookId}")
    Call<ReadTimeBean> bookReadTimeCall(@Path("bookId") String bookid);

    //更新阅读信息
    @POST("/book/read/update")
    @FormUrlEncoded
    Call<ModifyBean> updateBookCall(@Field("bookId") String bookid,
                                    @Field("progress") int progress,
                                    @Field("readTime") int readtime,
                                    @Field("position") String position);

    //完成阅读
    @POST("/book/read/readOver")
    @FormUrlEncoded
    Call<ModifyBean> bookReadDone(@Field("bookId") String bookid,
                                  @Field("readTime") int readtime);

    //-------------------------------------挑战阅读的计时和阅读时间合并-----------------------------------


    //------------------------------读呗---------------------------------------------
    //首页
    @POST("/challenge/home")
    Call<DuBeiBean> duBeiCall();

    //挑战总次数
    @POST("/challenge/num")
    Call<ChallengeNumBean> challengeNumCall();

    //挑战详情--PK榜
    @POST("/challenge/user/detail/{challengeId}")
    Call<ChallengeDetailBean> callengeDetailCall(@Path("challengeId") String challengeid);

    //    //挑战详情--首页
//    @POST("/challenge/detail/{challengeId}")
//    Call<HomeChallengeDetailBean> homeChallengeDetailCall(@Path("challengeId") String challengeid);
    //点赞参赛者
    @POST("/challenge/praise/thumbsUp")
    @FormUrlEncoded
    Call<ModifyBean> challengePraiseCall(@Field("challengeUserId") String challengeuserid);

    //取消点赞参赛者
    @POST("/challenge/praise/thumbsUp/cancel")
    @FormUrlEncoded
    Call<ModifyBean> challengePraiseCancelCall(@Field("challengeUserId") String challengeuserid);

    //查询挑战者信息
    @POST("/challenge/user/list")
    @FormUrlEncoded
    Call<ChallengeJoinUserListBean> challengeUserListCall(@Field("page") int page,
                                                          @Field("rows") int rows,
                                                          @Field("challengeId") String challengeId);

    //挑战阅读字数和理论阅读时间
    @POST("/book/getNumTime/{bookId}")
    Call<ChallengeNumTimeBean> ChallengNumTimeCall(@Path("bookId") String bookid);

    //发起挑战
    @POST("/challenge/initiate")
    @FormUrlEncoded
    Call<LauncherChallengeBean> LauncherChallengeCall(@Field("bookId") String bookId,
                                                      @Field("startTime") String starttime,
                                                      @Field("endTime") String endtime,
                                                      @Field("cMoney") double cmoney);

    //判断用户可否发起挑战
    @POST("/challenge/judge/book")
    @FormUrlEncoded
    Call<ModifyBean> judgeChallenge(@Field("bookId") String bookId);


    //PK榜查询
    @POST("/challenge/list")
    @FormUrlEncoded
    Call<PKListBean> PKListCall(@Field("page") int page,
                                @Field("rows") int rows);

    //我的挑战
    @POST("/challenge/list/my")
    @FormUrlEncoded
    Call<MyChallengeBean> myChallengeCall(@Field("page") int page,
                                          @Field("rows") int rows);

    //我可以参与的挑战
    @POST("/challenge/list/canJoin")
    @FormUrlEncoded
    Call<CanJoinChallengeBean> canJoinChallengeCall(@Field("page") int page,
                                                    @Field("rows") int rows,
                                                    @Field("bookId") String bookid);

    //加入挑战
    @POST("/challenge/join/{challengeId}")
    Call<ModifyBean> joinChallengeCall(@Path("challengeId") String challengeId);

    //分类热门书籍
    @POST("/book/hot/list")
    @FormUrlEncoded
    Call<HotBookBean> hotBookCall(@Field("page") int page,
                                  @Field("rows") int rows,
                                  @Field("classify") int classify);

    //热门书籍
    @POST("/book/hot/list")
    @FormUrlEncoded
    Call<HotBookBean> hotBookCall(@Field("page") int page,
                                  @Field("rows") int rows);

    //最新最热书籍
    @POST("/book/hot/list/new")
    @FormUrlEncoded
    Call<HotBookBean> recentHotBookCall(@Field("page") int page,
                                        @Field("rows") int rows);

    //-------------------------新发现--------------------------------------
    //新发现主界面
    @POST("/zone/discovery/home")
    @FormUrlEncoded
    Call<NewDiscoveryBean> newDiscoveryCall(@Field("longitude") double longitude,
                                            @Field("latitude") double latitude);
    //书吧列表
    @POST("/zone/discovery/bookbar/list")
    @FormUrlEncoded
    Call<BookbarListBean> bookbarListCall(@Field("longitude") double longitude,
                                          @Field("latitude") double latitude,
                                          @Field("pageNum") int pageNum,
                                          @Field("pageSize") int pageSize);

    //书吧详情
    @POST("/zone/discovery/bookbar/detail")
    @FormUrlEncoded
    Call<BookbarDetailBean> bookbarDetailCall(@Field("bookbarId") String bookbarId);

    //记录用户定位
    @POST("/zone/user/location/record")
    @FormUrlEncoded
    Call<ModifyBean> recordLocCall(@Field("userId") String userId,
                                   @Field("longitude") double longitude,
                                   @Field("latitude") double latitude,
                                   @Field("city") String city,
                                   @Field("address") String address);
    //活动列表--所有
    @POST("/zone/discovery/activity/list")
    @FormUrlEncoded
    Call<ActivityListBean> activityListCall(@Field("pageNum") int pageNum,
                                            @Field("pageSize") int pageSize);
    //活动详情
    @POST("/zone/discovery/activity/detail")
    @FormUrlEncoded
    Call<ActivityDetailBean> activityDetailCall(@Field("activityId") String activityId);

    //藏书列表 -- 所有
    @POST("/zone/discovery/book/favorite/list")
    @FormUrlEncoded
    Call<FavoriteBookListbean> favoriteBookListCall(@Field("longitude") double longitude,
                                                    @Field("latitude") double latitude,
                                                    @Field("pageNum") int pageNum,
                                                    @Field("pageSize") int pageSize);
    //藏书列表 -- 个人
    @POST("/zone/discovery/book/favorite/list")
    @FormUrlEncoded
    Call<FavoriteBookListbean> PersonFavoriteBookListCall(@Field("ownerId") String ownerId,
                                                    @Field("longitude") double longitude,
                                                    @Field("latitude") double latitude,
                                                    @Field("pageNum") int pageNum,
                                                    @Field("pageSize") int pageSize);

    //藏书详情
    @POST("/zone/discovery/book/favorite/detail")
    @FormUrlEncoded
    Call<FavoriteBookDetailBean> favoriteBookDetailCall(@Field("longitude") double longitude,
                                                        @Field("latitude") double latitude,
                                                        @Field("bookFavoriteId") String bookFavoriteId);

    //推荐书友
    @POST("/zone/user/location/list")
    @FormUrlEncoded
    Call<BookFriendBean>  RecommendFriendCall(@Field("longitude") double longitude,
                                              @Field("latitude") double latitude,
                                              @Field("pageNum") int pageNum,
                                              @Field("pageSize") int pageSize);

    //根据isbn查询藏书详情
    @POST("/zone/discovery/book/favorite/douban/detail")
    @FormUrlEncoded
    Call<DoubanDetailBean> doubanDetailCall(@Field("isbn") String isbn);
    //根据isbn上传藏书
    @POST("/zone/discovery/book/favorite/userAdd/isbn")
    @FormUrlEncoded
    Call<AddCollectBookBean> addCollectBookCall(@Field("userId") String userId,
                                                @Field("isbn") String isbn,
                                                @Field("tag") String tag,
                                                @Field("longitude") Double longitude,
                                                @Field("latitude") Double latitude);
    //删除藏书
    @POST("/zone/discovery/book/favorite/userDelete")
    @FormUrlEncoded
    Call<ModifyBean> deleteCollectBookCall(@Field("bookFavoriteId") String bookFavoriteId);

    //菜单列表
    @POST("/data/menu")
    @FormUrlEncoded
    Call<BookClassifyBean> menuTypeClassifyCall(@Field("type") String type);
    //----------------------支付、余额---------------------------------------
    //购买拆读书籍
  /*  @POST("/bookShelf/divideBook/buy")
    @FormUrlEncoded
    Call<ModifyBean> buyDivideBook(@Field("bookId") String bookId,
                                   @Field("money") double money);*/
    //支付挑战金
    @POST("/challenge/testPay")
    @FormUrlEncoded
    Call<ModifyBean> payChallenge(@Field("challengeId") String challengeId,
                                  @Field("money") double money);

    //账户余额
    @POST("/user/account/getBalance")
    Call<BalanceBean> getBalanceCall();

    //充值
    @POST("/user/recharge/apply")
    @FormUrlEncoded
    Call<ApplyBean> applyMoney(@Field("money") double money);

    //提现
    @POST("/withdraw/defrayPay")
    @FormUrlEncoded
    Call<WithdrawBean> withdrawCall(@Field("money") double money,
                                    @Field("alipayAccount") String alipayAccount,
                                    @Field("alipayName") String alipayName);

    //账户明細
    @POST("/record/list")
    @FormUrlEncoded
    Call<BalanceListBean> balanceListBeanCall(@Field("page") int page,
                                              @Field("rows") int rows);

    //--------------------------畅读卡---------------------------------------
    //畅读卡类型
    @POST("/sysParam/list")
    @FormUrlEncoded
    Call<ChangDuParamBean> changDuParamCall(@Field("type") String type);

    //购买畅读卡
    @POST("/vip/buy")
    @FormUrlEncoded
    Call<ModifyBean> buyChangDuCall(@Field("vipType") String vipType);

    //获取用户畅读卡信息
    @POST("/vip/info")
    Call<ChangduInfoBean> ChangDuInfoCall();

    //领取读币--注册过手机号的领取
    @POST("/user/coin/bind")
    Call<ModifyBean> getDubi();

    //领取读币--注册时领取
    @POST("/user/coin/bind")
    @FormUrlEncoded
    Call<ModifyBean> registerFGetDubi(@Field("telephone") String telephone,
                                      @Field("validNum") String validNum);

    //--------------------------分享----------------------------------------
    @POST("/share/getInfo")
    @FormUrlEncoded
    Call<ShareInfoBean> shareInfoCall(@Field("id") String id,
                                      @Field("type") String type);

    //-------------------------版本更新-----------------------------------
    @POST("/version/check")
    Call<CheckVersionBean> checkVersionCall();
}