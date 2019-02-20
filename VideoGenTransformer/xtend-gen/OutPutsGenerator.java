import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class OutPutsGenerator {
  public static void runCommands() {
    try {
      DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
      Date date = new Date();
      String _format = df.format(date);
      String _plus = ("video_" + _format);
      final String outPutVideoName = (_plus + ".mp4");
      String _format_1 = df.format(date);
      String _plus_1 = ("gif_" + _format_1);
      final String outPutGifName = (_plus_1 + ".gif");
      final String outPutVideoPath = "outputs/videos/";
      final String outPutGifPath = "outputs/gifs/";
      final String videoCommand = (("ffmpeg -f concat -safe 0 -i videos.txt -c copy " + outPutVideoPath) + outPutVideoName);
      final String playVideoCommand = (("vlc " + outPutVideoPath) + outPutVideoName);
      final String gifCommand = (((((("ffmpeg -i " + outPutVideoPath) + outPutVideoName) + " -r 120 -vf scale=360:-1 ") + outPutGifPath) + outPutGifName) + " -hide_banner");
      InputOutput.<String>println((("Generating " + outPutVideoPath) + outPutVideoName));
      Process p = Runtime.getRuntime().exec(videoCommand);
      int _waitFor = p.waitFor();
      boolean _equals = (_waitFor == 0);
      if (_equals) {
        InputOutput.<String>println(((outPutVideoPath + outPutVideoName) + " is generated"));
        InputOutput.<String>println((("Generating " + outPutGifPath) + outPutGifName));
        Process gifP = Runtime.getRuntime().exec(gifCommand);
        int _waitFor_1 = gifP.waitFor();
        boolean _equals_1 = (_waitFor_1 == 0);
        if (_equals_1) {
          InputOutput.<String>println(((outPutGifPath + outPutGifName) + "is generated"));
          Process vlcP = Runtime.getRuntime().exec(playVideoCommand);
          int _waitFor_2 = vlcP.waitFor();
          /* (_waitFor_2 == 0); */
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static void generateIcons(final ArrayList<String> videos) {
    try {
      int id = 0;
      for (final String video : videos) {
        {
          String iconName = (("icon_" + Integer.valueOf(id)) + ".png");
          String iconPath = ("outputs/icons/" + iconName);
          Double duration = Variants.getDuration(video);
          final double start = ((duration).doubleValue() / 2);
          final double end = (start + 1);
          final String command = ((((((("ffmpeg -y -i " + video) + " -r 1 -t ") + Double.valueOf(start)) + " -ss ") + Double.valueOf(end)) + " -vframes 1 ") + iconPath);
          final Process p = Runtime.getRuntime().exec(command);
          int _waitFor = p.waitFor();
          boolean _notEquals = (_waitFor != 0);
          if (_notEquals) {
            try {
              InputStream _errorStream = p.getErrorStream();
              InputStreamReader _inputStreamReader = new InputStreamReader(_errorStream);
              final BufferedReader stdInput = new BufferedReader(_inputStreamReader);
              int c = 0;
              while ((c > (-1))) {
                {
                  String line = "";
                  line = stdInput.readLine();
                  InputOutput.<String>println(line);
                  c = stdInput.read();
                }
              }
              return;
            } catch (final Throwable _t) {
              if (_t instanceof IOException) {
                final IOException e = (IOException)_t;
                System.err.println(e);
              } else {
                throw Exceptions.sneakyThrow(_t);
              }
            }
          } else {
            InputOutput.<String>println(((video + " icon is generated with name of ") + iconName));
          }
          id++;
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
