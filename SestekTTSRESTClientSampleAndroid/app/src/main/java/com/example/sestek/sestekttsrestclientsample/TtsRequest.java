package com.example.sestek.sestekttsrestclientsample;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by fatih.kiralioglu on 12.7.2016.
 */
public class TtsRequest implements Serializable {

    public TtsRequest()
    {
        Voice = new VoiceInformation();
        Audio = new AudioInfo();
        License = new HashMap<String, String>();
    }

    public  String Text;
    public VoiceInformation Voice;
    public AudioInfo Audio;
    public HashMap<String, String> License;
}
