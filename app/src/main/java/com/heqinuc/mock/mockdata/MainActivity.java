package com.heqinuc.mock.mockdata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.heqinuc.mock.mockdata.background.DataServiceGenerator;
import com.heqinuc.mock.mockdata.background.OnlineDownloadService;
import com.heqinuc.mock.mockdata.data.APResultBean;
import com.heqinuc.mock.mockdata.data.OnlineDownloadBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.heqinuc.mock.mockdata.background.DataServiceGenerator.createOnlineDownload;

public class MainActivity extends AppCompatActivity {

	public static final int DATA_COUNT = 6000;
	public static final int SLEEP_INTERVER = 1;
	public static final String FILE_NAME = "tongji6000.data";

	TextView mTxtInfo = null;
	OnlineDownloadService mService = null;

	AsyncTask mSendTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTxtInfo = (TextView) findViewById(R.id.txtInfo);

		mService = createOnlineDownload();

		findViewById(R.id.btnSendReq).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				mSendTask = new SendDataTask();
				mSendTask.execute((Object) null);

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mSendTask != null) {
			mSendTask.cancel(true);
		}
	}

	private void printLog(String msg) {
		mTxtInfo.setText(msg);
	}

	private final class SendDataTask extends AsyncTask<Object, Integer, Void> {

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);

			printLog("#" + values[0] + "Done");
		}

		@Override
		protected Void doInBackground(Object... objects) {
			Random random = new Random(System.currentTimeMillis());
			InputStream inputStream = null;
			OnlineDownloadService service = DataServiceGenerator.createOnlineDownload();

			try {
				inputStream = MainActivity.this.getAssets().open(FILE_NAME);

				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				String strLine = null;

				int i = 0;

				while ((strLine = reader.readLine()) != null && i < DATA_COUNT) {

					String[] reqData = strLine.split("\\,");

					Call<APResultBean> call = service.uploadLog(reqData[0],
							reqData[1],
							reqData[2],
							reqData[3],
							reqData[4],
							new OnlineDownloadBody(0, 0, 0, 0, random.nextInt(17) + 3.0F, 0)
					);

					try {

						Response<APResultBean> response = call.execute();
						Log.i("Req", "#" + i
								+ ":key = " + reqData[0]
								+ ",REQ = " + response.body().getStatus());

					} catch (IOException e) {

						Log.w("ReqErro", e.toString());

					}

					++i;

					publishProgress(i);

					TimeUnit.SECONDS.sleep(random.nextInt(SLEEP_INTERVER));
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {

					}
				}
			}

			return null;
		}
	}
}
