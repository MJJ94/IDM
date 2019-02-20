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

@SuppressWarnings("all")
public class Main {
  public static void main(final String[] args) {
    ArrayList<String> listMan = new ArrayList<String>();
    ArrayList<String> listOp = new ArrayList<String>();
    ArrayList<String> listAlt = new ArrayList<String>();
    HashMap<String, Long> mapSizes = new HashMap<String, Long>();
    final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("specification.videogen"));
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
            String _location_1 = des_1.getLocation();
            final File f_1 = new File(_location_1);
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
                String _location_2 = des_2.getLocation();
                final File f_2 = new File(_location_2);
                listAlt.add(des_2.getLocation());
                mapSizes.put(des_2.getLocation(), Long.valueOf(f_2.length()));
              }
            }
          }
        }
      }
    }
    ArrayList<ArrayList<String>> variants = Utils.calculateVariants(listMan, listOp, listAlt);
    ArrayList<String> videos = new ArrayList<String>();
    videos.addAll(listMan);
    videos.addAll(listOp);
    videos.addAll(listAlt);
    Utils.generateCSV(variants, mapSizes, listMan, listOp, listAlt);
    Utils.generateVideosSeq(variants);
    Utils.generateIcons(videos);
    Utils.runCommands();
  }
}
