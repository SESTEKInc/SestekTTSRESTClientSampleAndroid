package com.example.sestek.sestekttsrestclientsample;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import sestek.codec.JOpusDecoder;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button bSend_WAV_Request;
    private Button bSend_OPUS_Request;
    private JOpusDecoder decoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        bSend_WAV_Request = (Button)findViewById(R.id.button);
        bSend_OPUS_Request = (Button)findViewById(R.id.button2);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            Initialize();
            decoder = new JOpusDecoder(8000, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void send_wav_request(View view)
    {
        String text2Synthesize = editText.getText().toString();

        try {
            MakeRESTTTSRequestWav(text2Synthesize);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void send_opus_request(View view)
    {
        String text2Synthesize = editText.getText().toString();

        try {
            MakeRESTTTSRequestOpus(text2Synthesize);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void Initialize() throws Exception {
        LoadCriticalLibrary("c++_shared");
        LoadCriticalLibrary("JOpusJNI");
    }


    public static void LoadCriticalLibrary(String libraryName) {
        try {
            System.loadLibrary(libraryName);
        } catch (Error e) {
            Log.i("voice.recognition.rest", "[ERROR] library could not be loaded : " + libraryName + " Failure to load this library deems the encoding unusable.");
            e.printStackTrace();
            throw e;
        }
    }

    // HTTP POST request
    private void MakeRESTTTSRequestWav(String text2Synthesize) throws Exception {

        String url = "http://192.168.10.244:40000/v1/speech/synthesis/tts";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        VoiceInformation voice = new VoiceInformation();
        voice.Name = "GVZ Gul 16k_HV_Premium";
        voice.Rate = 1;
        voice.Volume = 1;
        AudioInfo audio = new AudioInfo();
        audio.Format = AudioFormat.Wav;

        audio.FormatDetails.put("Encoding", "pcm");
        audio.FormatDetails.put("SampleRate", "8k");

        TtsRequest myRequest = new TtsRequest();
        myRequest.Text = text2Synthesize;
        myRequest.Voice = voice;
        myRequest.Audio = audio;
        myRequest.License = new HashMap<String, String>();

        Gson gson=new Gson();
        String mapJsonStr=gson.toJson(myRequest);

        byte[] postData       = mapJsonStr.getBytes();
        int    postDataLength = postData.length;

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty( "Content-Type", "application/json");
        //con.setRequestProperty( "charset", "utf-8");
        con.setRequestProperty( "Content-Length", String.valueOf( postDataLength ));

        System.out.println("\nSending 'POST' request to URL : " + url);

        // Send post request
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(postData);

        int responseCode = con.getResponseCode();

        System.out.println("Response Code : " + responseCode);


        byte[] result = IOUtils.toByteArray(con.getInputStream());

        System.out.println(result.length);

        byte[] headerless_data = new byte[result.length - 44];
        System.arraycopy(result, 44, headerless_data, 0, headerless_data.length);
        PlayAudioFile(headerless_data);

    }

    // HTTP POST request for Opus
    private void MakeRESTTTSRequestOpus(String text2Synthesize) throws Exception {

        String url = "http://192.168.10.244:40000/v1/speech/synthesis/tts";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        VoiceInformation voice = new VoiceInformation();
        voice.Name = "GVZ Gul 16k_HV_Premium";
        voice.Rate = 1;
        voice.Volume = 1;
        AudioInfo audio = new AudioInfo();
        audio.Format = AudioFormat.Opus;


        audio.FormatDetails.put("BitRateKbps", "15");
        audio.FormatDetails.put("SampleRate", "8k");

        TtsRequest myRequest = new TtsRequest();
        myRequest.Text = text2Synthesize;
        myRequest.Voice = voice;
        myRequest.Audio = audio;
        myRequest.License = new HashMap<String, String>();

        Gson gson=new Gson();
        String mapJsonStr=gson.toJson(myRequest);

        byte[] postData       = mapJsonStr.getBytes();
        int    postDataLength = postData.length;

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty( "Content-Type", "application/json");
        //con.setRequestProperty( "charset", "utf-8");
        con.setRequestProperty( "Content-Length", String.valueOf( postDataLength ));

        System.out.println("\nSending 'POST' OPUS request to URL : " + url);

        // Send post request
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(postData);

        int responseCode = con.getResponseCode();

        System.out.println("OPUS Response Code : " + responseCode);


        byte[] resultOpus = IOUtils.toByteArray(con.getInputStream());
        System.out.println("OPUS file size: " + resultOpus.length);
        byte[] result = decoder.Decode(resultOpus);
        System.out.println("OPUS decoded!");
        System.out.println(result.length);

        PlayAudioFile(result);

    }

    private void PlayAudioFile(byte[] wavData) throws IOException {
        int bufferSize = AudioTrack.getMinBufferSize(8000, android.media.AudioFormat.CHANNEL_OUT_MONO, android.media.AudioFormat.ENCODING_PCM_16BIT);

        File f = new File(Environment.getExternalStoragePublicDirectory("/Sestek") + "/sample.wav");
        if(!f.exists())
        {
            Log.i("deneme", "yok");
        }

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStoragePublicDirectory("/Sestek") + "/sample.wav"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            bos.write(wavData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bos.flush();
        bos.close();

        AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, android.media.AudioFormat.CHANNEL_OUT_MONO, android.media.AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

        int count = 0;
        byte[] data = new byte[bufferSize];
        ByteArrayInputStream dataInputStream = new ByteArrayInputStream(wavData);
        mAudioTrack.play();

        while((count = dataInputStream.read(data, 0, bufferSize)) > -1){
            mAudioTrack.write(data, 0, count);
        }
        mAudioTrack.stop();
        mAudioTrack.release();
        try {
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(wavData.length);

    }

}
