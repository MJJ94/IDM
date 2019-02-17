import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoDescription;
import fr.istic.videoGen.VideoGeneratorModel;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
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
  
  @Test
  public void nbVariants() {
    this.initTest("specification.videogen");
    double nbOp = Math.pow(2, this.listOp.size());
    int nbAlt = this.listAlt.size();
    double nbVariants = (nbOp * nbAlt);
    Assert.assertEquals(nbVariants, this.variants.size(), 0);
  }
}
