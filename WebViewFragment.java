package com.braintreepayments.popupbridge.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.braintreepayments.api.PopupBridgeClient;

public class WebViewFragment extends Fragment {

    private static final String ARG_URL = "url";
    // MUST match the <data android:scheme="..."/> in your Manifest (MainActivity VIEW filter)
    private static final String RETURN_URL_SCHEME = "com.braintreepayments.popupbridgeexample";

    public static WebViewFragment newInstance(String url) {
        Bundle b = new Bundle();
        b.putString(ARG_URL, url);
        WebViewFragment f = new WebViewFragment();
        f.setArguments(b);
        return f;
    }

    private WebView webView;
    private PopupBridgeClient popupBridgeClient;

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        webView = new WebView(requireContext());
        webView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Basic WebView setup
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);

        // Keep navigation inside this WebView, and enable JS dialogs/popups
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        return webView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Attach Popup Bridge to this fragment's WebView
        popupBridgeClient = new PopupBridgeClient(requireActivity(), webView, RETURN_URL_SCHEME);

        // Load initial URL
        String url = requireArguments().getString(ARG_URL);
        if (url != null) {
            webView.loadUrl(url);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cleanup
        if (webView != null) {
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
        popupBridgeClient = null;
    }
}
