package com.inhim.pj.dowloadfile.adapter;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.inhim.pj.R;
import com.inhim.pj.dowloadfile.download.MyBusinessInfoDid;
import com.inhim.pj.dowloadfile.utils.FileUtil;
import com.inhim.pj.utils.GlideUtils;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * Created by renpingqing on 17/1/19.
 */
public class DownloadListDidAdapter extends
        BaseRecyclerDidViewAdapter<MyBusinessInfoDid, DownloadListDidAdapter.ViewHolder> {

    private static final String TAG = "DownloadListAdapter";
    private final Context context;
    private boolean isCheck,isAll;
    public DownloadListDidAdapter(Context context) {
        super(context);
        this.context = context;
    }
    public void setCheck(boolean isCheck,boolean isAll){
        this.isCheck=isCheck;
        this.isAll=isAll;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_download_info1, parent, false));
    }

    @Override
    public void onBindViewHolder(DownloadListDidAdapter.ViewHolder holder, final int position) {
        holder.bindData(getData(position), position);

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        //private final ImageView iv_icon;
        private final TextView tv_size;
        private final TextView tv_status;
        private final TextView tv_name;
        private ImageView imageview1, imageview2;
        private CheckBox checkbox;

        public ViewHolder(View view) {
            super(view);
            itemView.setClickable(true);
            tv_size = view.findViewById(R.id.tv_size);
            tv_status = view.findViewById(R.id.tv_status);
            tv_name = view.findViewById(R.id.tv_name);
            imageview1 = view.findViewById(R.id.imageview1);
            imageview2 = view.findViewById(R.id.imageview2);
            checkbox = view.findViewById(R.id.checkbox);
        }

        @SuppressWarnings("unchecked")
        public void bindData(final MyBusinessInfoDid data, final int position) {
            tv_name.setText(data.getTitle());
            tv_size.setText(FileUtil
                    .formatFileSize(data.getTotal()));
            if(data.getProgressText()!=null&&!data.getProgressText().equals("")){
                tv_status.setText(data.getProgressText());
            }else{
                tv_status.setText("未播放");
            }
            GlideUtils.displayFromUrl(data.getCover(), imageview1,context);
            imageview2.setImageResource(R.mipmap.icon_video);
            if(isCheck){
                checkbox.setVisibility(View.VISIBLE);
            }else{
                checkbox.setVisibility(View.GONE);
            }
            if(isAll){
                checkbox.setChecked(true);
            }else{
                checkbox.setChecked(false);
            }
            refresh(position);

        }

        private void refresh(int position) {
                //tv_size.setText(FileUtil.formatFileSize(downloadInfo.getSize()));
        }
    }
    public void deleteFiles(Map<Integer, Boolean> deleteMap){
        List<MyBusinessInfoDid> listInfo = getDownloadListData();
        for(int i=0;i<deleteMap.size();i++){
            if (deleteMap.get(i)) {
                File file = new File(listInfo.get(i).getFilePath());
                if(file.exists()){
                    file.delete();
                }
                //删除数据库movie表中id为1的记录
                listInfo.get(i).delete();
            }
        }
        setData(getDownloadListData());
    }
    private List<MyBusinessInfoDid> getDownloadListData() {
        List<MyBusinessInfoDid> myBusinessInfos = LitePal.findAll(MyBusinessInfoDid.class);
        if (myBusinessInfos.size() > 0) {
        }
        return myBusinessInfos;
    }

}
