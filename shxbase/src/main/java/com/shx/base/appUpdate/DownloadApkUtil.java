package com.shx.base.appUpdate;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.shx.base.constant.ConstantBase;
import com.shx.base.hipermission.HiPermission;
import com.shx.base.hipermission.PermissionCallback;
import com.shx.base.hipermission.PermissionItem;
import com.shx.base.utils.AppBaseUtils;
import com.shx.base.utils.MsStringUtils;
import com.shx.base.utils.SPUtils;
import com.shx.base.utils.ShxActivityStack;
import com.shx.base.utils.ToastUtils;
import com.shx.base.utils.ViewUtils;
import com.shx.base.view.ShxProgressBar;
import com.shx.base.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadApkUtil {

    private static String tag = "shx_upadte_apk";
    private static String ApkKey = "shx_apk_success_name";
    private static DownloadTask task = null;
    //文件是否下载结束
    private static boolean isDownFinish = false;
    //下载的文件
    private static File mFile = null;
    private static Activity mActivity;
    //文件下载地址
    private static String apkUrl;
    //升级提示框
    private static AlertDialog dialog;
    //确定按钮
    private static Button btnSure;
    //进度条按钮
    private static ShxProgressBar mProgressBar;
    //下载的版本
    private static String downLoadVersion = "1.0";
    //是否强制升级
    private static boolean forceUpdate;

    /**
     * 显示升级提示框
     */
    public static void showUpdateDialog(String apkurl, String downloadversion) {
        showUpdateDialog(apkurl,downloadversion,false);
    }

    public static void showUpdateDialog(String apkurl, String downloadversion,boolean forceupdate){
        //防止快速点击
        if (ViewUtils.isFastClick(500)) {
            return;
        }

        //与本地版本比较，是否需要更新APP
        if (!compileVersion(downloadversion)) {
            return;
        }

        mActivity = ShxActivityStack.getInstance().getTopActivity();
        if (mActivity == null) {
            return;
        }

        apkUrl = apkurl;
        downLoadVersion = downloadversion;
        isDownFinish = false;
        forceUpdate = forceupdate;

        dialog = new AlertDialog.Builder(mActivity, R.style.dialog).create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.shx_dialog_apk_update);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button btnCancle = window.findViewById(R.id.btnCancle);
        btnSure = window.findViewById(R.id.btnSure);
        mProgressBar = window.findViewById(R.id.mProgressBar);
        TextView txtTitle = window.findViewById(R.id.tv_title);
        TextView txtMsg = window.findViewById(R.id.tv_msg);

        if(forceUpdate){
            btnCancle.setVisibility(View.GONE);
            ViewUtils.setWidth(btnSure, ViewGroup.LayoutParams.MATCH_PARENT);
            ViewUtils.setWidth(mProgressBar, ViewGroup.LayoutParams.MATCH_PARENT);
            txtTitle.setText("当前版本已无法使用");
            txtMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing() && !TextUtils.isEmpty(apkUrl)) {
                    getSdPermission();
                }
            }
        });

        mProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    if (ViewUtils.isFastClick(500)) {
                        return;
                    }

                    if (mProgressBar != null && mProgressBar.isFinish() && dialog != null) {
                        openAPKFile();
                    }
                }
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                resetInfo();
            }
        });
    }

    /**
     * 打开安装包
     */
    public static void openAPKFile() {
        // 核心是下面几句代码
        if (mFile != null) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //兼容7.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(AppBaseUtils.getUri(mActivity, mFile), "application/vnd.android.package-archive");
                    //兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = mActivity.getPackageManager().canRequestPackageInstalls();
                        if (!hasInstallPermission) {
                            startInstallPermissionSettingActivity();
                            return;
                        }
                    }
                } else {
                    intent.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (mActivity.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    mActivity.startActivity(intent);

                    if(forceUpdate){
                        if (mProgressBar != null){
                            mProgressBar.setVisibility(View.GONE);
                            mProgressBar.reset();
                        }

                        if (btnSure != null)
                            btnSure.setVisibility(View.VISIBLE);

                    }else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startInstallPermissionSettingActivity() {

        ToastUtils.toastShort("请打开安装权限后，再次安装！");

        Uri packageURI = Uri.parse("package:" + mActivity.getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
    }

    /**
     * 检查文件是否存在
     */
    public static boolean exists(String path, String fileName) {
        return (new File(path, fileName)).exists();
    }

    /**
     * 获取SD卡读写权限
     */
    public static void getSdPermission() {
        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储权限", R.drawable.shx_permission_storage));
        HiPermission.create(mActivity)
                .title("尊敬的用户")
                .permissions(permissionItems)
                .msg("对于您的正常使用，您需要以下权限")
                .animStyle(R.style.PermissionAnimScale)
                .style(R.style.PermissionDefaultBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {
                        downloadApk();
                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });
    }

    /**
     * 下载APK包
     */
    public static void downloadApk() {

        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }

        //获取文件名称
        String filename = mActivity.getResources().getString(R.string.app_name) + downLoadVersion.replace(".", "_") + ".apk";

        //apk下载路径
        final File file = new File(ConstantBase.downloaddir);
        if (!file.exists()) {
            file.mkdirs();
        }

        //判断本地是否有该文件
        String successApk = SPUtils.getString(mActivity, ApkKey);
        if (dialog != null
                && !TextUtils.isEmpty(successApk)
                && successApk.equals(filename)
                && exists(ConstantBase.downloaddir, successApk)) {
            mFile = new File(ConstantBase.downloaddir, successApk);
            openAPKFile();
            return;
        }

        GetRequest<File> request = OkGo.get(apkUrl);
        task = OkDownload.request(tag, request);
        task.folder(file.getAbsolutePath());
        task.fileName(filename);
        task.save();
        task.register(new DownloadListener(tag) {
            @Override
            public void onStart(Progress progress) {
                if (mProgressBar != null)
                    mProgressBar.setVisibility(View.VISIBLE);

                if (btnSure != null)
                    btnSure.setVisibility(View.GONE);
            }

            @Override
            public void onProgress(Progress progress) {
                if (progress.status == progress.LOADING) {
                    float fraction = (float) Math.round(progress.fraction * 1000) / 10;
                    if (mProgressBar != null)
                        mProgressBar.setProgress(fraction);
                }
            }

            @Override
            public void onError(Progress progress) {

                ToastUtils.toastShort("文件异常，请点击重试！");

                if (task != null) {
                    //删除未完成的下载任务
                    task.remove(true);
                }

                if (mProgressBar != null) {
                    mProgressBar.setVisibility(View.GONE);
                    mProgressBar.reset();
                }


                if (btnSure != null) {
                    btnSure.setVisibility(View.VISIBLE);
                    btnSure.setText("重新升级");
                }

            }

            @Override
            public void onFinish(File file, Progress progress) {
                isDownFinish = true;

                //记录下载成功的apk名称
                if (!TextUtils.isEmpty(progress.fileName)) {
                    SPUtils.putString(mActivity, ApkKey, progress.fileName);
                }

                if (mProgressBar != null)
                    mProgressBar.setVisibility(View.VISIBLE);

                if (btnSure != null)
                    btnSure.setVisibility(View.GONE);

                if (file != null)
                    mFile = file;

                openAPKFile();

                if (mProgressBar != null)
                    mProgressBar.setProgress(100);
            }

            @Override
            public void onRemove(Progress progress) {

            }
        });
        task.start();
    }

    /**
     * 页面关闭，重置数据
     */
    private static void resetInfo() {
        if (task != null) {
            if (isDownFinish) {
                task.remove();
            } else {
                //删除未完成的下载任务
                task.remove(true);
            }
            task = null;
        }
        isDownFinish = false;
        forceUpdate = false;
        mFile = null;
        mActivity = null;
        apkUrl = null;
        dialog = null;
        btnSure = null;
        mProgressBar = null;
        downLoadVersion = "1.0";
    }

    /**
     * 版本对比
     * @return true需要更新，false不需要更新
     */
    public static boolean compileVersion(String newVersion) {

        /**
         * 新版本号为空，返回false
         */
        if (TextUtils.isEmpty(newVersion))
            return false;

        /**
         * 本地的版本号为空，返回true
         */
        String version = AppBaseUtils.getVersionName();
        if (TextUtils.isEmpty(version) || version.equals("-1"))
            return true;

        try {
            String[] oldVersions = version.split("\\.");
            String[] newVersions = newVersion.split("\\.");

            /**
             * 对版本号进行逐段的对比
             */
            for (int i = 0; i < newVersions.length; i++) {

                String sOld;
                String sNew;

                /**
                 * 原版本，不存在该片段，则直接返回true
                 */
                if (oldVersions.length > i) {
                    sOld = oldVersions[i];
                } else {
                    return true;
                }

                sNew = newVersions[i];

                if (!TextUtils.isEmpty(sOld) && !TextUtils.isEmpty(sNew)) {
                    int iOld = MsStringUtils.str2int(sOld);
                    int iNew = MsStringUtils.str2int(sNew);

                    /**
                     * 本片段，新版本大于原版本，直接返回true
                     */
                    if (iNew > iOld) {
                        return true;
                    } else if (iNew == iOld) {
                        /**
                         * 比较到最后一个片段，两个值仍然相等，就不需要更新，返回false
                         * 否则，就继续下一个片段的比较
                         */
                        if (i == newVersions.length - 1) {
                            return false;
                        }
                    } else if (iNew < iOld) {
                        /**
                         * 本片段，原版本号大于新版本号，返回false
                         */
                        return false;
                    }
                } else if (TextUtils.isEmpty(sOld) && !TextUtils.isEmpty(sNew)) {
                    /**
                     * 本段，原版本为空，新版本号存在值，返回true
                     */
                    return true;
                } else if (TextUtils.isEmpty(sNew)) {
                    /**
                     * 本段，新版本号不存在值，返回false
                     */
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return false;
    }

}
