package com.cookingshow.service.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.cookingshow.service.data.ShareVideoInfo;

public class ShareVideoParser {
	private static final String TAG = "ShareVideoParser";

	public ShareVideoParser() {
		// TODO Auto-generated constructor stub
		Log.i(TAG, "ShareVideoParser");
	}

	public List<ShareVideoInfo> getVideoDataList(HttpURLConnection conn) {
        List<ShareVideoInfo> dataList = new ArrayList<ShareVideoInfo>();

        InputStream is = null;
        
        try {
            is = conn.getInputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        byte[] bytes = readStream(is);
        String sDatas = new String(bytes);
        
        try {
			JSONObject jsonObject = new JSONObject(sDatas);
			
			String mStatus=jsonObject.getString("status");
			
			if(mStatus.equals("success")) {
				JSONArray jsonArray = jsonObject.getJSONArray("info");
				if (jsonArray.length() != 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject item = jsonArray.getJSONObject(i);
						ShareVideoInfo videoData = new ShareVideoInfo();
						videoData.setVideoId(Integer.parseInt(item.getString("share_id")));
						videoData.setUploadTime(item.getString("upload_time"));
						videoData.setUploader(item.getString("uploader"));
						videoData.setTitle(item.getString("name"));
						videoData.setVideoUrl(item.getString("video_url"));
						dataList.add(videoData);
					}
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return dataList;
	}
	
	public static byte[] readStream(InputStream inputStream) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048];
		int len = 0;
		
		try {
			while ((len = inputStream.read(buffer)) != -1) {
				bout.write(buffer, 0, len);
			}
			
			bout.close();
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bout.toByteArray(); 
	}
}
