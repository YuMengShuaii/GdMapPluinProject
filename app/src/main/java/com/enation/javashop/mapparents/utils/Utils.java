package com.enation.javashop.mapparents.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.enation.javashop.mapparents.R;

/**
 * Created by LDD on 17/7/27.
 */

public class Utils {
    public static void toastS(Context context,String string){
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
    }
    /**
     * 创建Dialog
     * @param paramString1         标题
     * @param paramString2         取消
     * @param paramString3         确定
     * @param paramContext         上下文
     * @param paramDialogInterface 监听接口
     */
    public static void createDialog(String paramString1, String paramString2, String paramString3, Context paramContext, final DialogInterface paramDialogInterface)
    {
        final Dialog localDialog = new Dialog(paramContext,R.style.Dialog);
        View localView = LayoutInflater.from(paramContext).inflate(R.layout.newdialog_lay, null);
        localDialog.setContentView(localView);
        TextView localTextView1 = (TextView)localView.findViewById(R.id.yes);
        TextView localTextView2 = (TextView)localView.findViewById(R.id.on);
        localTextView1.setText(paramString3);
        localTextView2.setText(paramString2);
        ((TextView)localView.findViewById(R.id.message)).setText(paramString1);
        localTextView1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramView)
            {
                paramDialogInterface.yes();
                localDialog.dismiss();
            }
        });
        localTextView2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramView)
            {
                paramDialogInterface.no();
                localDialog.dismiss();
            }
        });
        localDialog.setCanceledOnTouchOutside(true);
        localDialog.setCancelable(true);
        if (!(localDialog.isShowing()))
            localDialog.show();
    }
    public static abstract interface DialogInterface
    {
        public abstract void no();

        public abstract void yes();
    }

    /**
     * 获取屏幕宽度，px
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度，px
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

}
