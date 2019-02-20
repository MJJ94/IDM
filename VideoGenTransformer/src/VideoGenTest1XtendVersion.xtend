import org.junit.Test
import org.eclipse.emf.common.util.URI
import static org.junit.Assert.*
import fr.istic.videoGen.OptionalMedia
import fr.istic.videoGen.Media
import fr.istic.videoGen.MandatoryMedia
import fr.istic.videoGen.VideoDescription
import java.io.File
import fr.istic.videoGen.AlternativesMedia
import fr.istic.videoGen.MediaDescription
import java.util.ArrayList
import java.util.HashMap

class VideoGenTest1XtendVersion {
	var private listMan = new ArrayList<String>();
	var private listOp = new ArrayList<String>();
	var private listAlt = new ArrayList<String>();
	var private mapSizes = new HashMap<String, Long>
	var private variants = new ArrayList<ArrayList<String>>

	def void initTest(String specification) {
		this.listMan = new ArrayList<String>();
		this.listOp = new ArrayList<String>();
		this.listAlt = new ArrayList<String>();
		this.mapSizes = new HashMap<String, Long>

		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(specification))
		for (Media m : videoGen.medias) {
			if (m instanceof MandatoryMedia) {
				val man = m as MandatoryMedia
				if (man.description instanceof VideoDescription) {
					val des = man.description as VideoDescription
					val f = new File(des.location)
					this.listMan.add(des.location);
					this.mapSizes.put(des.location, f.length)
				}
			} else if (m instanceof OptionalMedia) {
				val op = m as OptionalMedia
				if (op.description instanceof VideoDescription) {
					val des = op.description as VideoDescription
					val f = new File(des.location)
					this.listOp.add(des.location);
					this.mapSizes.put(des.location, f.length)
				}
			} else if (m instanceof AlternativesMedia) {
				val alt = m as AlternativesMedia
				for (MediaDescription malt : alt.medias) {
					if (malt instanceof VideoDescription) {
						val des = malt as VideoDescription
						val f = new File(des.location)
						this.listAlt.add(des.location)
						this.mapSizes.put(des.location, f.length)
					}
				}
			}
		}
		this.variants = Utils.calculateVariants(listMan, listOp, listAlt)
	}

	def int nbVariants(int sizeMan, int sizeOpt, int sizeAlt) {
		var result = 1
		var nbOp = Math.pow(2, sizeOpt)

		if (sizeOpt > 0) {
			result *= nbOp.intValue
		}

		if (sizeAlt > 0) {
			result *= sizeAlt
		}

		return result
	}

	@Test
	def void nbVariants() {
		initTest("only_alternatives.videogen")
		var nbVariants = nbVariants(listMan.size(), listOp.size(), listAlt.size)
		assertEquals(nbVariants, variants.size(), 0)
	}
}
