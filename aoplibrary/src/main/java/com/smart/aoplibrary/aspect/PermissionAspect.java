package com.smart.aoplibrary.aspect;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;


import com.smart.aoplibrary.R;
import com.smart.aoplibrary.annotation.Permission;
import com.smart.aoplibrary.utils.RuntimeRationale;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Setting;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 权限申请
 */
@Aspect
public class PermissionAspect {

    /**
     * Permission
     */
    @Pointcut("execution(@com.example.pc.aopdemo.aop.annotation.Permission * *(..))")
    public void methodAnnotated() {
    }


    /**
     * AndPermission 权限申请
     * @param joinPoint
     * @throws Throwable
     */
    @Around("methodAnnotated()")//在连接点进行方法替换
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Permission annotation = method.getAnnotation(Permission.class);
        Annotation[] annotatins=method.getAnnotations();
        String[] values = annotation.values();
        final Context context = (Context) joinPoint.getThis();


        AndPermission.with(context)
                .runtime()
                .permission(values)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            showSettingDialog(context, permissions);
                        }
                    }
                })
                .start();

    }


    /**
     *
     * @param context
     * @param permissions 权限
     */
    public void showSettingDialog(final Context context, final List<String> permissions) {
        List<String> permissionNames = com.yanzhenjie.permission.Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission(context);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    /**
     *
     * @param context
     */
    private void setPermission(final Context context) {
        AndPermission.with(context)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        Toast.makeText(context, R.string.message_setting_comeback, Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

}