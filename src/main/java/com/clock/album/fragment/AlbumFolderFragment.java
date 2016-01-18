package com.clock.album.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.clock.album.R;
import com.clock.album.adapter.AlbumFolderAdapter;
import com.clock.album.entity.AlbumInfo;
import com.clock.utils.text.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 相册目录页面
 *
 * @author Clock
 * @since 2016-01-17
 */
public class AlbumFolderFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String ALL_IMAGE = "allImage";

    private OnAlbumDetailInteractionListener mInteractionListener;
    /**
     * 相册目录列表
     */
    private ArrayList<AlbumInfo> mAlbumInfoList;
    /**
     * 每个文件夹下面的首张图片
     */
    private HashMap<String, File> mFrontImageMap;
    private ListView mFolderListView;

    public AlbumFolderFragment() {
        // Required empty public constructor
    }

    /**
     * @param folderList    相册目录列表
     * @param frontImageMap 每个相册目录下的第一张图片
     * @return
     */
    public static AlbumFolderFragment newInstance(ArrayList<AlbumInfo> folderList, HashMap<String, File> frontImageMap) {
        AlbumFolderFragment fragment = new AlbumFolderFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, folderList);
        args.putSerializable(ARG_PARAM2, frontImageMap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAlbumInfoList = (ArrayList<AlbumInfo>) getArguments().getSerializable(ARG_PARAM1);
            mFrontImageMap = (HashMap<String, File>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_album_directory, container, false);
        mFolderListView = (ListView) rootView.findViewById(R.id.list_album);
        AlbumFolderAdapter albumFolderAdapter = new AlbumFolderAdapter(mAlbumInfoList, mFrontImageMap);
        mFolderListView.setAdapter(albumFolderAdapter);
        mFolderListView.setOnItemClickListener(this);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAlbumDetailInteractionListener) {
            mInteractionListener = (OnAlbumDetailInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInteractionListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mFolderListView) {
            if (mInteractionListener != null) {
                AlbumInfo albumInfo = mAlbumInfoList.get(position);
                mInteractionListener.switchAlbumFolder(albumInfo.getFolder());
                mInteractionListener.refreshFolderName(albumInfo.getFolder().getName());
            }
        }
    }

    /**
     * 相册详情交互接口
     */
    public interface OnAlbumDetailInteractionListener {
        /**
         * 切换到相册详情页面
         *
         * @param albumFolder 指定相册的目录
         */
        public void switchAlbumFolder(File albumFolder);

        /**
         * 刷新目录名
         *
         * @param albumFolderName
         */
        public void refreshFolderName(String albumFolderName);
    }
}
