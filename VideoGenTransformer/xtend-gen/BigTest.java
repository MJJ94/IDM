import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoDescription;
import fr.istic.videoGen.VideoGeneratorModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class BigTest {
  private static ArrayList<String> listMan = new ArrayList<String>();
  
  private static ArrayList<String> listOp = new ArrayList<String>();
  
  private static ArrayList<String> listAlt = new ArrayList<String>();
  
  private static HashMap<String, Long> mapSizes = new HashMap<String, Long>();
  
  private static ArrayList<ArrayList<String>> variants = new ArrayList<ArrayList<String>>();
  
  public static void initMain(final String specification, final String parentDir) {
    ArrayList<String> _arrayList = new ArrayList<String>();
    BigTest.listMan = _arrayList;
    ArrayList<String> _arrayList_1 = new ArrayList<String>();
    BigTest.listOp = _arrayList_1;
    ArrayList<String> _arrayList_2 = new ArrayList<String>();
    BigTest.listAlt = _arrayList_2;
    HashMap<String, Long> _hashMap = new HashMap<String, Long>();
    BigTest.mapSizes = _hashMap;
    final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(((parentDir + "/") + specification)));
    EList<Media> _medias = videoGen.getMedias();
    for (final Media m : _medias) {
      if ((m instanceof MandatoryMedia)) {
        final MandatoryMedia man = ((MandatoryMedia) m);
        MediaDescription _description = man.getDescription();
        if ((_description instanceof VideoDescription)) {
          MediaDescription _description_1 = man.getDescription();
          final VideoDescription des = ((VideoDescription) _description_1);
          String _location = des.getLocation();
          String _plus = ((parentDir + "/") + _location);
          final File f = new File(_plus);
          BigTest.listMan.add(des.getLocation());
          BigTest.mapSizes.put(des.getLocation(), Long.valueOf(f.length()));
        }
      } else {
        if ((m instanceof OptionalMedia)) {
          final OptionalMedia op = ((OptionalMedia) m);
          MediaDescription _description_2 = op.getDescription();
          if ((_description_2 instanceof VideoDescription)) {
            MediaDescription _description_3 = op.getDescription();
            final VideoDescription des_1 = ((VideoDescription) _description_3);
            String _location_1 = des_1.getLocation();
            String _plus_1 = ((parentDir + "/") + _location_1);
            final File f_1 = new File(_plus_1);
            BigTest.listOp.add(des_1.getLocation());
            BigTest.mapSizes.put(des_1.getLocation(), Long.valueOf(f_1.length()));
          }
        } else {
          if ((m instanceof AlternativesMedia)) {
            final AlternativesMedia alt = ((AlternativesMedia) m);
            EList<MediaDescription> _medias_1 = alt.getMedias();
            for (final MediaDescription malt : _medias_1) {
              if ((malt instanceof VideoDescription)) {
                final VideoDescription des_2 = ((VideoDescription) malt);
                String _location_2 = des_2.getLocation();
                String _plus_2 = ((parentDir + "/") + _location_2);
                final File f_2 = new File(_plus_2);
                BigTest.listAlt.add(des_2.getLocation());
                BigTest.mapSizes.put(des_2.getLocation(), Long.valueOf(f_2.length()));
              }
            }
          }
        }
      }
    }
  }
  
  public static ArrayList<File> runVideogens(final ArrayList<File> directories) {
    ArrayList<File> videoGens = new ArrayList<File>();
    for (final File dir : directories) {
      {
        File[] _listFiles = dir.listFiles();
        ArrayList<File> files = new ArrayList<File>((Collection<? extends File>)Conversions.doWrapArray(_listFiles));
        for (final File f : files) {
          {
            String fileName = f.getName();
            boolean _contains = fileName.contains(".videogen");
            if (_contains) {
              videoGens.add(f);
            }
          }
        }
      }
    }
    return videoGens;
  }
  
  public static void main(final String[] args) {
    File specificationsDirParent = new File("specifications/");
    File[] _listFiles = specificationsDirParent.listFiles();
    ArrayList<File> specificationsDir = new ArrayList<File>((Collection<? extends File>)Conversions.doWrapArray(_listFiles));
    ArrayList<File> videoGens = BigTest.runVideogens(specificationsDir);
    for (final File videoGen : videoGens) {
      {
        BigTest.initMain(videoGen.getName(), videoGen.getParent());
        String _parent = videoGen.getParent();
        String parentDir = (_parent + "/");
        InputOutput.<String>println(parentDir);
        BigTest.variants = Variants.calculateVariants(BigTest.listMan, BigTest.listOp, BigTest.listAlt);
        ArrayList<String> videos = new ArrayList<String>();
        videos.addAll(BigTest.listMan);
        videos.addAll(BigTest.listOp);
        videos.addAll(BigTest.listAlt);
        CsvTxtGenerator.generateCSV(BigTest.variants, BigTest.mapSizes, BigTest.listMan, BigTest.listOp, BigTest.listAlt, parentDir);
        OutPutsGenerator.generateIcons(videos, parentDir);
        int i = 0;
        while ((i < 10)) {
          {
            CsvTxtGenerator.generateVideosSeq(BigTest.variants, parentDir);
            OutPutsGenerator.generateVideosAndGifs(parentDir, false);
            i++;
          }
        }
      }
    }
  }
}
