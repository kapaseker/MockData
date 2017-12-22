package com.heqinuc.mock.mockdata.background;

import android.os.Build;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heqinuc.mock.mockdata.data.OnlineDownloadBody;
import com.heqinuc.mock.mockdata.utils.AESUtil;
import com.heqinuc.mock.mockdata.utils.GzipUtil;
import com.heqinuc.mock.mockdata.utils.MD5Tool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Panoo on 2017/7/27.
 */

public class DataServiceGenerator {

	//	private static final String CURRENT_VERSION = "6.0.23.3.5.20160725";
	private static final String[] NET_TYPE = new String[]{"WLAN", "3G", "4G"};
	private static final String BASE_URL = "http://nav7.mlocso.com:10001";
	private static final Random random = new Random(234);
	private static final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
		@Override
		public Response intercept(Chain chain) throws IOException {

			Request orignalRequest = chain.request();
			HttpUrl orignalUrl = orignalRequest.url();

			RequestBody body = orignalRequest.body();

			Buffer buffer = new Buffer();
			body.writeTo(buffer);
			byte[] allByte = buffer.readByteArray();


			String version = orignalRequest.header("svn");

			HttpUrl newUrl = orignalUrl.newBuilder()
					.addQueryParameter("sn", getSn())
					.addQueryParameter("type", "log_upload")
					.addQueryParameter("function", "online_map_download").build();

			String productMkr = "cnlid=" + orignalRequest.header("channel")
					+ "&productid=" + orignalRequest.header("product")
					+ "&svn=" + version;

			String model = orignalRequest.header("model");

			String netType = NET_TYPE[random.nextInt(3)];

			Request newRequest = orignalRequest.newBuilder()
					.url(newUrl)
					.removeHeader("channel")
					.removeHeader("product")
					.removeHeader("model")
					.header("Content-Type", "text/html; charset=utf-8")
					.header("product-mark", productMkr)
					.header("user-agent", getUserAgent(model))
					.header("Accept-Encoding", "gzip")
					.header("upBearType", netType)
					.header("User_NetType", netType)
					.header("nettype", netType)
					.header("postbodyMD5", MD5Tool.toMD5(allByte, false))
					.build();

			return chain.proceed(newRequest);
		}

		protected String getSn() {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
			String time = format.format(new Date(System.currentTimeMillis()));
			return time;
		}


		private String getUserAgent(String model) {
			StringBuffer sb = new StringBuffer();

			sb.append("Heqinuc/" + "6.0" + " ( Android ");
			sb.append(Build.VERSION.RELEASE);
			sb.append("; ");
			sb.append(model);
			sb.append("; ");
			sb.append(Build.MANUFACTURER);
			sb.append("; ");
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				sb.append(Build.SUPPORTED_ABIS[0]);
			} else {
				sb.append(Build.CPU_ABI);
			}
			sb.append("; ) (");
			sb.append(Build.FINGERPRINT);
			sb.append(")");

			String result;
			try {
				result = URLEncoder.encode(sb.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				result = URLEncoder.encode(sb.toString());
			}

			return result;
		}
	});


	private static final class OnlineDownloadBodyConverter implements Converter<OnlineDownloadBody, RequestBody> {

		@Override
		public RequestBody convert(OnlineDownloadBody value) throws IOException {

			ObjectMapper mapper = new ObjectMapper();
			String strBody = mapper.writeValueAsString(value);

			byte[] postByte = strBody.getBytes("UTF-8");
			byte[] postByteZip = GzipUtil.compress(postByte);
			byte[] postInfo = AESUtil.encrypt(postByteZip, "heqinuc2016");

			return RequestBody.create(MediaType.parse("application/octet-stream"), postInfo);
		}
	}

	private static final Retrofit serviceBuilder = new Retrofit
			.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(new Converter.Factory() {
				@Nullable
				@Override
				public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
					return new OnlineDownloadBodyConverter();
				}
			})
			.addConverterFactory(JacksonConverterFactory.create())
			.client(httpClientBuilder.build()).build();

	public static final OnlineDownloadService createOnlineDownload() {
		return serviceBuilder.create(OnlineDownloadService.class);
	}
}
