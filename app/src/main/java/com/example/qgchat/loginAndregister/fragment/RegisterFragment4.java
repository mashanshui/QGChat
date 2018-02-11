package com.example.qgchat.loginAndregister.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.qgchat.activity.BaseActivity;
import com.example.qgchat.R;
import com.example.qgchat.bean.StatusResponse;
import com.example.qgchat.listener.PermissionListener;
import com.example.qgchat.loginAndregister.AtyRegister;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.view.StateButton;
import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment4 extends Fragment {
    private static final String TAG = "RegisterFragment4";
    private static final int REQUEST_CODE_CHOOSE = 23;

    Unbinder unbinder;
    @BindView(R.id.btn_register)
    StateButton btnRegister;
    @BindView(R.id.icon_preview)
    ImageView iconPreview;
    private Uri imageUri;
    public static final int REQUEST_SUCCESS = 0;
    public static final int REQUEST_FAIL = 1;

    public RegisterFragment4() {
        // Required empty public constructor
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REQUEST_SUCCESS) {
                ((AtyRegister) getActivity()).dismissBufferDialog();
                Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new Icon(true));
            } else if (msg.what == REQUEST_FAIL) {
                ((AtyRegister) getActivity()).dismissBufferDialog();
                Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.register_fragment4, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.icon_preview, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.icon_preview:
                BaseActivity.requestRuntimePermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                    @Override
                    public void onGranted() {
                        Matisse.from(RegisterFragment4.this)
                                .choose(MimeType.ofAll(), false)
                                .maxSelectable(1)
                                .countable(true)
                                .capture(true)
                                .captureStrategy(
                                        new CaptureStrategy(true, "com.example.qgchat.fileprovider"))
                                .maxSelectable(9)
                                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                .gridExpectedSize(
                                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                .thumbnailScale(0.85f)
                                .imageEngine(new GlideEngine())
                                .forResult(REQUEST_CODE_CHOOSE);
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                    }
                });
                break;
            case R.id.btn_register:
                uploadIcon();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            beginCrop(Matisse.obtainResult(data).get(0));
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void beginCrop(Uri source) {
        File file = new File(getActivity().getExternalFilesDir(null), "icon.jpg");
        if (file.exists()) {
            file.delete();
        }
        Uri destination = Uri.fromFile(file);
        Crop.of(source, destination).asSquare().withMaxSize(300, 300).start(getContext(), RegisterFragment4.this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            imageUri = Crop.getOutput(result);
            /**
             * 使用gilde加载图片时要设置禁止内存缓存和禁止磁盘缓存，否则再次选择图片时会加载原来缓存的图片
             */
            Glide.with(getContext()).load(imageUri).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iconPreview);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadIcon() {
        String filePath = imageUri.getEncodedPath();
        String imagePath = Uri.decode(filePath);
        Map<String, String> param = new HashMap<>();
        String filename = ((AtyRegister) getActivity()).phoneNumber;
        param.put("account", filename);
        ((AtyRegister) getActivity()).showBufferDialog();
        HttpUtil.uploadImage(HttpUtil.uploadImageURL, param, imagePath, filename, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = REQUEST_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Message message = new Message();
                String responseData=response.body().string();
                Gson gson=new Gson();
                StatusResponse statusResponse=gson.fromJson(responseData,StatusResponse.class);
                if (statusResponse.getCode().equals("200")) {
                    message.what = REQUEST_SUCCESS;
                    handler.sendMessage(message);
                } else if (statusResponse.getCode().equals("400")) {
                    message.what = REQUEST_FAIL;
                    handler.sendMessage(message);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public class Icon {
        private boolean isUpload;

        public boolean isUpload() {
            return isUpload;
        }

        public void setUpload(boolean upload) {
            isUpload = upload;
        }

        public Icon(boolean isUpload) {
            this.isUpload = isUpload;
        }
    }
}
