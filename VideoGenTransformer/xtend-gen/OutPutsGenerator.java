import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class OutPutsGenerator {
  public static void generateVideosAndGifs(final String parentDir, final boolean playVideo) {
    try {
      DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
      Date date = new Date();
      String _format = df.format(date);
      String _plus = ("video_" + _format);
      final String outPutVideoName = (_plus + ".mp4");
      String _format_1 = df.format(date);
      String _plus_1 = ("gif_" + _format_1);
      final String outPutGifName = (_plus_1 + ".gif");
      String outPutsPath = (parentDir + "outputs");
      try {
        Files.createDirectories(Paths.get(outPutsPath));
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
          System.err.println(e);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      final String outPutVideoPath = (outPutsPath + "/videos");
      final String outPutGifPath = (outPutsPath + "/gifs");
      try {
        Files.createDirectories(Paths.get(outPutVideoPath));
        Files.createDirectories(Paths.get(outPutGifPath));
      } catch (final Throwable _t_1) {
        if (_t_1 instanceof Exception) {
          final Exception e_1 = (Exception)_t_1;
          System.err.println(e_1);
        } else {
          throw Exceptions.sneakyThrow(_t_1);
        }
      }
      final String videoCommand = ((((("ffmpeg -f concat -safe 0 -i " + parentDir) + "videos.txt -c copy ") + outPutVideoPath) + "/") + outPutVideoName);
      final String playVideoCommand = ((("vlc " + outPutVideoPath) + "/") + outPutVideoName);
      final String gifCommand = (((((((("ffmpeg -i " + outPutVideoPath) + "/") + outPutVideoName) + " -r 120 -vf scale=360:-1 ") + outPutGifPath) + "/") + outPutGifName) + " -hide_banner");
      InputOutput.<String>println(((("Generating " + outPutVideoPath) + "/") + outPutVideoName));
      Process p = Runtime.getRuntime().exec(videoCommand);
      try {
        InputStream _inputStream = p.getInputStream();
        InputStreamReader _inputStreamReader = new InputStreamReader(_inputStream);
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
      } catch (final Throwable _t_2) {
        if (_t_2 instanceof IOException) {
          final IOException e_2 = (IOException)_t_2;
          System.err.println(e_2);
        } else {
          throw Exceptions.sneakyThrow(_t_2);
        }
      }
      int _waitFor = p.waitFor();
      boolean _equals = (_waitFor == 0);
      if (_equals) {
        InputOutput.<String>println(((outPutVideoPath + outPutVideoName) + " is generated"));
        InputOutput.<String>println(((("Generating " + outPutGifPath) + "/") + outPutGifName));
        Process gifP = Runtime.getRuntime().exec(gifCommand);
        int _waitFor_1 = gifP.waitFor();
        boolean _equals_1 = (_waitFor_1 == 0);
        if (_equals_1) {
          InputOutput.<String>println((((outPutGifPath + "/") + outPutGifName) + "is generated"));
          if (playVideo) {
            Process vlcP = Runtime.getRuntime().exec(playVideoCommand);
            int _waitFor_2 = vlcP.waitFor();
            /* (_waitFor_2 == 0); */
          }
        }
      } else {
        try {
          InputStream _errorStream = p.getErrorStream();
          InputStreamReader _inputStreamReader_1 = new InputStreamReader(_errorStream);
          final BufferedReader stdInput_1 = new BufferedReader(_inputStreamReader_1);
          int c_1 = 0;
          while ((c_1 > (-1))) {
            {
              String line = "";
              line = stdInput_1.readLine();
              InputOutput.<String>println(line);
              c_1 = stdInput_1.read();
            }
          }
          return;
        } catch (final Throwable _t_3) {
          if (_t_3 instanceof IOException) {
            final IOException e_3 = (IOException)_t_3;
            System.err.println(e_3);
          } else {
            throw Exceptions.sneakyThrow(_t_3);
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static void generateIcons(final ArrayList<String> videos, final String parentDir) {
    try {
      int id = 0;
      String outPutsPath = (parentDir + "outputs");
      try {
        Files.createDirectories(Paths.get(outPutsPath));
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
          System.err.println(e);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      String iconsDirPath = (outPutsPath + "/icons");
      try {
        Files.createDirectories(Paths.get(iconsDirPath));
      } catch (final Throwable _t_1) {
        if (_t_1 instanceof Exception) {
          final Exception e_1 = (Exception)_t_1;
          System.err.println(e_1);
        } else {
          throw Exceptions.sneakyThrow(_t_1);
        }
      }
      for (final String video : videos) {
        {
          String iconName = (("icon_" + Integer.valueOf(id)) + ".png");
          String iconPath = ((iconsDirPath + "/") + iconName);
          Double duration = Variants.getDuration((parentDir + video));
          final double start = ((duration).doubleValue() / 2);
          final double end = (start + 1);
          final String command = (((((((("ffmpeg -y -i " + parentDir) + video) + " -r 1 -t ") + Double.valueOf(start)) + " -ss ") + Double.valueOf(end)) + " -vframes 1 ") + iconPath);
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
            } catch (final Throwable _t_2) {
              if (_t_2 instanceof IOException) {
                final IOException e_2 = (IOException)_t_2;
                System.err.println(e_2);
              } else {
                throw Exceptions.sneakyThrow(_t_2);
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
