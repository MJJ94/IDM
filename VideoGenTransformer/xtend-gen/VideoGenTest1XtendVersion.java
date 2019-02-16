import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoDescription;
import fr.istic.videoGen.VideoGeneratorModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class VideoGenTest1XtendVersion {
  @Test
  public void testLoadModel() {
    final ArrayList<String> listMan = new ArrayList<String>();
    final ArrayList<String> listOp = new ArrayList<String>();
    final ArrayList<String> listAlt = new ArrayList<String>();
    HashMap<String, Long> mapSizes = new HashMap<String, Long>();
    final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("specification.videogen"));
    Assert.assertNotNull(videoGen);
    EList<Media> _medias = videoGen.getMedias();
    for (final Media m : _medias) {
      if ((m instanceof MandatoryMedia)) {
        final MandatoryMedia man = ((MandatoryMedia) m);
        MediaDescription _description = man.getDescription();
        if ((_description instanceof VideoDescription)) {
          MediaDescription _description_1 = man.getDescription();
          final VideoDescription des = ((VideoDescription) _description_1);
          String _location = des.getLocation();
          final File f = new File(_location);
          String _location_1 = des.getLocation();
          String _plus = (_location_1 + " ");
          long _length = f.length();
          String _plus_1 = (_plus + Long.valueOf(_length));
          InputOutput.<String>println(_plus_1);
          listMan.add(des.getLocation());
          mapSizes.put(des.getLocation(), Long.valueOf(f.length()));
        }
      } else {
        if ((m instanceof OptionalMedia)) {
          final OptionalMedia op = ((OptionalMedia) m);
          MediaDescription _description_2 = op.getDescription();
          if ((_description_2 instanceof VideoDescription)) {
            MediaDescription _description_3 = op.getDescription();
            final VideoDescription des_1 = ((VideoDescription) _description_3);
            String _location_2 = des_1.getLocation();
            final File f_1 = new File(_location_2);
            String _location_3 = des_1.getLocation();
            String _plus_2 = (_location_3 + " ");
            long _length_1 = f_1.length();
            String _plus_3 = (_plus_2 + Long.valueOf(_length_1));
            InputOutput.<String>println(_plus_3);
            listOp.add(des_1.getLocation());
            mapSizes.put(des_1.getLocation(), Long.valueOf(f_1.length()));
          }
        } else {
          if ((m instanceof AlternativesMedia)) {
            final AlternativesMedia alt = ((AlternativesMedia) m);
            EList<MediaDescription> _medias_1 = alt.getMedias();
            for (final MediaDescription malt : _medias_1) {
              if ((malt instanceof VideoDescription)) {
                final VideoDescription des_2 = ((VideoDescription) malt);
                String _location_4 = des_2.getLocation();
                final File f_2 = new File(_location_4);
                String _location_5 = des_2.getLocation();
                String _plus_4 = (_location_5 + " ");
                long _length_2 = f_2.length();
                String _plus_5 = (_plus_4 + Long.valueOf(_length_2));
                InputOutput.<String>println(_plus_5);
                listAlt.add(des_2.getLocation());
                mapSizes.put(des_2.getLocation(), Long.valueOf(f_2.length()));
              }
            }
          }
        }
      }
    }
    final ArrayList<ArrayList<String>> result = this.calculateVariants(listMan, listOp, listAlt);
    this.generateCSV(result, mapSizes, listMan, listOp, listAlt);
    this.generateVideosSeq(result);
    this.runCommands();
  }
  
  public ArrayList<ArrayList<String>> calculateVariants(final ArrayList<String> listMan, final ArrayList<String> listOp, final ArrayList<String> listAlt) {
    int _size = listOp.size();
    String _plus = ("listOp.size() = " + Integer.valueOf(_size));
    String _plus_1 = (_plus + " listAlt.size() = ");
    int _size_1 = listAlt.size();
    String _plus_2 = (_plus_1 + Integer.valueOf(_size_1));
    InputOutput.<String>println(_plus_2);
    ArrayList<ArrayList<String>> opCombinations = this.insertOp(listOp);
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> _insertMan = this.insertMan(listMan, opCombinations);
    ArrayList<ArrayList<String>> manOpResult = new ArrayList<ArrayList<String>>(_insertMan);
    ArrayList<ArrayList<String>> _insertAlt = this.insertAlt(listAlt, manOpResult);
    ArrayList<ArrayList<String>> _arrayList = new ArrayList<ArrayList<String>>(_insertAlt);
    result = _arrayList;
    return result;
  }
  
  public ArrayList<ArrayList<String>> insertMan(final ArrayList<String> listMan, final ArrayList<ArrayList<String>> opResult) {
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>(opResult);
    for (final String man : listMan) {
      for (final ArrayList<String> l : result) {
        l.add(man);
      }
    }
    return result;
  }
  
  public ArrayList<ArrayList<String>> insertOp(final ArrayList<String> lOp) {
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
        InputOutput.<String>println(((elem + " ") + Integer.valueOf(sizePreced)));
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
  
  public ArrayList<ArrayList<String>> insertAlt(final ArrayList<String> listAlt, final ArrayList<ArrayList<String>> manOpResult) {
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
  
  public void generateCSV(final ArrayList<ArrayList<String>> variantes, final HashMap<String, Long> mapMediaSizes, final ArrayList<String> listMan, final ArrayList<String> listOpt, final ArrayList<String> listAlt) {
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
      writer.write(this.toCSV(variantes, mapMediaSizes, listMan, listOpt, listAlt));
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
  
  public String toCSV(final ArrayList<ArrayList<String>> variantes, final HashMap<String, Long> mapMediaSizes, final ArrayList<String> listMan, final ArrayList<String> listOpt, final ArrayList<String> listAlt) {
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
    stringBuilder.append(separatorLine);
    int id = 1;
    for (final ArrayList<String> l : variantes) {
      {
        long totalSize = 0;
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
        stringBuilder.append(separatorLine);
        id++;
      }
    }
    return stringBuilder.toString();
  }
  
  public void generateVideosSeq(final ArrayList<ArrayList<String>> variantes) {
    BufferedWriter writer = null;
    try {
      FileWriter _fileWriter = new FileWriter(("./videos" + ".txt"));
      BufferedWriter _bufferedWriter = new BufferedWriter(_fileWriter);
      writer = _bufferedWriter;
      writer.write(this.toTxt(variantes));
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
  
  public String toTxt(final ArrayList<ArrayList<String>> variantes) {
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
  
  public void runCommands() {
    try {
      String command = "ffmpeg -f concat -safe 0 -i videos.txt -c copy output.mp4";
      String playVideoCommand = "vlc output.mp4";
      String remove = "rm output.mp4";
      DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
      Date date = new Date();
      String _format = df.format(date);
      String _plus = ("ffmpeg -i output.mp4 gif_" + _format);
      String gifCommand = (_plus + ".gif -hide_banner");
      Process p = Runtime.getRuntime().exec(command);
      int _waitFor = p.waitFor();
      boolean _equals = (_waitFor == 0);
      if (_equals) {
        Process gifP = Runtime.getRuntime().exec(gifCommand);
        int _waitFor_1 = gifP.waitFor();
        boolean _equals_1 = (_waitFor_1 == 0);
        if (_equals_1) {
          Process vlcP = Runtime.getRuntime().exec(playVideoCommand);
          int _waitFor_2 = vlcP.waitFor();
          boolean _equals_2 = (_waitFor_2 == 0);
          if (_equals_2) {
            Process removeP = Runtime.getRuntime().exec(remove);
            removeP.waitFor();
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
