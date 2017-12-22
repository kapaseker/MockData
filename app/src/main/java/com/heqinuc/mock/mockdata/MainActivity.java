package com.heqinuc.mock.mockdata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.heqinuc.mock.mockdata.background.DataServiceGenerator;
import com.heqinuc.mock.mockdata.background.OnlineDownloadService;
import com.heqinuc.mock.mockdata.data.APResultBean;
import com.heqinuc.mock.mockdata.data.OnlineDownloadBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.heqinuc.mock.mockdata.background.DataServiceGenerator.createOnlineDownload;

public class MainActivity extends AppCompatActivity {

	//	public static final int DATA_COUNT = 7000;
//	public static final int SLEEP_INTERVER = 3;
	private static final String FILE_PATH = "mock";

	TextView mTxtInfo = null;
	OnlineDownloadService mService = null;

	AsyncTask mSendTask = null;
	private String mCurrentPath = "";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTxtInfo = (TextView) findViewById(R.id.txtInfo);
		mService = createOnlineDownload();
		chooseFiles();

		findViewById(R.id.btnSendReq).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startMock();
			}
		});
	}

	private void startMock() {
		if (TextUtils.isEmpty(mCurrentPath)) {
			Toast.makeText(this, R.string.tip_choose, Toast.LENGTH_SHORT).show();
		} else {
			cancelTask();
			mSendTask = new SendDataTask().execute((Object) null);
		}
	}

	private void cancelTask() {
		if (mSendTask != null) {
			mSendTask.cancel(true);
		}
	}

	private void chooseFiles() {

		try {

			RadioGroup group = (RadioGroup) findViewById(R.id.grp_choose);
			group.removeAllViews();

			String[] paths = this.getAssets().list(FILE_PATH);
			for (String path : paths) {
				RadioButton radioButton = new RadioButton(this);
				radioButton.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				radioButton.setText(path);
				radioButton.setTag(path);
				group.addView(radioButton, 0);
			}

			group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					Log.d("CHOOSE", checkedId + "");
					mCurrentPath = (String) findViewById(checkedId).getTag();
				}
			});

//			mTxtInfo.setText(Arrays.toString(paths));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		cancelTask();
		super.onDestroy();
	}

	private void printLog(String msg) {
		mTxtInfo.setText(msg);
	}

	private final class SendDataTask extends AsyncTask<Object, String, Void> {

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);

			printLog(values[0]);
		}

		@Override
		protected Void doInBackground(Object... objects) {
			Random random = new Random(System.currentTimeMillis());
			InputStream inputStream = null;
			OnlineDownloadService service = DataServiceGenerator.createOnlineDownload();

			try {

				String[] nameSep = mCurrentPath.split("\\.")[0].split("\\-");
				int count = Integer.valueOf(nameSep[0]);
				int gap = Integer.valueOf(nameSep[1]);

				inputStream = MainActivity.this.getAssets().open(FILE_PATH + File.separator + mCurrentPath);

				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				String strLine = null;

				int i = 0;

				while ((strLine = reader.readLine()) != null && i < count) {

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
						String logMsg = "#" + i
								+ ":key = " + reqData[0]
								+ ",REQ = " + response.body().getStatus();
						Log.i("Req", logMsg);

						publishProgress(logMsg);

					} catch (IOException e) {

						Log.w("ReqErro", e.toString());

					}

					++i;

					TimeUnit.SECONDS.sleep(random.nextInt(gap));
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
