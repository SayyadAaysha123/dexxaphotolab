package com.dexxaphotolab.app.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dexxaphotolab.app.Activities.ActivityPreview;
import com.dexxaphotolab.app.StickerClasses.DrawableSticker;
import com.dexxaphotolab.app.StickerClasses.Sticker;
import com.dexxaphotolab.app.StickerClasses.StickerView;
import com.dexxaphotolab.app.StickerClasses.TextSticker;
import com.dexxaphotolab.app.Adapters.AdapterBackgroundImage;
import com.dexxaphotolab.app.Adapters.AdapterFontList;
import com.dexxaphotolab.app.Adapters.AdapterFrames;
import com.dexxaphotolab.app.Adapters.AdapterTextColourPicker;
import com.dexxaphotolab.app.Model.ModelBackgroundImage;
import com.dexxaphotolab.app.Model.ModelColorList;
import com.dexxaphotolab.app.Model.ModelFontDetail;
import com.dexxaphotolab.app.Model.ModelFramesDetails;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Constance;
import com.dexxaphotolab.app.Utills.PaletteBar;
import com.dexxaphotolab.app.Utills.SharedPrefrenceConfig;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import kotlin.jvm.internal.Intrinsics;


public class CreateCustomImageFragment extends Fragment {

    View view;
    Context context;
    ImageView iv_customimage;
    TextSticker txtsticker;
    int textStickerColor = R.color.colorBlack;
    EditText et_usertext, et_text_sticker;
    public AdapterFrames adapterFrame;
    public ArrayList<ModelFontDetail> modelFontDetailArrayList;
    Bitmap bitmapsave;
    int textcolor, fontcolor;
    StickerView sticker_view;
    LinearLayout ll_content, ll_main_custom, ll_next;
    int seekvalue;
    Float dx, dy;
    RelativeLayout opacitybg, rlBackgroundColor, rlOverlay, rl_addlogo,
            rl_AddText, rl_fontcolor, rl_Fontstyle, rlTextSize, rlBackgroundImageLocal,
            rl_addimage;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static CreateCustomImageFragment instance = null;
    PopupWindow mPopupWindow, mPopupWindowpw;

    public CreateCustomImageFragment() {
        instance = CreateCustomImageFragment.this;
    }

    public static synchronized CreateCustomImageFragment getInstance() {
        if (instance == null) {
            instance = new CreateCustomImageFragment();
        }
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_custom_image, container, false);

        context = getContext();
        bindView();


        sharedPrefrenceConfig = new SharedPrefrenceConfig(context);
        calculationForHeight();

        Constance.isStickerAvail = false;
        if (!Constance.isStickerAvail) {
            Constance.isStickerTouch = false;
            sticker_view.setLocked(true);
        }
        touchListener(ll_content);

        modelFontDetailArrayList = new ArrayList<>();

        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeImg = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getString(R.string.app_name) + "/" + timeImg;

                String filename=path.substring(path.lastIndexOf("/")+1);

                Log.e("1212", "onClick: " + filename );

                sticker_view.hideIcons(true);
                bitmapsave = viewToBitmap(ll_content);
                Constance.createdBitmap = bitmapsave;

                try {
                    saveImage(context,bitmapsave,getString(R.string.app_name),filename+".png");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent save = new Intent(context, ActivityPreview.class);
                save.putExtra("name", "image");
                save.putExtra("fileName", filename);
                save.putExtra("status", 1);
                startActivity(save);

            }
        });

        rlBackgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDailogForBackgroundcolour();
            }
        });

        rlOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDailogForOverlayBg();
            }
        });

        rl_addlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        rl_fontcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textStickerColorPopUp("font");
            }
        });

        rl_Fontstyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDailogForFontStyle();
            }
        });
        rlTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDailogfForFontSize();
            }
        });

        rlBackgroundImageLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDailogForLocalBackgroundImage();
            }
        });

        rl_addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageFromMobileGallery();
            }
        });

        rl_AddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_text_sticker.setVisibility(View.VISIBLE);
                et_text_sticker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        et_text_sticker.post(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                                imm.showSoftInput(et_text_sticker, InputMethodManager.SHOW_IMPLICIT);
                            }
                        });
                    }
                });
                et_text_sticker.requestFocus();
                et_text_sticker.getText().clear();
                et_text_sticker.setTextColor(getResources().getColor(R.color.hint));
                et_text_sticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + "Acme.ttf"));
                et_text_sticker.setHint("your text for sticker");

            }
        });
        et_text_sticker.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                      if (et_text_sticker.getText().toString().equals("") || et_text_sticker.getText().toString().equals(null)) {
                            } else {
                                et_text_sticker.setVisibility(View.GONE);
                                txtsticker = new TextSticker(context);

                                et_text_sticker.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(et_text_sticker.getWindowToken(), 0);
                                    }
                                });

                                txtsticker.setText("");
                                txtsticker.getText();
                                txtsticker.setText(et_text_sticker.getText().toString());
                                txtsticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + Constance.FontStyle));
                                txtsticker.setTextColor(getResources().getColor(textStickerColor));
                                textStickerColor = R.color.colorBlack;
                                txtsticker.resizeText();
                                sticker_view.addSticker(txtsticker);
                                Constance.isStickerAvail = true;
                                Constance.isStickerTouch = true;
                                sticker_view.setLocked(false);
                            }
                return false;
            }
        });
        sticker_view.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {


            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker1) {
                sticker_view.hideIcons(false);
                sticker_view.setLocked(false);
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                /*sticker_view.hideIcons(false);
                sticker_view.setLocked(false);
                et_usertext.setVisibility(View.VISIBLE);
                sticker = (TextSticker) sticker_view.getCurrentSticker();
                et_usertext.setText(((TextSticker) sticker).getText().toString());
                et_usertext.setTextColor(((TextSticker) sticker).getTextColor());
                sticker_view.remove(sticker);*/

                sticker_view.hideIcons(false);
                sticker_view.setLocked(false);
                et_text_sticker.setVisibility(View.VISIBLE);
                sticker = sticker_view.getCurrentSticker();
                et_text_sticker.setText(((TextSticker) sticker).getText());
                et_text_sticker.setTextColor(((TextSticker) sticker).getTextColor());
                sticker_view.remove(sticker);
            }
        });

        return view;
    }

    private Uri saveImage(Context context, Bitmap bitmap, @NonNull String folderName, @NonNull String fileName) throws IOException {
        OutputStream fos = null;
        File imageFile = null;
        File imagesDir;
        Uri imageUri = null;
        File imagesDir1 = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "girl");
        imagesDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() + File.separator + folderName);

        if (!imagesDir.exists())
            imagesDir.mkdir();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/*");
                contentValues.put(
                        MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + folderName);
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);


                if (imageUri == null)
                    throw new IOException("Failed to create new MediaStore record.");

                fos = resolver.openOutputStream(imageUri);
            } else {
                imagesDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString() + File.separator + folderName);

                if (!imagesDir.exists())
                    imagesDir.mkdir();

                imageFile = new File(imagesDir, fileName);
                fos = new FileOutputStream(imageFile);
            }


            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos))
                throw new IOException("Failed to save bitmap.");
            fos.flush();
        } finally {
            if (fos != null)
                fos.close();
        }


        return imageUri;
    }


    public void bindView() {

        iv_customimage = view.findViewById(R.id.iv_customimage);
        et_usertext = view.findViewById(R.id.et_usertext);
        et_usertext.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sticker_view = view.findViewById(R.id.sticker_view);
        ll_content = view.findViewById(R.id.ll_content);
        opacitybg = view.findViewById(R.id.opacitybg);
        et_text_sticker = view.findViewById(R.id.et_text_sticker);
        et_text_sticker.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ll_main_custom = view.findViewById(R.id.ll_main_custom);
        ll_next = view.findViewById(R.id.ll_next);
        rlBackgroundColor = view.findViewById(R.id.rlBackgroundColor);
        rlOverlay = view.findViewById(R.id.rlOverlay);
        rl_addlogo = view.findViewById(R.id.rl_addlogo);
        rl_AddText = view.findViewById(R.id.rl_AddText);
        rl_fontcolor = view.findViewById(R.id.rl_fontcolor);
        rl_Fontstyle = view.findViewById(R.id.rl_Fontstyle);
        rlTextSize = view.findViewById(R.id.rlTextSize);
        rlBackgroundImageLocal = view.findViewById(R.id.rlBackgroundImageLocal);
        rl_addimage = view.findViewById(R.id.rl_addimage);
    }

    public void onclickCustomFrame(View view1) {
        switch (view1.getId()) {
            case R.id.iv_backarrow:
                //onBackPressed();
                break;
            case R.id.ll_next:
                sticker_view.hideIcons(true);
                bitmapsave = viewToBitmap(ll_content);
                Constance.createdBitmap = bitmapsave;
                Intent save = new Intent(context, ActivityPreview.class);
                save.putExtra("name", "image");
                startActivity(save);

                break;
            case R.id.rlBackgroundColor:
                openDailogForBackgroundcolour();
                break;
            case R.id.rlOverlay:
                openDailogForOverlayBg();
                break;
            case R.id.rl_addlogo:
                openGallery();
                break;
            case R.id.rl_AddText:
                et_text_sticker.setVisibility(View.VISIBLE);
                et_text_sticker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        et_text_sticker.post(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                                imm.showSoftInput(et_text_sticker, InputMethodManager.SHOW_IMPLICIT);
                            }
                        });
                    }
                });
                et_text_sticker.requestFocus();
                et_text_sticker.getText().clear();
                et_text_sticker.setTextColor(getResources().getColor(R.color.hint));
                et_text_sticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + "Acme.ttf"));
                et_text_sticker.setHint("your text for sticker");


                break;
            case R.id.rl_fontcolor:

                textStickerColorPopUp("font");

                break;
            case R.id.rl_Fontstyle:
                openDailogForFontStyle();

                break;
            case R.id.rlTextSize:
                openDailogfForFontSize();

                break;
            case R.id.rlBackgroundImageLocal:
                OpenDailogForLocalBackgroundImage();

                break;
            case R.id.rl_addimage:
                PickImageFromMobileGallery();
                break;

        }
    }

    public void onclickCustomLayout(View view) {
        switch (view.getId()) {

        }
    }


    public void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    public void openDailogForFontColor() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_bg_color);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        final TextView tvOpacity = dialog.findViewById(R.id.tvOpacity);
        TextView tv_dailog_tittle = dialog.findViewById(R.id.tv_dailog_tittle);
        final SeekBar sbOpacity = dialog.findViewById(R.id.sbOpacity);
        RecyclerView rv_bg_color = dialog.findViewById(R.id.rv_bg_color);

        tv_dailog_tittle.setText("Change Font Colour");

        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 7);
        rv_bg_color.setLayoutManager(linearLayoutManager);
        AdapterTextColourPicker adapterTextColourPicker = new AdapterTextColourPicker(context, getColorList(), "fontcolor");
        rv_bg_color.setAdapter(adapterTextColourPicker);

        sbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = Math.round(progress / 5) * 5;
                et_usertext.setAlpha((Float.valueOf(progress) / Float.valueOf(100)));
                tvOpacity.setText(progress + "%");


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setFontColor(int colour) {
        et_usertext.setTextColor(context.getColor(colour));
    }

    public void openDailogForFontStyle() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_fontstyle);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        Button btnOk = dialog.findViewById(R.id.btnOk);

        RecyclerView rvList = dialog.findViewById(R.id.rv_font_style);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvList.setLayoutManager(linearLayoutManager);

        AdapterFontList adapter = new AdapterFontList(context, getfontList(), "fontstyle");
        rvList.setAdapter(adapter);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void setFontStyle(String fontName) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);

        et_usertext.setTypeface(font);
    }

    public void openDailogfForFontSize() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_fontsize);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        final SeekBar sbopacity_fontsize = dialog.findViewById(R.id.sbOpacity_fontsize);
        final TextView tvOpacity = dialog.findViewById(R.id.tvOpacity);

        sbopacity_fontsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekvalue = progress;
                tvOpacity.setText(progress + "%");
                StickerView stickerView2 = (StickerView) view.findViewById(R.id.sticker_view);
                Intrinsics.checkExpressionValueIsNotNull(stickerView2, "stickerView");
                Sticker currentSticker = stickerView2.getCurrentSticker();
                if (currentSticker != null) {
                    TextSticker text = (TextSticker) currentSticker;
                    text.setMaxTextSize(seekvalue);
                    text.resizeText();
                    ((StickerView) view.findViewById(R.id.sticker_view)).invalidate();

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

//                et_text_sticker.setTextSize(seekvalue);
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("abxzxc", "createquote");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Log.d("abxzxc", "createquote--1");

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                iv_customimage.setImageURI(resultUri);
                Log.d("abxzxc", "createquote--2" + resultUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("abxzxc", "createquote--3" + error.getMessage());

            }
        }
        if (resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            if (photoUri != null) {
                try {
                    Bitmap currentImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
                    Drawable d = new BitmapDrawable(getResources(), currentImage);
                    DrawableSticker drawableSticker = new DrawableSticker(d);
                    sticker_view.addSticker(drawableSticker);
                    Constance.isStickerAvail = true;
                    Constance.isStickerTouch = true;
                    sticker_view.setLocked(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public static ArrayList<ModelColorList> getColorList() {
        ArrayList<ModelColorList> data = new ArrayList<>();
        data.add(new ModelColorList(R.color.color1));
        data.add(new ModelColorList(R.color.colorWhite));
        data.add(new ModelColorList(R.color.color6));
        data.add(new ModelColorList(R.color.color7));
        data.add(new ModelColorList(R.color.color3));
        data.add(new ModelColorList(R.color.color12));
        data.add(new ModelColorList(R.color.colorBlack));
        data.add(new ModelColorList(R.color.color2));
        data.add(new ModelColorList(R.color.color4));
        data.add(new ModelColorList(R.color.color5));
        data.add(new ModelColorList(R.color.color9));
        data.add(new ModelColorList(R.color.color8));
        data.add(new ModelColorList(R.color.color10));
        data.add(new ModelColorList(R.color.color11));


        return data;
    }


    public void openDailogForBackgroundcolour() {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_bg_color);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        final TextView tvOpacity = dialog.findViewById(R.id.tvOpacity);
        final SeekBar sbOpacity = dialog.findViewById(R.id.sbOpacity);
        RecyclerView rv_bg_color = dialog.findViewById(R.id.rv_bg_color);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 7);
        rv_bg_color.setLayoutManager(linearLayoutManager);


        AdapterTextColourPicker adapterTextColourPicker = new AdapterTextColourPicker(context, getColorList(), "bgcolor");
        rv_bg_color.setAdapter(adapterTextColourPicker);


        sbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = Math.round(progress / 5) * 5;
                iv_customimage.setAlpha((Float.valueOf(progress) / Float.valueOf(100)));
                tvOpacity.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void PickImageFromMobileGallery() {
        Constance.checkFragment = "CreateCustom";
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start((Activity) context);

    }


    public void OpenDailogForLocalBackgroundImage() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_bg_local_image);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);

        Button btnok = dialog.findViewById(R.id.btnOk);
        RecyclerView rl_bglocalimage = dialog.findViewById(R.id.rl_bglocalimage);

        rl_bglocalimage.setLayoutManager(new GridLayoutManager(context, 2));
        AdapterBackgroundImage adapterBackgroundImage = new AdapterBackgroundImage(context, getLocalImageList(), "customimage");
        rl_bglocalimage.setAdapter(adapterBackgroundImage);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    public static ArrayList<ModelBackgroundImage> getLocalImageList() {
        ArrayList<ModelBackgroundImage> data = new ArrayList<>();
        data.add(new ModelBackgroundImage(R.drawable.img_1));
        data.add(new ModelBackgroundImage(R.drawable.img_2));
        data.add(new ModelBackgroundImage(R.drawable.img_3));
        data.add(new ModelBackgroundImage(R.drawable.pager_1));
        data.add(new ModelBackgroundImage(R.drawable.pager_2));

        return data;
    }

    public static ArrayList<ModelFramesDetails> getFramesList() {
        ArrayList<ModelFramesDetails> data = new ArrayList<>();
        data.add(new ModelFramesDetails(R.drawable.f1));
        data.add(new ModelFramesDetails(R.drawable.f2));
        data.add(new ModelFramesDetails(R.drawable.f3));
        data.add(new ModelFramesDetails(R.drawable.f4));
        data.add(new ModelFramesDetails(R.drawable.f5));
        data.add(new ModelFramesDetails(R.drawable.f6));
        data.add(new ModelFramesDetails(R.drawable.f7));
        data.add(new ModelFramesDetails(R.drawable.f8));

        return data;
    }

    public void setbackgroundLocalImage(int image) {
        iv_customimage.setImageResource(image);
    }


    public void openDailogForOverlayBg() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_bg_color);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);

        final Button btnOk = dialog.findViewById(R.id.btnOk);
        final SeekBar sbOpacity = dialog.findViewById(R.id.sbOpacity);
        final RecyclerView rv_bg_color = dialog.findViewById(R.id.rv_bg_color);
        final TextView tvOpacity = dialog.findViewById(R.id.tvOpacity);
        final TextView tv_dailog_tittle = dialog.findViewById(R.id.tv_dailog_tittle);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 7);
        rv_bg_color.setLayoutManager(linearLayoutManager);
        AdapterTextColourPicker adapterTextColourPicker = new AdapterTextColourPicker(context, getColorList(), "overlay_custom");
        rv_bg_color.setAdapter(adapterTextColourPicker);
        tv_dailog_tittle.setText("Overlay");

        sbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                tvOpacity.setText(progress + "%");
                opacitybg.setAlpha((Float.valueOf(progress) / Float.valueOf(100)));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbOpacity.setProgress(20);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void setOverlayBackground(int colour) {
        opacitybg.setBackgroundResource(colour);
    }

    public static ArrayList<ModelFontDetail> getfontList() {
        ArrayList<ModelFontDetail> data = new ArrayList<>();
        String[] fontnamelist = new String[]
                {"abhayalibre_bold.ttf", "abhayalibre_extrabold.ttf", "abhayalibre_medium.ttf", "artifika_regular.ttf", "archivo_black.ttf",
                        "ArchivoNarrow.otf", "ABeeZee.otf", "After_Shok.ttf", "AbrilFatface.otf", "Acknowledgement.otf",
                        "Acme.ttf", "AlfaSlabOne.ttf", "AlmendraDisplay.otf", "Almendra.otf", "alpha_echo.ttf",
                        "Amadeus.ttf", "AMERSN.ttf", "ANUDI.ttf", "AquilineTwo.ttf", "Arbutus.ttf", "AlexBrush.ttf",
                        "Alisandra.ttf", "Allura.ttf", "Amarillo.ttf", "BEARPAW.ttf", "bigelowrules.ttf", "BLACKR.ttf",
                        "BOYCOTT.ttf", "BebasNeue.ttf", "BLKCHCRY.TTF", "Carousel.ttf", "Caslon_Calligraphic.ttf",
                        "CroissantOne.ttf", "Carnevalee-Freakshow.ttf", "CAROBTN.TTF", "CaviarDreams.ttf",
                        "Cocogoose.ttf", "diplomata.ttf", "deftone stylus.ttf", "Dosis.ttf", "FONTL.TTF",
                        "Hugtophia.ttf", "ICE_AGE.ttf", "Kingthings_Calligraphica.ttf", "Love Like This.ttf",
                        "MADE Canvas.otf", "Merci-Heart-Brush.ttf", "Metropolis.otf", "Montserrat.otf",
                        "MontserratAlternates.otf",
                        "norwester.otf", "ostrich.ttf", "squealer.ttf", "Titillium.otf", "Ubuntu.ttf"};


        for (int i = 0; i < fontnamelist.length; i++) {

            data.add(new ModelFontDetail(fontnamelist[i], fontnamelist[i]));
        }
        return data;

    }

    private void touchListener(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    dx = et_usertext.getX() - event.getRawX();
                    dy = et_usertext.getY() - event.getRawY();
                    if (Constance.isStickerAvail) {

                        if (Constance.isStickerTouch || !Constance.isStickerTouch) {
                            Constance.isStickerTouch = false;
                            sticker_view.setLocked(true);
                        }
                    }
                } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    if (Constance.isStickerAvail) {

                        if (!Constance.isStickerTouch) {
                            Constance.isStickerTouch = true;
                            sticker_view.setLocked(false);
                        }
                    }
                } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                    et_usertext.setY(event.getRawY() + dy);
                    et_usertext.setX(event.getRawX() + dx);
                    if (Constance.isStickerAvail) {
                        if (!Constance.isStickerTouch || Constance.isStickerTouch) {
                            Log.e("image move", "sticker lock");
                            Constance.isStickerTouch = false;
                            sticker_view.setLocked(true);
                        }
                    }
                }
                return true;
            }
        });
    }


    public void setbackgroundcolor(int color) {
        iv_customimage.setImageResource(color);
    }

    public void calculationForHeight() {
        ViewTreeObserver vto = ll_content.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    ll_content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    ll_content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                Constance.widthOfImage = ll_content.getMeasuredWidth();
                Constance.heightOfImage = ll_content.getMeasuredHeight();

                ViewGroup.LayoutParams params = ll_content.getLayoutParams();
                params.height = Constance.widthOfImage;
                params.width = Constance.widthOfImage;
                ll_content.setLayoutParams(params);
            }
        });

    }

    public void textStickerEditPopUp() {


        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        final View customView = inflater.inflate(R.layout.edit_text_sticker_popup, null);


        mPopupWindowpw = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


//
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindowpw.setElevation(5.0f);
        }
        mPopupWindowpw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindowpw.setOutsideTouchable(false);
        mPopupWindowpw.showAtLocation(ll_main_custom, Gravity.BOTTOM, 0, 0);


        final TextView dialogTitle = customView.findViewById(R.id.cp_accent_title);
        final LinearLayout ll_text_color = customView.findViewById(R.id.ll_text_color);
        final LinearLayout ll_text_style = customView.findViewById(R.id.ll_text_style);
        final Button btn_Ok = customView.findViewById(R.id.btn_Ok);


        ll_text_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textStickerColorPopUp("stickerText");

            }
        });


        ll_text_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTextFontStyle();

            }
        });
        dialogTitle.setText("Edit Sticker");


        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_text_sticker.getText().toString().equals("") || et_text_sticker.getText().toString().equals(null)) {
                    mPopupWindowpw.dismiss();


                } else {
                    et_text_sticker.setVisibility(View.GONE);
                    txtsticker = new TextSticker(context);
                    txtsticker.setText("");
                    et_text_sticker.post(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(et_text_sticker.getWindowToken(), 0);

                        }
                    });
                    txtsticker.setText(et_text_sticker.getText().toString());
                    txtsticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + Constance.FontStyle));
                    txtsticker.setTextColor(textcolor);
                    textStickerColor = R.color.colorBlack;
                    txtsticker.resizeText();
                    sticker_view.addSticker(txtsticker);
                    Constance.isStickerAvail = true;
                    Constance.isStickerTouch = true;
                    sticker_view.setLocked(false);
                    mPopupWindowpw.dismiss();


                }


            }


        });


    }

    public void textStickerColorPopUp(String type) {
        et_text_sticker.setTextColor(fontcolor);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.select_color_popup, null);

        PaletteBar paletteBar = customView.findViewById(R.id.paletteBar);
        paletteBar.setListener(new PaletteBar.PaletteBarListener() {
            @Override
            public void onColorSelected(int color) {
                StickerView stickerView2 = (StickerView) view.findViewById(R.id.sticker_view);
                Intrinsics.checkExpressionValueIsNotNull(stickerView2, "stickerView");
                Sticker currentSticker = stickerView2.getCurrentSticker();
                if (currentSticker != null) {
                    TextSticker text = (TextSticker) currentSticker;
                    text.setTextColor(color);
                    text.resizeText();
                    ((StickerView) view.findViewById(R.id.sticker_view)).invalidate();

                }
            }
        });

        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(false);


        final TextView dialogTitle = customView.findViewById(R.id.cp_accent_title);
        final Button btnOk = customView.findViewById(R.id.btnOk);
        final Button btnCancel = customView.findViewById(R.id.btnCancel);
/*
        final RecyclerView rvList = (RecyclerView) customView.findViewById(R.id.rvList);
*/


        dialogTitle.setText("Text Color");


      /*  GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 7);
        rvList.setLayoutManager(linearLayoutManager);*/


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();

            }
        });


        mPopupWindow.showAtLocation(ll_main_custom, Gravity.BOTTOM, 0, 0);
    }

    public void SetFontToText(String FontName) {
        et_text_sticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + FontName));
        Constance.FontStyle = FontName;

        StickerView stickerView2 = view.findViewById(R.id.sticker_view);
        Intrinsics.checkExpressionValueIsNotNull(stickerView2, "sticker_view");
        Sticker currentSticker = stickerView2.getCurrentSticker();
        TextSticker text = (TextSticker) currentSticker;
        text.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + FontName));
        text.resizeText();
        view.findViewById(R.id.sticker_view).invalidate();
    }

    public void openTextFontStyle() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_fontstyle);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        Button btnOk = dialog.findViewById(R.id.btnOk);

        RecyclerView rvList = dialog.findViewById(R.id.rv_font_style);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvList.setLayoutManager(linearLayoutManager);

        AdapterFontList adapter = new AdapterFontList(context, getfontList(), "fontforstickertext");
        rvList.setAdapter(adapter);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

}