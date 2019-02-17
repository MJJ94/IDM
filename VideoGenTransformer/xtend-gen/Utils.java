import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class Utils {
  public static ArrayList<ArrayList<String>> calculateVariants(final ArrayList<String> listMan, final ArrayList<String> listOp, final ArrayList<String> listAlt) {
    ArrayList<ArrayList<String>> opCombinations = Utils.insertOp(listOp);
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> _insertMan = Utils.insertMan(listMan, opCombinations);
    ArrayList<ArrayList<String>> manOpResult = new ArrayList<ArrayList<String>>(_insertMan);
    ArrayList<ArrayList<String>> _insertAlt = Utils.insertAlt(listAlt, manOpResult);
    ArrayList<ArrayList<String>> _arrayList = new ArrayList<ArrayList<String>>(_insertAlt);
    result = _arrayList;
    return result;
  }
  
  public static ArrayList<ArrayList<String>> insertMan(final ArrayList<String> listMan, final ArrayList<ArrayList<String>> opResult) {
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>(opResult);
    for (final String man : listMan) {
      for (final ArrayList<String> l : result) {
        l.add(man);
      }
    }
    return result;
  }
  
  public static ArrayList<ArrayList<String>> insertOp(final ArrayList<String> lOp) {
    int size = Double.valueOf(Math.pow(2, lOp.size())).intValue();
    int sizePreced = (size / 2);
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    int i = 0;
    int j = 1;
    int initSizePreced = sizePreced;
    int initI = 0;
    while ((i < size)) {
      {
        ArrayList<String> _arrayList = new ArrayList<String>();
        result.add(_arrayList);
        i++;
      }
    }
    i = 0;
    for (final String elem : lOp) {
      {
        i = 0;
        initI = i;
        sizePreced = (size / (j * 2));
        initSizePreced = sizePreced;
        while (((i < size) && (sizePreced < size))) {
          {
            while ((i < sizePreced)) {
              {
                result.get(i).add(elem);
                i++;
              }
            }
            int _initI = initI;
            initI = (_initI + (initSizePreced * 2));
            i = initI;
            sizePreced = ((initSizePreced * 2) + sizePreced);
          }
        }
        int _j = j;
        j = (_j * 2);
      }
    }
    return result;
  }
  
  public static ArrayList<ArrayList<String>> insertAlt(final ArrayList<String> listAlt, final ArrayList<ArrayList<String>> manOpResult) {
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    int j = 0;
    for (final ArrayList<String> l : manOpResult) {
      for (final String alt : listAlt) {
        ArrayList<String> _arrayList = new ArrayList<String>(l);
        result.add(_arrayList);
      }
    }
    int size = result.size();
    while ((j < size)) {
      for (final String alt_1 : listAlt) {
        {
          result.get(j).add(alt_1);
          j++;
        }
      }
    }
    return result;
  }
  
  public static void generateCSV(final ArrayList<ArrayList<String>> variantes, final HashMap<String, Long> mapMediaSizes, final ArrayList<String> listMan, final ArrayList<String> listOpt, final ArrayList<String> listAlt) {
    DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
    Date date = new Date();
    BufferedWriter writer = null;
    try {
      String _format = df.format(date);
      String _plus = ("./results/result-" + _format);
      String _plus_1 = (_plus + ".csv");
      FileWriter _fileWriter = new FileWriter(_plus_1);
      BufferedWriter _bufferedWriter = new BufferedWriter(_fileWriter);
      writer = _bufferedWriter;
      writer.write(Utils.toCSV(variantes, mapMediaSizes, listMan, listOpt, listAlt));
      writer.flush();
      writer.close();
    } catch (final Throwable _t) {
      if (_t instanceof IOException) {
        final IOException exception = (IOException)_t;
        System.err.println(exception);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public static String toCSV(final ArrayList<ArrayList<String>> variantes, final HashMap<String, Long> mapMediaSizes, final ArrayList<String> listMan, final ArrayList<String> listOpt, final ArrayList<String> listAlt) {
    String separator = ";";
    String separatorLine = "\n";
    StringBuilder stringBuilder = new StringBuilder();
    ArrayList<String> columnNames = new ArrayList<String>();
    stringBuilder.append("id");
    stringBuilder.append(separator);
    for (final String man : listMan) {
      {
        stringBuilder.append(man);
        columnNames.add(man);
        stringBuilder.append(separator);
      }
    }
    for (final String opt : listOpt) {
      {
        stringBuilder.append(opt);
        columnNames.add(opt);
        stringBuilder.append(separator);
      }
    }
    for (final String alt : listAlt) {
      {
        stringBuilder.append(alt);
        columnNames.add(alt);
        stringBuilder.append(separator);
      }
    }
    stringBuilder.append("size");
    stringBuilder.append(separator);
    stringBuilder.append("duration");
    stringBuilder.append(separator);
    stringBuilder.append(separatorLine);
    int id = 1;
    for (final ArrayList<String> l : variantes) {
      {
        long totalSize = 0;
        Double totalDuration = Double.valueOf(0.0);
        stringBuilder.append(id);
        stringBuilder.append(separator);
        for (final String column : columnNames) {
          {
            stringBuilder.append(l.contains(column));
            stringBuilder.append(separator);
          }
        }
        for (final String v : l) {
          {
            Long size = mapMediaSizes.get(v);
            long _talSize = totalSize;
            totalSize = (_talSize + (size).longValue());
          }
        }
        stringBuilder.append(totalSize);
        stringBuilder.append(separator);
        totalDuration = Utils.getDurations(l);
        stringBuilder.append(totalDuration);
        stringBuilder.append(separatorLine);
        id++;
      }
    }
    return stringBuilder.toString();
  }
  
  public static void generateVideosSeq(final ArrayList<ArrayList<String>> variantes) {
    BufferedWriter writer = null;
    try {
      FileWriter _fileWriter = new FileWriter(("./videos" + ".txt"));
      BufferedWriter _bufferedWriter = new BufferedWriter(_fileWriter);
      writer = _bufferedWriter;
      writer.write(Utils.toTxt(variantes));
      writer.flush();
      writer.close();
    } catch (final Throwable _t) {
      if (_t instanceof IOException) {
        final IOException exception = (IOException)_t;
        System.err.println(exception);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public static String toTxt(final ArrayList<ArrayList<String>> variantes) {
    String separatorLine = "\n";
    StringBuilder stringBuilder = new StringBuilder();
    Random random = new Random();
    int _size = variantes.size();
    int _minus = (_size - 1);
    int randomIndex = random.nextInt(_minus);
    ArrayList<String> randomVar = variantes.get(randomIndex);
    for (final String elem : randomVar) {
      {
        stringBuilder.append((("file \'" + elem) + "\'"));
        stringBuilder.append(separatorLine);
      }
    }
    return stringBuilder.toString();
  }
  
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
  
  public static Double getDurations(final ArrayList<String> videos) {
    double result = 0.0;
    for (final String video : videos) {
      {
        final Double duration = Utils.getDuration(video);
        double _result = result;
        result = (_result + (duration).doubleValue());
      }
    }
    return Double.valueOf(result);
  }
  
  public static Double getDuration(final String videoName) {
    try {
      String durationCommand = ((" ffprobe -i " + videoName) + " -show_format");
      Process durationP = Runtime.getRuntime().exec(durationCommand);
      int _waitFor = durationP.waitFor();
      boolean _equals = (_waitFor == 0);
      if (_equals) {
        try {
          InputStream _inputStream = durationP.getInputStream();
          InputStreamReader _inputStreamReader = new InputStreamReader(_inputStream);
          final BufferedReader stdInput = new BufferedReader(_inputStreamReader);
          int c = 0;
          while ((c > (-1))) {
            {
              String line = "";
              line = stdInput.readLine().toLowerCase();
              boolean _contains = line.contains("uration");
              if (_contains) {
                final String durationStr = line.split("=")[1];
                final double duration = Double.parseDouble(durationStr);
                return Double.valueOf(duration);
              }
              c = stdInput.read();
            }
          }
        } catch (final Throwable _t) {
          if (_t instanceof IOException) {
            final IOException e = (IOException)_t;
            System.err.println(e);
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
        return null;
      }
      return null;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
