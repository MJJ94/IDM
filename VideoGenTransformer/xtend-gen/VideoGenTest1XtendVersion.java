import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoDescription;
import fr.istic.videoGen.VideoGeneratorModel;
import java.io.File;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class VideoGenTest1XtendVersion {
  @Test
  public void testLoadModel() {
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
              }
            }
          }
        }
      }
    }
  }
}
