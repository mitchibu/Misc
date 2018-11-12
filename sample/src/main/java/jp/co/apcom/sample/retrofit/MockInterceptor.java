package jp.co.apcom.sample.retrofit;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockInterceptor implements Interceptor {
	private static final MediaType JSON = MediaType.parse("application/json");

	private final AssetManager am;

	public MockInterceptor(Context context) {
		am = context.getAssets();
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		return new Response.Builder()
				.protocol(Protocol.HTTP_1_1)
				.request(request)
				.code(200)
				.message("OK")
				.body(ResponseBody.create(JSON, load(request.url().url().getPath().substring(1) + "." + request.method().toLowerCase())))
				.build();
	}

	private String load(String path) throws IOException {
		//if(true)throw new IOException();
		InputStream in = null;
		try {
			in = am.open(path);
			StringBuilder sb = new StringBuilder();
			byte[] b = new byte[4096];
			int n;
			while((n = in.read(b)) > 0) sb.append(new String(b, 0, n));
			return sb.toString();
		} finally {
			if(in != null) try {in.close();} catch(Exception e) {e.printStackTrace();}
		}
	}
}
