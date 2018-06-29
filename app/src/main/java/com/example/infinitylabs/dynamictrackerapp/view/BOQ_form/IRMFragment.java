package com.example.infinitylabs.dynamictrackerapp.view.BOQ_form;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Toast;

import com.example.infinitylabs.dynamictrackerapp.R;
import com.example.infinitylabs.dynamictrackerapp.base_view.fragements.BaseFragment;
import com.example.infinitylabs.dynamictrackerapp.network.APIClientCallback;
import com.example.infinitylabs.dynamictrackerapp.network.APIClientUtils;
import com.example.infinitylabs.dynamictrackerapp.network.UrlConstant;
import com.example.infinitylabs.dynamictrackerapp.request_response.BOQFormData;
import com.example.infinitylabs.dynamictrackerapp.request_response.GetBOQIRMRequest;
import com.example.infinitylabs.dynamictrackerapp.request_response.GetBOQIRMResponse;
import com.example.infinitylabs.dynamictrackerapp.request_response.SubmitBOQIRMRequest;
import com.example.infinitylabs.dynamictrackerapp.request_response.SubmitBOQIRMResponse;
import com.example.infinitylabs.dynamictrackerapp.utilities.Logger;
import com.example.infinitylabs.dynamictrackerapp.utilities.SelectedTaskUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class IRMFragment extends BaseFragment  implements AbsListView.OnScrollListener {

    private static final String LOG_TAG = IRMFragment.class.getSimpleName();


    @BindView(R.id.rv)
    RecyclerView rv;
    Unbinder unbinder;
    @BindView(R.id.btn_submit_irm)
    Button btnSubmitIrm;

    private List<BOQFormData> boqFormDataList = new ArrayList<>();
    private IRMFormAdapter irmFormAdapter;

    public IRMFragment() {
        // Required empty public constructor
    }


    @Override
    public View getFragmentLayout() {
        View view = inflater.inflate(R.layout.fragment_irm, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initializeViews() {

        showLoadingView(R.id.please_wait);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void initializePresenter() {

        GetBOQIRMRequest getBOQIRMRequest = new GetBOQIRMRequest();
        getBOQIRMRequest.setSiteId(SelectedTaskUtility.getInstance().getSiteID());
        callAPI(getBOQIRMRequest, new APIClientUtils(context));


    }

    private void callAPI(GetBOQIRMRequest getBOQIRMRequest, APIClientUtils apiClientUtils) {


        try {
            apiClientUtils.getServiceResponseByPost(UrlConstant.getGetIRMform(),
                    new APIClientCallback<GetBOQIRMResponse>() {

                        @Override
                        public void onSuccess(GetBOQIRMResponse getBOQIRMResponse) {


                            hideLoadingView();

                            boqFormDataList = getBOQIRMResponse.getData();


                            if (boqFormDataList != null) {


                                irmFormAdapter = new IRMFormAdapter(boqFormDataList, context, IRMFragment.this);
                                rv.setAdapter(irmFormAdapter);
                            } else {
                            }

                        }

                        @Override
                        public void onFailure(Exception e) {
                            hideLoadingView();
                            Logger.logError(LOG_TAG, "Exception " + e);
                        }

                        @NonNull
                        @Override
                        public Class<GetBOQIRMResponse> getClassOfType() {
                            return GetBOQIRMResponse.class;
                        }

                    }, new JSONObject(apiClientUtils.getObjectMapper().writeValueAsString(getBOQIRMRequest)));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.btn_submit_irm)
    public void onViewClicked() {

        showLoadingView(R.id.please_wait);
        SubmitBOQIRMRequest submitBOQIRMRequest = new SubmitBOQIRMRequest();
        submitBOQIRMRequest.setData(irmFormAdapter.getBoqFormDataList());
        submitBOQIRMRequest.setSiteId(SelectedTaskUtility.getInstance().getSiteID());
        submitBOQIRMRequest.setFormType("irm");

        callSubmitAPI(submitBOQIRMRequest, new APIClientUtils(context));

    }

    private void callSubmitAPI(final SubmitBOQIRMRequest submitBOQIRMRequest, APIClientUtils apiClientUtils) {

        try {
            apiClientUtils.getServiceResponseByPost(UrlConstant.getSubmitBOQIRMform(),
                    new APIClientCallback<SubmitBOQIRMResponse>() {

                        @Override
                        public void onSuccess(SubmitBOQIRMResponse submitBOQIRMResponse) {


                            hideLoadingView();
                            Toast.makeText(context, submitBOQIRMResponse.getMsg(), Toast.LENGTH_LONG).show();

                            if (submitBOQIRMResponse.getStatus()) {
                                btnSubmitIrm.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                                btnSubmitIrm.setEnabled(false);
                            }


                        }

                        @Override
                        public void onFailure(Exception e) {
                            hideLoadingView();
                            Logger.logError(LOG_TAG, "Exception " + e);
                        }

                        @NonNull
                        @Override
                        public Class<SubmitBOQIRMResponse> getClassOfType() {
                            return SubmitBOQIRMResponse.class;
                        }

                    }, new JSONObject(apiClientUtils.getObjectMapper().writeValueAsString(submitBOQIRMRequest)));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // do nothing
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (SCROLL_STATE_TOUCH_SCROLL == scrollState) {
            View currentFocus = getActivity().getCurrentFocus();
            if (currentFocus != null) {
                currentFocus.clearFocus();
            }
        }
    }
}
