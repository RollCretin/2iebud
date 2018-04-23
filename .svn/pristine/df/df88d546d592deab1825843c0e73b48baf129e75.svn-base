/*
 * Copyright (C) 2009-2015 FBReader.ORG Limited <contact@fbreader.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.BookmarkListAdapter;
import com.hxjf.dubei.adapter.CatalogListAdapter;
import com.hxjf.dubei.adapter.ReaderNoteListAdapter;
import com.hxjf.dubei.bean.BSBookListBean;
import com.hxjf.dubei.bean.BookDetailBean;
import com.hxjf.dubei.bean.DuBeiBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.ShareInfoBean;
import com.hxjf.dubei.bean.ShudanDetailBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.BookDetailActivity;
import com.hxjf.dubei.ui.activity.ReadDoneActivity;
import com.hxjf.dubei.util.DensityUtil;
import com.hxjf.dubei.util.LightUtils;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.widget.UpAndBottomSlideView;
import com.hxjf.dubei.widget.XCSlideView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xw.repo.BubbleSeekBar;

import org.geometerplus.android.fbreader.api.ApiListener;
import org.geometerplus.android.fbreader.api.ApiServerImplementation;
import org.geometerplus.android.fbreader.api.FBReaderIntents;
import org.geometerplus.android.fbreader.api.MenuNode;
import org.geometerplus.android.fbreader.api.PluginApi;
import org.geometerplus.android.fbreader.dict.DictionaryUtil;
import org.geometerplus.android.fbreader.formatPlugin.PluginUtil;
import org.geometerplus.android.fbreader.httpd.DataService;
import org.geometerplus.android.fbreader.libraryService.BookCollectionShadow;
import org.geometerplus.android.fbreader.sync.SyncOperations;
import org.geometerplus.android.fbreader.tips.TipsActivity;
import org.geometerplus.android.util.DeviceType;
import org.geometerplus.android.util.SearchDialogUtil;
import org.geometerplus.android.util.UIMessageUtil;
import org.geometerplus.android.util.UIUtil;
import org.geometerplus.fbreader.Paths;
import org.geometerplus.fbreader.book.Book;
import org.geometerplus.fbreader.book.BookUtil;
import org.geometerplus.fbreader.book.Bookmark;
import org.geometerplus.fbreader.book.BookmarkQuery;
import org.geometerplus.fbreader.bookmodel.BookModel;
import org.geometerplus.fbreader.bookmodel.TOCTree;
import org.geometerplus.fbreader.fbreader.ActionCode;
import org.geometerplus.fbreader.fbreader.DictionaryHighlighting;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.fbreader.fbreader.FBView;
import org.geometerplus.fbreader.fbreader.options.CancelMenuHelper;
import org.geometerplus.fbreader.fbreader.options.ColorProfile;
import org.geometerplus.fbreader.fbreader.options.PageTurningOptions;
import org.geometerplus.fbreader.formats.ExternalFormatPlugin;
import org.geometerplus.fbreader.formats.PluginCollection;
import org.geometerplus.fbreader.tips.TipsManager;
import org.geometerplus.zlibrary.core.application.ZLApplicationWindow;
import org.geometerplus.zlibrary.core.application.ZLKeyBindings;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.core.library.ZLibrary;
import org.geometerplus.zlibrary.core.options.Config;
import org.geometerplus.zlibrary.core.options.ZLBooleanOption;
import org.geometerplus.zlibrary.core.options.ZLIntegerRangeOption;
import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.zlibrary.core.util.ZLColor;
import org.geometerplus.zlibrary.core.view.ZLView;
import org.geometerplus.zlibrary.core.view.ZLViewEnums;
import org.geometerplus.zlibrary.core.view.ZLViewWidget;
import org.geometerplus.zlibrary.text.view.ZLTextRegion;
import org.geometerplus.zlibrary.text.view.ZLTextView;
import org.geometerplus.zlibrary.ui.android.error.ErrorKeys;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidApplication;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidLibrary;
import org.geometerplus.zlibrary.ui.android.view.AndroidFontUtil;
import org.geometerplus.zlibrary.ui.android.view.ZLAndroidWidget;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class FBReader extends FBReaderMainActivity implements ZLApplicationWindow {
    public static final int RESULT_DO_NOTHING = RESULT_FIRST_USER;
    public static final int RESULT_REPAINT = RESULT_FIRST_USER + 1;
    private static final String TAG = "FBReader...";
    private static final int BACKGROUND_REQUEST_CODE = 3000;
    private Dialog bottomdialog;
    private Dialog topdialog;
    private BubbleSeekBar sb_light;
    private ImageView bg_white;
    private ImageView bg_wink;
    private ImageView bg_black;
    private BubbleSeekBar sb_font;
    private BubbleSeekBar sb_progress;
    private UpAndBottomSlideView mSlideViewTop;
    private UpAndBottomSlideView mSlideViewBottom;
    private BookCollectionShadow myCollection;
    private Timer timer = new Timer();
    int sum = 0;
    private BookDetailBean.ResponseDataBean bookdetailbean;
    private String bookid;
    private BSBookListBean.ResponseDataBean bs_bookdetailbean;
    private int challengeflag;
    private ShareInfoBean.ResponseDataBean shareinfo;
    private UMWeb web;
    private boolean isDestroyed = false;
    private ShudanDetailBean.ResponseDataBean.BooksBean list_bookdetailbean;
    private DuBeiBean.ResponseDataBean.ChallengeInfoBean.BookInfoBean challenge_bookdetailbean;

    public static Intent defaultIntent(Context context) {
        return new Intent(context, FBReader.class)
                .setAction(FBReaderIntents.Action.VIEW)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public static void openBookActivity(Context context, Book book, Bookmark bookmark) {
        final Intent intent = defaultIntent(context);
        FBReaderIntents.putBookExtra(intent, book);
        FBReaderIntents.putBookmarkExtra(intent, bookmark);
        context.startActivity(intent);
    }

    private FBReaderApp myFBReaderApp;
    private volatile Book myBook;

    private RelativeLayout myRootView;
    private ZLAndroidWidget myMainView;

    private volatile boolean myShowStatusBarFlag;
    private String myMenuLanguage;

    final DataService.Connection DataConnection = new DataService.Connection();

    volatile boolean IsPaused = false;
    private volatile long myResumeTimestamp;
    volatile Runnable OnResumeAction = null;

    private Intent myCancelIntent = null;
    private Intent myOpenBookIntent = null;

    private final FBReaderApp.Notifier myNotifier = new AppNotifier(this);

    private static final String PLUGIN_ACTION_PREFIX = "___";
    private final List<PluginApi.ActionInfo> myPluginActions =
            new LinkedList<PluginApi.ActionInfo>();
    private final BroadcastReceiver myPluginInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final ArrayList<PluginApi.ActionInfo> actions = getResultExtras(true).<PluginApi.ActionInfo>getParcelableArrayList(PluginApi.PluginInfo.KEY);
            if (actions != null) {
                synchronized (myPluginActions) {
                    int index = 0;
                    while (index < myPluginActions.size()) {
                        myFBReaderApp.removeAction(PLUGIN_ACTION_PREFIX + index++);
                    }
                    myPluginActions.addAll(actions);
                    index = 0;
                    for (PluginApi.ActionInfo info : myPluginActions) {
                        myFBReaderApp.addAction(
                                PLUGIN_ACTION_PREFIX + index++,
                                new RunPluginAction(FBReader.this, myFBReaderApp, info.getId())
                        );
                    }
                }
            }
        }
    };

    private synchronized void openBook(Intent intent, final Runnable action, boolean force) {
        if (!force && myBook != null) {
            return;
        }
        myBook = FBReaderIntents.getBookExtra(intent, myFBReaderApp.Collection);
        final Bookmark bookmark = FBReaderIntents.getBookmarkExtra(intent);
        if (myBook == null) {
            final Uri data = intent.getData();
            if (data != null) {
                myBook = createBookForFile(ZLFile.createFileByPath(data.getPath()));
            }
        }
        if (myBook != null) {
            ZLFile file = BookUtil.fileByBook(myBook);
            if (!file.exists()) {
                if (file.getPhysicalFile() != null) {
                    file = file.getPhysicalFile();
                }
                UIMessageUtil.showErrorMessage(this, "fileNotFound", file.getPath());
                myBook = null;
            } else {
                NotificationUtil.drop(this, myBook);
            }
        }
        Config.Instance().runOnConnect(new Runnable() {
            public void run() {
                myFBReaderApp.openBook(myBook, bookmark, action, myNotifier);
                AndroidFontUtil.clearFontCache();
            }
        });
    }

    private Book createBookForFile(ZLFile file) {
        if (file == null) {
            return null;
        }
        Book book = myFBReaderApp.Collection.getBookByFile(file.getPath());
        if (book != null) {
            return book;
        }
        if (file.isArchive()) {
            for (ZLFile child : file.children()) {
                book = myFBReaderApp.Collection.getBookByFile(child.getPath());
                if (book != null) {
                    return book;
                }
            }
        }
        return null;
    }

    private Runnable getPostponedInitAction() {
        return new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        new TipRunner().start();
                        DictionaryUtil.init(FBReader.this, null);
                        final Intent intent = getIntent();
                        if (intent != null && FBReaderIntents.Action.PLUGIN.equals(intent.getAction())) {
                            new RunPluginAction(FBReader.this, myFBReaderApp, intent.getData()).run();
                        }
                    }
                });
            }
        };
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        bindService(
                new Intent(this, DataService.class),
                DataConnection,
                DataService.BIND_AUTO_CREATE
        );

        final Config config = Config.Instance();
        config.runOnConnect(new Runnable() {
            public void run() {
                config.requestAllValuesForGroup("Options");
                config.requestAllValuesForGroup("Style");
                config.requestAllValuesForGroup("LookNFeel");
                config.requestAllValuesForGroup("Fonts");
                config.requestAllValuesForGroup("Colors");
                config.requestAllValuesForGroup("Files");
            }
        });

        final ZLAndroidLibrary zlibrary = getZLibrary();
        myShowStatusBarFlag = zlibrary.ShowStatusBarOption.getValue();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        myRootView = (RelativeLayout) findViewById(R.id.root_view);
        myMainView = (ZLAndroidWidget) findViewById(R.id.main_view);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        myFBReaderApp = (FBReaderApp) FBReaderApp.Instance();
        if (myFBReaderApp == null) {
            myFBReaderApp = new FBReaderApp(Paths.systemInfo(this), new BookCollectionShadow());
        }
        getCollection().bindToService(this, null);
        myBook = null;

        myFBReaderApp.setWindow(this);
        myFBReaderApp.initWindow();

        myFBReaderApp.setExternalFileOpener(new ExternalFileOpener(this));

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                myShowStatusBarFlag ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        if (myFBReaderApp.getPopupById(TextSearchPopup.ID) == null) {
            new TextSearchPopup(myFBReaderApp);
        }
        if (myFBReaderApp.getPopupById(NavigationPopup.ID) == null) {
            new NavigationPopup(myFBReaderApp);
        }
        if (myFBReaderApp.getPopupById(SelectionPopup.ID) == null) {
            new SelectionPopup(myFBReaderApp);
        }

        myFBReaderApp.addAction(ActionCode.SHOW_LIBRARY, new ShowLibraryAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.SHOW_PREFERENCES, new ShowPreferencesAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.SHOW_BOOK_INFO, new ShowBookInfoAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.SHOW_TOC, new ShowTOCAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.SHOW_BOOKMARKS, new ShowBookmarksAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.SHOW_NETWORK_LIBRARY, new ShowNetworkLibraryAction(this, myFBReaderApp));

        myFBReaderApp.addAction(ActionCode.SHOW_MENU, new ShowMenuAction(this, myFBReaderApp));

        myFBReaderApp.addAction(ActionCode.SHOW_NAVIGATION, new ShowNavigationAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.SEARCH, new SearchAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.SHARE_BOOK, new ShareBookAction(this, myFBReaderApp));

        myFBReaderApp.addAction(ActionCode.SELECTION_SHOW_PANEL, new SelectionShowPanelAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.SELECTION_HIDE_PANEL, new SelectionHidePanelAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.SELECTION_COPY_TO_CLIPBOARD, new SelectionCopyAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.SELECTION_SHARE, new SelectionShareAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.SELECTION_TRANSLATE, new SelectionTranslateAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.SELECTION_BOOKMARK, new SelectionBookmarkAction(this, myFBReaderApp));

        myFBReaderApp.addAction(ActionCode.DISPLAY_BOOK_POPUP, new DisplayBookPopupAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.PROCESS_HYPERLINK, new ProcessHyperlinkAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.OPEN_VIDEO, new OpenVideoAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.HIDE_TOAST, new HideToastAction(this, myFBReaderApp));

        myFBReaderApp.addAction(ActionCode.SHOW_CANCEL_MENU, new ShowCancelMenuAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.OPEN_START_SCREEN, new StartScreenAction(this, myFBReaderApp));

        myFBReaderApp.addAction(ActionCode.SET_SCREEN_ORIENTATION_SYSTEM, new SetScreenOrientationAction(this, myFBReaderApp, ZLibrary.SCREEN_ORIENTATION_SYSTEM));
        myFBReaderApp.addAction(ActionCode.SET_SCREEN_ORIENTATION_SENSOR, new SetScreenOrientationAction(this, myFBReaderApp, ZLibrary.SCREEN_ORIENTATION_SENSOR));
        myFBReaderApp.addAction(ActionCode.SET_SCREEN_ORIENTATION_PORTRAIT, new SetScreenOrientationAction(this, myFBReaderApp, ZLibrary.SCREEN_ORIENTATION_PORTRAIT));
        myFBReaderApp.addAction(ActionCode.SET_SCREEN_ORIENTATION_LANDSCAPE, new SetScreenOrientationAction(this, myFBReaderApp, ZLibrary.SCREEN_ORIENTATION_LANDSCAPE));
        if (getZLibrary().supportsAllOrientations()) {
            myFBReaderApp.addAction(ActionCode.SET_SCREEN_ORIENTATION_REVERSE_PORTRAIT, new SetScreenOrientationAction(this, myFBReaderApp, ZLibrary.SCREEN_ORIENTATION_REVERSE_PORTRAIT));
            myFBReaderApp.addAction(ActionCode.SET_SCREEN_ORIENTATION_REVERSE_LANDSCAPE, new SetScreenOrientationAction(this, myFBReaderApp, ZLibrary.SCREEN_ORIENTATION_REVERSE_LANDSCAPE));
        }
        myFBReaderApp.addAction(ActionCode.OPEN_WEB_HELP, new OpenWebHelpAction(this, myFBReaderApp));
        myFBReaderApp.addAction(ActionCode.INSTALL_PLUGINS, new InstallPluginsAction(this, myFBReaderApp));

        myFBReaderApp.addAction(ActionCode.SWITCH_TO_DAY_PROFILE, new SwitchProfileAction(this, myFBReaderApp, ColorProfile.DAY));
        myFBReaderApp.addAction(ActionCode.SWITCH_TO_NIGHT_PROFILE, new SwitchProfileAction(this, myFBReaderApp, ColorProfile.NIGHT));

        final Intent intent = getIntent();
        final String action = intent.getAction();

        myOpenBookIntent = intent;
        if ((intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0) {
            if (FBReaderIntents.Action.CLOSE.equals(action)) {
                myCancelIntent = intent;
                myOpenBookIntent = null;
            } else if (FBReaderIntents.Action.PLUGIN_CRASH.equals(action)) {
                myFBReaderApp.ExternalBook = null;
                myOpenBookIntent = null;
                getCollection().bindToService(this, new Runnable() {
                    public void run() {
                        myFBReaderApp.openBook(null, null, null, myNotifier);
                    }
                });
            }
        }

        /**
         * 获取传递过来的bean以及是否在挑战中
         */
        Intent bookidintent = getIntent();
        //从书籍详情中进来的
        bookdetailbean = (BookDetailBean.ResponseDataBean) bookidintent.getSerializableExtra("bookdetail");
        //从书架进来
        bs_bookdetailbean = (BSBookListBean.ResponseDataBean) bookidintent.getSerializableExtra("bs_bookdetail");
        //从书单书籍列表中进来
        list_bookdetailbean = (ShudanDetailBean.ResponseDataBean.BooksBean) bookidintent.getSerializableExtra("list_bookdetail");
        //从激战正酣的卡片中进来
        challenge_bookdetailbean = (DuBeiBean.ResponseDataBean.ChallengeInfoBean.BookInfoBean) bookidintent.getSerializableExtra("challenge_bookdetail");


        //获取是否挑战
        challengeflag = bookidintent.getIntExtra("challengeflag", 0);

        if (bookdetailbean != null) {
            bookid = bookdetailbean.getId();
        } else if (bs_bookdetailbean != null) {
            bookid = bs_bookdetailbean.getBookInfo().getId();
        } else if (list_bookdetailbean != null) {
            bookid = list_bookdetailbean.getId();
        } else if (challenge_bookdetailbean != null) {
            bookid = challenge_bookdetailbean.getId();
        }
        myMainView.setBookId(bookid);

        //设置字体大小
        float fontSize = SPUtils.getFloat(FBReader.this, "fontSize", 16f);
        myFBReaderApp.ViewOptions.getTextStyleCollection().getBaseStyle().FontSizeOption.setValue(DensityUtil.dip2px(this, fontSize));


        //设置图片不适应背景
        ZLBooleanOption zlBooleanOption = new ZLBooleanOption("Colors", "ImageMatchBackground", false);
        //只设置图片适应屏幕
        myFBReaderApp.ImageOptions.FitToScreen.setValue(FBView.ImageFitting.covers);
        myFBReaderApp.ViewOptions.getTextStyleCollection().getBaseStyle().LineSpaceOption.setValue(56);//1.3倍行间距
        myFBReaderApp.ViewOptions.getTextStyleCollection().getDescriptionList().get(1).MarginTopOption.setValue("0.6em");//段前间距
        //上下左右边距
        myFBReaderApp.ViewOptions.TopMargin.setValue(60);
        myFBReaderApp.ViewOptions.BottomMargin.setValue(60);
        myFBReaderApp.ViewOptions.LeftMargin.setValue(80);
        myFBReaderApp.ViewOptions.RightMargin.setValue(80);
        //绑定音量键上下翻页
        ZLKeyBindings keyBindings = myFBReaderApp.keyBindings();
        keyBindings.bindKey(KeyEvent.KEYCODE_VOLUME_DOWN, false, ActionCode.VOLUME_KEY_SCROLL_FORWARD);
        keyBindings.bindKey(KeyEvent.KEYCODE_VOLUME_UP, false, ActionCode.VOLUME_KEY_SCROLL_BACK);

        myFBReaderApp.ViewOptions.getTextStyleCollection().getDescriptionList().get(1).FontFamilyOption.setValue("Droid Mono");//字体
        //设置为夜间模式
        myFBReaderApp.ViewOptions.ColorProfileName.setValue(ColorProfile.NIGHT);
        int[] backgroundcolorArrays = SPUtils.getIntArray(this, "backgroundcolorArray", 3);
        int[] textcolorArrays = SPUtils.getIntArray(this, "textcolorArray", 3);
        if (backgroundcolorArrays[0] == -1){
            myFBReaderApp.ViewOptions.getColorProfile().BackgroundOption.setValue(new ZLColor(255, 255, 255));
        }else{
            myFBReaderApp.ViewOptions.getColorProfile().BackgroundOption.setValue(new ZLColor(backgroundcolorArrays[0], backgroundcolorArrays[1], backgroundcolorArrays[2]));
        }

        if (textcolorArrays[0] == -1){
            myFBReaderApp.ViewOptions.getColorProfile().RegularTextOption.setValue(new ZLColor(0, 0, 0));
        }else{
            myFBReaderApp.ViewOptions.getColorProfile().RegularTextOption.setValue(new ZLColor(textcolorArrays[0], textcolorArrays[1], textcolorArrays[2]));
        }
        myFBReaderApp.clearTextCaches();
        myFBReaderApp.getViewWidget().reset();
        myFBReaderApp.getViewWidget().repaint();
        //设置滚动条
        myFBReaderApp.ViewOptions.ScrollbarType.setValue(FBView.SCROLLBAR_SHOW_AS_FOOTER);
        myFBReaderApp.ViewOptions.getFooterOptions().ShowBattery.setValue(false);//不显示电量
        myFBReaderApp.ViewOptions.getFooterOptions().ShowTOCMarks.setValue(false);//不显示章节标识
        myFBReaderApp.ViewOptions.getFooterOptions().showProgressAsPages();//显示进度和页数

        //设置翻页动画,速度
        myFBReaderApp.PageTurningOptions.Animation.setValue(ZLViewEnums.Animation.shift);
        myFBReaderApp.PageTurningOptions.AnimationSpeed.setValue(2);
        //禁止屏幕左侧上下滑动手指调整亮度
        myFBReaderApp.MiscOptions.AllowScreenBrightnessAdjustment.setValue(false);
        //开始计时
        if (timer != null) {
            timer.schedule(task, 0, 1000);
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    sum++;
                    myMainView.addTime(sum);
            }
        }
    };

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//		setStatusBarVisibility(true);
//		setupMenu(menu);
        return false;
//		return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
//		super.onOptionsMenuClosed(menu);
        //	setStatusBarVisibility(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//		setStatusBarVisibility(false);
        Menu();
//		return super.onOptionsItemSelected(item);
        return false;
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        final String action = intent.getAction();
        final Uri data = intent.getData();

        if ((intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
            super.onNewIntent(intent);
        } else if (Intent.ACTION_VIEW.equals(action)
                && data != null && "fbreader-action".equals(data.getScheme())) {
            myFBReaderApp.runAction(data.getEncodedSchemeSpecificPart(), data.getFragment());
        } else if (Intent.ACTION_VIEW.equals(action) || FBReaderIntents.Action.VIEW.equals(action)) {
            myOpenBookIntent = intent;
            if (myFBReaderApp.Model == null && myFBReaderApp.ExternalBook != null) {
                final BookCollectionShadow collection = getCollection();
                final Book b = FBReaderIntents.getBookExtra(intent, collection);
                if (!collection.sameBook(b, myFBReaderApp.ExternalBook)) {
                    try {
                        final ExternalFormatPlugin plugin =
                                (ExternalFormatPlugin) BookUtil.getPlugin(
                                        PluginCollection.Instance(Paths.systemInfo(this)),
                                        myFBReaderApp.ExternalBook
                                );
                        startActivity(PluginUtil.createIntent(plugin, FBReaderIntents.Action.PLUGIN_KILL));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (FBReaderIntents.Action.PLUGIN.equals(action)) {
            new RunPluginAction(this, myFBReaderApp, data).run();
        } else if (Intent.ACTION_SEARCH.equals(action)) {
            final String pattern = intent.getStringExtra(SearchManager.QUERY);
            final Runnable runnable = new Runnable() {
                public void run() {
                    final TextSearchPopup popup = (TextSearchPopup) myFBReaderApp.getPopupById(TextSearchPopup.ID);
                    popup.initPosition();
                    myFBReaderApp.MiscOptions.TextSearchPattern.setValue(pattern);
                    if (myFBReaderApp.getTextView().search(pattern, true, false, false, false) != 0) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                myFBReaderApp.showPopup(popup.getId());
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                UIMessageUtil.showErrorMessage(FBReader.this, "textNotFound");
                                popup.StartPosition = null;
                            }
                        });
                    }
                }
            };
            UIUtil.wait("search", runnable, this);
        } else if (FBReaderIntents.Action.CLOSE.equals(intent.getAction())) {
            myCancelIntent = intent;
            myOpenBookIntent = null;
        } else if (FBReaderIntents.Action.PLUGIN_CRASH.equals(intent.getAction())) {
            final Book book = FBReaderIntents.getBookExtra(intent, myFBReaderApp.Collection);
            myFBReaderApp.ExternalBook = null;
            myOpenBookIntent = null;
            getCollection().bindToService(this, new Runnable() {
                public void run() {
                    final BookCollectionShadow collection = getCollection();
                    Book b = collection.getRecentBook(0);
                    if (collection.sameBook(b, book)) {
                        b = collection.getRecentBook(1);
                    }
                    myFBReaderApp.openBook(b, null, null, myNotifier);
                }
            });
        } else {
            super.onNewIntent(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        getCollection().bindToService(this, new Runnable() {
            public void run() {
                new Thread() {
                    public void run() {
                        getPostponedInitAction().run();
                    }
                }.start();

                myFBReaderApp.getViewWidget().repaint();
            }
        });

        initPluginActions();

        final ZLAndroidLibrary zlibrary = getZLibrary();

        Config.Instance().runOnConnect(new Runnable() {
            public void run() {
                final boolean showStatusBar = zlibrary.ShowStatusBarOption.getValue();
                if (showStatusBar != myShowStatusBarFlag) {
                    finish();
                    startActivity(new Intent(FBReader.this, FBReader.class));
                }
                zlibrary.ShowStatusBarOption.saveSpecialValue();
                myFBReaderApp.ViewOptions.ColorProfileName.saveSpecialValue();
                SetScreenOrientationAction.setOrientation(FBReader.this, zlibrary.getOrientationOption().getValue());
            }
        });

        ((PopupPanel) myFBReaderApp.getPopupById(TextSearchPopup.ID)).setPanelInfo(this, myRootView);
        ((NavigationPopup) myFBReaderApp.getPopupById(NavigationPopup.ID)).setPanelInfo(this, myRootView);
        ((PopupPanel) myFBReaderApp.getPopupById(SelectionPopup.ID)).setPanelInfo(this, myRootView);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        switchWakeLock(hasFocus &&
                getZLibrary().BatteryLevelToTurnScreenOffOption.getValue() <
                        myFBReaderApp.getBatteryLevel()
        );
    }

    private void initPluginActions() {
        synchronized (myPluginActions) {
            int index = 0;
            while (index < myPluginActions.size()) {
                myFBReaderApp.removeAction(PLUGIN_ACTION_PREFIX + index++);
            }
            myPluginActions.clear();
        }

        sendOrderedBroadcast(
                new Intent(PluginApi.ACTION_REGISTER),
                null,
                myPluginInfoReceiver,
                null,
                RESULT_OK,
                null,
                null
        );
    }

    private class TipRunner extends Thread {
        TipRunner() {
            setPriority(MIN_PRIORITY);
        }

        public void run() {
            final TipsManager manager = new TipsManager(Paths.systemInfo(FBReader.this));
            switch (manager.requiredAction()) {
                case Initialize:
                    startActivity(new Intent(
                            TipsActivity.INITIALIZE_ACTION, null, FBReader.this, TipsActivity.class
                    ));
                    break;
                case Show:
                    startActivity(new Intent(
                            TipsActivity.SHOW_TIP_ACTION, null, FBReader.this, TipsActivity.class
                    ));
                    break;
                case Download:
                    manager.startDownloading();
                    break;
                case None:
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        myStartTimer = true;
        Config.Instance().runOnConnect(new Runnable() {
            public void run() {
                SyncOperations.enableSync(FBReader.this, myFBReaderApp.SyncOptions);

                final int brightnessLevel =
                        getZLibrary().ScreenBrightnessLevelOption.getValue();
                if (brightnessLevel != 0) {
                    getViewWidget().setScreenBrightness(brightnessLevel);
                } else {
                    setScreenBrightnessAuto();
                }
                if (getZLibrary().DisableButtonLightsOption.getValue()) {
                    setButtonLight(false);
                }

                getCollection().bindToService(FBReader.this, new Runnable() {
                    public void run() {
                        final BookModel model = myFBReaderApp.Model;
                        if (model == null || model.Book == null) {
                            return;
                        }
                        onPreferencesUpdate(myFBReaderApp.Collection.getBookById(model.Book.getId()));
                    }
                });
            }
        });

        registerReceiver(myBatteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        IsPaused = false;
        myResumeTimestamp = System.currentTimeMillis();
        if (OnResumeAction != null) {
            final Runnable action = OnResumeAction;
            OnResumeAction = null;
            action.run();
        }

        registerReceiver(mySyncUpdateReceiver, new IntentFilter(FBReaderIntents.Event.SYNC_UPDATED));

        SetScreenOrientationAction.setOrientation(this, getZLibrary().getOrientationOption().getValue());
        if (myCancelIntent != null) {
            final Intent intent = myCancelIntent;
            myCancelIntent = null;
            getCollection().bindToService(this, new Runnable() {
                public void run() {
                    runCancelAction(intent);
                }
            });
            return;
        } else if (myOpenBookIntent != null) {
            final Intent intent = myOpenBookIntent;
            myOpenBookIntent = null;
            getCollection().bindToService(this, new Runnable() {
                public void run() {
                    openBook(intent, null, true);

                }
            });
        } else if (myFBReaderApp.getCurrentServerBook(null) != null) {
            getCollection().bindToService(this, new Runnable() {
                public void run() {
                    myFBReaderApp.useSyncInfo(true, myNotifier);
                }
            });
        } else if (myFBReaderApp.Model == null && myFBReaderApp.ExternalBook != null) {
            getCollection().bindToService(this, new Runnable() {
                public void run() {
                    myFBReaderApp.openBook(myFBReaderApp.ExternalBook, null, null, myNotifier);
                }
            });
        } else {
            getCollection().bindToService(this, new Runnable() {
                public void run() {
                    myFBReaderApp.useSyncInfo(true, myNotifier);
                }
            });
        }

        //设置屏幕亮度
        int readerLight = SPUtils.getInt(FBReader.this, "readLight", 100);
        LightUtils.setBrightness(FBReader.this, readerLight);

        PopupPanel.restoreVisibilities(myFBReaderApp);
        ApiServerImplementation.sendEvent(this, ApiListener.EVENT_READ_MODE_OPENED);

    }

    @Override
    protected void onPause() {
        if (isFinishing()) {
            destroy();
        }
        SyncOperations.quickSync(this, myFBReaderApp.SyncOptions);

        IsPaused = true;
        try {
            unregisterReceiver(mySyncUpdateReceiver);
        } catch (IllegalArgumentException e) {
        }

        try {
            unregisterReceiver(myBatteryInfoReceiver);
        } catch (IllegalArgumentException e) {
            // do nothing, this exception means that myBatteryInfoReceiver was not registered
        }

        myFBReaderApp.stopTimer();
        if (getZLibrary().DisableButtonLightsOption.getValue()) {
            setButtonLight(true);
        }
        myFBReaderApp.onWindowClosing();
        super.onPause();
    }

    @Override
    protected void onStop() {

        ApiServerImplementation.sendEvent(this, ApiListener.EVENT_READ_MODE_CLOSED);
        PopupPanel.removeAllWindows(myFBReaderApp, this);
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroy();
    }

    private void destroy() {
        if (isDestroyed) {
            return;
        }
        if (timer != null) {
            timer.cancel();//计时取消
            timer.purge();
            timer = null;
        }
        int readtime = myMainView.getTime();
        Log.d(TAG, "destroy: 距上次更新后的阅读时间" + readtime);

        //TODO
        //进度
        ZLTextView.PagePosition pagePosition = myFBReaderApp.getTextView().pagePosition();
        final int progress = (int) (pagePosition.Current * 100 / ((float) pagePosition.Total));
        //第几章
        int chapter = 0;
        TOCTree currentTOCElement = myFBReaderApp.getCurrentTOCElement();
        if (myFBReaderApp.Model != null) {
            TOCTree root = myFBReaderApp.Model.TOCTree;
            List<TOCTree> subtrees = root.subtrees();
            for (int i = 0; i < subtrees.size(); i++) {
                if (currentTOCElement != null && subtrees.get(i).getText().equals(currentTOCElement.getText())) {
                    chapter = i;
                }
            }
        }
        //字节数
        //TODO
        String position = chapter + ",";
        //上传阅读时间
        Log.d("readTime", "destroy: "+readtime);
        Call<ModifyBean> updateBookCall = ReaderRetroift.getInstance(this).getApi().updateBookCall(bookid, progress, readtime, position);
        updateBookCall.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null) {
                    Log.d(TAG, "onResponse: 进度是..." + progress);
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {

            }
        });
        //判断是否到达最后一页
        ZLTextView.PagePosition endpagePosition = myFBReaderApp.getTextView().pagePosition();
        if (endpagePosition.Current == endpagePosition.Total) {
            //跳转到阅读完成页
            Intent readdoneIntent = new Intent(this, ReadDoneActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("bookdetailBean", bookdetailbean);
            bundle.putSerializable("bs_bookdetail", bs_bookdetailbean);
            bundle.putSerializable("list_bookdetail", list_bookdetailbean);
            bundle.putSerializable("challenge_bookdetail", challenge_bookdetailbean);

            readdoneIntent.putExtras(bundle);
            readdoneIntent.putExtra("challengeflag", challengeflag);
            startActivity(readdoneIntent);
        }

        getCollection().unbind();
        unbindService(DataConnection);

        isDestroyed = true;

    }

    @Override
    public void onLowMemory() {
        myFBReaderApp.onWindowClosing();
        super.onLowMemory();
    }

    @Override
    public boolean onSearchRequested() {
        final FBReaderApp.PopupPanel popup = myFBReaderApp.getActivePopup();
        myFBReaderApp.hideActivePopup();
        if (DeviceType.Instance().hasStandardSearchDialog()) {
            final SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
            manager.setOnCancelListener(new SearchManager.OnCancelListener() {
                public void onCancel() {
                    if (popup != null) {
                        myFBReaderApp.showPopup(popup.getId());
                    }
                    manager.setOnCancelListener(null);
                }
            });
            startSearch(myFBReaderApp.MiscOptions.TextSearchPattern.getValue(), true, null, false);
        } else {
            SearchDialogUtil.showDialog(
                    this, FBReader.class, myFBReaderApp.MiscOptions.TextSearchPattern.getValue(), new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface di) {
                            if (popup != null) {
                                myFBReaderApp.showPopup(popup.getId());
                            }
                        }
                    }
            );
        }
        return true;
    }

    public void showSelectionPanel() {
        //TODO
        PopupPanel.restoreVisibilities(myFBReaderApp);
        final ZLTextView view = myFBReaderApp.getTextView();
        ((SelectionPopup) myFBReaderApp.getPopupById(SelectionPopup.ID))
                .move(view.getSelectionStartY(), view.getSelectionEndY());
        myFBReaderApp.showPopup(SelectionPopup.ID);
    }

    public void hideSelectionPanel() {
        final FBReaderApp.PopupPanel popup = myFBReaderApp.getActivePopup();
        if (popup != null && popup.getId() == SelectionPopup.ID) {
            myFBReaderApp.hideActivePopup();
        }
    }

    private void onPreferencesUpdate(Book book) {
        AndroidFontUtil.clearFontCache();
        myFBReaderApp.onBookUpdated(book);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
            case REQUEST_PREFERENCES:
                if (resultCode != RESULT_DO_NOTHING && data != null) {
                    final Book book = FBReaderIntents.getBookExtra(data, myFBReaderApp.Collection);
                    if (book != null) {
                        getCollection().bindToService(this, new Runnable() {
                            public void run() {
                                onPreferencesUpdate(book);
                            }
                        });
                    }
                }
                break;
            case REQUEST_CANCEL_MENU:
                runCancelAction(data);
                break;
        }
    }

    private void runCancelAction(Intent intent) {
        final CancelMenuHelper.ActionType type;
        try {
            type = CancelMenuHelper.ActionType.valueOf(
                    intent.getStringExtra(FBReaderIntents.Key.TYPE)
            );
        } catch (Exception e) {
            // invalid (or null) type value
            return;
        }
        Bookmark bookmark = null;
        if (type == CancelMenuHelper.ActionType.returnTo) {
            bookmark = FBReaderIntents.getBookmarkExtra(intent);
            if (bookmark == null) {
                return;
            }
        }
        myFBReaderApp.runCancelAction(type, bookmark);
    }

    public void navigate() {
        ((NavigationPopup) myFBReaderApp.getPopupById(NavigationPopup.ID)).runNavigation();
    }

    private Menu addSubmenu(Menu menu, String id) {
        return menu.addSubMenu(ZLResource.resource("menu").getResource(id).getValue());
    }

    private void addMenuItem(Menu menu, String actionId, Integer iconId, String name) {
        if (name == null) {
            name = ZLResource.resource("menu").getResource(actionId).getValue();
        }
        final MenuItem menuItem = menu.add(name);
        if (iconId != null) {
            menuItem.setIcon(iconId);
        }
        menuItem.setOnMenuItemClickListener(myMenuListener);
        myMenuItemMap.put(menuItem, actionId);
    }

    private void addMenuItem(Menu menu, String actionId, String name) {
        addMenuItem(menu, actionId, null, name);
    }

    private void addMenuItem(Menu menu, String actionId, int iconId) {
        addMenuItem(menu, actionId, iconId, null);
    }

    private void addMenuItem(Menu menu, String actionId) {
        addMenuItem(menu, actionId, null, null);
    }

    private void fillMenu(Menu menu, List<MenuNode> nodes) {
        for (MenuNode n : nodes) {
            if (n instanceof MenuNode.Item) {
                final Integer iconId = ((MenuNode.Item) n).IconId;
                if (iconId != null) {
                    addMenuItem(menu, n.Code, iconId);
                } else {
                    addMenuItem(menu, n.Code);
                }
            } else /* if (n instanceof MenuNode.Submenu) */ {
                final Menu submenu = addSubmenu(menu, n.Code);
                fillMenu(submenu, ((MenuNode.Submenu) n).Children);
            }
        }
    }

    private void setupMenu(Menu menu) {
        final String menuLanguage = ZLResource.getLanguageOption().getValue();
        if (menuLanguage.equals(myMenuLanguage)) {
            return;
        }
        myMenuLanguage = menuLanguage;

        menu.clear();
        fillMenu(menu, MenuData.topLevelNodes());
        synchronized (myPluginActions) {
            int index = 0;
            for (PluginApi.ActionInfo info : myPluginActions) {
                if (info instanceof PluginApi.MenuActionInfo) {
                    addMenuItem(
                            menu,
                            PLUGIN_ACTION_PREFIX + index++,
                            ((PluginApi.MenuActionInfo) info).MenuItemName
                    );
                }
            }
        }

        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Menu();
        setupMenu(menu);

        return true;
    }

    protected void onPluginNotFound(final Book book) {
        final BookCollectionShadow collection = getCollection();
        collection.bindToService(this, new Runnable() {
            public void run() {
                final Book recent = collection.getRecentBook(0);
                if (recent != null && !collection.sameBook(recent, book)) {
                    myFBReaderApp.openBook(recent, null, null, null);
                } else {
                    myFBReaderApp.openHelpBook();
                }
            }
        });
    }

    private void setStatusBarVisibility(boolean visible) {
        final ZLAndroidLibrary zlibrary = getZLibrary();
        if (DeviceType.Instance() != DeviceType.KINDLE_FIRE_1ST_GENERATION && !myShowStatusBarFlag) {
            if (visible) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (myMainView != null && myMainView.onKeyDown(keyCode, event)) || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return (myMainView != null && myMainView.onKeyUp(keyCode, event)) || super.onKeyUp(keyCode, event);
    }

    private void setButtonLight(boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            setButtonLightInternal(enabled);
        }
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private void setButtonLightInternal(boolean enabled) {
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.buttonBrightness = enabled ? -1.0f : 0.0f;
        getWindow().setAttributes(attrs);
    }

    private PowerManager.WakeLock myWakeLock;
    private boolean myWakeLockToCreate;
    private boolean myStartTimer;

    public final void createWakeLock() {
        if (myWakeLockToCreate) {
            synchronized (this) {
                if (myWakeLockToCreate) {
                    myWakeLockToCreate = false;
                    myWakeLock =
                            ((PowerManager) getSystemService(POWER_SERVICE))
                                    .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "FBReader");
                    myWakeLock.acquire();
                }
            }
        }
        if (myStartTimer) {
            myFBReaderApp.startTimer();
            myStartTimer = false;
        }
    }

    private final void switchWakeLock(boolean on) {
        if (on) {
            if (myWakeLock == null) {
                myWakeLockToCreate = true;
            }
        } else {
            if (myWakeLock != null) {
                synchronized (this) {
                    if (myWakeLock != null) {
                        myWakeLock.release();
                        myWakeLock = null;
                    }
                }
            }
        }
    }

    private BroadcastReceiver myBatteryInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final int level = intent.getIntExtra("level", 100);
            final ZLAndroidApplication application = (ZLAndroidApplication) getApplication();
            setBatteryLevel(level);
            switchWakeLock(
                    hasWindowFocus() &&
                            getZLibrary().BatteryLevelToTurnScreenOffOption.getValue() < level
            );
        }
    };

    private BookCollectionShadow getCollection() {
        return (BookCollectionShadow) myFBReaderApp.Collection;
    }

    // methods from ZLApplicationWindow interface
    @Override
    public void showErrorMessage(String key) {
        UIMessageUtil.showErrorMessage(this, key);
    }

    @Override
    public void showErrorMessage(String key, String parameter) {
        UIMessageUtil.showErrorMessage(this, key, parameter);
    }

    @Override
    public FBReaderApp.SynchronousExecutor createExecutor(String key) {
        return UIUtil.createExecutor(this, key);
    }

    private int myBatteryLevel;

    @Override
    public int getBatteryLevel() {
        return myBatteryLevel;
    }

    private void setBatteryLevel(int percent) {
        myBatteryLevel = percent;
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public ZLViewWidget getViewWidget() {
        return myMainView;
    }

    private final HashMap<MenuItem, String> myMenuItemMap = new HashMap<MenuItem, String>();

    private final MenuItem.OnMenuItemClickListener myMenuListener =
            new MenuItem.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    myFBReaderApp.runAction(myMenuItemMap.get(item));
                    return true;
                }
            };

    @Override
    public void refresh() {
        runOnUiThread(new Runnable() {
            public void run() {
                for (Map.Entry<MenuItem, String> entry : myMenuItemMap.entrySet()) {
                    final String actionId = entry.getValue();
                    final MenuItem menuItem = entry.getKey();
                    menuItem.setVisible(myFBReaderApp.isActionVisible(actionId) && myFBReaderApp.isActionEnabled(actionId));
                    switch (myFBReaderApp.isActionChecked(actionId)) {
                        case TRUE:
                            menuItem.setCheckable(true);
                            menuItem.setChecked(true);
                            break;
                        case FALSE:
                            menuItem.setCheckable(true);
                            menuItem.setChecked(false);
                            break;
                        case UNDEFINED:
                            menuItem.setCheckable(false);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void processException(Exception exception) {
        exception.printStackTrace();

        final Intent intent = new Intent(
                FBReaderIntents.Action.ERROR,
                new Uri.Builder().scheme(exception.getClass().getSimpleName()).build()
        );
        intent.setPackage(FBReaderIntents.DEFAULT_PACKAGE);
        intent.putExtra(ErrorKeys.MESSAGE, exception.getMessage());
        final StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        intent.putExtra(ErrorKeys.STACKTRACE, stackTrace.toString());
        /*
        if (exception instanceof BookReadingException) {
			final ZLFile file = ((BookReadingException)exception).File;
			if (file != null) {
				intent.putExtra("file", file.getPath());
			}
		}
		*/
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // ignore
            e.printStackTrace();
        }
    }

    @Override
    public void setWindowTitle(final String title) {
        runOnUiThread(new Runnable() {
            public void run() {
                setTitle(title);
            }
        });
    }

    private BroadcastReceiver mySyncUpdateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            myFBReaderApp.useSyncInfo(myResumeTimestamp + 10 * 1000 > System.currentTimeMillis(), myNotifier);
        }
    };

    public void outlineRegion(ZLTextRegion.Soul soul) {
        myFBReaderApp.getTextView().outlineRegion(soul);
        myFBReaderApp.getViewWidget().repaint();
    }

    public void hideOutline() {
        myFBReaderApp.getTextView().hideOutline();
        myFBReaderApp.getViewWidget().repaint();
    }

    public void hideDictionarySelection() {
        myFBReaderApp.getTextView().hideOutline();
        myFBReaderApp.getTextView().removeHighlightings(DictionaryHighlighting.class);
        myFBReaderApp.getViewWidget().reset();
        myFBReaderApp.getViewWidget().repaint();
    }

    //top菜单选项
    public void Menu() {
        int mScreenHeight = DensityUtil.getScreenWidthAndHeight(this)[1];
        //上面的布局
        View topcontentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_reader_top, null);
        mSlideViewTop = UpAndBottomSlideView.create(this);

        mSlideViewTop.setMenuView(this, topcontentView);
        mSlideViewTop.setMenuHeight(mScreenHeight * 1 / 15);

        if (!mSlideViewTop.isShow()) {
            mSlideViewTop.show();
        }

        ImageView back = (ImageView) topcontentView.findViewById(R.id.reader_back);
        ImageView bookinfo = (ImageView) topcontentView.findViewById(R.id.reader_bookinfo);
        ImageView share = (ImageView) topcontentView.findViewById(R.id.reader_share);

        //菜单的布局
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_reader_bottom, null);
        mSlideViewBottom = UpAndBottomSlideView.create(this, UpAndBottomSlideView.Position.BOTTOM);
        mSlideViewBottom.setMenuView(this, contentView);
        mSlideViewBottom.setMenuHeight(mScreenHeight * 1 / 4);
        mSlideViewBottom.addOtherView(mSlideViewTop);
        if (!mSlideViewBottom.isShow()) {
            mSlideViewBottom.show();
        }
        //书籍详情页
        bookinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewTop.dismiss();
                mSlideViewBottom.dismiss();
                Intent intent = new Intent(FBReader.this, BookDetailActivity.class);
                intent.putExtra("bookid", bookid);
                intent.putExtra("isreader", true);
                startActivity(intent);
            }
        });
        //分享
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewTop.dismiss();
                mSlideViewBottom.dismiss();
                final Dialog bottomDialog = new Dialog(FBReader.this, R.style.myDialogTheme);
                View contentView = LayoutInflater.from(FBReader.this).inflate(R.layout.view_dialog_share, null);
                final TextView tv_wx = (TextView) contentView.findViewById(R.id.dialog_share_wx);
                final TextView tv_pengyouquan = (TextView) contentView.findViewById(R.id.dialog_share_pengyouquan);
                Button btn_cancel = (Button) contentView.findViewById(R.id.dialog_share_cancel);
                tv_wx.setClickable(false);
                tv_pengyouquan.setClickable(false);
                //获取分享信息
                Call<ShareInfoBean> shareInfoCall = ReaderRetroift.getInstance(FBReader.this).getApi().shareInfoCall(bookid, "book");
                shareInfoCall.enqueue(new Callback<ShareInfoBean>() {
                    @Override
                    public void onResponse(Call<ShareInfoBean> call, Response<ShareInfoBean> response) {
                        ShareInfoBean bean = response.body();
                        if (bean != null) {
                            shareinfo = bean.getResponseData();
                            web = new UMWeb(shareinfo.getUrl());
                            web.setTitle(shareinfo.getTitle());//标题
                            web.setThumb(new UMImage(FBReader.this, ReaderRetroift.IMAGE_URL + shareinfo.getImagePath()));  //缩略图
                            web.setDescription(shareinfo.getSummary());//描述
                            tv_wx.setClickable(true);
                            tv_pengyouquan.setClickable(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<ShareInfoBean> call, Throwable t) {

                    }
                });

                tv_wx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //分享微信好友
                        new ShareAction(FBReader.this)
                                .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                                .withMedia(web)
                                .setCallback(shareListener)//回调监听器
                                .share();
                    }
                });
                tv_pengyouquan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //分享到盆友圈
                        new ShareAction(FBReader.this)
                                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                                .withMedia(web)
                                .setCallback(shareListener)//回调监听器
                                .share();
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomDialog.dismiss();
                    }
                });
                bottomDialog.setContentView(contentView);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
                params.width = getResources().getDisplayMetrics().widthPixels;
                contentView.setLayoutParams(params);

                bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
                bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
                bottomDialog.show();
            }
        });
        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final LinearLayout ll_progress = (LinearLayout) contentView.findViewById(R.id.reader_ll_progress);
        final LinearLayout ll_light = (LinearLayout) contentView.findViewById(R.id.reader_ll_light);
        final LinearLayout ll_font = (LinearLayout) contentView.findViewById(R.id.reader_ll_font);
        RadioGroup radiogroup = (RadioGroup) contentView.findViewById(R.id.reader_RadioGroup);
        RadioButton rb_progress = (RadioButton) contentView.findViewById(R.id.reader_rb_progress);
        RadioButton rb_light = (RadioButton) contentView.findViewById(R.id.reader_rb_light);
        RadioButton rb_font = (RadioButton) contentView.findViewById(R.id.reader_rb_font);
        sb_light = (BubbleSeekBar) contentView.findViewById(R.id.reader_sb_light);
        sb_font = (BubbleSeekBar) contentView.findViewById(R.id.reader_sb_font);
        sb_progress = (BubbleSeekBar) contentView.findViewById(R.id.reader_sb_progress);
        bg_white = (ImageView) contentView.findViewById(R.id.reader_iv_bg_white);
        bg_wink = (ImageView) contentView.findViewById(R.id.reader_iv_bg_wink);
        bg_black = (ImageView) contentView.findViewById(R.id.reader_iv_bg_black);
        final ImageView catalog = (ImageView) contentView.findViewById(R.id.reader_catalog);

        catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示侧滑菜单
                showcatalog();
            }
        });


        //设置亮度
        settingLight();
        //设置背景和字体颜色
        settingBg();
        //设置字体大小
        settingSize();
        //设置进度
        settingProgress();

        rb_progress.setChecked(true);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.reader_rb_progress) {
                    ll_progress.setVisibility(View.VISIBLE);
                    ll_light.setVisibility(View.GONE);
                    ll_font.setVisibility(View.GONE);
                } else if (checkedId == R.id.reader_rb_light) {
                    ll_progress.setVisibility(View.GONE);
                    ll_light.setVisibility(View.VISIBLE);
                    ll_font.setVisibility(View.GONE);
                } else if (checkedId == R.id.reader_rb_font) {
                    ll_progress.setVisibility(View.GONE);
                    ll_light.setVisibility(View.GONE);
                    ll_font.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    //显示左滑菜单
    private void showcatalog() {

        int mScreenWidth = DensityUtil.getScreenWidthAndHeight(this)[0];
        myCollection = (BookCollectionShadow) myFBReaderApp.Collection;
        //左滑菜单的布局
        View menuViewLeft = LayoutInflater.from(this).inflate(R.layout.view_reader_menu, null);
        final XCSlideView mSlideViewLeft = XCSlideView.create(this, XCSlideView.Position.LEFT);
        mSlideViewLeft.setMenuView(this, menuViewLeft);
        mSlideViewLeft.setMenuWidth(mScreenWidth * 7 / 9);
        if (!mSlideViewLeft.isShow()) {
            mSlideViewLeft.show();
            mSlideViewTop.dismiss();
            mSlideViewBottom.dismiss();
        }
        FrameLayout menu_container = (FrameLayout) menuViewLeft.findViewById(R.id.menu_container);
        RadioGroup tab_container = (RadioGroup) menuViewLeft.findViewById(R.id.tab_menu_container);
        RadioButton menu_catalog = (RadioButton) menuViewLeft.findViewById(R.id.menu_catalog);
        RadioButton menu_bookmark = (RadioButton) menuViewLeft.findViewById(R.id.menu_bookmark);
        RadioButton menu_note = (RadioButton) menuViewLeft.findViewById(R.id.menu_note);
        TextView book_name = (TextView) menuViewLeft.findViewById(R.id.menu_book_name);
        TextView book_author = (TextView) menuViewLeft.findViewById(R.id.menu_book_author);
        if (bookdetailbean != null) {
            book_name.setText(bookdetailbean.getName());
            book_author.setText(bookdetailbean.getAuthor());
        } else if (bs_bookdetailbean != null) {
            book_name.setText(bs_bookdetailbean.getBookInfo().getName());
            book_author.setText(bs_bookdetailbean.getBookInfo().getAuthor());
        } else if (list_bookdetailbean != null) {
            book_name.setText(list_bookdetailbean.getName());
            book_author.setText(list_bookdetailbean.getAuthor());
        } else if (challenge_bookdetailbean != null) {
            book_name.setText(challenge_bookdetailbean.getName());
            book_author.setText(challenge_bookdetailbean.getAuthor());
        }

        final LinearLayout ll_catalog = (LinearLayout) menuViewLeft.findViewById(R.id.menu_ll_catalog);
        final LinearLayout ll_bookmark = (LinearLayout) menuViewLeft.findViewById(R.id.menu_ll_bookmark);
        final LinearLayout ll_note = (LinearLayout) menuViewLeft.findViewById(R.id.menu_ll_note);
        ListView lv_catalog = (ListView) menuViewLeft.findViewById(R.id.menu_catalog_listview);
        ListView lv_bookmark = (ListView) menuViewLeft.findViewById(R.id.menu_bookmark_listview);
        ListView lv_note = (ListView) menuViewLeft.findViewById(R.id.menu_note_listview);

        //左侧菜单栏切换
        menu_catalog.setChecked(true);
        tab_container.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.menu_catalog:
                        ll_catalog.setVisibility(View.VISIBLE);
                        ll_bookmark.setVisibility(View.GONE);
                        ll_note.setVisibility(View.GONE);
                        break;
                    case R.id.menu_bookmark:
                        ll_catalog.setVisibility(View.GONE);
                        ll_bookmark.setVisibility(View.VISIBLE);
                        ll_note.setVisibility(View.GONE);
                        break;
                    case R.id.menu_note:
                        ll_catalog.setVisibility(View.GONE);
                        ll_bookmark.setVisibility(View.GONE);
                        ll_note.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        TOCTree currentTOCElement = myFBReaderApp.getCurrentTOCElement();

        if (myFBReaderApp != null && myFBReaderApp.Model != null && myFBReaderApp.Model.TOCTree != null) {

            TOCTree root = myFBReaderApp.Model.TOCTree;
            final List<TOCTree> subtrees = root.subtrees();
            CatalogListAdapter catalogAdapter = new CatalogListAdapter(this, subtrees, currentTOCElement);
            lv_catalog.setAdapter(catalogAdapter);
            lv_catalog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    myFBReaderApp.BookTextView.gotoPosition(subtrees.get(position).getReference().ParagraphIndex, 0, 0);
                    myFBReaderApp.clearTextCaches();
                    myFBReaderApp.getViewWidget().repaint();
                    mSlideViewLeft.dismiss();
                }
            });
        }
        //获取所有书签
        List<Bookmark> thisBookBookmarks = myCollection.bookmarks(new BookmarkQuery(myFBReaderApp.getCurrentBook(), 50));
        //thisBookBookmarks将其中笔记和书签分开
        final List<Bookmark> bookmarkList = new ArrayList<>();
        final List<Bookmark> noteList = new ArrayList<>();
        for (Bookmark bookmark : thisBookBookmarks) {
            if (bookmark.getText().toString().startsWith("#")) {
                noteList.add(bookmark);
            } else {
                bookmarkList.add(bookmark);
            }
        }
        lv_bookmark.setAdapter(new BookmarkListAdapter(this, bookmarkList));
        lv_note.setAdapter(new ReaderNoteListAdapter(this, noteList));

        lv_bookmark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSlideViewLeft.dismiss();
                Bookmark bookmark = bookmarkList.get(position);
                gotoBookmark(bookmark);
            }
        });
        lv_note.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSlideViewLeft.dismiss();
                Bookmark bookmark = noteList.get(position);
                gotoBookmark(bookmark);
            }
        });


    }

    //设置进度
    private void settingProgress() {
        final ZLTextView.PagePosition pagePosition = myFBReaderApp.getTextView().pagePosition();
        sb_progress.getConfigBuilder()
                .min(0.0f)
                .max(pagePosition.Total)
                .progress(pagePosition.Current)
                .build();

        sb_progress.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                myFBReaderApp.getTextView().gotoPage(progress);
                myFBReaderApp.clearTextCaches();
                myFBReaderApp.getViewWidget().repaint();
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
    }

    //设置字体大小
    private void settingSize() {
        ZLIntegerRangeOption option =
                myFBReaderApp.ViewOptions.getTextStyleCollection().getBaseStyle().FontSizeOption;
        sb_font.setProgress(DensityUtil.px2dip(FBReader.this, option.getValue()));
        sb_font.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                myFBReaderApp.ViewOptions.getTextStyleCollection().getBaseStyle().FontSizeOption.setValue(DensityUtil.dip2px(FBReader.this, progress));
                myFBReaderApp.clearTextCaches();
                getViewWidget().repaint();
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {
                SPUtils.putFloat(FBReader.this,"fontSize",  progressFloat);
            }
        });
    }

    //背景和字体颜色设置
    private void settingBg() {
        bg_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageTurningOptions pageTurningOptions = new PageTurningOptions();
                myFBReaderApp.PageTurningOptions.Animation.setValue(ZLView.Animation.slideOldStyle);
                myFBReaderApp.ViewOptions.ColorProfileName.setValue(ColorProfile.NIGHT);
                myFBReaderApp.ViewOptions.getColorProfile().BackgroundOption.setValue(new ZLColor(255,255,255));
                myFBReaderApp.ViewOptions.getColorProfile().RegularTextOption.setValue(new ZLColor(0, 0, 0));
                SPUtils.putIntArray(FBReader.this,"backgroundcolorArray",new int[]{255,255,255});
                SPUtils.putIntArray(FBReader.this,"textcolorArray",new int[]{0,0,0});
                myFBReaderApp.clearTextCaches();
                getViewWidget().repaint();
            }
        });
        bg_wink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFBReaderApp.ViewOptions.ColorProfileName.setValue(ColorProfile.NIGHT);
                myFBReaderApp.ViewOptions.getColorProfile().BackgroundOption.setValue(new ZLColor(247, 240, 226));
                myFBReaderApp.ViewOptions.getColorProfile().RegularTextOption.setValue(new ZLColor(0, 0, 0));
                SPUtils.putIntArray(FBReader.this,"backgroundcolorArray",new int[]{247,240,226});
                SPUtils.putIntArray(FBReader.this,"textcolorArray",new int[]{0,0,0});
                myFBReaderApp.clearTextCaches();
                getViewWidget().repaint();
            }
        });
        bg_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFBReaderApp.ViewOptions.ColorProfileName.setValue(ColorProfile.NIGHT);
                myFBReaderApp.ViewOptions.getColorProfile().BackgroundOption.setValue(new ZLColor(0, 0, 0));
                myFBReaderApp.ViewOptions.getColorProfile().RegularTextOption.setValue(new ZLColor(136, 136, 136));
                SPUtils.putIntArray(FBReader.this,"backgroundcolorArray",new int[]{0,0,0});
                SPUtils.putIntArray(FBReader.this,"textcolorArray",new int[]{136,136,136});
                myFBReaderApp.clearTextCaches();
                getViewWidget().repaint();
            }
        });

    }

    //设置亮度
    private void settingLight() {
        //获取当前亮度
        int screenBrightness = LightUtils.getScreenBrightness(this);
        sb_light.setProgress(screenBrightness);

        sb_light.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                if (progress < 10) {
                } else {
                    LightUtils.setBrightness(FBReader.this, progress);
                }
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                //保存亮度
                SPUtils.putInt(FBReader.this,"readLight",progress);
                Log.d(TAG, "getProgressOnActionUp: "+progress);
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {
                Log.d(TAG, "getProgressOnFinally: "+progress);
            }
        });
    }

    //书签跳转
    private void gotoBookmark(Bookmark bookmark) {
        bookmark.markAsAccessed();
        myCollection.saveBookmark(bookmark);
        final Book book = myCollection.getBookById(bookmark.BookId);
        if (book != null) {
            FBReader.openBookActivity(this, book, bookmark);
        } else {
            UIMessageUtil.showErrorMessage(this, "cannotOpenBook");
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(FBReader.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(FBReader.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(FBReader.this, "分享取消", Toast.LENGTH_LONG).show();

        }
    };
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                return true;
//
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


}
