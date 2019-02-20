import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoDescription;
import fr.istic.videoGen.VideoGeneratorModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class VideoGenTest1XtendVersion {
  private ArrayList<String> listMan = new ArrayList<String>();
  
  private ArrayList<String> listOp = new ArrayList<String>();
  
  private ArrayList<String> listAlt = new ArrayList<String>();
  
  private HashMap<String, Long> mapSizes = new HashMap<String, Long>();
  
  private ArrayList<ArrayList<String>> variants = new ArrayList<ArrayList<String>>();
  
  public void initTest(final String specification) {
    ArrayList<String> _arrayList = new ArrayList<String>();
    this.listMan = _arrayList;
    ArrayList<String> _arrayList_1 = new ArrayList<String>();
    this.listOp = _arrayList_1;
    ArrayList<String> _arrayList_2 = new ArrayList<String>();
    this.listAlt = _arrayList_2;
    HashMap<String, Long> _hashMap = new HashMap<String, Long>();
    this.mapSizes = _hashMap;
    this.cleanDirectories();
    final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(specification));
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
          this.listMan.add(des.getLocation());
          this.mapSizes.put(des.getLocation(), Long.valueOf(f.length()));
        }
      } else {
        if ((m instanceof OptionalMedia)) {
          final OptionalMedia op = ((OptionalMedia) m);
          MediaDescription _description_2 = op.getDescription();
          if ((_description_2 instanceof VideoDescription)) {
            MediaDescription _description_3 = op.getDescription();
            final VideoDescription des_1 = ((VideoDescription) _description_3);
            String _location_1 = des_1.getLocation();
            final File f_1 = new File(_location_1);
            this.listOp.add(des_1.getLocation());
            this.mapSizes.put(des_1.getLocation(), Long.valueOf(f_1.length()));
          }
        } else {
          if ((m instanceof AlternativesMedia)) {
            final AlternativesMedia alt = ((AlternativesMedia) m);
            EList<MediaDescription> _medias_1 = alt.getMedias();
            for (final MediaDescription malt : _medias_1) {
              if ((malt instanceof VideoDescription)) {
                final VideoDescription des_2 = ((VideoDescription) malt);
                String _location_2 = des_2.getLocation();
                final File f_2 = new File(_location_2);
                this.listAlt.add(des_2.getLocation());
                this.mapSizes.put(des_2.getLocation(), Long.valueOf(f_2.length()));
              }
            }
          }
        }
      }
    }
    this.variants = Utils.calculateVariants(this.listMan, this.listOp, this.listAlt);
  }
  
  public int nbVariants(final int sizeMan, final int sizeOpt, final int sizeAlt) {
    int result = 1;
    double nbOp = Math.pow(2, sizeOpt);
    if ((sizeOpt > 0)) {
      int _result = result;
      int _intValue = Double.valueOf(nbOp).intValue();
      result = (_result * _intValue);
    }
    if ((sizeAlt > 0)) {
      int _result_1 = result;
      result = (_result_1 * sizeAlt);
    }
    return result;
  }
  
  public int nbLinesCSV(final String path) {
    final File file = new File(path);
    int result = 0;
    boolean _exists = file.exists();
    if (_exists) {
      try {
        FileReader _fileReader = new FileReader(file);
        final BufferedReader stdInput = new BufferedReader(_fileReader);
        int c = 0;
        while ((c > (-1))) {
          {
            String line = stdInput.readLine();
            boolean _contains = line.contains("id");
            boolean _not = (!_contains);
            if (_not) {
              result++;
            }
            c = stdInput.read();
          }
        }
        return result;
      } catch (final Throwable _t) {
        if (_t instanceof IOException) {
          final IOException e = (IOException)_t;
          System.err.println(e);
          return (-1);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    System.err.println((("the File: " + path) + " doesn\'t exist"));
    return (-2);
  }
  
  public void cleanDirectories() {
    try {
      final String outputs = "./outputs/";
      File results = new File("./results/");
      File videoOutputs = new File((outputs + "videos/"));
      File gifsOutputs = new File((outputs + "gifs/"));
      File iconsOutputs = new File((outputs + "icons/"));
      ArrayList<File> files = new ArrayList<File>();
      CollectionExtensions.<File>addAll(files, results.listFiles());
      CollectionExtensions.<File>addAll(files, videoOutputs.listFiles());
      CollectionExtensions.<File>addAll(files, gifsOutputs.listFiles());
      CollectionExtensions.<File>addAll(files, iconsOutputs.listFiles());
      for (final File file : files) {
        Files.delete(Paths.get(file.getPath()));
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void nbVariants() {
    this.initTest("only_alternatives.videogen");
    final int nbVariants = this.nbVariants(this.listMan.size(), this.listOp.size(), this.listAlt.size());
    Assert.assertEquals(nbVariants, this.variants.size(), 0);
  }
  
  @Test
  public void nbLinesCSV() {
    this.initTest("only_alternatives.videogen");
    final int nbVariants = this.nbVariants(this.listMan.size(), this.listOp.size(), this.listAlt.size());
    final String csvPath = Utils.generateCSV(this.variants, this.mapSizes, this.listMan, this.listOp, this.listAlt);
    final int nbLinesCsv = this.nbLinesCSV(csvPath);
    Assert.assertEquals(nbLinesCsv, nbVariants, 0);
  }
  
  public int getNbIcons() {
    int result = 0;
    final String iconFolderPath = "./outputs/icons/";
    File iconFolder = new File(iconFolderPath);
    File[] _listFiles = iconFolder.listFiles();
    ArrayList<File> icons = new ArrayList<File>((Collection<? extends File>)Conversions.doWrapArray(_listFiles));
    for (final File icon : icons) {
      result++;
    }
    return result;
  }
  
  @Test
  public void nbIcons() {
    this.initTest("specification.videogen");
    ArrayList<String> allVideos = new ArrayList<String>();
    allVideos.addAll(this.listMan);
    allVideos.addAll(this.listOp);
    allVideos.addAll(this.listAlt);
    Utils.generateIcons(allVideos);
    final int nbIcons = this.getNbIcons();
    Assert.assertEquals(allVideos.size(), nbIcons, 0);
  }
}
