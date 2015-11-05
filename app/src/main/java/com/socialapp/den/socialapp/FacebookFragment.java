package com.socialapp.den.socialapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.Arrays;

/**
 * Created by Den on 02.11.15.
 */
public class FacebookFragment extends Fragment implements View.OnClickListener {

    private Button mButtonLog;
    private Button mButtonShare;

    private ImageView mImageViewPictureUser;

    private TextView mTextViewIdUser;
    private TextView mTextViewEmailUser;
    private TextView mTextViewNameUser;

    private String mUrlPictureUser;

    private ShareDialog mShareDialog;

    CallbackManager callbackManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_facebook, container, false);

        mButtonLog = (Button) view.findViewById(R.id.btnLog_FF);
        mButtonShare = (Button) view.findViewById(R.id.btnShare_FF);

        if(AccessToken.getCurrentAccessToken() != null){
            getUserInformation();
            mButtonLog.setText("Log Out");
            mButtonShare.setVisibility(View.VISIBLE);

        } else {
            mButtonShare.setVisibility(View.INVISIBLE);
            mButtonLog.setText("Log In");
        }

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("LOGIN: ", " Login success!");
                        mButtonLog.setText("Log Out");
                        getUserInformation();
                        mButtonShare.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onCancel() {
                        Log.d("LOGIN: ", " Login cancel");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("LOGIN: ", " error");

                    }
                });

        mImageViewPictureUser = (ImageView) view.findViewById(R.id.imgPictureUser_FF);

        mTextViewIdUser = (TextView) view.findViewById(R.id.tvIdUser_FF);
        mTextViewEmailUser = (TextView) view.findViewById(R.id.tvEmailUser_FF);
        mTextViewNameUser = (TextView) view.findViewById(R.id.tvNameUser_FF);

        mButtonLog.setOnClickListener(this);
        mButtonShare.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnLog_FF:
                if(AccessToken.getCurrentAccessToken() != null){
                    LoginManager.getInstance().logOut();
                    try {
                        setData("http://" ,"","","");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mButtonLog.setText("Log In");
                    mButtonShare.setVisibility(View.INVISIBLE);

                } else {
                    LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
                }
                break;
            case R.id.btnShare_FF:
                mShareDialog = new ShareDialog();
                mShareDialog.setCancelable(false);
                mShareDialog.show(getActivity().getFragmentManager(),"shareDialog");

                break;
        }


    }

    private void getUserInformation() {

        Bundle parameters = new Bundle();
        parameters.putString("fields", "picture, id, name, email");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            mUrlPictureUser = response.getJSONObject().
                                    getJSONObject("picture").
                                    getJSONObject("data").
                                    getString("url");

                            String id    = "Id: " + response.getJSONObject().getString("id");
                            String email = "email: " + response.getJSONObject().getString("email");
                            String name  = "Name: " + response.getJSONObject().getString("name");

                            setData(mUrlPictureUser, id, email, name);

                            Log.d("LOGIN:", "-" + response.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).executeAsync();
    }

    private void setData(String url, String id, String email, String name) throws JSONException {
        Picasso.with(getContext())
                .load(url)
                .into(mImageViewPictureUser);

        mTextViewIdUser.setText(id);
        mTextViewEmailUser.setText(email);
        mTextViewNameUser.setText(name);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
