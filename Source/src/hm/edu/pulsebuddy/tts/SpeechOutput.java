
package hm.edu.pulsebuddy.tts;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

public class SpeechOutput implements TextToSpeech.OnInitListener
{
  
  private TextToSpeech tts = null;
  private int result=0;
  
  private static final String testStart = "Der Test beginnt! Laufe bitte jetzt los!";
  private static final String testEnd = "Das wars, der Test ist vorbei!";
  private static final String walkFaster = "Laufe bitte etwas schneller!";
  private static final String walkSlower = "Laufe bitte etwas langsamer!";
  private static final String holdSpeed = "Halte das Tempo genau so!";
  

  private Context context = null;
  public SpeechOutput(Context context){
    this.context = context;
    tts = new TextToSpeech(context, this);
    
 }

  public void onInit( int status )
  {
    if (status == TextToSpeech.SUCCESS) {
        //set Language
        result = tts.setLanguage(Locale.GERMAN);
//         tts.setPitch(5); // set pitch level
//         tts.setSpeechRate(2); // set speech speed rate
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
          Log.e("TTS", "Language not supported, or missing language Data.");
//          showToast("Language not supported, or missing language Data.");
        } 

    } else {
        Log.e("TTS", "Initilization Failed");
    }
    
  }
  
  public void walkFaster(){
    sayText( walkFaster );
  }
  
  public void walkSlower(){
    sayText( walkSlower );
  }
  
  public void holdSpeed(){
    sayText( holdSpeed );
  }
  
  public void testStart(){
    sayText( testStart );
  }
  
  public void testEnd(){
    sayText( testEnd );
  }
  
  public void sayText(String text) {
    if(result!=tts.setLanguage(Locale.GERMAN))
    {
      Log.e("TTS", "Not German");
    }else
    {
      tts.speak(text, TextToSpeech.QUEUE_ADD, null);
      showToast( text );
    }
   }
  
  private void showToast( String text )
  {
    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
    toast.show();
  }
  
  
  public void shutdown(){
    tts.shutdown();
  }
}

