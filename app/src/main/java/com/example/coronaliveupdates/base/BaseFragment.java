package com.example.coronaliveupdates.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.example.coronaliveupdates.api.ApiClient;
import com.example.coronaliveupdates.api.ApiService;
import com.example.coronaliveupdates.api.Const;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public abstract class BaseFragment extends Fragment {

    public ApiService apiService;
    private ProgressDialog progressDialog;
    ViewDataBinding bingObj;

    protected abstract int getFragmentLayout();

    protected abstract void initViewsHere();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bingObj = DataBindingUtil.inflate(inflater, getFragmentLayout(), container, false);
        return bingObj.getRoot();
    }

    public ViewDataBinding getBindObj() {
        return bingObj;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = ApiClient.getClient().create(ApiService.class);
        initViewsHere();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void showProgressDialog(String message, boolean isCancelabe) {
        try {
            if (progressDialog == null) {
                progressDialog = Const.getProgressDialog(getActivity(), message, isCancelabe);
            }
            progressDialog.show();
        } catch (Exception e) {
        }
    }

    protected void hideDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

    protected boolean isNetworkAvailable(Context context) {
        return Const.isNetworkAvailable(context);
    }

    protected void openActivity(Class destination) {
        startActivity(new Intent(getActivity(), destination));
    }

    protected void openActivity(Class destination, String data) {
        Intent intent = new Intent(getActivity(), destination);
        if (!TextUtils.isEmpty(data)) {
            intent.putExtra(Const.DATA, data);
        }
        startActivity(intent);
    }

    public <T> T fromJsonToGsonModel(String json, Type type) {
        return new Gson().fromJson(json, type);
    }

    public void onGoBack() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
    }

   /* public LoginModel.Userdetail getLocalUser() {
        sessionManager = new SessionManager(getActivity());
        return fromJsonToGsonModel(sessionManager.getStoredData(SessionManager.KEY_UserLoginData), new TypeToken<LoginModel.Userdetail>() {
        }.getType());
    }*/

    @Override
    public void onStop() {
        super.onStop();
        hideKeyboard();
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().findViewById(android.R.id.content).getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    protected void openActivityWithClearTask(Class destination) {
        Intent intent = new Intent(getActivity(), destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}