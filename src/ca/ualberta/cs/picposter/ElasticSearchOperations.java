package ca.ualberta.cs.picposter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.ualberta.cs.CMPUT301.chenlei.ElasticSearchResponse;
import ca.ualberta.cs.CMPUT301.chenlei.ElasticSearchSearchResponse;
import ca.ualberta.cs.CMPUT301.chenlei.Recipe;
import ca.ualberta.cs.picposter.model.PicPostModel;


public class ElasticSearchOperations
{
	public static void pushPicPostModel(final PicPostModel model){
		Thread thread = new Thread(){
			@Override
			public void run(){
				Gson gson = new Gson();
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://cmput301.softwareprocess.es:8080/testing/zuo2/");
				
				try{
					String jsonString =  gson.toJson(model);
					request.setEntity(new StringEntity(jsonString));
					
					HttpResponse response = client.execute(request);
					
					Log.w("ElasticSearch",response.getStatusLine().toString());
					
					response.getStatusLine().toString();
					HttpEntity entity = response.getEntity();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					while(output != null){
						Log.w("ElasticSearch",output);
						output = reader.readLine();
					}
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}
	

	//reference by the code ESDemo
	public void earchPicPostModel(final String str) throws ClientProtocolException, IOException {
		HttpGet searchRequest = new HttpGet("http://cmput301.softwareprocess.es:8080/testing/zuo2/_search?q="+str);
		searchRequest.setHeader("Accept","application/json");
		HttpResponse response = httpclient.execute(searchRequest);
		
		String status = response.getStatusLine().toString();
		System.out.println(status);

		String json = getEntityContent(response);

		Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Recipe>>(){}.getType();
		ElasticSearchSearchResponse<Recipe> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
		System.err.println(esResponse);
		for (ElasticSearchResponse<Recipe> r : esResponse.getHits()) {
			Recipe recipe = r.getSource();
			System.err.println(recipe);
		}
		searchRequest.releaseConnection();
	}
	

