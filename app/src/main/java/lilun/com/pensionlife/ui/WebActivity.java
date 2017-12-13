package lilun.com.pensionlife.ui;

import android.content.Context;
import android.os.Build;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseActivity;
import lilun.com.pensionlife.ui.home.HomeFragment;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 网页详情
 */
public class WebActivity extends BaseActivity {


    private NormalTitleBar titleBar;
    private WebView wvH5;
    private String url;
    private String title;


    @Override
    protected void getTransferData() {
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_detail;
    }

    @Override
    protected void initView() {
        titleBar = (NormalTitleBar) findViewById(R.id.titleBar);
        titleBar.setOnBackClickListener(this::finish);
        titleBar.setTitle(title);
        wvH5 = (WebView) findViewById(R.id.wv_h5);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        showH5();
    }

    private void showH5() {
        wvH5.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
        wvH5.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                }
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        wvH5.getSettings().setJavaScriptEnabled(true);
        wvH5.addJavascriptInterface(new JavaScriptinterface(this), "android");
        wvH5.getSettings().setUseWideViewPort(true);
        wvH5.getSettings().setSupportZoom(true);
        wvH5.getSettings().setBuiltInZoomControls(true);
        wvH5.getSettings().setDisplayZoomControls(false);
        wvH5.getSettings().setLoadWithOverviewMode(true);
        wvH5.loadUrl(url);
    }

    public class JavaScriptinterface {
        Context context;

        public JavaScriptinterface(Context c) {
            context = c;
        }

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        public void callAndroid() {
            Toast.makeText(context, "显示地址", Toast.LENGTH_LONG).show();
        }
    }

}
