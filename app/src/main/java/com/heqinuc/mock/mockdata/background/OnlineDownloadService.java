package com.heqinuc.mock.mockdata.background;

import com.heqinuc.mock.mockdata.data.APResultBean;
import com.heqinuc.mock.mockdata.data.OnlineDownloadBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Panoo on 2017/7/27.
 */

public interface OnlineDownloadService {

	@POST("/AbilityPlatformV2")
	Call<APResultBean> uploadLog(
			@Query("key") String key,
			@Header("channel") String chanel,
			@Header("product") String product,
			@Header("svn") String version,
			@Header("model") String model,
			@Body OnlineDownloadBody content);
}
