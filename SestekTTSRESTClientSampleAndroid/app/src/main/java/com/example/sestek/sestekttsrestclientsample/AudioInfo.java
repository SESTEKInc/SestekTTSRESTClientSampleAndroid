package com.example.sestek.sestekttsrestclientsample;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by fatih.kiralioglu on 12.7.2016.
 */
public class AudioInfo  implements Serializable {
    public AudioInfo()
    {
        FormatDetails = new HashMap<String, String>();
    }

    public AudioFormat Format;

    public HashMap<String, String> FormatDetails;
}
