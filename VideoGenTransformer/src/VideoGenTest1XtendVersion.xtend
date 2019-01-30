import org.junit.Test
import org.eclipse.emf.common.util.URI

import static org.junit.Assert.*
import fr.istic.videoGen.OptionalMedia
import fr.istic.videoGen.Media
import fr.istic.videoGen.MandatoryMedia
import fr.istic.videoGen.VideoDescription
import fr.istic.videoGen.VideoText
import java.io.File
import fr.istic.videoGen.AlternativesMedia
import fr.istic.videoGen.MediaDescription

class VideoGenTest1XtendVersion {

	@Test
	def void testLoadModel() {

		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("specification.videogen"))
		assertNotNull(videoGen)
		for (Media m : videoGen.medias) {
			if (m instanceof MandatoryMedia) {
				val man = m as MandatoryMedia
				if (man.description instanceof VideoDescription) {
					val des = man.description as VideoDescription
					val f = new File(des.location)
					println(des.location + " " + f.length)

				}
			} else if (m instanceof OptionalMedia) {
				val op = m as OptionalMedia
				if (op.description instanceof VideoDescription) {
					val des = op.description as VideoDescription
					val f = new File(des.location)
					println(des.location + " " + f.length)

				}
			} else if (m instanceof AlternativesMedia) {
				val alt = m as AlternativesMedia
				for (MediaDescription malt : alt.medias) {
					if (malt instanceof VideoDescription) {
						val des = malt as VideoDescription
						val f = new File(des.location)
						println(des.location + " " + f.length)
					}
				}
			}
//			val omedia = (m as OptionalMedia)
		}
//		println(omedia.description.location)
	// and then visit the model
	// eg access video sequences: videoGen.videoseqs
	}

}
