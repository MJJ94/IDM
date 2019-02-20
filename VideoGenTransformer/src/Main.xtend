import org.eclipse.emf.common.util.URI
import fr.istic.videoGen.OptionalMedia
import fr.istic.videoGen.Media
import fr.istic.videoGen.MandatoryMedia
import fr.istic.videoGen.VideoDescription
import java.io.File
import fr.istic.videoGen.AlternativesMedia
import fr.istic.videoGen.MediaDescription
import java.util.ArrayList
import java.util.HashMap

class Main {

	def static void main(String[] args) {

		var listMan = new ArrayList<String>();
		var listOp = new ArrayList<String>();
		var listAlt = new ArrayList<String>();
		var mapSizes = new HashMap<String, Long>

		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("specification.videogen"))
		for (Media m : videoGen.medias) {
			if (m instanceof MandatoryMedia) {
				val man = m as MandatoryMedia
				if (man.description instanceof VideoDescription) {
					val des = man.description as VideoDescription
					val f = new File(des.location)
					listMan.add(des.location);
					mapSizes.put(des.location, f.length)
				}
			} else if (m instanceof OptionalMedia) {
				val op = m as OptionalMedia
				if (op.description instanceof VideoDescription) {
					val des = op.description as VideoDescription
					val f = new File(des.location)
					listOp.add(des.location);
					mapSizes.put(des.location, f.length)
				}
			} else if (m instanceof AlternativesMedia) {
				val alt = m as AlternativesMedia
				for (MediaDescription malt : alt.medias) {
					if (malt instanceof VideoDescription) {
						val des = malt as VideoDescription
						val f = new File(des.location)
						listAlt.add(des.location)
						mapSizes.put(des.location, f.length)
					}
				}
			}
		}
		var variants = Variants.calculateVariants(listMan, listOp, listAlt)
		var videos = new ArrayList<String>()
		videos.addAll(listMan)
		videos.addAll(listOp)
		videos.addAll(listAlt)
		CsvTxtGenerator.generateCSV(variants, mapSizes, listMan, listOp, listAlt)
		CsvTxtGenerator.generateVideosSeq(variants)
		OutPutsGenerator.generateIcons(videos)
		OutPutsGenerator.runCommands();
	}
}
