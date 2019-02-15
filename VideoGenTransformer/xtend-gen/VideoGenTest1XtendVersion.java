import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoDescription;
import fr.istic.videoGen.VideoGeneratorModel;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class VideoGenTest1XtendVersion {
  @Test
  public void testLoadModel() {
    final ArrayList<MandatoryMedia> listMan = new ArrayList<MandatoryMedia>();
    final ArrayList<OptionalMedia> listOp = new ArrayList<OptionalMedia>();
    final ArrayList<AlternativesMedia> listAlt = new ArrayList<AlternativesMedia>();
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
          listMan.add(man);
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
            listOp.add(op);
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
                listAlt.add(alt);
              }
            }
          }
        }
      }
    }
    final ArrayList result = this.calculateVariants(listMan, listOp, listAlt);
  }
  
  public ArrayList calculateVariants(final ArrayList<MandatoryMedia> listMan, final ArrayList<OptionalMedia> listOp, final ArrayList<AlternativesMedia> listAlt) {
    int _size = listOp.size();
    String _plus = ("listOp.size() = " + Integer.valueOf(_size));
    String _plus_1 = (_plus + " listAlt.size() = ");
    int _size_1 = listAlt.size();
    String _plus_2 = (_plus_1 + Integer.valueOf(_size_1));
    InputOutput.<String>println(_plus_2);
    int _size_2 = listOp.size();
    int _multiply = (_size_2 * 2);
    int _size_3 = listAlt.size();
    final int resultNumber = (_multiply * _size_3);
    ArrayList<ArrayList<Media>> result = this.initResult(listMan, resultNumber);
    ArrayList<ArrayList<String>> optionelCombinations = new ArrayList<ArrayList<String>>();
    int j = 0;
    int i = 0;
    int _size_4 = result.size();
    String _plus_3 = ("size = " + Integer.valueOf(_size_4));
    InputOutput.<String>println(_plus_3);
    this.recursive_combinations_start();
    while ((j < resultNumber)) {
      {
        for (final AlternativesMedia alt : listAlt) {
          {
            result.get(j).add(alt);
            int _size_5 = optionelCombinations.size();
            boolean _lessThan = (i < _size_5);
            if (_lessThan) {
              InputOutput.<Integer>println(Integer.valueOf(optionelCombinations.get(i).size()));
            }
            j++;
          }
        }
        i++;
      }
    }
    return result;
  }
  
  public boolean contain(final ArrayList<ArrayList<OptionalMedia>> used, final ArrayList<OptionalMedia> combo) {
    boolean compar = false;
    for (final ArrayList<OptionalMedia> l : used) {
      {
        InputOutput.<String>println(((("l: " + l) + " combo: ") + combo));
        HashSet<OptionalMedia> _hashSet = new HashSet<OptionalMedia>(l);
        HashSet<OptionalMedia> _hashSet_1 = new HashSet<OptionalMedia>(combo);
        compar = _hashSet.equals(_hashSet_1);
        if (compar) {
          return true;
        }
      }
    }
    return false;
  }
  
  public ArrayList<ArrayList<Media>> initResult(final ArrayList<MandatoryMedia> listMan, final int size) {
    final ArrayList<ArrayList<Media>> result = new ArrayList<ArrayList<Media>>();
    int i = 0;
    while ((i < size)) {
      {
        ArrayList<Media> _arrayList = new ArrayList<Media>(listMan);
        result.add(_arrayList);
        i++;
      }
    }
    return result;
  }
  
  public void recursive_combinations(final ArrayList<String> combination, final int ndx, final ArrayList<String> elems, final ArrayList<ArrayList<String>> result) {
    int _length = ((Object[])Conversions.unwrapArray(elems, Object.class)).length;
    boolean _equals = (ndx == _length);
    if (_equals) {
      result.add(combination);
    } else {
      combination.add(elems.get(ndx));
      this.recursive_combinations(combination, (ndx + 1), elems, result);
      combination.remove(elems.get(ndx));
      this.recursive_combinations(combination, (ndx + 1), elems, result);
    }
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
            InputOutput.<String>println(("i = " + Integer.valueOf(i)));
            sizePreced = ((initSizePreced * 2) + sizePreced);
            InputOutput.<String>println(("sizePreced = " + Integer.valueOf(sizePreced)));
          }
        }
        int _j = j;
        j = (_j * 2);
      }
    }
    return result;
  }
  
  public void recursive_combinations_start() {
    final ArrayList<String> combination = new ArrayList<String>();
    combination.add("A");
    combination.add("B");
    combination.add("C");
    combination.add("D");
    final ArrayList<ArrayList<String>> result = this.insertOp(combination);
    for (final ArrayList<String> l : result) {
      InputOutput.<String>println(("l:" + l));
    }
  }
}
