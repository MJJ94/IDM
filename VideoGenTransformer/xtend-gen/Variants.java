import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class Variants {
  public static ArrayList<ArrayList<String>> calculateVariants(final ArrayList<String> listMan, final ArrayList<String> listOp, final ArrayList<String> listAlt) {
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> opCombinations = Variants.insertOp(listOp);
    ArrayList<ArrayList<String>> _insertMan = Variants.insertMan(listMan, opCombinations);
    ArrayList<ArrayList<String>> manOpResult = new ArrayList<ArrayList<String>>(_insertMan);
    ArrayList<ArrayList<String>> _insertAlt = Variants.insertAlt(listAlt, manOpResult);
    ArrayList<ArrayList<String>> _arrayList = new ArrayList<ArrayList<String>>(_insertAlt);
    result = _arrayList;
    return result;
  }
  
  public static ArrayList<ArrayList<String>> insertMan(final ArrayList<String> listMan, final ArrayList<ArrayList<String>> opResult) {
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>(opResult);
    boolean _isEmpty = listMan.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      boolean _isEmpty_1 = result.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
        for (final String man : listMan) {
          for (final ArrayList<String> l : result) {
            l.add(man);
          }
        }
      } else {
        result.add(listMan);
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
    boolean _isEmpty = lOp.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
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
    }
    return result;
  }
  
  public static ArrayList<ArrayList<String>> insertAlt(final ArrayList<String> listAlt, final ArrayList<ArrayList<String>> manOpResult) {
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    boolean _isEmpty = listAlt.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      boolean _isEmpty_1 = manOpResult.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
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
      } else {
        for (final String alt_1 : listAlt) {
          {
            ArrayList<String> l_1 = new ArrayList<String>();
            l_1.add(alt_1);
            result.add(l_1);
          }
        }
      }
    } else {
      return manOpResult;
    }
    return result;
  }
  
  public static Double getDurations(final ArrayList<String> videos) {
    double result = 0.0;
    for (final String video : videos) {
      {
        final Double duration = Variants.getDuration(video);
        double _result = result;
        result = (_result + (duration).doubleValue());
      }
    }
    return Double.valueOf(result);
  }
  
  public static Double getDuration(final String videoName) {
    try {
      Double _xblockexpression = null;
      {
        String durationCommand = ((" ffprobe -i " + videoName) + " -show_format");
        Process durationP = Runtime.getRuntime().exec(durationCommand);
        Double _xifexpression = null;
        int _waitFor = durationP.waitFor();
        boolean _equals = (_waitFor == 0);
        if (_equals) {
          Double _xtrycatchfinallyexpression = null;
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
              return null;
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
          _xifexpression = _xtrycatchfinallyexpression;
        }
        _xblockexpression = _xifexpression;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
