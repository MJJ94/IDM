import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class CsvTxtGenerator {
  public static String generateCSV(final ArrayList<ArrayList<String>> variantes, final HashMap<String, Long> mapMediaSizes, final ArrayList<String> listMan, final ArrayList<String> listOpt, final ArrayList<String> listAlt, final String parentDir) {
    DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
    Date date = new Date();
    BufferedWriter writer = null;
    String _format = df.format(date);
    String _plus = ("result-" + _format);
    final String fileName = (_plus + ".csv");
    final String resultsDirPath = (parentDir + "results");
    try {
      Files.createDirectories(Paths.get(resultsDirPath));
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        System.err.println(e);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    final String filePath = ((resultsDirPath + "/") + fileName);
    try {
      FileWriter _fileWriter = new FileWriter(filePath);
      BufferedWriter _bufferedWriter = new BufferedWriter(_fileWriter);
      writer = _bufferedWriter;
      writer.write(CsvTxtGenerator.toCSV(variantes, mapMediaSizes, listMan, listOpt, listAlt, parentDir));
      writer.flush();
      writer.close();
      return filePath;
    } catch (final Throwable _t_1) {
      if (_t_1 instanceof IOException) {
        final IOException exception = (IOException)_t_1;
        System.err.println(exception);
        return null;
      } else {
        throw Exceptions.sneakyThrow(_t_1);
      }
    }
  }
  
  public static String toCSV(final ArrayList<ArrayList<String>> variantes, final HashMap<String, Long> mapMediaSizes, final ArrayList<String> listMan, final ArrayList<String> listOpt, final ArrayList<String> listAlt, final String parentDir) {
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
        totalDuration = Variants.getDurations(l, parentDir);
        stringBuilder.append(totalDuration);
        stringBuilder.append(separatorLine);
        id++;
      }
    }
    return stringBuilder.toString();
  }
  
  public static void generateVideosSeq(final ArrayList<ArrayList<String>> variantes, final String parentDir) {
    BufferedWriter writer = null;
    try {
      FileWriter _fileWriter = new FileWriter(((parentDir + "videos") + ".txt"));
      BufferedWriter _bufferedWriter = new BufferedWriter(_fileWriter);
      writer = _bufferedWriter;
      writer.write(CsvTxtGenerator.toTxt(variantes));
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
    ArrayList<String> randomVar = new ArrayList<String>();
    int _size = variantes.size();
    boolean _greaterThan = (_size > 1);
    if (_greaterThan) {
      Random random = new Random();
      int _size_1 = variantes.size();
      int _minus = (_size_1 - 1);
      int randomIndex = random.nextInt(_minus);
      randomVar = variantes.get(randomIndex);
    } else {
      randomVar = variantes.get(0);
    }
    for (final String elem : randomVar) {
      {
        stringBuilder.append((("file \'" + elem) + "\'"));
        stringBuilder.append(separatorLine);
      }
    }
    return stringBuilder.toString();
  }
}
